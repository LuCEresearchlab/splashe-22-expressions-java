"""
CheckStyle Bazel rule
"""

def _checkstyle_bazel_impl(ctx):
    input = []
    output = []

    args = ctx.actions.args()

    # Output
    report_file = ctx.actions.declare_file("checkstyle_report_%s.%s" % (
        ctx.label.name,
        ctx.attr.format,
    ))
    args.add("-f", ctx.attr.format)
    args.add("-o", report_file)
    output.append(report_file)

    # Configuration
    args.add("-c", ctx.file.configuration)
    input.append(ctx.file.configuration)

    # Target files
    args.add_all(ctx.files.srcs)
    input.extend(ctx.files.srcs)

    ctx.actions.run(
        mnemonic = "checkstyle",
        executable = ctx.executable._checkstyle,
        inputs = input,
        outputs = output,
        arguments = [args],
    )

    return [DefaultInfo(files = depset(output))]

checkstyle_check = rule(
    attrs = {
        "srcs": attr.label_list(
            allow_empty = False,
            allow_files = True,
            mandatory = True,
        ),
        "configuration": attr.label(
            allow_single_file = True,
            default = "//tools:checkstyle_java_rules",
        ),
        "format": attr.string(
            default = "plain",
            values = [
                "plain",
                "xml",
            ],
        ),
        "_checkstyle": attr.label(
            executable = True,
            cfg = "host",
            default = Label("//tools/checkstyle:wrapper"),
            allow_files = True,
        ),
    },
    implementation = _checkstyle_bazel_impl,
)
