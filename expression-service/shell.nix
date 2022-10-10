{ pkgs ? import (fetchTarball "http://nixos.org/channels/nixpkgs-22.05-darwin/nixexprs.tar.xz") {} }:
pkgs.mkShell {
  nativeBuildInputs = [
    pkgs.buildPackages.bazel_5
    pkgs.buildPackages.bazel-buildtools
    pkgs.buildPackages.git
    pkgs.buildPackages.graphviz
    pkgs.buildPackages.jdk11
    pkgs.buildPackages.lcov
    pkgs.buildPackages.zip
  ];
  shellHook = ''
    printf "Build with:\t\t'$ bazel build //...'\n"
    printf "Test with:\t\t'$ bazel test //...'\n"
    export JAVA_HOME="$(dirname $(dirname $(which java)))/zulu-11.jdk/Contents/Home"
  '';
}
