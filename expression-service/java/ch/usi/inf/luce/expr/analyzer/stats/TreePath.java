package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.analyzer.java.ExprConstructMapper;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Represents a path from the root of a Java AST (CompilationUnit) to a node.
 * That's used to determine the boundaries of an expression (triangle problem).
 * With that information we can calculate the depth, find the declaring node etc.
 */
public final class TreePath {
    // Path represented as a list of nodes,
    // stored from the leaf (first position) to the root (last position).
    private final Deque<ASTNode> path;

    private TreePath(final ASTNode node) {
        this(Stream.of(node));
    }

    private TreePath(final Stream<ASTNode> stream) {
        this.path = stream.collect(Collectors.toCollection(LinkedList::new));
    }
    
    private TreePath addChild(final ASTNode node) {
        final TreePath newPath = new TreePath(path.stream());
        newPath.path.addFirst(node);
        return newPath;
    }

    private String name(final ASTNode node) {
        return node.getClass().getSimpleName();
    }

    private ASTNode getLeaf() {
        return path.getFirst();
    }

    public String getLeafName() {
        return name(getLeaf());
    }

    public String getExprConstructName() {
        // Supported only for TreePath to expressions.
        final int nodeType = ExprConstructMapper.toConstruct(getLeaf()).orElseThrow();
        return ASTNode.nodeClassForType(nodeType).getSimpleName();
    }

    public boolean isPrefixOf(final TreePath other) {
        // Check that every ASTNode from the root starts at the same position in
        // the original source (this is a cheap way to get identity avoiding
        // object equality).
        final Iterator<ASTNode> thisIt = this.path.descendingIterator();
        final Iterator<ASTNode> otherIt = other.path.descendingIterator();
        while (thisIt.hasNext() && otherIt.hasNext()) {
            if (thisIt.next().getStartPosition() != otherIt.next().getStartPosition()) {
                return false;
            }
        }
        return this.path.size() <= other.path.size();
    }
    
    public String getStringRepresentation() {
        final List<String> nodeNames = path.stream().map(this::name).collect(Collectors.toList());
        Collections.reverse(nodeNames);
        return String.join(">", nodeNames);
    }
    
    public String getUniqueStringRepresentation() {
        return getStringRepresentation() + "#" + getLeaf().hashCode();
    }

    public TreePath pathToExprRoot() {
        return new TreePath(path.stream().skip(depth()));
    }

    private boolean isDeclaringNode(final ASTNode node) {
        // JLS §15.1 (*excluding Annotations*)
        // An expression occurs in the declaration of [...]
        // a field initializer,
        // a static initializer, 
        // an instance initializer
        // a constructor
        // a method declaration
        // 
        // While not explicitly mentioned in JLS §15.1, expressions can also
        // occur inside enum declarations. More precisely, they can be passed
        // as arguments in enum constant declarations.
        final int type = node.getNodeType();
        return type == ASTNode.FIELD_DECLARATION
            || type == ASTNode.ENUM_CONSTANT_DECLARATION
            || type == ASTNode.INITIALIZER          // static and instance
            || type == ASTNode.METHOD_DECLARATION;  // method + constructor
    }

    private boolean isExpressionNode(final ASTNode node) {
        return ExprConstructMapper.isExpressionConstruct(node);
    }

    public TreePath pathToDeclaringNode() {
        // Find the nearest ancestor that is a "declaring node".
        final Stream<ASTNode> nodes = path.stream().takeWhile(n -> !isDeclaringNode(n));
        return new TreePath(path.stream().skip(nodes.count()));
    }

    public boolean isExprRoot() {
        return this.equals(pathToExprRoot());
    }

    public long depth() {
        // Returns the depth of this node in the expression subtree.
        // Note that in a tree, the root node (first level) has depth 0, its
        // children have depth 1, and so on.

        // Starting from the end (i.e., from the leaf), find the first
        // non-expression node.  All the nodes until the child of that
        // non-expression node constitute the path to the root of this
        // expression subtree.
        return path.stream().takeWhile(this::isExpressionNode).count() - 1;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TreePath that = (TreePath) o;
        return this.path.equals(that.path);
    }

    public int hashCode() {
        return path.hashCode();
    }

    /**
     * Constructs a TreePath from `node` to the root of the AST,
     * skipping ParenthesizedExpression nodes.
     */
    public static TreePath getPath(final ASTNode node) {
        if (node.getParent() == null) {
            return new TreePath(node);
        } else {
            final TreePath pathToParent = getPath(node.getParent());
            return node.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION
                    ? pathToParent
                    : pathToParent.addChild(node);
        }
    }

}
