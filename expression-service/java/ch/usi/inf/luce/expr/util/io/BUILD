load("@rules_java//java:defs.bzl", "java_library")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "io",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//javatests/ch/usi/inf/luce/expr/analyzer:__subpackages__",
        "//javatests/ch/usi/inf/luce/expr/rule:__pkg__",
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
