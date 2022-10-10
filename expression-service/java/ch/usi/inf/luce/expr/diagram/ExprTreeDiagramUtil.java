package ch.usi.inf.luce.expr.diagram;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32C;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("PMD.TooManyMethods")
public final class ExprTreeDiagramUtil {
    @NotNull
    private static final CRC32C CRC_32 = new CRC32C();

    private ExprTreeDiagramUtil() {
    }

    @NotNull
    public static Optional<Node> findLinkedNode(@NotNull ExprTreeDiagram diagram,
                                                @NotNull Plug parentPlug) {
        final Optional<Edge> edgeOpt = diagram.edges.stream()
                .filter(it -> it.plugA.equals(parentPlug))
                .findFirst();
        if (edgeOpt.isPresent()) {
            final Edge edge = edgeOpt.get();
            return diagram.nodes.stream()
                    .filter(it -> it.nodePlug.valA == edge.plugB.valA)
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }

    /**
     * Find the <i>main tree</i> of a given {@link ExprTreeDiagram}.
     * We define the <i>main tree</i> as the diagram containing just
     * the nodes connected to the root. In case there is no root,
     * the tree with the highest number of nodes is returned.
     */
    @NotNull
    public static ExprTreeDiagram findMainConnectedComponent(@NotNull ExprTreeDiagram diagram) {
        if (diagram.nodes.isEmpty()) {
            // empty tree
            return diagram;
        } else if (diagram.root == null) {
            ExprTreeDiagram best = null;
            for (final Node n : diagram.nodes) {
                final ExprTreeDiagram d = findConnectedComponent(diagram, n);
                if (best == null || best.nodes.size() < d.nodes.size()) {
                    best = d;
                }
            }
            // diagram.nodes is non-empty so the best* values are guaranteed to not be null
            return best;
        } else {
            // If a root is defined, we just take the connected component that includes it
            return findConnectedComponent(diagram, diagram.root);
        }
    }

    @NotNull
    private static ExprTreeDiagram findConnectedComponent(@NotNull ExprTreeDiagram diagram,
                                                          @NotNull Node startingNode) {
        final Set<Node> nodes = new HashSet<>();
        final Set<Edge> edges = new HashSet<>();
        final Queue<Node> queue = new ArrayDeque<>();
        queue.add(startingNode);

        while (!queue.isEmpty()) {
            final Node visiting = queue.remove();
            nodes.add(visiting);

            final List<Plug> plugs = visiting.getPlugs();
            final Set<Edge> newEdges = diagram.edges.stream()
                    .filter(e -> plugs.contains(e.plugA) || plugs.contains(e.plugB))
                    .filter(e -> !edges.contains(e))
                    .collect(Collectors.toUnmodifiableSet());

            final Set<Plug> edgePlugs = newEdges.stream()
                    .flatMap(e -> Stream.of(e.plugA, e.plugB))
                    .collect(Collectors.toUnmodifiableSet());

            edges.addAll(newEdges);
            final Set<Node> newNeighbors = diagram.nodes.stream()
                    .filter(n -> n.getPlugs().stream().anyMatch(edgePlugs::contains))
                    .filter(n -> !nodes.contains(n))
                    .collect(Collectors.toUnmodifiableSet());

            queue.addAll(newNeighbors);
        }

        final Node root = findTreeRoot(nodes, edges).orElse(null);
        return new ExprTreeDiagram(nodes, edges, root);
    }

    /**
     * Compute a subset diagram made of a single tree
     * starting from a given root.
     */
    @NotNull
    public static ExprTreeDiagram subset(@NotNull ExprTreeDiagram diagram,
                                         @NotNull Node root) {
        final Set<Node> nodesToKeep = walkFromNode(diagram, root);
        return subset(diagram, nodesToKeep, root);
    }

    @NotNull
    private static ExprTreeDiagram subset(@NotNull ExprTreeDiagram diagram,
                                          @NotNull Set<Node> nodes,
                                          @NotNull Node root) {
        final Set<Edge> edgesToKeep = diagram.edges.stream()
                .filter(it ->
                        // Plug A connected
                        nodes.stream().anyMatch(n -> n.nodePlug.valA == it.plugA.valA)
                                // Plug B connected
                                && nodes.stream().anyMatch(n -> n.nodePlug.equals(it.plugB)))
                .collect(Collectors.toUnmodifiableSet());

        return new ExprTreeDiagram(Collections.unmodifiableSet(nodes),
                edgesToKeep,
                root);
    }

    /**
     * Start from a given "root" node and find all the
     * other nodes we can reach from it.
     */
    @NotNull
    private static Set<Node> walkFromNode(@NotNull ExprTreeDiagram diagram,
                                          @NotNull Node root) {
        final Set<Node> visited = new HashSet<>();
        final Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            final Node visiting = queue.remove();
            visited.add(visiting);
            for (final NodeContentElem it : visiting.content) {
                if (it instanceof NodeContentElem.Hole) {
                    final NodeContentElem.Hole hole = (NodeContentElem.Hole) it;
                    findLinkedNode(diagram, hole.plug).ifPresent(queue::add);
                }
            }
        }
        return visited;
    }

