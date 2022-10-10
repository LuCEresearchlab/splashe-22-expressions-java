load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "antlr-grammar-analyzer",
    main_class = "src.Main",
    runtime_deps = [
        "//src"
    ]
)

java_binary(
    name = "antlr-grammar-noexpr",
    main_class = "src.NoExprGrammar",
    runtime_deps = [
        "//src"
    ]
)

java_binary(
    name = "fits-grammar-noexpr",
    main_class = "src.FitsWithinGrammar",
    runtime_deps = [
        "//src"
    ]
)
