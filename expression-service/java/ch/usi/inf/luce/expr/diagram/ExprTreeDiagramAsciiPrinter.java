package ch.usi.inf.luce.expr.diagram;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public final class ExprTreeDiagramAsciiPrinter {

    private ExprTreeDiagramAsciiPrinter() {
    }

    @NotNull
    public static String prettyPrint(@NotNull ExprTreeDiagram diagram) {
        final StringBuilder sb = new StringBuilder();
        if (diagram.root == null) {
            prettyPrintNoRoot(diagram, sb);
        } else {
            prettyPrintWithRoot(diagram, diagram.root, sb);
        }
        return sb.toString();
    }

    @NotNull
    public static String prettyPrint(@NotNull ExprTreeDiagram diagram,
                                     @NotNull Node node) {
        final StringBuilder sb = new StringBuilder();
        prettyPrintWithRoot(diagram, node, sb);
        return sb.toString();
    }

    private static void prettyPrintWithRoot(@NotNull ExprTreeDiagram diagram,
                                            @NotNull Node root,
                                            @NotNull StringBuilder sb) {
        final Set<Node> visited = new HashSet<>();
        final Function<Node, Boolean> markVisited = (n) -> {
            if (visited.contains(n)) {
                return true;
            } else {
                visited.add(n);
                return false;
            }
        };
        prettyPrintImpl(diagram, markVisited, sb, root, "", true);
    }

    private static void prettyPrintNoRoot(@NotNull ExprTreeDiagram diagram,
                                          @NotNull StringBuilder sb) {
        diagram.nodes.stream()
                .filter(ExprTreeDiagramUtil.filterRoots(diagram))
                .forEach(root -> prettyPrintWithRoot(diagram, root, sb));
    }

    private static void prettyPrintImpl(@NotNull ExprTreeDiagram diagram,
                                        @NotNull Function<Node, Boolean> checkVisited,
                                        @NotNull StringBuilder sb,
                                        @NotNull Node node,
                                        @NotNull String prefix,
                                        boolean isLast) {
        sb.append(prefix)
                .append(isLast ? "└─ " : "├─ ")
                .append(node.content.stream()
                        .map(it -> {
                            if (it instanceof NodeContentElem.Hole) {
                                return "#";
                            } else {
                                return it.toString();
                            }
                        })
                        .collect(Collectors.joining(" ")));

        if (!node.type.isEmpty()) {
            sb.append(" ∈ ").append(node.type);
        }

        if (checkVisited.apply(node)) {
            // Loop
            sb.append(" ∞\n");
            return;
        }

        sb.append('\n');

        final String childPrefix = prefix + (isLast ? "   " : "│  ");
        for (int i = 0; i < node.content.size(); i++) {
            final NodeContentElem it = node.content.get(i);
            if (it instanceof NodeContentElem.Hole) {
                final NodeContentElem.Hole hole = (NodeContentElem.Hole) it;
                final Optional<Node> childOpt = ExprTreeDiagramUtil.findLinkedNode(diagram, hole.plug);
                if (childOpt.isPresent()) {
                    final boolean isLastChild = isLastChild(node.content, i + 1);
                    prettyPrintImpl(diagram, checkVisited, sb, childOpt.get(), childPrefix, isLastChild);
                }
            }
        }
    }

    private static boolean isLastChild(@NotNull List<NodeContentElem> contents, int n) {
        for (int i = n; i < contents.size(); i++) {
            if (contents.get(i) instanceof NodeContentElem.Hole) {
                return false;
            }
        }
        return true;
    }
}
