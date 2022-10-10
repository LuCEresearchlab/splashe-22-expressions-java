package ch.usi.inf.luce.expr.util.range;

import ch.usi.inf.luce.expr.util.range.IntRange;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.util.Random;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class IntRangeTest {

    @Property
    public void test(int a,
                     int b,
                     @InRange(min = "0", max = "100") int trials) {
        final int min;
        final int max;
        if (a > b) {
            min = b;
            max = a;
        } else {
            min = a;
            max = b;
        }
        final IntRange range = new IntRange(min, max);

        final Random random = new Random();
        final int rngBound = Math.abs(max - min);
        for (int i = 0; i < trials; i++) {
            final int val = min + random.nextInt(rngBound);
            Assert.assertTrue(range.contains(val));
        }
    }

    @Property
    public void isEmpty(int a, int b) {
        final boolean expected = a > b;
        final IntRange range = new IntRange(a, b);
        Assert.assertEquals(expected, range.isEmpty());
    }

    @Property
    public void toString(int a, int b) {
        Assert.assertEquals(a + ".." + b, new IntRange(a, b).toString());
    }
}
