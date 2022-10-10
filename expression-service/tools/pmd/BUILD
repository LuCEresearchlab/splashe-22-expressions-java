load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name = "wrapper_impl",
    srcs = [
        "src/pmd/PmdWrapper.java",
    ],
    deps = [
        "@maven//:net_sourceforge_pmd_pmd_core",
        "@maven//:net_sourceforge_pmd_pmd_java",
    ],
)

java_binary(
    name = "wrapper",
    main_class = "pmd.PmdWrapper",
    visibility = [
        "//visibility:public",
    ],
    runtime_deps = [
        ":wrapper_impl",
    ],
)
