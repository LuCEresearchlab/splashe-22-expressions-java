load("@rules_java//java:defs.bzl", "java_library", "java_test")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "pcg",
    srcs = [
        "Pcg32.java",
        "RngProvider.java",
    ],
    visibility = [
        "//java/ch/usi/inf/luce/expr/generator/core:__pkg__",
        "//javatests/ch/usi/inf/luce/expr/util/pcg:__pkg__",
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
