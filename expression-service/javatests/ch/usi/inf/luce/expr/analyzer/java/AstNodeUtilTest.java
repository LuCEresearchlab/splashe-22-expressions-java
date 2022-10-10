package ch.usi.inf.luce.expr.analyzer.java;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.junit.Assert;
import org.junit.Test;


public class AstNodeUtilTest {

    @Test
    public void testTokenCountEmptyClass() throws InvalidInputException {
        SourceCode source = new SourceCode("Demo.java", "class Demo {}");
        Assert.assertEquals(4, AstNodeUtil.tokensInSource(source));
    }
    
    @Test
    public void testTokenCountParenthesizedExpression() throws InvalidInputException {
        SourceCode source = new SourceCode("Demo.java", "class Demo { int a = (1 + 2);}");
        Assert.assertEquals(13, AstNodeUtil.tokensInSource(source));
    }

}
