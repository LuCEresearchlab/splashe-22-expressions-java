load("@rules_java//java:defs.bzl", "java_test")

java_test(
    name = "pair",
    srcs = glob([
        "AllTests.java",
        "**/*Test.java",
    ]),
    test_class = "ch.usi.inf.luce.expr.util.pair.AllTests",
    deps = [
        "//java/ch/usi/inf/luce/expr/util/pair",
        "@maven//:junit_junit",
    ],
)
