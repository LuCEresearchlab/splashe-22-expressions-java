# splashe-22-expressions-java

This repository contains the artifacts related to the SPLASH-E 2022 paper "Expressions in Java: Essential, Prevalent, Neglected?".

It consists of three folders:

- `antlr-grammar-analyzer` contains the tooling necessary to verify the source code in Listing 1, described in Section 3;
- `expression-stats` contains Python code that retrieves code from Blackbox, initiates the analysis, and computes the statistics described in Section 4;
- `expression-service` contains Java code that analyses a Java project and stores information in a database.

Each folder contains a `README.md` with a more detailed description of its content and instructions to build and run the programs.
