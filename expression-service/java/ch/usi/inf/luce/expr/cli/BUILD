load("@rules_java//java:defs.bzl", "java_library")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "cli",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//:__pkg__",
    ],
    deps = [
        "//java/ch/usi/inf/luce/expr/analyzer/cli",
        "@maven//:info_picocli_picocli",
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
