load("@rules_java//java:defs.bzl", "java_library")

java_library(
    name = "src",
    srcs = glob([
        "**/*.java",
    ]),
    visibility = [
        "//:__pkg__",
    ],
    deps = [
        "@maven//:org_antlr_antlr4_runtime",
        "@maven//:org_antlr_antlr4",
    ],
)
