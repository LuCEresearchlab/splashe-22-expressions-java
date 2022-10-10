#!/usr/bin/env bash
#
# Usage
#
#   tools/build-executable.sh [target] [/path/to/out-file]
#
# - [target] can be "cli" or "server"
#
# Then run [/path/to/out-file] to run the java file as stand-alone executable.
# Note that the "java" command must be in $PATH in order for the executable to work.
#

bazel_bin=$(command -v bazelisk 2>/dev/null)
if [ -z "$bazel_bin" ]; then
  bazel_bin=bazel
fi

target="$1"
if [ -z "${target}" ]; then
  echo "Specify a target"
  exit 1
fi

output="$2"
if [ -z "${output}" ]; then
  echo "Specify an output file"
  exit 1
fi

available_targets=( $(${bazel_bin} query "//:*" | grep "_deploy.jar" | sed "s/_deploy.jar//" | sed "s|//:||") )
if [[ ! "${available_targets[*]}" =~  ${target} ]]; then
  echo "Error: target must be one of [${available_targets[*]}]"
  exit 1
fi


${bazel_bin} build "//:${target}_deploy.jar"

out_jar="bazel-bin/${target}_deploy.jar"
if [ -f "${out_jar}" ]; then
#  if [ -f "${output}" ]; then
#    rm -f "${output}"
#  fi
#
  {
    printf "#!/usr/bin/env sh\n"
    printf "test \$(command -v java)"
    printf " && exec java -jar \"\$0\" \"\$@\""
    printf " || echo \"Could not find java in \\\$PATH\" && exit 1\n"
    cat "${out_jar}"
  } > "${output}"
  chmod a+x "${output}"
fi

java -cp "${out_jar}" picocli.AutoComplete ch.usi.inf.luce.expr.cli.Main \
  -n "$(basename "${output}")" \
  -f \
  -o "${output}_completion" 2> /dev/null
