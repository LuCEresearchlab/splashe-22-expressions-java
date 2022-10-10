package ch.usi.inf.luce.expr.analyzer.stats;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import ch.usi.inf.luce.expr.util.io.IoUtils;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
public class JavaStatsAnalyzerTest {

    @Test
    public void testJavaAnalysisCompilationCharsTokens() throws IOException, SQLException {
        final String[] fileNames = {"UsingClass.java", "WithClass.java"};
        int totalChars = 0;
        for (final String fileName : fileNames) {
            final String fileContent = IoUtils.readFile(fileName);
            totalChars += fileContent.length();
        }
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(fileNames);
        // Assertions with data from the database.
        final FileStatsTable fileStatsTable = new FileStatsTable(dbManager);
        Assert.assertEquals(fileNames.length, fileStatsTable.countSuccessfullyCompiledFiles());
        Assert.assertEquals(totalChars, fileStatsTable.getTotalChars());
        // Tokens from both files counted by hand (to check against numbers reported by
        // Eclipse's tokenizer).
        Assert.assertEquals(34 + 26, fileStatsTable.getTotalTokens());
    }

    @Test
    public void testTokensCommentsAnnotations() throws IOException, SQLException {
        final String[] fileNames = {"CommentsAnnotations.java"};
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(fileNames);
        final FileStatsTable fileStatsTable = new FileStatsTable(dbManager);
        Assert.assertEquals(14, fileStatsTable.getTotalTokens());
    }

    @Test
    public void testCompilationError() throws IOException, SQLException {
        final String[] fileNames = {"CompileError.java"};
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(fileNames);
        final FileStatsTable fileStatsTable = new FileStatsTable(dbManager);
        Assert.assertEquals(0, fileStatsTable.countSuccessfullyCompiledFiles());
        Assert.assertEquals(0, fileStatsTable.getTotalChars());
        Assert.assertEquals(0, fileStatsTable.getTotalTokens());
    }
    
    @Test
    public void testCountDistinctExpressions() throws IOException, SQLException {
        final String fileName = "BinarySearch.java";
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(new String[] {fileName});
        final ExprStatsTable exprStatsTable = new ExprStatsTable(dbManager);
        Assert.assertEquals(12, exprStatsTable.countDistinctExpressionsInFile(fileName));
    }
    
    @Test
    public void testCountExpressionLength() throws IOException, SQLException {
        final String[] fileNames = {"UsingClass.java", "WithClass.java"};
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(fileNames);
        final ExprStatsTable exprStatsTable = new ExprStatsTable(dbManager);
        // Ensure that the length (measured in tokens and characters) matches. 
        Assert.assertEquals(10, exprStatsTable.countExpressionTokensInFile(fileNames[0]));
        Assert.assertEquals(29, exprStatsTable.countExpressionCharsInFile(fileNames[0]));
        Assert.assertEquals(1+7, exprStatsTable.countExpressionTokensInFile(fileNames[1]));
        Assert.assertEquals(1+9, exprStatsTable.countExpressionCharsInFile(fileNames[1]));
    }
    
    @Test
    public void testCountExpressionAstNodes() throws IOException, SQLException {
        final String[] fileNames = {"UsingClass.java", "WithClass.java"};
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(fileNames);
        final ExprStatsTable exprStatsTable = new ExprStatsTable(dbManager);
        Assert.assertEquals(2, exprStatsTable.countExpressionAstNodesInFile(fileNames[0]));
        Assert.assertEquals(6, exprStatsTable.countExpressionAstNodesInFile(fileNames[1]));
    }

    @Test
    public void testCountSpecificAstNode() throws IOException, SQLException {
        final String fileName = "BinarySearch.java";
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(new String[] {fileName});
        final ExprStatsTable exprStatsTable = new ExprStatsTable(dbManager);
        Assert.assertEquals(7, exprStatsTable.countAstNodeInFile(fileName, "NumberLiteral"));
        Assert.assertEquals(2, exprStatsTable.countAstNodeInFile(fileName, "ArrayCreation"));
    }
    
    @Test
    public void testCountFilesInProject() throws IOException, SQLException {
        final String[] fileNames = {"UsingClass.java", "WithClass.java"};
        final Path projectDir = Utils.copyFilesIntoTempDirectory(fileNames);
        final JavaStatsDbManager dbManager = Utils.initInMemoryDatabase();
        final JavaStatsAnalyzer analyzer = new JavaStatsAnalyzer(projectDir, dbManager);
        analyzer.analyze();
        final ProjectsTable projectsTable = new ProjectsTable(dbManager);
        Assert.assertEquals(fileNames.length, projectsTable.countFilesInProject(projectDir));
    }
    
    
}
