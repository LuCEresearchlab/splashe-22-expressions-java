import pytest

from expression_stats.analysis_queries import (
    expr_nodes_per_method,
    perc_expr_tokens_per_project,
    tree_height_per_expr,
    nodes_per_expr,
)
from sqlalchemy import create_engine
from sqlalchemy.engine import Engine

DEMO_DB = "demo.sqlite3"


@pytest.fixture(scope="module")
def engine():
    return create_engine(f"sqlite:///{DEMO_DB}")


def equals_no_order(list1: list, list2: list):
    assert sorted(list1) == sorted(list2)


def test_perc_expr_tokens_per_project(engine: Engine):
    equals_no_order(
        perc_expr_tokens_per_project(engine),
        [
            (3 + 14) / (20 + 41),  # Calculator + LazyCalculator
            9 / 24,  # Hello
        ],
    )


def test_expr_nodes_per_method(engine: Engine):
    equals_no_order(
        expr_nodes_per_method(engine),
        [
            3,  # Calculator.add(int, int)
            3,  # LazyCalculator.LazyCalculator()
            4,  # LazyCalculator.add(int, int)
            3,  # Hello.m()
        ],
    )


def test_nodes_per_expr(engine: Engine):
    equals_no_order(
        nodes_per_expr(engine),
        [
            3,  # "a + b" in Calculator.add(int, int)
            3,  # "calc = new Calculator()" in LazyCalculator.LazyCalculator()
            4,  # "calc.add(a, b)" in LazyCalculator.add(int, int)
            1,  # ""Hello World"" in Hello
            3,  # "System.out.println(f) in Hello.m()
        ],
    )


def test_tree_height_per_expr(engine: Engine):
    equals_no_order(
        tree_height_per_expr(engine),
        [
            1,  # "a + b" in Calculator.add(int, int)
            1,  # "calc = new Calculator()" in LazyCalculator.LazyCalculator()
            1,  # "calc.add(a, b)" in LazyCalculator.add(int, int)
            0,  # ""Hello World"" in Hello
            1,  # "System.out.println(f) in Hello.m()
        ],
    )
