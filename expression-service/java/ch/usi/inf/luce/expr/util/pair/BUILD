load("@rules_java//java:defs.bzl", "java_library", "java_test")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "pair",
    srcs = [
        "Pair.java",
    ],
    visibility = [
        "//java/ch/usi/inf/luce/expr/assess/core:__pkg__",
        "//javatests/ch/usi/inf/luce/expr/util/pair:__pkg__",
    ],
)

checkstyle_check(
    name = "checkstyle",
    srcs = glob([
        "**/*.java",
    ]),
)

pmd_check(
    name = "pmd",
    srcs = glob([
        "**/*.java",
    ]),
)
