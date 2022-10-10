package ch.usi.inf.luce.expr.util.range;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Range of integer values.
 */
public final class IntRange extends IntProgression {

    @JsonCreator
    public IntRange(@JsonProperty("start") int start,
                    @JsonProperty("endInclusive") int endInclusive) {
        super(start, endInclusive, 1);
    }

    @Override
    public boolean isEmpty() {
        return first > last;
    }

    @Override
    public String toString() {
        return first + ".." + last;
    }

    public boolean contains(int value) {
        return value >= first && value <= last;
    }
}
