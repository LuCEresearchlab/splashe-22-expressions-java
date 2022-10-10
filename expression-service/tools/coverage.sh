#!/usr/bin/env bash
#
# Usage
#
#   COVERAGE_CPUS=32 tools/coverage.sh [/path/to/report-directory/]
#
# COVERAGE_CPUS defaults to 2, and the default destination is a temp
# dir.
#
# Based on https://gerrit.googlesource.com/gerrit/+/refs/heads/stable-3.3/tools/coverage.sh
# With modifications specific for this project
#

bazel_bin=$(command -v bazelisk 2>/dev/null)
if [ -z "$bazel_bin" ]; then
  bazel_bin=bazel
fi

genhtml=$(command -v genhtml 2>/dev/null)
if [ -z "${genhtml}" ]; then
  echo "Install 'genhtml' (contained in the 'lcov' package)"
  exit 1
fi

destdir="$1"
if [ -z "${destdir}" ]; then
  echo "Specify a destination directory"
  exit 1
fi

destdir="$1/output"
if [ ! -d "${destdir}" ]; then
  mkdir -p "${destdir}"
fi

echo "Running 'bazel coverage'; this may take a while"
# coverage is expensive to run; use --jobs=2 to avoid overloading the
# machine.
${bazel_bin} coverage -k --jobs="${COVERAGE_CPUS:-2}" -- ...

# The coverage data contains filenames relative to the Java root, and
# genhtml has no logic to search these elsewhere. Workaround this
# limitation by running genhtml in a directory with the files in the
# right place. Also -inexplicably- genhtml wants to have the source
# files relative to the output directory.
mkdir -p "${destdir}/java"
cp -r {java,javatests}/* "${destdir}/java"

base=$(${bazel_bin} info bazel-testlogs)
find "${base}" -name "coverage.dat" -exec \
  sh -c 'cp "$0" "$1/$(echo "$0" | sed "s|$2/||" | sed "s|/|_|g")"' {} "${destdir}" "${base}" \;

find "${destdir}" -name "*coverage.dat" -size 0 -delete
cd "${destdir}" || exit 1
genhtml -o . --ignore-errors source *coverage.dat

# Remove java sources
rm -r java

# Zip and remove tmp files
zip -r coverage.zip .
if [ -f coverage.zip ]; then
  mv coverage.zip ../coverage.zip
  echo "coverage report at file://${destdir}/coverage.zip"
fi

cd .. || exit 1
rm -rf "output"
