This repository contains the tools used to gather statistics about expressions using Java source code from the Blackbox database.

# Architecture

This Python project is managed using [Poetry](https://python-poetry.org/),
which is required to install the dependencies and run the tools.

This project makes use of `expression-service`. See the [expression-service repository](https://github.com/LuCEresearchlab/expression-service/) for instructions on how to obtain and build it.

A bird's eye view on the workflow:

- the source files of several Java projects are downloaded from the Blackbox database;
- those sources are parsed and analyzed using `expression-service` and the raw results are stored in a SQLite database;
- queries on the database produce aggregate data and information, also turned into plots.

# Usage

1. Clone this repository and `cd` into its directory:
    ```sh
    cd expression-stats
    ```
1. Configure a Poetry environment with the dependencies:
    ```sh
    poetry install
    ```
1. Download Java projects from BlackBox or use the already-provided demo folder (`sources_demo`) which contains some sample projects.
   To download from BlackBox:
    1. Copy the default secrets inside `.env` and *change* them using your credentials:
        ```sh
        cp example.env .env
        ```
    1. Map MySQL's port from BlackBox to localhost via SSH:
        ```sh
        ssh -L 3306:localhost:3306 <your_username>@white.bluej.org
        ```
    1. Execute the "blackbox extractor" tool to download the relevant source files of successful compilation events in a given period of time:
        ```sh
        poetry run python3 expression_stats/main.py extract-blackbox "2022-05-01 00:00:00" "2022-05-02 00:00:00" sources
        ```
1. Initialize a new, empty SQLite database to store the statistics:
    ```sh
    poetry run python3 expression_stats/main.py init db.sqlite3
    ```
1. Run `expression-service` to parse/compile the Java source files (eventually replacing `sources_demo` with the folder that contains your actual sources) and gather statistics, storing them inside the SQLite database:
    ```sh
    poetry run python expression_stats/main.py stats db.sqlite3 ../../expression-service/bazel-bin/cli sources_demo
    ```
1. Analyze the database and produce answers to the six Research Questions:
    ```sh
    poetry run python3 expression_stats/main.py analysis db.sqlite3
    ```

# How we select projects from Blackbox

As part of our analysis, we need not only to parse Java source files, but also to resolve bindings: we want to know what each name refers to. We need therefore to analyze projects that successfully compile.

Another requirement for our purposes is to avoid "double counting": a fragment of (potentially evolving) code shouldn't be analyzed more than once. How can one determine identity? In our scenario, this is a bit tricky. BlueJ has the notion of a _project_. However, given their use in education, projects in BlueJ cannot be equated to traditional projects in Software Engineering: they are often long-lived, with several classes being added and deleted, and they might contain "logically different" projects. 

Moreover, compilations in BlueJ can happen at a different granularity: a user might request (or, autocompilation might trigger) the compilation of a single file or of a whole package. While we can determine which files are being compiled at a given event, this set does not include "dependencies" (e.g., the compilation might have been requested for class `A`, which contains a reference of type `B`: `B` is part of the project and needed to compile `A`, but is not included in the set).

Taking into account all of this, we gather data as follows:

- out of all compilations events, we keep only the _successful_ ones;
- in the specified time window, we consider only the _latest_ successful compilation event per each _project_;
- we retrieve _all_ the Java source files in that project at that compilation event.

Only a residual fraction (<2%) of source files cannot be downloaded due to internal consistency errors in the Blackbox database.

Even after these careful considerations, a minority (~10-15%) of source files cannot be compiled successfully. This can be due to a variety of reasons, out of which the two main ones are:

- the use of external dependencies;
- projects that contained some files that were not compiling but were also not involved in the selected successful compilation event (e.g., a class `C` containing errors, but the compilation was requested for class `A` which does not require `C`).
