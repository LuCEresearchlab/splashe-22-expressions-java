package ch.usi.inf.luce.expr.util.tui;

import org.junit.Assert;
import org.junit.Test;

public class TermTextTest {
    private static final char ANSI_ESCAPE = 0x1b;

    @Test
    public void testNestedSingleReset() {
        final TermText termText = TermText.bold(TermText.underline("Hello world"));
        final String strVal = termText.toString();
        int i = strVal.indexOf(ANSI_ESCAPE + "[0m") + 4; // 4 is (ANSI_ESCAPE + "[0m").length()
        Assert.assertEquals(-1, strVal.indexOf(ANSI_ESCAPE + "[0m", i));
    }

    @Test
    public void testMultipleNestedStyles() {
        final TermText p = TermText.foreground(TermTextColor.GREEN,
                "I am a green line ",
                TermText.foreground(TermTextColor.BLUE,
                        TermText.underline(
                                TermText.bold("with a blue substring"))),
                " that becomes green again");
        Assert.assertEquals("\033[32mI am a green line \033[32m\033[34m\033[4m\033[1mwith a blue substring\033[0m\033[32m that becomes green again\033[0m", p.toString());
    }
}
