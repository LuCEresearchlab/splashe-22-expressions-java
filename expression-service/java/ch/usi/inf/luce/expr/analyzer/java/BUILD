load("@rules_java//java:defs.bzl", "java_library")
load("//tools/checkstyle:checkstyle.bzl", "checkstyle_check")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_library(
    name = "java",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//java/ch/usi/inf/luce/expr/analyzer:__subpackages__",
        "//java/ch/usi/inf/luce/expr/table/java:__pkg__",
        "//javatests/ch/usi/inf/luce/expr/analyzer:__subpackages__",
        "//javatests/ch/usi/inf/luce/expr/diagram:__subpackages__",
    ],
    exports = [
        "//java/ch/usi/inf/luce/expr/analyzer/core",
        "//java/ch/usi/inf/luce/expr/diagram",
    ],
    deps = [
        "//java/ch/usi/inf/luce/expr/analyzer/core",
        "//java/ch/usi/inf/luce/expr/diagram",
        "@maven//:org_apache_commons_commons_lang3",
        "@maven//:org_eclipse_jdt_org_eclipse_jdt_core",
        "@maven//:org_eclipse_platform_org_eclipse_text",
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