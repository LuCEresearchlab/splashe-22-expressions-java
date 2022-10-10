# Code quality

## Continuous integration

GitHub Actions are defined in the [bazel.yml](../.github/workflows/bazel.yml) file.
When running the GitHub Actions pipeline will compile all the code, run all the
code quality checks and run all tests.

## Quality

- CheckStyle is made available thanks to a custom bazel rule. Add a `checkstyle_check` 
  target to a module specifying with the `srcs` attribute the files to analyze.
  The build will fail if any warning or error is given.
  Running `bazel build //path/to/package:checkstyle` will run checkstyle and
  eventually output the errors and violations. To see all the available
  `:checkstyle` targets, run
  ```shell
  bazel query "//..." --notool_deps | grep ":checkstyle$"
  ```

- PMD is made available thanks to a custom bazel rule. Add a `pmd_check` target
  to a module specifying with the `srcs` attribute the files to analyze.
  The build will fail if any violation is found.
  Running `bazel build //path/to/package:pmd` will run pmd and eventually output
  the errors and violations. To see all the available `:pmd` targets, run
  ```shell
  bazel query "//..." --notool_deps | grep ":pmd$"
  ```
- ErrorProne is built into the default java toolchain to report errors at
  compile time.
- Running `bazel build //...` or `bazel test //...` will also run all the tools for
  quality checks

## Tests

- Tests are available in the [javatests](../javatests) directory, and they 
  are run with `junit4`
- Code coverage can be measured with `lcov`. A convenience script
  that provides global coverage data can be found in `tools/coverage.sh`
  and it requires a single argument for the output directory in which a
  `coverage.zip` report file will be generated. Inside the zip there will
  be a report in the `index.html` file. Code coverage data is generated
  using the `bazel coverage` command. Refer to the
  [official documentation](https://docs.bazel.build/versions/4.0.0/command-line-reference.html#coverage)
  for more information.
    - Example usage: 
      ```shell
      ./tools/coverage.sh .local
      ```
      will generate `.local/coverage.zip` with the html code coverage report.
