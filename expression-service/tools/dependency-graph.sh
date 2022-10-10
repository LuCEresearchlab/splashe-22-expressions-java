#!/usr/bin/env bash
#
# Usage
#
#   tools/dependency-graph.sh [/path/to/output]
#
# Make sure the output file includes an extension so that
# the script can use the proper format for the output.
# The most important available formats are "png", "jpg" and "svg"
#

bazel_bin=$(command -v bazelisk 2>/dev/null)
if [ -z "$bazel_bin" ]; then
  bazel_bin=bazel
fi

output="$1"
if [ -z "${output}" ]; then
  echo "Specify an output file"
  exit 1
fi

file_name=$(basename -- "${output}")
format="${file_name##*.}"

graph=$(bazel query "//..." --notool_deps --output graph)

to_remove=("checkstyle" "pmd" "test" "tools")
for it in "${to_remove[@]}"; do
  graph=$(echo "${graph}" | sed "/${it}/d")
done

dot_bin=$(command -v dot 2>/dev/null)
if [ -z "$dot_bin" ]; then
  echo "dot: command not found"
  exit 1
else
  echo "${graph}" | dot -T"${format}" > "${output}"
fi
