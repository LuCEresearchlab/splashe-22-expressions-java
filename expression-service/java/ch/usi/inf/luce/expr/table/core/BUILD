load("@rules_java//java:defs.bzl", "java_library")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "core",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//java/ch/usi/inf/luce/expr/table:__subpackages__",
        "//javatests/ch/usi/inf/luce/expr/analyzer:__subpackages__",
    ],
    exports = [
        "//java/ch/usi/inf/luce/expr/diagram",
    ],
    deps = [
        "//java/ch/usi/inf/luce/expr/analyzer/core",
        "//java/ch/usi/inf/luce/expr/diagram",
        "@maven//:com_fasterxml_jackson_core_jackson_annotations",
        "@maven//:com_fasterxml_jackson_core_jackson_core",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:org_eclipse_jdt_org_eclipse_jdt_core",
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
