package ch.usi.inf.luce.expr.diagram;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Data structure of the Expression Tutor diagram.
 */
public final class ExprTreeDiagram {
    @NotNull
    public final Set<Node> nodes;
    @NotNull
    public final Set<Edge> edges;
    @Nullable
    public final Node root;

    @JsonCreator
    public ExprTreeDiagram(@JsonProperty("nodes") @NotNull Set<Node> nodes,
                           @JsonProperty("edges") @NotNull Set<Edge> edges,
                           @JsonProperty(value = "root", defaultValue = "null") @Nullable Node root) {
        if (root != null && !nodes.contains(root)) {
            throw new IllegalArgumentException("The root must be in the set of nodes.");
        }
        final List<Long> nodePlugValAs = nodes.stream()
                .map(node -> node.nodePlug.valA)
                .collect(Collectors.toList());
        if (new HashSet<>(nodePlugValAs).size() < nodePlugValAs.size()) {
            throw new IllegalArgumentException("All plugs in the set of nodes must be unique.");
        }
        this.nodes = nodes;
        this.edges = edges;
        this.root = root;
    }

    @NotNull
    public Set<Edge> getEdges(@NotNull final Plug plug) {
        final Set<Edge> edges = new HashSet<>();
        for (final Edge edge : this.edges) {
            if (edge.contains(plug)) {
                edges.add(edge);
            }
        }
        return edges;
    }

    public Optional<Node> parentNode(final Node node) {
        final List<Edge> relevantEdges = 
                edges.stream()
                     .filter(e -> e.contains(node.nodePlug))
                     .collect(Collectors.toList());
        if (relevantEdges.size() == 1) {
            final Edge e = relevantEdges.get(0);
            final Plug parentPlug = e.plugA.equals(node.nodePlug) ? e.plugB : e.plugA;
            return findNodeByPlug(parentPlug);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Node> findNodeByPlug(final Plug plug) {
        return nodes.stream().filter(n -> n.nodePlug.valA == plug.valA).findFirst();
    }
    
    public void addEdge(final Node parent, final Node child) {
        final Long plugId = nextEmptyPlugId(parent).orElseThrow(
                () -> new IllegalStateException("No empty plug found in the parent\n"
                        + "Trying to connect " + child.toString() + " to " 
                        + parent + " in diagram\n" + this)
        );
        edges.add(
            new Edge(new Plug(parent.getId(), plugId),
                     new Plug(child.getId(), 0)));
    }

    public boolean hasEmptyPlug(final Node node) {
        return nextEmptyPlugId(node).isPresent();
    }
    
    private Optional<Long> nextEmptyPlugId(final Node node) {
        final List<Plug> emptyPlugs = node.getHoles().stream()
                .filter(plug -> edges.stream().noneMatch(e -> e.contains(plug)))
                .collect(Collectors.toList());
        return emptyPlugs.isEmpty() ? Optional.empty() : Optional.of(emptyPlugs.get(0).valB);
    }


    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExprTreeDiagram)) {
            return false;
        }
        final ExprTreeDiagram that = (ExprTreeDiagram) o;
        return nodes.equals(that.nodes)
                && edges.equals(that.edges)
                && Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges, root);
    }

    @Override
    public String toString() {
        return "ExprTreeDiagram("
                + "nodes:\n" + nodes.stream().map(Object::toString).collect(Collectors.joining(",\n"))
                + ",\nedges: " + edges.stream().map(Object::toString).collect(Collectors.joining(",\n"))
                + ",\nroot: " + root
                + ')';
    }
}
