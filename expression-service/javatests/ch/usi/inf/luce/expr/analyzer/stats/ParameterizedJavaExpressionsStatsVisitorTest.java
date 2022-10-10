package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.analyzer.core.ParserException;
import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import ch.usi.inf.luce.expr.analyzer.java.JavaSourceCodeUtil;
import ch.usi.inf.luce.expr.table.core.DefinitionEntry;
import ch.usi.inf.luce.expr.table.core.TableEntryParser;
import ch.usi.inf.luce.expr.util.io.IoUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class ParameterizedJavaExpressionsStatsVisitorTest {

    @Parameter
    public String stmts;
    
    @Parameter(1)
    public String expressionNode;
    
    @Parameter(2)
    public int expectedTokens;

    @Parameters(name = "{index} - testStatsAnalyzer with stmts: {0}")
    public static Object[][] data() throws IOException {
        final List<DefinitionEntry> definitionEntries = new TableEntryParser().getDefinitionEntries(IoUtils.readFile("definitions.json"));
        final Object[][] inputs = new Object[definitionEntries.size()][];
        for (int i = 0; i < definitionEntries.size(); i++) {
            final DefinitionEntry entry = definitionEntries.get(i);
            inputs[i] = new Object[] {entry.example,
                                      entry.expressionNode,
                                      entry.expressionTokens};
        }
        return inputs;
    }

    @Test
    public void testStatsProperties() throws ParserException {
        final SourceCode sourceCode = JavaSourceCodeUtil.stmtsToCompilationUnit(new SourceCode(stmts));
        final ExpressionsStatsVisitor visitor = new ExpressionsStatsVisitor(sourceCode);
        final CompilationUnit ast = AstNodeUtil.getCompilationUnit(sourceCode.code, new String[0], "");
        AstNodeUtil.assertNoErrors(ast);
        ast.accept(visitor);
        final Set<StatsInfo> stats = visitor.getExpressionsStats();
        // Assert that we collected statistics about at least one expression.
        Assert.assertFalse(stats.isEmpty());

        // Basic sanity check: the number of characters is never less than the
        // number of tokens.
        for (final StatsInfo nodeStats : stats) {
            Assert.assertTrue(nodeStats.getCharCount() >= nodeStats.getTokenCount());
        }

        // When we have information about the expected simple name of the
        // ASTNode class that should match the expression, and its expected
        // length in number of tokens, add assertions on them.
        if (expressionNode != null) {
            final Set<String> exprRootNodeNames = new HashSet<>();
            for (final StatsInfo nodeStats : stats) {
                final TreePath nodePath = nodeStats.getTreePath();
                final String exprRootNodeName = nodePath.pathToExprRoot().getLeafName();
                exprRootNodeNames.add(exprRootNodeName);
                if (exprRootNodeName.equals(expressionNode) && nodePath.isExprRoot()) {
                    Assert.assertEquals(expectedTokens, nodeStats.getTokenCount());
                }
            }
            Assert.assertTrue(exprRootNodeNames.contains(expressionNode));
        }
    }

}
