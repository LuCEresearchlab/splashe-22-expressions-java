package ch.usi.inf.luce.expr.diagram;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Node of the Expression Tutor Diagram.
 */
@JsonIgnoreProperties({"id", "holes", "plugs"})
public final class Node {
    @NotNull
    public final Plug nodePlug;
    @NotNull
    public final List<NodeContentElem> content;
    @NotNull
    public final String type;
    @NotNull
    public final String value;

    public Node(@NotNull Plug nodePlug,
                @NotNull List<NodeContentElem> content) {
        this(nodePlug, content, "", "");
    }

    public Node(@NotNull Plug nodePlug,
                @NotNull List<NodeContentElem> content,
                @NotNull String type) {
        this(nodePlug, content, type, "");
    }

    /**
     * Constructor that ensures the Plugs are numbered following this pattern:
     * - The plug on the top of each node is numbered as `Plug (id, 0)`
     * - The holes inside the node are numbered `Plug (id, m)` where `m` goes
     * from 1 until the number of holes in the node.
     */
    @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
    public Node(long id, @NotNull List<NodeContentElem> content, @NotNull String type) {
        this(id, content, type, "");
    }

    /**
     * Constructor that ensures the Plugs are numbered following this pattern:
     * - The plug on the top of each node is numbered as `Plug (id, 0)`
     * - The holes inside the node are numbered `Plug (id, m)` where `m` goes
     * from 1 until the number of holes in the node.
     */
    @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
    public Node(long id, @NotNull List<NodeContentElem> content, @NotNull String type,
                @NotNull String value) {
        final List<NodeContentElem> newContent = new ArrayList<>();
        int holeId = 1;
        for (final NodeContentElem c : content) {
            newContent.add(c instanceof NodeContentElem.Hole
                    ? new NodeContentElem.Hole(new Plug(id, holeId++))
                    : c);
        }
        this.nodePlug = new Plug(id, 0);
        this.content = newContent;
        this.type = type;
        this.value = value;
    }


    @JsonCreator
    public Node(@JsonProperty("nodePlug") @NotNull Plug nodePlug,
                @JsonProperty("content") @NotNull List<NodeContentElem> content,
                @JsonProperty("type") @NotNull String type,
                @JsonProperty("value") @NotNull String value) {
        this.nodePlug = nodePlug;
        this.content = content;
        this.type = type;
        this.value = value;
        final List<Plug> plugs = getPlugs();
        final List<Long> valAs = plugs.stream().map(it -> it.valA).collect(Collectors.toList());
        final long valA = valAs.get(0);
        for (int i = 1; i < valAs.size(); i++) {
            if (valAs.get(i) != valA) {
                throw new IllegalArgumentException("All plugs of a node must start with the same number.");
            }
        }
        final List<Long> valBs = plugs.stream().map(it -> it.valB).collect(Collectors.toList());
        for (int i = 0; i < valBs.size(); i++) {
            if (valBs.get(i) != i) {
                throw new IllegalArgumentException("A plug with an invalid second value was found.");
            }
        }
    }

    @NotNull
    @JsonIgnore
    public List<Plug> getPlugs() {
        final List<Plug> plugs = new ArrayList<>();
        plugs.add(nodePlug);
        plugs.addAll(getHoles());
        return plugs;
    }

    public long getId() {
        return nodePlug.valA;
    }

    @NotNull
    // Returns a list of Plugs in the holes.
    public List<Plug> getHoles() {
        final List<Plug> holes = new ArrayList<>();
        for (final NodeContentElem elem : content) {
            if (elem instanceof NodeContentElem.Hole) {
                holes.add(((NodeContentElem.Hole) elem).plug);
            }
        }
        return holes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        final Node other = (Node) o;
        return nodePlug.equals(other.nodePlug)
                && content.equals(other.content)
                && type.equals(other.type)
                && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodePlug, content, type, value);
    }

    @Override
    public String toString() {
        return "[ID=" + getId() + "] "
                + content.stream()
                .map(it -> it instanceof NodeContentElem.Hole
                        ? NodeContentElem.Hole.CONNECTOR_PLACEHOLDER
                        : it.toString())
                .collect(Collectors.joining(" "));
    }
}
