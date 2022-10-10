import backoff
import logging
import paramiko
import os
import sqlalchemy

from collections import defaultdict
from datetime import datetime
from pathlib import Path
from os import getenv
from typing import Optional

from dotenv import load_dotenv
from fabric import Connection
from invoke import UnexpectedExit
from sqlalchemy import MetaData, Table, and_, create_engine, select
from sqlalchemy.engine import Engine

load_dotenv()
logging.getLogger('backoff').addHandler(logging.StreamHandler())

def new_engine() -> Engine:
    return create_engine(f'mysql+mysqlconnector://{getenv("DB_USER")}:{getenv("DB_PASS")}@{getenv("DB_HOST")}/{getenv("DB_NAME")}')

def load_table(name: str, engine: Engine) -> Table:
    return Table(name, MetaData(), autoload=True, autoload_with=engine)

def master_events_table(engine: Engine):
    return load_table('master_events', engine)

def compile_events_table(engine: Engine):
    return load_table('compile_events', engine)

def source_files_table(engine: Engine):
    return load_table('source_files', engine)

def source_histories_table(engine: Engine):
    return load_table('source_histories', engine)

# Returns dict from project_it to (master_event_id, session_id)
def find_compilation_events(start_date: datetime, end_date: datetime) -> dict[int, tuple[int, int]]:
    engine = new_engine()
    master_events = master_events_table(engine)
    compile_events = compile_events_table(engine)
    stmt = select(master_events).where(
        and_(
            master_events.c.created_at >= start_date,
            master_events.c.created_at < end_date,
            master_events.c.event_type == 'CompileEvent'
        )
    ).join(
        compile_events, master_events.c.event_id == compile_events.c.id
    ).where(
        compile_events.c.success == 1
    )
    successful_compilations = defaultdict(list)
    with engine.connect() as conn:
        for row in conn.execute(stmt):
            successful_compilations[row.project_id].append((row.id, row.session_id))
    # latest_successful compilation
    return {project: max(events) for project, events in successful_compilations.items()}

@backoff.on_exception(backoff.expo,
                      sqlalchemy.exc.DatabaseError)
def find_sources_in_project(project_id: int, master_event_id: int, session_id: int, engine: Engine) -> list[int]:
    source_histories = source_histories_table(engine)
    master_events = master_events_table(engine)
    source_files = source_files_table(engine)
    stmt = select(source_histories).where(and_(
        source_files.c.project_id == project_id,
        source_files.c.source_type == "Java",
        source_histories.c.master_event_id <= master_event_id,
        master_events.c.session_id == session_id)
    ).join(
        source_histories, source_histories.c.source_file_id == source_files.c.id
    ).join(
        master_events, master_events.c.id == source_histories.c.master_event_id
    )
    source_id_history_types = defaultdict(list)
    with engine.connect() as conn:
        for row in conn.execute(stmt):
            source_id_history_types[row.source_file_id].append(row.source_history_type)
    clean_sources = filter(lambda source: all(t == "complete" or t == "diff" for t in source[1]), source_id_history_types.items())
    return [source[0] for source in clean_sources]


@backoff.on_exception(backoff.expo,
                      (paramiko.ssh_exception.SSHException,
                       TimeoutError))
def retrieve_source(source_id: int, master_event_id: int, conn: Connection) -> Optional[str]:
    command = f"/tools/nccb/bin/print-source-state {source_id} {master_event_id} 2>/dev/null"
    try:
        result = conn.run(command, hide="out")
        return result.stdout
    except UnexpectedExit as e:
        # Some diff issues, source cannot be retrieved
        # e.g. (Error finding source: ProblemInternalError: [...]
        return None

def create_source_files(source_ids: list[int], master_event_id: int, project_id: int, sources_dir: Path, conn: Connection) -> tuple[int, int]:
    """
    :return: a tuple with:
        - number of source files successfully retrieved and written
        - total number of source files
    """
    os.mkdir(f"{sources_dir}/{project_id}")
    successes = 0
    for source in source_ids:
        java_source = retrieve_source(source, master_event_id, conn)
        if java_source is not None:
            with open(f"{sources_dir}/{project_id}/{source}.java", "w") as f:
                f.write(java_source)
            successes += 1
    return (successes, len(source_ids))

def process_compilation_event(compilation_event_info: tuple[int, tuple[int, int]], sources_dir: Path) -> tuple[int, int]:
    project_id, (master_event_id, session_id) = compilation_event_info
    source_ids = find_sources_in_project(project_id, master_event_id, session_id, process_compilation_event.engine)
    return create_source_files(source_ids, master_event_id, project_id, sources_dir, process_compilation_event.conn)

def setup(fn):
    fn.conn = Connection(f'{getenv("SSH_USER")}@{getenv("SSH_HOST")}')
    fn.engine = new_engine()
