package ch.usi.inf.luce.expr.diagram;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Undirected edge of the Expression Tutor diagram.
 */
public final class Edge {
    @NotNull
    public final Plug plugA;
    @NotNull
    public final Plug plugB;

    @JsonCreator
    public Edge(@JsonProperty("plugA") @NotNull Plug plugA,
                @JsonProperty("plugB") @NotNull Plug plugB) {
        if (plugA.equals(plugB)) {
            throw new IllegalArgumentException("An edge cannot be a loop.");
        }
        this.plugA = plugA;
        this.plugB = plugB;
    }

    public boolean contains(@NotNull final Plug plug) {
        return plug.equals(plugA) || plug.equals(plugB);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        final Edge other = (Edge) o;
        return (plugA.equals(other.plugA) && plugB.equals(other.plugB))
                || (plugA.equals(other.plugB) && plugB.equals(other.plugA));
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugA, plugB);
    }

    @Override
    public String toString() {
        return "Edge(" + plugA + ", " + plugB + ')';
    }
}
