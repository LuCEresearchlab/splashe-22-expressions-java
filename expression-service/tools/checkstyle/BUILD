load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name = "wrapper_impl",
    srcs = [
        "src/checkstyle/CheckStyleWrapper.java",
    ],
    deps = [
        "@maven//:com_puppycrawl_tools_checkstyle",
    ],
)

java_binary(
    name = "wrapper",
    main_class = "checkstyle.CheckStyleWrapper",
    visibility = [
        "//visibility:public",
    ],
    runtime_deps = [
        ":wrapper_impl",
    ],
)
