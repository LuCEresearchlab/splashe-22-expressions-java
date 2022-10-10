load("@rules_java//java:defs.bzl", "java_library")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "core",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//java/ch/usi/inf/luce/expr/analyzer:__subpackages__",
        "//java/ch/usi/inf/luce/expr/table:__subpackages__",
    ],
    exports = [
        "//java/ch/usi/inf/luce/expr/util/range",
    ],
    deps = [
        "//java/ch/usi/inf/luce/expr/util/range",
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
