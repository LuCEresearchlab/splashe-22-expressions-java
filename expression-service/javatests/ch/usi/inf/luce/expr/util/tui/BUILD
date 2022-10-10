load("@rules_java//java:defs.bzl", "java_test")

java_test(
    name = "tui",
    srcs = glob([
        "AllTests.java",
        "**/*Test.java",
    ]),
    test_class = "ch.usi.inf.luce.expr.util.tui.AllTests",
    deps = [
        "//java/ch/usi/inf/luce/expr/util/tui",
        "@maven//:junit_junit",
    ],
)
