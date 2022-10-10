package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert", "PMD.TooManyMethods"})
public class StatsTest {

    private final static String SUM_EXTRACTED = "class C { int m() { int a = 1; int b = 2; return a + b; } }";
    private final static String SUM_INLINE = "class C { int m() { return 1 + 2; } }";

    private final static double TOLERANCE = 0.0001;
    
    @Test
    public void testExpressionSumInline() throws InvalidInputException, IOException, SQLException {
        testExpressionsPercentage(SUM_INLINE, 3, 5);
        testAvgExpressionNodes(SUM_INLINE, 3, 1, 1);
    }

    @Test
    public void testExpressionSumExtracted() throws InvalidInputException, IOException, SQLException {
        testExpressionsPercentage(SUM_EXTRACTED, 5, 7);
        testAvgExpressionNodes(SUM_EXTRACTED, 5, 1, 1);
    }

    @Test
    public void testAvgExpressionSize() throws IOException, SQLException {
        testAvgExpressionSize(SUM_INLINE, 3, 1);
        testAvgExpressionSize(SUM_EXTRACTED, 5, 3);
    }

    @Test
    public void testAvgTreeHeight() throws IOException, SQLException {
        testAvgTreeHeight(SUM_EXTRACTED, new int[] {0, 0, 1});
        testAvgTreeHeight(SUM_INLINE, new int[] {1});
    }
    
    @Test
    public void testExpressionNodesPrevalence() throws IOException, SQLException {
        testExpressionNodesPrevalence(SUM_EXTRACTED, Map.of("NumberLiteral", 2, "InfixExpression", 1, "SimpleName", 2));
        testExpressionNodesPrevalence(SUM_INLINE, Map.of("NumberLiteral", 2, "InfixExpression", 1));
    }

    @Test
    public void testExpressionNodesInProjects() throws IOException, SQLException {
        final String[] fileNames = {"UsingClass.java", "WithClass.java"};
        final JavaStatsDbManager dbManager = Utils.analyzeFiles(fileNames);
        Utils.analyzeAdditionalFiles(dbManager, new String[] {"HelloWorld.java"});
        final Stats stats = new Stats(dbManager);
        final Map<String, Double> expectedNodeInProjects = Map.ofEntries(Map.entry("NumberLiteral", 0.5),
                                                                         Map.entry("ClassInstanceCreation", 0.5),
                                                                         Map.entry("MethodInvocation", 1.0),
                                                                         Map.entry("StringLiteral", 0.5),
                                                                         Map.entry("SimpleName", 0.5),
                                                                         Map.entry("InfixExpression", 0.5),
                                                                         Map.entry("FieldAccess", 0.5));
        compareMapsWithTolerance(expectedNodeInProjects, stats.expressionNodesPresenceInProjects());
    }

    private void compareMapsWithTolerance(final Map<String, Double> expectedMap, final Map<String, Double> actualMap) {
        Assert.assertEquals(expectedMap.size(), actualMap.size());
        for (final Map.Entry<String, Double> e : expectedMap.entrySet()) {
            Assert.assertTrue(actualMap.containsKey(e.getKey()));
            Assert.assertEquals(actualMap.get(e.getKey()), e.getValue(), TOLERANCE);
        }
    }

    private void testExpressionNodesPrevalence(final String source, final Map<String, Integer> occurrences) throws IOException, SQLException {
        final int nodes = occurrences.values().stream().mapToInt(Integer::intValue).sum();
        final Map<String, Double> expectedPrevalence =
                occurrences.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / ((double) nodes))); 
        final Stats stats = computeStats(source);
        compareMapsWithTolerance(expectedPrevalence, stats.expressionNodesPrevalence());
        // The prevalence should add up to 100% (i.e., 1.0)
        Assert.assertEquals(1.0, stats.expressionNodesPrevalence().values()
                .stream().mapToDouble(Double::doubleValue).sum(), TOLERANCE);
    }
    
    private void testAvgTreeHeight(final String source, final int[] heights) throws IOException, SQLException {
        final Stats stats = computeStats(source);
        final double expectedAvgHeight = Arrays.stream(heights).average().orElseThrow();
        Assert.assertEquals(expectedAvgHeight, stats.avgTreeHeightPerExpression(), TOLERANCE);
    }

    private void testAvgExpressionSize(final String source, final int expectedTokens, final int expectedExpressions) throws IOException, SQLException {
        final Stats stats = computeStats(source);
        final double expectedAvgExprSize =  ((double) expectedTokens) / expectedExpressions;
        Assert.assertEquals(expectedAvgExprSize, stats.avgAstNodesPerExpression(), TOLERANCE);
    }

    private void testExpressionsPercentage(final String source, final int expectedTokens, final int expectedChars)
            throws InvalidInputException, IOException, SQLException {
        final Stats stats = computeStats(source);
        final double expectedPercentageInTokens = ((double) expectedTokens) / countTokens(source);
        Assert.assertEquals(expectedPercentageInTokens, stats.expressionsPercentageInTokens(), TOLERANCE);
        final double expectedPercentageInChars = ((double) expectedChars) / source.length();
        Assert.assertEquals(expectedPercentageInChars, stats.expressionsPercentageInChars(), TOLERANCE);
    }
    
    private int countTokens(final String source) throws InvalidInputException {
        return AstNodeUtil.tokensInSource(new SourceCode(source));
    }
    
    @SuppressWarnings("SameParameterValue")
    private void testAvgExpressionNodes(final String source,
                                        final int expectedExpressionNodes,
                                        final int expectedClasses,
                                        final int expectedMethods) throws IOException, SQLException {
        final Stats stats = computeStats(source);
        final double expectedAvgNodesPerClass = ((double) expectedExpressionNodes) / expectedClasses;
        Assert.assertEquals(expectedAvgNodesPerClass, stats.avgExpressionNodesPerClass(), TOLERANCE);
        final double expectedPercentageInChars = ((double) expectedExpressionNodes) / expectedMethods;
        Assert.assertEquals(expectedPercentageInChars, stats.avgExpressionNodesPerMethod(), TOLERANCE);
    }

    private Stats computeStats(final String source) throws IOException {
        final JavaStatsDbManager dbManager = Utils.analyzeSources(new String[] { source }, new String[] {"test.java"});
        return new Stats(dbManager);
    }

}
