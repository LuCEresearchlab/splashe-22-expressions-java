package ch.usi.inf.luce.expr.analyzer.java;

import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * Maps from an (expression) AST node to an "expression construct".
 * Requires an AST with resolved bindings.
 *
 * Constructs are terms in a conceptual AST of expressions
 * (see Table 1 of paper on Java Expressions).
 * They map almost 1-to-1 to JDT node types, therefore we are reusing them,
 * reinterpreting their meaning in some cases.
 * Notably, with "SimpleName" we mean "Simple Variable Access"
 * (i.e., excluding "Field Access").
 * Other node types are intuitively renamed into constructs.
 */
public final class ExprConstructMapper {

    private ExprConstructMapper() {}

    public static boolean isExpressionConstruct(final ASTNode node) {
        return toConstruct(node).isPresent();
    }

    /**
     * @return Optional.empty() when `node` does not correspond to
     *         an expression construct; an AST node_type otherwise
     *         (using JDT representation with integers).
     */
    public static Optional<Integer> toConstruct(final ASTNode node) {
        if (node instanceof Expression) {
            final int nodeType = node.getNodeType();
            switch (nodeType) {
                case ASTNode.ARRAY_INITIALIZER:
                case ASTNode.PARENTHESIZED_EXPRESSION:
                    return Optional.empty();
                case ASTNode.QUALIFIED_NAME:
                    return toConstruct((QualifiedName) node);
                case ASTNode.SIMPLE_NAME:
                    return toConstruct((SimpleName) node);
                default:
                    return Optional.of(nodeType);
            }
        } else {
            // Clearly not an expression.
            return Optional.empty();
        }
    }

    private static Optional<Integer> toConstruct(final QualifiedName node) {
        // We only care about VARIABLE because all other kinds are not expressions.
        return node.resolveBinding().getKind() == IBinding.VARIABLE
                ? Optional.of(ASTNode.FIELD_ACCESS)
                : Optional.empty();
    }

    private static Optional<Integer> toConstruct(final SimpleName node) {
        final IBinding binding = node.resolveBinding();
        // We consider this an expression only when
        // - its kind is a variable (local variable / field / enum constant)
        // - it's not a declaration (needs to be a use)
        // - it is not the field name of a qualified field access
        //   (because these names are not subexpressions, they are
        //   NameUses already included in some parent)
        if (binding.getKind() == IBinding.VARIABLE
                && !node.isDeclaration()
                && !AstNodeUtil.isFieldNameOfQualifiedFieldAccess(node)) {
            final IVariableBinding varBinding = (IVariableBinding) binding;
            return varBinding.isField() || varBinding.isEnumConstant()
                ? Optional.of(ASTNode.FIELD_ACCESS)
                : Optional.of(ASTNode.SIMPLE_NAME);
        }
        return Optional.empty();
    }

}
