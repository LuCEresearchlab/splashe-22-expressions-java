package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Assert;
import org.junit.Test;


@SuppressWarnings({"PMD.TooManyMethods"})
public class TreePathTest {

    @Test
    public void testSimplePathStringRep() {
        final CompilationUnit ast = getAst("");
        final TreePath path = TreePath.getPath(ast);
        Assert.assertEquals("CompilationUnit", path.getStringRepresentation());
    }
    
    @Test
    public void testTypeDeclStringRep() {
        final CompilationUnit ast = getAst("public class X {}");
        final TreePath path = TreePath.getPath((TypeDeclaration)ast.types().get(0));
        Assert.assertEquals("CompilationUnit>TypeDeclaration", path.getStringRepresentation());
    }
    
    @Test
    public void testUniqueStringRep() {
        final CompilationUnit ast = getAst("class X {} class Y {}");
        final TreePath pathX = TreePath.getPath((TypeDeclaration)ast.types().get(0));
        final TreePath pathY = TreePath.getPath((TypeDeclaration)ast.types().get(1));
        Assert.assertEquals("CompilationUnit>TypeDeclaration", pathX.getStringRepresentation());
        Assert.assertEquals("CompilationUnit>TypeDeclaration", pathY.getStringRepresentation());
        Assert.assertNotEquals(pathX.getUniqueStringRepresentation(), pathY.getUniqueStringRepresentation());
    }
    
    @Test
    public void testGetLeafName() {
        final CompilationUnit ast = getAst("public class X {}");
        final TreePath path = TreePath.getPath((TypeDeclaration)ast.types().get(0));
        Assert.assertEquals("TypeDeclaration", path.getLeafName());
    }
    
    @Test
    public void testIsExprRoot() {
        final CompilationUnit ast = getAst("public class X { boolean b = !false; } }");
        final TreePath path = TreePath.getPath(((PrefixExpression)getFieldInitializer(ast)).getOperand());
        Assert.assertFalse(path.isExprRoot());
        final TreePath exprRootPath = path.pathToExprRoot();
        Assert.assertTrue(exprRootPath.isExprRoot());
    }

    @Test
    public void testPathToExprRoot() {
        final CompilationUnit ast = getAst("public class X { boolean b = !false; } }");
        final TreePath path = TreePath.getPath(((PrefixExpression)getFieldInitializer(ast)).getOperand());
        final TreePath exprRootPath = path.pathToExprRoot();
        Assert.assertEquals("PrefixExpression", exprRootPath.getLeafName());
    }

    @Test
    public void testDepth() {
        final CompilationUnit ast = getAst("public class X { boolean b = !false; } }");
        final TreePath path = TreePath.getPath(((PrefixExpression)getFieldInitializer(ast)).getOperand());
        Assert.assertEquals(1, path.depth());
        Assert.assertEquals(0, path.pathToExprRoot().depth());
    }

    @Test
    public void testParenthesizedExpression() {
        final CompilationUnit ast = getAst("public class X { int n = ((42)); } }");
        final TreePath path = TreePath.getPath(
                ((ParenthesizedExpression)
                        (((ParenthesizedExpression)getFieldInitializer(ast)).getExpression())).getExpression()
        );
        Assert.assertEquals("NumberLiteral", path.getLeafName());
        Assert.assertTrue(path.isExprRoot());
        Assert.assertEquals(0, path.depth());
    }

    @Test
    public void testParenthesizedExpressionCornerCase() {
        // "The use of parentheses affects only the order of evaluation, except for
        // a corner case whereby (-2147483648) and (-9223372036854775808L) are legal
        // but -(2147483648) and -(9223372036854775808L) are illegal."
        final CompilationUnit ast = getAst("public class X { int n = (-2147483648); } }");
        final TreePath path = TreePath.getPath(((ParenthesizedExpression)getFieldInitializer(ast)).getExpression());
        Assert.assertEquals("NumberLiteral", path.getLeafName());
        Assert.assertTrue(path.isExprRoot());
        Assert.assertEquals(0, path.depth());
    }

    @Test
    public void testIsPrefixOf() {
        final CompilationUnit ast = getAst("public class X {}");
        final TreePath typeDeclPath = TreePath.getPath((TypeDeclaration)ast.types().get(0));
        final TreePath cuPath = TreePath.getPath(ast);
        Assert.assertTrue(cuPath.isPrefixOf(typeDeclPath));
        Assert.assertFalse(typeDeclPath.isPrefixOf(cuPath));
    }
    
    @Test
    public void testIsPrefixOfMoreThanOneNodeSameAsParent() {
        final CompilationUnit ast = getAst("public class X { public void m() {} } public class Y {}");
        // X's path should be a prefix of m, but Y should not!
        final TreePath classXPath = TreePath.getPath((TypeDeclaration)ast.types().get(0));
        final TreePath classYPath = TreePath.getPath((TypeDeclaration)ast.types().get(1));
        final TreePath methodPath = TreePath.getPath(((TypeDeclaration)ast.types().get(0)).getMethods()[0]);
        Assert.assertTrue(classXPath.isPrefixOf(methodPath));
        Assert.assertFalse(classYPath.isPrefixOf(methodPath));
    }
    
    @Test
    public void testPathToDeclaringNodeFieldDeclaration() {
        final CompilationUnit ast = getAst("public class X { int f = 1; } }");
        final TreePath path = TreePath.getPath(getFieldInitializer(ast));
        Assert.assertEquals("FieldDeclaration", path.pathToDeclaringNode().getLeafName());
    }

    @Test
    public void testPathToDeclaringNodeMethodDeclaration() {
        final CompilationUnit ast = getAst("public class X { int m() { return 1; } }");
        final TreePath path = TreePath.getPath(((ReturnStatement)((TypeDeclaration)ast.types().get(0))
                                                 .getMethods()[0].getBody().statements().get(0))
                                                 .getExpression());
        Assert.assertEquals("MethodDeclaration", path.pathToDeclaringNode().getLeafName());
    }

    private Expression getFieldInitializer(final CompilationUnit ast) {
        return ((VariableDeclarationFragment)
                    ((TypeDeclaration)ast.types().get(0)).getFields()[0].fragments().get(0)).getInitializer();
    }

    private CompilationUnit getAst(final String source) {
        final SourceCode sourceCode = new SourceCode(source);
        return AstNodeUtil.getCompilationUnit(sourceCode.code, new String[0], "");
    }
    
}
