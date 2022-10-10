package ch.usi.inf.luce.expr.analyzer.cli;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
        name = "analyzer",
        subcommands = {
                StatsCmdModule.class,
        },
        description = "Analysis of Expressions"
)
public class AnalyzerCmdModule implements Callable<Integer> {

    @Override
    public Integer call() {
        // Print help message
        CommandLine.usage(this, System.out);
        return CommandLine.ExitCode.USAGE;
    }
}
