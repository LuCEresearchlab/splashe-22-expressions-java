"""
PMD Bazel rule
"""

def _pmd_bazel_impl(ctx):
    input = []
    output = []

    args = ctx.actions.args()

    # Cache
    cache_file = ctx.actions.declare_file("pmd_%s.cache" % ctx.label.name)
    args.add("--cache", cache_file)
    output.append(cache_file)

    # Language
    args.add("-language", ctx.attr.language)
    if len(ctx.attr.language_version) > 0:
        args.add("-version", ctx.attr.language_version)

    # Report
    report_file = ctx.actions.declare_file("pmd_report_%s.%s" % (
        ctx.label.name,
        ctx.attr.report,
    ))
    args.add("--format", ctx.attr.report)
    args.add("--report-file", report_file)
    output.append(report_file)

    # Rules
    args.add_joined("--rulesets", ctx.files.rules, join_with = ",")
    input.extend(ctx.files.rules)

    # Target files
    srcs_file = ctx.actions.declare_file("pmd_srcs_%s.txt" % ctx.label.name)
    srcs_file_content = ",".join([src.path for src in ctx.files.srcs])
    ctx.actions.write(srcs_file, srcs_file_content, is_executable = False)
    args.add("--file-list", srcs_file)
    input.append(srcs_file)
    input.extend(ctx.files.srcs)

    ctx.actions.run(
        mnemonic = "PMD",
        executable = ctx.executable._pmd,
        inputs = input,
        outputs = output,
        arguments = [args],
    )

    return [DefaultInfo(files = depset(output))]

pmd_check = rule(
    attrs = {
        "language": attr.string(
            default = "java",
            values = [
                "java",
            ],
        ),
        "language_version": attr.string(
            default = "11",
            values = [
                "1.8",
                "1.9",
                "10",
                "11",
                "17",
            ],
        ),
        "srcs": attr.label_list(
            allow_empty = False,
            allow_files = True,
            mandatory = True,
        ),
        "report": attr.string(
            default = "text",
            values = [
                "html",
                "json",
                "text",
                "xml",
            ],
        ),
        "rules": attr.label_list(
            allow_empty = False,
            allow_files = True,
            default = [
                "//tools:pmd_java_rules",
            ],
        ),
        "_pmd": attr.label(
            executable = True,
            cfg = "host",
            default = Label("//tools/pmd:wrapper"),
            allow_files = True,
        ),
    },
    implementation = _pmd_bazel_impl,
)
