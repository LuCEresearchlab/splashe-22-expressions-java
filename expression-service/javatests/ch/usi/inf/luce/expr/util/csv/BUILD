load("@rules_java//java:defs.bzl", "java_test")

java_test(
    name = "csv",
    srcs = glob([
        "AllTests.java",
        "**/*Test.java",
    ]),
    test_class = "ch.usi.inf.luce.expr.util.csv.AllTests",
    deps = [
        "//java/ch/usi/inf/luce/expr/util/csv",
        "@maven//:junit_junit",
    ],
)
