load("@rules_java//java:defs.bzl", "java_test")

java_test(
    name = "range",
    srcs = glob([
        "AllTests.java",
        "**/*Test.java",
    ]),
    test_class = "ch.usi.inf.luce.expr.util.range.AllTests",
    deps = [
        "//java/ch/usi/inf/luce/expr/util/range",
        "@maven//:com_pholser_junit_quickcheck_core",
        "@maven//:com_pholser_junit_quickcheck_generators",
        "@maven//:junit_junit",
    ],
)
