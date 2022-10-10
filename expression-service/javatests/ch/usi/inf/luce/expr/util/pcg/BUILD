load("@rules_java//java:defs.bzl", "java_test")

java_test(
    name = "pcg",
    srcs = glob([
        "AllTests.java",
        "**/*Test.java",
    ]),
    test_class = "ch.usi.inf.luce.expr.util.pcg.AllTests",
    deps = [
        "//java/ch/usi/inf/luce/expr/util/pcg",
        "@maven//:junit_junit",
    ],
)