    public static int computeDepth(@NotNull ExprTreeDiagram diagram,
                                   @NotNull Node root) {
        return computeDepth(diagram, root, 0);
    }

    private static int computeDepth(@NotNull ExprTreeDiagram diagram,
                                    @NotNull Node node,
                                    int depth) {
        int maxDepth = depth;
        for (final NodeContentElem it : node.content) {
            if (it instanceof NodeContentElem.Hole) {
                final NodeContentElem.Hole hole = (NodeContentElem.Hole) it;
                final Optional<Node> childOpt = findLinkedNode(diagram, hole.plug);
                if (childOpt.isPresent()) {
                    final int childDepth = computeDepth(diagram, childOpt.get(), 1 + depth);
                    if (maxDepth < childDepth) {
                        maxDepth = childDepth;
                    }
                }
            }
        }
        return maxDepth;
    }

    /**
     * In a correct ExprTreeDiagram, the root is the only node
     * which {@link Node#nodePlug} is not connected to any edge.
     */
    @NotNull
    private static Optional<Node> findTreeRoot(@NotNull Set<Node> nodes,
                                               @NotNull Set<Edge> edges) {
        return nodes.stream()
                .filter(n -> edges.stream()
                        .flatMap(e -> Stream.of(e.plugA, e.plugB))
                        .noneMatch(p -> p.equals(n.nodePlug)))
                .findFirst();
    }

    public static long getNodeCrc32(long id,
                                    @NotNull List<NodeContentElem> content,
                                    @NotNull String type,
                                    @NotNull String value) {
        final List<String> contentStr = content.stream()
                .map(it -> {
                    if (it instanceof NodeContentElem.Hole) {
                        return NodeContentElem.Hole.CONNECTOR_PLACEHOLDER;
                    } else {
                        return it.toString();
                    }
                })
                .collect(Collectors.toList());
        final int numContentChars = contentStr.stream()
                .mapToInt(String::length)
                .sum();
        final int size = Long.BYTES
                + Character.BYTES * (type.length()
                + value.length()
                + numContentChars);
        final ByteBuffer buffer = ByteBuffer.allocate(size);

        buffer.putLong(id);
        type.chars().forEach(c -> buffer.putChar((char) c));
        value.chars().forEach(c -> buffer.putChar((char) c));

        for (final String e : contentStr) {
            e.chars().forEach(c -> buffer.putChar((char) c));
        }

        CRC_32.reset();
        CRC_32.update(buffer.position(0));
        return CRC_32.getValue();
    }

    public static String serializeAndFullyParenthesize(final ExprTreeDiagram diagram,
                                                       final Node root) {
        return serialize(diagram, root, true);
    }

    public static String serialize(final ExprTreeDiagram diagram, final Node root) {
        return serialize(diagram, root, false);
    }

    /**
     * Return a type-less and value-less string representation of a diagram
     * starting from a given root.
     */
    @NotNull
    public static String serialize(@NotNull ExprTreeDiagram diagram,
                                   @NotNull Node root, boolean addParen) {
        return root.content.stream()
                .map(elem -> {
                    if (elem instanceof NodeContentElem.Hole) {
                        final NodeContentElem.Hole hole = (NodeContentElem.Hole) elem;
                        return ExprTreeDiagramUtil.findLinkedNode(diagram, hole.plug)
                                .map(n -> {
                                    final String childSerialization = serialize(diagram, n, addParen);
                                    return addParen
                                            ? "(" + childSerialization + ")"
                                            : childSerialization;
                                })
                                .orElse("#");
                    } else {
                        return elem.toString();
                    }
                })
                .collect(Collectors.joining());
    }

    /**
     * Return a type-less and value-less string representation of a node.
     */
    @NotNull
    public static String serialize(@NotNull Node node) {
        return node.content.stream()
                .map(elem -> {
                    if (elem instanceof NodeContentElem.Hole) {
                        return "#";
                    } else {
                        return elem.toString();
                    }
                })
                .collect(Collectors.joining());
    }

    /**
     * Return a predicate that can filter nodes which
     * top plug ({@link Node#nodePlug}) is not connected
     * (and so, by <i>definition of a tree</i>, are roots).
     */
    @NotNull
    public static Predicate<Node> filterRoots(@NotNull ExprTreeDiagram diagram) {
        return n -> diagram.edges.stream().noneMatch(e -> n.nodePlug.equals(e.plugA) || n.nodePlug.equals(e.plugB));
    }
}
