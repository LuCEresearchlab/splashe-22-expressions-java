package ch.usi.inf.luce.expr.util.pair;

import org.junit.Assert;
import org.junit.Test;

public class PairTest {

    @Test
    public void equality() {
        final Pair<String, Integer> a = new Pair<>("hello", 1);
        final Pair<String, Integer> b = new Pair<>("hello", 2);
        final Pair<String, Integer> c = new Pair<>("world", 1);
        final Pair<String, Integer> d = new Pair<>("world", 2);
        final Pair<String, Integer> e = new Pair<>("hello", 1);

        Assert.assertNotEquals(a, b);
        Assert.assertNotEquals(a.hashCode(), b.hashCode());

        Assert.assertNotEquals(a, c);
        Assert.assertNotEquals(a.hashCode(), c.hashCode());

        Assert.assertNotEquals(a, d);
        Assert.assertNotEquals(a.hashCode(), d.hashCode());

        Assert.assertEquals(a, e);
        Assert.assertEquals(a.hashCode(), e.hashCode());
        Assert.assertEquals(a.toString(), e.toString());

        Assert.assertEquals("hello", a.first);
        Assert.assertEquals(2L, (long) a.second);
    }
}
