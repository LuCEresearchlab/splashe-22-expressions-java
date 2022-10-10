from csv import writer
from operator import itemgetter
from numpy import mean, median

from analysis_queries import (
    expr_nodes_in_projects,
    expr_nodes_per_method,
    expr_nodes_per_type,
    nodes_per_expr,
    perc_expr_tokens_per_project,
    tree_height_per_expr,
)
from expr_constructs import ast_to_expr_construct
from matplotlib.pyplot import bar, figure, pie, savefig, title, xticks
from seaborn import boxplot
from sqlalchemy.engine import Engine


def rq1_2_3_4(engine: Engine):
    rqs_values = [
        perc_expr_tokens_per_project(engine),
        expr_nodes_per_method(engine),
        nodes_per_expr(engine),
        tree_height_per_expr(engine),
    ]
    rqs = [
        f"% of expression tokens per project",
        f"# of expression AST nodes per method",
        f"AST nodes per expression",
        f'Tree height per expression ("0 based")',
    ]
    for idx, (values, rq) in enumerate(zip(rqs_values, rqs), start=1):
        figure()
        boxplot(y=values, showfliers=False).set_title(
            f"Median {median(values):.2f} - Average {mean(values):.2f}"
        )
        # Tight white space around the figure
        savefig(f"rq{idx}.png", bbox_inches="tight")


def rq5(engine: Engine):
    desc = "RQ5 - Prevalence of expression AST nodes"
    nodes_type = expr_nodes_per_type(engine)
    pie(x=nodes_type.values(), labels=nodes_type.keys(), autopct="%1.0f%%")
    title(desc)
    print(desc)
    print_nodes_table(sort_table(normalize(nodes_type)))
    save_table_as_csv("RQ5.csv", normalize(nodes_type).items())


def rq6(engine: Engine):
    desc = (
        f"RQ6 - % of projects containing at least one occurrence of given AST node type"
    )
    nodes_project_prevalence = expr_nodes_in_projects(engine)
    labels = list(nodes_project_prevalence.keys())
    values = list(nodes_project_prevalence.values())
    idxs = list(range(len(labels)))
    bar(x=idxs, height=values)
    xticks(idxs, labels)
    title(desc)
    print(desc)
    print_nodes_table(sort_table(nodes_project_prevalence))
    save_table_as_csv("RQ6.csv", nodes_project_prevalence.items())


def sort_table(d: dict[str, float]) -> list[tuple[str, float]]:
    return sorted(d.items(), key=itemgetter(1), reverse=True)


def normalize(d: dict[str, int]) -> dict[str, float]:
    total = sum(d.values())
    return {k: v / total for k, v in d.items()}


def print_nodes_table(items: list[tuple[str, float]]):
    for node, perc in items:
        print(f"{ast_to_expr_construct(node):<25} {perc:.2f}")


def save_table_as_csv(filename: str, items: list[tuple[str, float]]):
    with open(filename, "w") as f:
        csvwriter = writer(f)
        csvwriter.writerow(["node", "perc"])
        for item in items:
            csvwriter.writerow([item[0], f"{item[1]:.10f}"])
