package ch.usi.inf.luce.expr.util.pcg;

import ch.usi.inf.luce.expr.util.pcg.Pcg32;
import org.junit.Assert;
import org.junit.Test;

public class Pcg32Test {

    @Test
    public void testGlobalSameOutputAsOriginalImplementation() {
        final Pcg32 rng = Pcg32.defaultInstance();

        // Random integers
        Assert.assertEquals(0x1bbeb4f2, rng.nextInt());
        Assert.assertEquals(0xe82e89e9, rng.nextInt());
        Assert.assertEquals(0x681cfdeb, rng.nextInt());
        Assert.assertEquals(0xe00fa2ec, rng.nextInt());
        Assert.assertEquals(0xb1e1a434, rng.nextInt());
        Assert.assertEquals(0xbe56068d, rng.nextInt());

        // Flip a coin
        final StringBuilder sb = new StringBuilder(66);
        for (int i = 0; i < 65; i++) {
            sb.append(rng.nextInt(2) == 1 ? 'H' : 'T');
        }
        Assert.assertEquals("THTHHTHHTHTTTHHTHTHTHTHHTHTHTHHTTHTTTHHHTTTHTHTHTTHTHHTTTTTTHTHHT", sb.toString());
    }

    @Test
    public void testSeedSameOutputAsOriginalImplementation() {
        final Pcg32 rng = new Pcg32(42, 54);

        // Random integers
        Assert.assertEquals(0xa15c02b7, rng.nextInt());
        Assert.assertEquals(0x7b47f409, rng.nextInt());
        Assert.assertEquals(0xba1d3330, rng.nextInt());
        Assert.assertEquals(0x83d2f293, rng.nextInt());
        Assert.assertEquals(0xbfa4784b, rng.nextInt());
        Assert.assertEquals(0xcbed606e, rng.nextInt());

        // Flip a coin
        final StringBuilder sb = new StringBuilder(66);
        for (int i = 0; i < 65; i++) {
            sb.append(rng.nextInt(2) == 1 ? 'H' : 'T');
        }
        Assert.assertEquals("THHHHHHHTTTHHTHTTHHHHHTTTTTTTTTTHTTHTHTHTTTHTHTHHHTHHTHTTTHHTTHTH", sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailOnNegativeBounds() {
        final Pcg32 rng = Pcg32.defaultInstance();
        rng.nextInt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailOnZeroBound() {
        final Pcg32 rng = Pcg32.defaultInstance();
        rng.nextInt(0);
    }
}
