# Work environment

Ensure [bazel](https://bazel.build/) is installed on your system to compile and run the project.

The version of bazel needed is specified in [.bazelversion](../.bazelversion).
You can either explicitly install that version, or you can install [bazelisk](https://github.com/bazelbuild/bazelisk)
which then automatically installs whatever bazel version is configured in the `.bazelversion` file of a bazel project.
Note that bazelisk will install itself as `bazel`. So if you already installed bazel itself, remove it before installing
bazelisk. On macOS, you can install bazelisk with [Homebrew](https://brew.sh/).
You can install bazelisk with Homebrew, but do not install bazel itself with Homebrew (because unless you are careful,
Homebrew will automatically update its bazel installation, and you may end up with a newer version of bazel than is
needed to build this project).

Some extra tools (such as [coverage.sh](../tools/coverage.sh)) may require additional dependencies to run.

If the [nix package manager](https://nixos.org/) is available, it is also possible to simply use the included
[shell.nix](../shell.nix) to get a work environment with all the required dependencies (even in `--pure` mode).

# Building

This project uses the [bazel build system](https://bazel.build/).

- Build the whole project with:
  ```shell
  bazel build //...
  ```

# Running

## Command line

- With bazel:
  ```shell
  bazel run //:cli -- <args> 
  ```
- By hand, after having built the `//:cli` target:
  ```shell
  ./bazel-bin/cli <args>
  ```

# Testing

- Run all tests using bazel:
  ```shell
  bazel test //...
  ```
- Run tests for a specific module (e.g. `pcg`):
  ```shell
  bazel test //javatests/ch/usi/inf/luce/expr/util/pcg
  ```

# Deployable binaries

It is possible to generate portable executable binaries for the `//:cli` target.
To build an executable binary, run the following script:

```shell
tools/build-executable.sh cli $CLI_BIN
```

## Implementation details

The _"deployable binaries"_ are jar files that not only include all their
dependencies, but have also been modified to let the OS treat them as executables.
By inserting a shebang at the beginning of the jar file, the jar file will
invoke the `java` command from `$PATH` on itself.
If the `java -jar` command is running the file, the "shebang" part will be
skipped, and the java program executed as usual.
Even when executing the binary, it is also always possible to use any jvm
configuration parameters and flags as one would do with a jar file.
See [build-executable.sh](../tools/build-executable.sh) to see how the script
generates these _"deployable executables"_.
To convert a binary executable to a plain jar, remove the first 120 bytes from it:

```shell
tail -c +121 $CLI_BIN > "${CLI_BIN}.jar"
```