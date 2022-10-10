import os
import sqlite3
import subprocess
from datetime import datetime
from functools import partial
from multiprocessing import Pool
from pathlib import Path
from shutil import rmtree

import typer
from sqlalchemy import create_engine
from tqdm import tqdm

from blackbox import (
    find_compilation_events,
    process_compilation_event,
    setup,
)
from visualizations import rq1_2_3_4, rq5, rq6

app = typer.Typer()


@app.command()
def analysis(sqlite_db: Path):
    engine = create_engine(f"sqlite:///{sqlite_db}")
    rq1_2_3_4(engine)
    rq5(engine)
    rq6(engine)


@app.command()
def extract_blackbox(start_date: datetime, end_date: datetime, sources_dir: Path):
    print("[+] Finding relevant compilation events...")
    latest_successful_compilations = find_compilation_events(start_date, end_date)
    print(f"[*] Found {len(latest_successful_compilations)} projects.")
    if os.path.isdir(sources_dir):
        delete = typer.confirm(
            f"Deleting everything inside {sources_dir}, are you sure?"
        )
        if delete:
            rmtree(sources_dir)
    os.mkdir(sources_dir)
    print(f"[+] Downloading all the related Java sources...")
    with Pool(initializer=setup, initargs=(process_compilation_event,)) as p:
        results = list(
            tqdm(
                p.imap(
                    partial(process_compilation_event, sources_dir=sources_dir),
                    latest_successful_compilations.items()
                ),
                total=len(latest_successful_compilations),
            )
        )
    files_written, files_total = map(sum, zip(*results))
    print(f"[*] Java sources downloaded in `{sources_dir}` directory.")
    print(f"[*] (written: {files_written} out of {files_total} total files).")


def analyze_project(
    db_path: Path, expression_service_binary: Path, project_folder: Path
) -> int:
    res = subprocess.run(
        [expression_service_binary, "analyzer", "stats", db_path, project_folder],
        capture_output=True,
    )
    if res.returncode != 0:
        print(res)
        print(res.stderr.decode())
    return res.returncode


@app.command()
def stats(db_path: Path, expression_service_binary: Path, projects_folder: Path):
    projects = [d for d in projects_folder.iterdir() if d.is_dir()]
    pool = Pool()
    analyze = partial(analyze_project, db_path, expression_service_binary)
    returncodes = list(
        tqdm(pool.imap_unordered(analyze, projects), total=len(projects))
    )
    successes = returncodes.count(0)
    failures = len(returncodes) - successes
    print(
        f"Analyzed {len(projects)} projects (successes: {successes}, failures: {failures})"
    )


@app.command()
def init(db_path: Path):
    with open("init_queries.sql", "r") as f:
        init_queries = f.read().replace("\n", "")
    con = sqlite3.connect(str(db_path.resolve()))
    cur = con.cursor()
    cur.executescript(init_queries)
    con.close()
    print(f"Initialized empty database at {db_path}")


if __name__ == "__main__":
    app()
