load("@rules_java//java:defs.bzl", "java_library", "java_test")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "range",
    srcs = [
        "IntProgression.java",
        "IntRange.java",
    ],
    visibility = [
        "//java/ch/usi/inf/luce/expr/activity/core:__pkg__",
        "//java/ch/usi/inf/luce/expr/analyzer:__subpackages__",
        "//javatests/ch/usi/inf/luce/expr/util/range:__pkg__",
    ],
    deps = [
        "@maven//:com_fasterxml_jackson_core_jackson_annotations",
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
