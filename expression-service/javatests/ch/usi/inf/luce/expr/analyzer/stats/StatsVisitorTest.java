package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import java.util.Map;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Assert;
import org.junit.Test;


@SuppressWarnings({"PMD.TooManyMethods"})
public class StatsVisitorTest {

    private final static SourceCode SOURCE_ANNOTATIONS_COMMENTS = new SourceCode("@foo class X { /* comment */ }");

    @Test
    public void testTwoClassesInOneCompilationUnit() {
        assertClassesMethods("public class X {} class Y {}", 2, 0);
    }
    
    @Test
    public void testTwoMethodsInOneClass() {
        assertClassesMethods("public class X { public void m() {} public int m2() { return 1; } }", 1, 2);
    }
    
    @Test
    public void testInnerClass() {
        assertClassesMethods("public class X { class Inner {} }", 2, 0);
    }
    
    @Test
    public void testAnonymousClassDeclaration() {
        assertClassesMethods("public class X { Object o = new Object(){ public void m() {}}; }", 2, 1);
    }

    @Test
    public void testNestedAnonymousClassDeclaration() {
        assertClassesMethods("public class X { Object o = new Object(){ public void m() { Object o = new Object(){ public void m() {}}; }}; }", 3, 2);
    }
    
    @Test
    public void testLambda() {
        // We expect lambda expressions NOT to be counted as class declarations.
        assertClassesMethods("public class X { java.util.function.Supplier<Integer> s = () -> 1; }", 1, 0);
    }

    private void assertClassesMethods(final String source, final int expectedClasses, final int expectedMethods) {
        final SourceCode sourceCode = new SourceCode(source);
        final StatsVisitor visitor = compileAndVisit(sourceCode);
        final Map<String, Integer> allStats = visitor.getStats();
        Assert.assertEquals((expectedClasses > 0 ? 1 : 0) + (expectedMethods > 0 ? 1 : 0), allStats.size());
        if (expectedClasses > 0) {
            Assert.assertTrue(allStats.containsKey(StatsVisitor.CLASS_DECLARATION));
            Assert.assertEquals(expectedClasses, allStats.get(StatsVisitor.CLASS_DECLARATION).intValue());
        }
        if (expectedMethods > 0) {
            Assert.assertTrue(allStats.containsKey(StatsVisitor.METHOD_DECLARATION));
            Assert.assertEquals(expectedMethods, allStats.get(StatsVisitor.METHOD_DECLARATION).intValue());
        }
    }

    @Test
    public void testAstNodesCount() {
        final SourceCode sourceCode = new SourceCode("public class X { public int m() { return 1 + 2; } }");
        final StatsVisitor visitor = compileAndVisit(sourceCode);
        // 1. Compilation Unit
        // 2. TypeDeclaration
        // 3. SimpleName (inside AbstractTypeDeclaration)
        // 4. Modifier (inside BodyDeclaration for TypeDecl)
        // 5. MethodDeclaration
        // 6. SimpleName (inside MethodDeclaration)
        // 7. PrimitiveType (return type inside MethodDeclaration)
        // 8. Modifier (inside BodyDeclaration for MethodDecl)
        // 9. Block
        // 10. ReturnStatement
        // 11. InfixExpression
        // 12. NumberLiteral
        // 13. NumberLiteral
        Assert.assertEquals(13, visitor.astNodesCount());
    }


    private StatsVisitor compileAndVisit(final SourceCode source) {
        final StatsVisitor visitor = new StatsVisitor(source);
        final CompilationUnit ast = AstNodeUtil.getCompilationUnit(source.code, new String[0], "");
        ast.accept(visitor);
        return visitor;
    }

    @Test
    public void testAnnotationTokensCount() {
        final StatsVisitor visitor = compileAndVisit(SOURCE_ANNOTATIONS_COMMENTS);
        Assert.assertEquals(2, visitor.annotationsTokensCount());
    }

    @Test
    public void testNodesCount() {
        final StatsVisitor visitor = compileAndVisit(SOURCE_ANNOTATIONS_COMMENTS);
        Assert.assertEquals(3, visitor.astNodesCount());
    }

}
