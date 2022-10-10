# Expression Service

This repository contains services to analyze expressions (e.g., compute statistics).

Expression Service is built with [Bazel](https://bazel.build). See [docs/build-run.md](docs/build-run.md) for information how to build and run.

Services can be accessed via stand-alone command line tools (based on the [picocli](https://picocli.info/) one-file CLI framework).

Currently, the following key tools are included:

* **Analyzer** - find expressions in source code and compute statistics:
  [cli](java/ch/usi/inf/luce/expr/analyzer/cli/AnalyzerCmdModule.java);
* **Table** - declarative documentation generation for expression constructs
  ([example input](resources/ch/usi/inf/luce/expr/table/java/test/definitions.json)).

## Further Documentation

The documentation is available in the [`docs`](docs) folder.