load("@rules_java//java:defs.bzl", "java_library", "java_test")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "tui",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//java/ch/usi/inf/luce/expr/analyzer/cli:__pkg__",
        "//javatests/ch/usi/inf/luce/expr/util/tui:__pkg__",
    ],
    deps = [
        "@maven//:org_jetbrains_annotations",
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
