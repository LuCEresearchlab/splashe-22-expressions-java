package ch.usi.inf.luce.expr.analyzer.cli;

import ch.usi.inf.luce.expr.analyzer.stats.JavaStatsAnalyzer;
import ch.usi.inf.luce.expr.analyzer.stats.JavaStatsDbManager;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

@CommandLine.Command(
        name = "stats",
        description = "Compute stats for the specified directory, save the output in a stats db file"
)
@SuppressWarnings({
        "CanBeFinal",
        "FieldMayBeFinal",
        "PMD.ImmutableField",
})
final class StatsCmdModule implements Callable<Integer> {

    @CommandLine.Parameters(
            arity = "1",
            index = "0",
            description = "Collect statistics in the specified SQLite database (providing a directory)")
    @NotNull
    private Path dbFile = Path.of("");

    @NotNull
    @CommandLine.Parameters(
            arity = "1",
            index = "1",
            description = "Input directory")
    private String inputs = "";


    @Override
    public Integer call() {
        if (inputs.isBlank()) {
            return CommandLine.ExitCode.USAGE;
        } else {
            final Path baseDir = Path.of(inputs);
            analyzeStats(baseDir, dbFile);
            return CommandLine.ExitCode.OK;
        }
    }

    private void analyzeStats(@NotNull Path directory, @NotNull Path dbFile) {
        // TODO: the stats module should be refactored to allow
        //       the same feature set for languages other than Java.
        final JavaStatsDbManager dbManager = new JavaStatsDbManager(dbFile);
        final JavaStatsAnalyzer analyzer = new JavaStatsAnalyzer(directory, dbManager);
        analyzer.analyze();
    }
}
