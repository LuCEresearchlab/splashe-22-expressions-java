package ch.usi.inf.luce.expr.util.range;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class IntProgressionTest {

    @Property
    public void testLastWithPositiveStep(int first,
                                         int last,
                                         @InRange(max = "10000", min = "1") int step) {
        final IntProgression progression = new IntProgression(first, last, step);
        if (first >= last) {
            Assert.assertEquals(last, progression.last);
        } else {
            int i = first;
            while (i < last) {
                i += step;
            }
            if (i != last) {
                i -= step;
            }
            Assert.assertEquals(i, progression.last);
        }
    }

    @Property
    public void testLastWithNegativeStep(int first,
                                         int last,
                                         @InRange(min = "-10000", max = "-1") int step) {
        final IntProgression progression = new IntProgression(first, last, step);
        if (first <= last) {
            Assert.assertEquals(last, progression.last);
        } else {
            int i = first;
            while (i > last) {
                i += step;
            }
            if (i != last) {
                i -= step;
            }
            Assert.assertEquals(i, progression.last);
        }
    }

    @Property
    public void testIsEmpty(int a, int b, @InRange int step) {
        if (step == 0) {
            // Skip this trial
            return;
        }
        final IntProgression progression;
        final boolean expectedEmpty;
        if (step > 0) {
            progression = new IntProgression(Math.min(a, b), Math.max(a, b), step);
            expectedEmpty = progression.first > progression.last;
        } else {
            progression = new IntProgression(Math.max(a, b), Math.min(a, b), step);
            expectedEmpty = progression.first < progression.last;
        }
        Assert.assertEquals(expectedEmpty, progression.isEmpty());
    }

    @Property
    public void testStreamSum(@InRange(min = "1", max = "2048") int n) {
        final long sum = n * (n + 1L) / 2L;
        Assert.assertEquals(sum, new IntProgression(1, n, 1).stream().sum());
        Assert.assertEquals(sum, new IntProgression(n, 1, -1).stream().sum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStepZero() {
        new IntProgression(1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStepIntMin() {
        new IntProgression(1, 2, Integer.MIN_VALUE);
    }

    @Test
    public void testForEach() {
        final StringBuilder sb = new StringBuilder();
        new IntProgression(0, 9, 2).forEach(sb::append);
        Assert.assertEquals("02468", sb.toString());
    }

    @Test
    public void testForLoop() {
        final StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i : new IntProgression(0, 9, 2)) {
            count++;
            sb.append(i);
        }
        Assert.assertEquals(5, count);
        Assert.assertEquals("02468", sb.toString());
    }

    @Test
    public void testEquality() {
        final IntProgression a = new IntProgression(1, 10, 1);
        final IntProgression b = new IntProgression(1, 10, 1);
        final IntProgression c = new IntProgression(2, 10, 1);
        final IntProgression d = new IntProgression(1, 11, 1);
        final IntProgression e = new IntProgression(1, 10, 2);
        final Object f = new Object();

        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());
        Assert.assertNotEquals(a, c);
        Assert.assertNotEquals(a.hashCode(), c.hashCode());
        Assert.assertNotEquals(a, d);
        Assert.assertNotEquals(a.hashCode(), d.hashCode());
        Assert.assertNotEquals(a, e);
        Assert.assertNotEquals(a.hashCode(), e.hashCode());
        Assert.assertNotEquals(a, f);
        Assert.assertNotEquals(a.hashCode(), f.hashCode());
    }

    @Test
    public void testEmptyHashCode() {
        Assert.assertEquals(-1, new IntProgression(2, 1, 1).hashCode());
    }
}
