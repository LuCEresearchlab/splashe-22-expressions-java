package ch.usi.inf.luce.expr.analyzer.java;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.diagram.ExprTreeDiagram;
import java.util.List;
import java.util.Objects;

/**
 * Information extracted by the parser from the provided
 * source code.
 */
final class ParseResult {
    private final List<NodeInfo> roots;
    private final ExprTreeDiagram diagram;
    private final SourceCode sourceCode;

    public ParseResult(List<NodeInfo> roots,
                       ExprTreeDiagram diagram,
                       SourceCode sourceCode) {
        this.roots = roots;
        this.diagram = diagram;
        this.sourceCode = sourceCode;
    }

    /**
     * Returns a list of root nodes extracted from the source code.
     */
    public List<NodeInfo> getRoots() {
        return roots;
    }

    /**
     * Returns a diagram containing all the nodes extracted from the source code.
     */
    public ExprTreeDiagram getDiagram() {
        return diagram;
    }

    public SourceCode getSourceCode() {
        return sourceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else {
            final ParseResult that = (ParseResult) o;
            return Objects.equals(roots, that.roots)
                    && Objects.equals(diagram, that.diagram)
                    && Objects.equals(sourceCode, that.sourceCode);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(roots, diagram, sourceCode);
    }
}
