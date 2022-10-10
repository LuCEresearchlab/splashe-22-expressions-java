package ch.usi.inf.luce.expr.util.pcg;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Pseudo Random Number Generator.
 *
 * <p>This code is a port of the
 * <a href="https://github.com/imneme/pcg-c-basic">pcg-c-basic</a>
 * implementation of the PCG algorithm.
 */
public final class Pcg32 {
    private static final long DEFAULT_INIT_STATE = 0x853c49e6748fea9bL;
    private static final long DEFAULT_INIT_SEQ = 0xda3e39cb94b95bdbL;

    private final AtomicLong state = new AtomicLong(0L);
    private final long inc;

    /**
     * Default instance that can be used for tests.
     */
    public static Pcg32 defaultInstance() {
        return new Pcg32(DEFAULT_INIT_STATE, DEFAULT_INIT_SEQ);
    }

    /**
     * Seed the rng.
     *
     * @param initState State initializer
     * @param initSeq   Sequence selection constant
     */
    public Pcg32(long initState, long initSeq) {
        inc = (initSeq * 2L) + 1L;
        nextInt();
        state.set(state.get() + initState);
        nextInt();
    }

    /**
     * Generate a uniformly distributed 32-bit random number.
     */
    public int nextInt() {
        final long oldState = state.get();
        state.set(oldState * 6_364_136_223_846_793_005L + inc);
        final int xorShifted = (int) (((oldState >>> 18) ^ oldState) >>> 27);
        final int rot = (int) (oldState >> 59);
        return Integer.rotateRight(xorShifted, rot);
    }

    /**
     * Generate a uniformly distributed number, r,
     * where 0 <= r < bound.
     */
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("The bound must be positive");
        }

        // To avoid bias, we need to make the range of the RNG a multiple of
        // bound, which we do by dropping output less than a threshold.
        // A naive scheme to calculate the threshold would be to do
        //
        //     uint32_t threshold = 0x100000000ull % bound;
        //
        // but 64-bit div/mod is slower than 32-bit div/mod (especially on
        // 32-bit platforms).  In essence, we do
        //
        //     uint32_t threshold = (0x100000000ull-bound) % bound;
        //
        // because this version will calculate the same modulus, but the LHS
        // value is less than 2^32.
        final int threshold = -bound % bound;

        // Uniformity guarantees that this loop will terminate.  In practice, it
        // should usually terminate quickly; on average (assuming all bounds are
        // equally likely), 82.25% of the time, we can expect it to require just
        // one iteration.  In the worst case, someone passes a bound of 2^31 + 1
        // (i.e., 2147483649), which invalidates almost 50% of the range.  In
        // practice, bounds are typically small and only a tiny amount of the range
        // is eliminated.
        int r;
        do {
            r = nextInt();
        } while (r < threshold);
        return r % bound;
    }

    public boolean nextBoolean() {
        return (nextInt() & 1) == 1;
    }
}
