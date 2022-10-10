package ch.usi.inf.luce.expr.util.pcg;

import org.jetbrains.annotations.NotNull;

/**
 * Util class that provides instances
 * of {@link Pcg32} given an integer
 * seed.
 */
public final class RngProvider {

    private RngProvider() {
    }

    @NotNull
    public static Pcg32 getRng(int seed) {
        final long longSeed = seed * 31L;
        final long initState = longSeed >> 31L;
        final long initSeq = longSeed & ((1L << 31L) - 1L);
        return new Pcg32(initState, initSeq);
    }

    @NotNull
    public static Pcg32 getRng() {
        return Pcg32.defaultInstance();
    }
}
