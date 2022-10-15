from sqlalchemy import MetaData, Table, func, select
from sqlalchemy.engine import Engine
from sqlalchemy.sql import column, expression


def autoload_table(engine: Engine, name: str) -> Table:
    return Table(name, MetaData(), autoload=True, autoload_with=engine)


def info_per_expr(engine: Engine, sql_expr: expression) -> list[int]:
    exprstats = autoload_table(engine, "exprstats")
    values = []
    with engine.connect() as conn:
        stmt = (
            select(sql_expr)
            .select_from(exprstats)
            .group_by(exprstats.c.filename, exprstats.c.path_expr_root)
        )
        for row in conn.execute(stmt):
            values.append(row[0])
    return values


def nodes_per_expr(engine: Engine) -> list[int]:
    return info_per_expr(engine, func.count(column("path_expr")))


def tree_levels_per_expr(engine: Engine) -> list[int]:
    depths = info_per_expr(engine, func.max(column("depth")))
    return [d + 1 for d in depths]


def is_method_declaration(declaring_node_path: str) -> bool:
    return declaring_node_path.split("#")[0].endswith("MethodDeclaration")


def expr_nodes_per_method(engine: Engine) -> list[int]:
    exprstats = autoload_table(engine, "exprstats")
    nodes = []
    with engine.connect() as conn:
        stmt = (
            select([exprstats.c.declaring_node, func.count(exprstats.c.path_expr)])
            .select_from(exprstats)
            .group_by(exprstats.c.declaring_node)
        )
        for row in conn.execute(stmt):
            if is_method_declaration(row[0]):
                nodes.append(row[1])
    return nodes


def expr_nodes_per_type(engine: Engine) -> dict[str, int]:
    exprstats = autoload_table(engine, "exprstats")
    stmt = select(exprstats.c.ast_node, func.count(exprstats.c.ast_node)).group_by(
        exprstats.c.ast_node
    )
    type_count = {}
    with engine.connect() as conn:
        for row in conn.execute(stmt):
            type_count[row[0]] = row[1]
    return type_count


def expr_nodes_in_projects(engine: Engine) -> dict[str, float]:
    exprstats = autoload_table(engine, "exprstats")
    projects = autoload_table(engine, "projects")
    project_count_stmt = select(func.count(func.distinct(projects.c.project_path)))
    node_percentage = {}
    with engine.connect() as conn:
        total_projects = conn.execute(project_count_stmt).scalar()
        stmt = (
            select(
                exprstats.c.ast_node, func.count(func.distinct(projects.c.project_path))
            )
            .join(projects, exprstats.c.filename == projects.c.filename)
            .group_by(exprstats.c.ast_node)
        )
        for row in conn.execute(stmt):
            node_percentage[row[0]] = row[1] / total_projects
    return node_percentage


def perc_expr_tokens_per_project(engine: Engine) -> list[float]:
    exprstats = autoload_table(engine, "exprstats")
    projects = autoload_table(engine, "projects")
    filestats = autoload_table(engine, "filestats")
    total_tokens = {}
    expr_tokens = {}
    perc_tokens = {}
    with engine.connect() as conn:
        total_stmt = (
            select(projects.c.project_path, func.sum(filestats.c.tokens))
            .outerjoin(filestats, projects.c.filename == filestats.c.filename)
            .group_by(projects.c.project_path)
        )
        root_expr = (
            select(exprstats)
            .where(exprstats.c.path_expr == exprstats.c.path_expr_root)
            .subquery()
        )
        expr_stmt = (
            select(
                projects.c.project_path, func.coalesce(func.sum(root_expr.c.tokens), 0)
            )
            .outerjoin(root_expr, projects.c.filename == root_expr.c.filename)
            .group_by(projects.c.project_path)
        )
        for row in conn.execute(total_stmt):
            if row[1] > 0:
                total_tokens[row[0]] = row[1]
        for row in conn.execute(expr_stmt):
            expr_tokens[row[0]] = row[1]
        for project in total_tokens.keys():
            perc_tokens[project] = expr_tokens[project] / total_tokens[project]
    return list(perc_tokens.values())
