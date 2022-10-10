package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.util.io.IoUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Utils {
    private Utils() {
    }

    public static JavaStatsDbManager analyzeFiles(final String[] fileNames) throws IOException {
        final Path tmpDir = copyFilesIntoTempDirectory(fileNames);
        return analyzeDir(tmpDir);
    }
    
    public static void analyzeAdditionalFiles(final JavaStatsDbManager dbManager, final String[] fileNames) throws IOException {
        final Path tmpDir = copyFilesIntoTempDirectory(fileNames);
        final JavaStatsAnalyzer analyzer = new JavaStatsAnalyzer(tmpDir, dbManager);
        analyzer.analyze();
    }
    
    public static JavaStatsDbManager analyzeSources(final String[] sources, final String[] fileNames) throws IOException {
        final Path tmpDir = writeSourcesAsFilesIntoTempDirectory(sources, fileNames);
        return analyzeDir(tmpDir);
    }

    public static JavaStatsDbManager analyzeDir(final Path tmpDir) throws IOException {
        final JavaStatsDbManager dbManager = initInMemoryDatabase();
        final JavaStatsAnalyzer analyzer = new JavaStatsAnalyzer(tmpDir, dbManager);
        analyzer.analyze();
        return dbManager;
    }

    public static Path copyFilesIntoTempDirectory(final String[] fileNames) throws IOException {
        // Read source files and write them in a temporary directory.
        final Path tmpDir = Files.createTempDirectory("sources");
        for (final String fileName : fileNames) {
            final String fileContent = IoUtils.readFile(fileName);
            final Path destinationFile = tmpDir.resolve(fileName);
            Files.write(destinationFile, fileContent.getBytes());
        }
        return tmpDir;
    }
    
    public static Path writeSourcesAsFilesIntoTempDirectory(final String[] sources, final String[] fileNames) throws IOException {
        // Read source files and write them in a temporary directory.
        final Path tmpDir = Files.createTempDirectory("sources");
        for (int i = 0; i < sources.length; i++) {
            final String source = sources[i];
            final String fileName = fileNames[i];
            final Path destinationFile = tmpDir.resolve(fileName);
            Files.write(destinationFile, source.getBytes());
        }
        return tmpDir;
    }

    public static JavaStatsDbManager initInMemoryDatabase() throws IOException {
        // Use in-memory SQLite and initialize it.
        final JavaStatsDbManager dbManager = new JavaStatsDbManager(":memory:");
        dbManager.executeQueries(IoUtils.readFile("init_queries.sql"));
        return dbManager;
    }
}
