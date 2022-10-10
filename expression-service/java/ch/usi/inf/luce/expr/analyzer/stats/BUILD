load("@rules_java//java:defs.bzl", "java_library")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "stats",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//java/ch/usi/inf/luce/expr/analyzer:__subpackages__",
        "//javatests/ch/usi/inf/luce/expr/analyzer:__subpackages__",
    ],
    exports = [
        "//java/ch/usi/inf/luce/expr/analyzer/core",
    ],
    deps = [
        "//java/ch/usi/inf/luce/expr/analyzer/core",
        "//java/ch/usi/inf/luce/expr/analyzer/java",
        "@maven//:org_eclipse_jdt_org_eclipse_jdt_core",
        "@maven//:org_jetbrains_annotations",
        "@maven//:org_xerial_sqlite_jdbc",
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
