package ch.usi.inf.luce.expr.diagram;

import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public final class ExprTreeDiagramSourcePrinter {
    private static final String CLOSE_QUOTE_PARENT = "\")";

    private ExprTreeDiagramSourcePrinter() {
    }

    /**
     * Return Java source code of an expression
     * that creates a given {@link ExprTreeDiagram}.
     */
    @NotNull
    public static String printSource(@NotNull ExprTreeDiagram diagram) {
        return "new ExprTreeDiagram(\n    Set.of("
                + diagram.nodes.stream()
                .map(ExprTreeDiagramSourcePrinter::printSource)
                .collect(Collectors.joining(", "))
                + "),\n    Set.of("
                + diagram.edges.stream()
                .map(ExprTreeDiagramSourcePrinter::printSource)
                .collect(Collectors.joining(", "))
                + "),\n    "
                + (diagram.root == null
                ? "null"
                : printSource(diagram.root))
                + ")";
    }

    @NotNull
    private static String printSource(@NotNull Node node) {
        return "new Node(new Plug("
                + node.nodePlug.valA
                + ", "
                + node.nodePlug.valB
                + "), List.of("
                + node.content.stream()
                .map(ExprTreeDiagramSourcePrinter::printSource)
                .collect(Collectors.joining(", "))
                + "), \""
                + node.type
                + "\", \""
                + node.value
                + CLOSE_QUOTE_PARENT;
    }

    @NotNull
    private static String printSource(@NotNull NodeContentElem elem) {
        if (elem instanceof NodeContentElem.OtherContent) {
            final NodeContentElem.OtherContent otherContent = (NodeContentElem.OtherContent) elem;
            return "new NodeContentElem.OtherContent(\""
                    + escape(otherContent.content)
                    + CLOSE_QUOTE_PARENT;
        } else if (elem instanceof NodeContentElem.Hole) {
            final NodeContentElem.Hole hole = (NodeContentElem.Hole) elem;
            return "new NodeContentElem.Hole(new Plug("
                    + hole.plug.valA
                    + ", "
                    + hole.plug.valB
                    + "))";
        } else if (elem instanceof NodeContentElem.NameDef) {
            final NodeContentElem.NameDef nameDef = (NodeContentElem.NameDef) elem;
            return "new NodeContentElem.NameDef(\""
                    + escape(nameDef.name)
                    + CLOSE_QUOTE_PARENT;
        } else if (elem instanceof NodeContentElem.NameUse) {
            final NodeContentElem.NameUse nameUse = (NodeContentElem.NameUse) elem;
            return "new NodeContentElem.NameUse(\""
                    + escape(nameUse.name)
                    + CLOSE_QUOTE_PARENT;
        } else {
            throw new IllegalArgumentException("Unknown type of NodeContentElem: " + elem.getClass());
        }
    }

    @NotNull
    private static String printSource(@NotNull Edge edge) {
        return "new Edge(new Plug("
                + edge.plugA.valA
                + ", "
                + edge.plugA.valB
                + "), new Plug("
                + edge.plugB.valA
                + ", "
                + edge.plugB.valB
                + "))";
    }

    @NotNull
    private static String escape(@NotNull String s) {
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\"", "\\\"");
    }
}
