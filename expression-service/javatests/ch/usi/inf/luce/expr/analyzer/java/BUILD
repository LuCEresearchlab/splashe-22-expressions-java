load("@rules_java//java:defs.bzl", "java_test")
load("//tools/pmd:pmd.bzl", "pmd_check")

java_test(
    name = "java",
    srcs = glob([
        "AllTests.java",
        "**/*Test.java",
    ]),
    resource_strip_prefix = "resources/ch/usi/inf/luce/expr/analyzer/java/test",
    resources = [
        "//resources/ch/usi/inf/luce/expr/analyzer/java:test",
    ],
    test_class = "ch.usi.inf.luce.expr.analyzer.java.AllTests",
    visibility = [
        "//javatests/ch/usi/inf/luce/expr/analyzer:__subpackages__",
    ],
    deps = [
        "//java/ch/usi/inf/luce/expr/analyzer/java",
        "//java/ch/usi/inf/luce/expr/util/io",
        "@maven//:junit_junit",
        "@maven//:org_apache_commons_commons_lang3",
        "@maven//:org_eclipse_jdt_org_eclipse_jdt_core",
        "@maven//:org_jetbrains_annotations",
    ],
)

pmd_check(
    name = "pmd",
    srcs = glob([
        "ParameterizedJavaExprTreeDiagramAnalyzerTest.java",
        "ExprConstructMapperTest.java",
    ]),
)
