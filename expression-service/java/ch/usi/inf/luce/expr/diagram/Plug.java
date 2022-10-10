package ch.usi.inf.luce.expr.diagram;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Plug for the Expression Tutor diagram.
 */
@SuppressWarnings({"checkstyle:MemberName"})
public final class Plug {
    public final long valA;
    public final long valB;

    @JsonCreator
    public Plug(@JsonProperty("valA") long valA, @JsonProperty("valB") long valB) {
        this.valA = valA;
        this.valB = valB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plug)) {
            return false;
        }
        final Plug other = (Plug) o;
        return valA == other.valA && valB == other.valB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valA, valB);
    }

    @Override
    public String toString() {
        return valA + " -- " + valB;
    }
}
