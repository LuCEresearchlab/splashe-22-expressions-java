package ch.usi.inf.luce.expr.analyzer.java;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD.TooManyMethods")
public class ExprConstructMapperTest {

    @Test
    public void testArrayInitializerNotExprConstruct() {
        final CompilationUnit ast = getAst("class X { int[] a = {1, 2}; } }");
        assertNotExpression(firstFieldInitializer(ast));
    }

    @Test
    public void testQualifiedNameAsFieldAccess() {
        final CompilationUnit ast = getAst("class X { int f = X.f2; int f2; } }");
        assertNodeType(ASTNode.FIELD_ACCESS, firstFieldInitializer(ast));
    }

    @Test
    public void testSimpleNameAsFieldAccess() {
        final CompilationUnit ast = getAst("class X { int f = f2; int f2; } }");
        assertNodeType(ASTNode.FIELD_ACCESS, firstFieldInitializer(ast));
    }

    @Test
    public void testSimpleNameAsEnumConstantThusFieldAccess() {
        final CompilationUnit ast = getAst("enum E { E1(); E1 m() { return E1; } }");
        final EnumDeclaration e = (EnumDeclaration) ast.types().get(0);
        final MethodDeclaration m = (MethodDeclaration) e.bodyDeclarations().get(0);
        final ReturnStatement ret = (ReturnStatement) m.getBody().statements().get(0);
        assertNodeType(ASTNode.FIELD_ACCESS, ret.getExpression());
    }

    @Test
    public void testSimpleNameAsLocalVariableAccess() {
        final CompilationUnit ast = getAst("class X { int m() { int v = 42; return v; } }");
        final ReturnStatement ret = (ReturnStatement) getClass(ast).getMethods()[0].getBody().statements().get(1);
        assertNodeType(ASTNode.SIMPLE_NAME, ret.getExpression());
    }

    @Test
    public void testSimpleNameAsParameterAccess() {
        final CompilationUnit ast = getAst("class X { int m(int p) { return p; } }");
        final ReturnStatement ret = (ReturnStatement) getClass(ast).getMethods()[0].getBody().statements().get(0);
        assertNodeType(ASTNode.SIMPLE_NAME, ret.getExpression());
    }

    @Test
    public void testSimpleNameNotExpr() {
        final CompilationUnit ast = getAst("class X { }");
        assertNotExpression(getClass(ast).getName());
    }

    @Test
    public void testQualifiedNameNotExpr() {
        final CompilationUnit ast = getAst("class X { int n = java.lang.Math.abs(1); }");
        final Expression qualifier = ((MethodInvocation) firstFieldInitializer(ast)).getExpression();
        Assert.assertEquals(ASTNode.QUALIFIED_NAME, qualifier.getNodeType());
        assertNotExpression(qualifier);
    }

    private void assertNotExpression(final ASTNode node) {
        Assert.assertFalse(ExprConstructMapper.isExpressionConstruct(node));
    }

    private void assertNodeType(final int nodeType, final ASTNode node) {
        Assert.assertEquals(Optional.of(nodeType), ExprConstructMapper.toConstruct(node));
    }

    private Expression firstFieldInitializer(final CompilationUnit ast) {
        return ((VariableDeclarationFragment)
                getClass(ast).getFields()[0].fragments().get(0)).getInitializer();
    }

    private TypeDeclaration getClass(final CompilationUnit ast) {
        return (TypeDeclaration) ast.types().get(0);
    }

    private CompilationUnit getAst(final String source) {
        final SourceCode sourceCode = new SourceCode(source);
        return AstNodeUtil.getCompilationUnit(sourceCode.code, new String[0], "");
    }

}
