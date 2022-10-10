package ch.usi.inf.luce.expr.cli;

import ch.usi.inf.luce.expr.analyzer.cli.AnalyzerCmdModule;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

@CommandLine.Command(
        name = "expr-service",
        description = "ExpressionService provides various features to operate on expression and the ExprTreeDiagram data structure",
        subcommands = {
                CommandLine.HelpCommand.class,
                AnalyzerCmdModule.class,
        }
)
@SuppressWarnings({"PMD.SystemPrintln"})
public final class Main implements Callable<Integer> {

    private static final String MODULE_WEB_CLASS_NAME = "ch.usi.inf.luce.expr.web.WebServerModule";

    private Main() {
    }

    public static void main(@NotNull String[] args) {
        final CommandLine commandLine = new CommandLine(new Main());
        commandLine.setCaseInsensitiveEnumValuesAllowed(true);

        getModule(MODULE_WEB_CLASS_NAME).ifPresent(commandLine::addSubcommand);

        if (args.length == 0) {
            commandLine.usage(System.err);
            System.exit(CommandLine.ExitCode.USAGE);
        } else {
            final int exitCode = commandLine.execute(args);
            if (exitCode >= 0) {
                System.exit(exitCode);
            }
        }
    }

    @NotNull
    @SuppressWarnings("SameParameterValue")
    private static Optional<?> getModule(@NotNull String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            final Object instance = clazz.getDeclaredConstructor()
                    .newInstance();
            return Optional.of(instance);
        } catch (ClassNotFoundException
                 | NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException
                 | InstantiationException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer call() {
        return CommandLine.ExitCode.USAGE;
    }
}