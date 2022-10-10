package ch.usi.inf.luce.expr.util.range;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A progression of integer numbers.
 */
public class IntProgression implements Iterable<Integer> {

    public final int first;
    public final int last;
    public final int step;

    public IntProgression(int start, int endInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step must be non-zero");
        } else if (step == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Integer.MIN_VALUE to avoid underflow");
        }

        this.first = start;
        this.last = getLastElementOfProgression(start, endInclusive, step);
        this.step = step;
    }

    @Override
    public void forEach(@NotNull Consumer<? super Integer> action) {
        Iterable.super.forEach(action);
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new IntProgressionIterator(first, last, step);
    }

    @NotNull
    public IntStream stream() {
        return IntStream.iterate(first,
                previous -> step > 0 ? previous <= last : previous >= last,
                previous -> previous + step);
    }

    public boolean isEmpty() {
        return step > 0 ? first > last : first < last;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntProgression)) {
            return false;
        }
        final IntProgression other = (IntProgression) o;
        return first == other.first
                && last == other.last
                && step == other.step;
    }

    @Override
    public int hashCode() {
        return isEmpty()
                ? -1
                : (31 * (31 * first + last) + step);
    }

    @Override
    public String toString() {
        return first + ".." + last + " step " + step;
    }

    public static final class IntProgressionIterator implements PrimitiveIterator.OfInt {
        private final int step;
        private final int finalValue;
        private boolean hasMoreValues;
        private int nextValue;

        public IntProgressionIterator(int first,
                                      int last,
                                      int step) {
            this.step = step;
            this.finalValue = last;
            this.hasMoreValues = step > 0 ? first <= last : first >= last;
            this.nextValue = hasMoreValues ? first : last;
        }

        @Override
        public boolean hasNext() {
            return hasMoreValues;
        }

        @Override
        public int nextInt() {
            final int value = nextValue;
            if (value == finalValue) {
                if (hasMoreValues) {
                    hasMoreValues = false;
                } else {
                    throw new NoSuchElementException();
                }
            } else {
                nextValue += step;
            }
            return value;
        }
    }

    private static int getLastElementOfProgression(int start, int end, int step) {
        if (step > 0) {
            return start < end
                    ? end - mod(mod(end, step) - mod(start, step), step)
                    : end;
        } else if (step < 0) {
            return start > end
                    ? end + mod(mod(start, -step) - mod(end, -step), -step)
                    : end;
        } else {
            throw new IllegalArgumentException("Step can't be zero");
        }
    }

    private static int mod(int a, int b) {
        final int m = a % b;
        return m < 0 ? m + b : m;
    }
}
