load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name = "utils",
    srcs = glob(["src/com/android/tools/utils/*.java"]),
    visibility = [
        "//visibility:public",
    ],
)

java_binary(
    name = "antlr",
    srcs = glob(["src/com/android/tools/binaries/*.java"]),
    main_class = "com.android.tools.binaries.AntlrCompiler",
    visibility = [
        "//visibility:public",
    ],
    deps = [
        ":utils",
        "@maven//:org_antlr_antlr4",
    ],
)
