package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import ch.usi.inf.luce.expr.analyzer.java.ExprConstructMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"PMD.ExcessiveImports", "PMD.TooManyMethods", "PMD.GodClass",
        "PMD.AvoidInstantiatingObjectsInLoops"})
public class ExpressionsStatsVisitor extends ASTVisitor {

    private final SourceCode sourceCode;
    private final Logger logger = Logger.getLogger("ExpressionsStatsVisitor");
    // Information collected about a TreePath with its counts (in tokens and
    // characters), grouped under StatsInfo.
    private Set<StatsInfo> exprStats = new HashSet<>();

    public ExpressionsStatsVisitor(final SourceCode sourceCode) {
        super();
        this.sourceCode = sourceCode;
    }

    public Set<StatsInfo> getExpressionsStats() {
        return exprStats;
    }

    /* Explicitly exclude annotations */

    @Override
    public boolean visit(@NotNull MarkerAnnotation node) {
        return false;
    }

    @Override
    public boolean visit(@NotNull NormalAnnotation node) {
        return false;
    }

    @Override
    public boolean visit(@NotNull SingleMemberAnnotation node) {
        return false;
    }

    @Override
    public boolean visit(@NotNull AnnotationTypeDeclaration node) {
        return false;
    }

    @Override
    public boolean visit(@NotNull AnnotationTypeMemberDeclaration node) {
        return false;
    }

    /* False expressions */

    @Override
    public boolean visit(@NotNull AnonymousClassDeclaration node) {
        // "Anonymous class declaration AST node type. For JLS2, this type of
        // node appears only as a child on a class instance creation expression."
        // "For JLS3, this type of node may also appear as the child of
        // an enum constant declaration." (which is not an expression,
        // therefore we do not need to discount it).
        if (node.getParent().getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
            countAsFalseExpression(node);
        }
        return true;
    }

    @Override
    public boolean visit(@NotNull Block node) {
        if (node.getParent().getNodeType() == ASTNode.LAMBDA_EXPRESSION) {
            countAsFalseExpression(node);
        }
        return true;
    }

    /* Expressions */

    @Override
    public boolean visit(@NotNull ArrayAccess node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull ArrayCreation node) {
        countAsExpression(node);
        // Expressions inside the initializer that are themselves array initializers
        // (i.e., nested array initializers) are "false expressions".
        if (node.getInitializer() != null) {
            //noinspection unchecked
            final List<Expression> expressions = (List<Expression>) node.getInitializer().expressions();
            for (final Expression e : expressions) {
                if (e.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
                    countAsFalseExpression(e);
                }
            }
        }
        return true;
    }

    @Override
    public boolean visit(@NotNull Assignment node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull BooleanLiteral node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull CastExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull CharacterLiteral node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull ClassInstanceCreation node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull ConditionalExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull CreationReference node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull ExpressionMethodReference node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull FieldAccess node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull InfixExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull InstanceofExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull LambdaExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull MethodInvocation node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull NullLiteral node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull NumberLiteral node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull PostfixExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull PrefixExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull StringLiteral node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull SuperFieldAccess node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull SuperMethodInvocation node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull SuperMethodReference node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull ThisExpression node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull TypeLiteral node) {
        countAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(@NotNull QualifiedName node) {
        if (ExprConstructMapper.isExpressionConstruct(node)) {
            countAsExpression(node);
        }
        return true;
    }

    @Override
    public boolean visit(@NotNull SimpleName node) {
        if (ExprConstructMapper.isExpressionConstruct(node)) {
            countAsExpression(node);
        }
        return true;
    }

    @Override
    public boolean visit(@NotNull ImportDeclaration node) {
        // We currently avoid going down the tree here because the name
        // contained in an ImportDeclaration does not have resolved bindings
        // *and* this is not reported as a problem during the compilation.
        // If we proceed, we get a NullPointerException when trying to access
        // the name's binding. 
        return false;
    }

    @Override
    public boolean visit(@NotNull TypeMethodReference node) {
        // We do not know how to reach this, as this kind of node "can be
        // represented as ExpressionMethodReference or as TypeMethodReference.
        // The ASTParser currently prefers the first form".
        throw new IllegalStateException("Visitor for TypeMethodReference");
    }

    private void countAsFalseExpression(final ASTNode node) {
        // For all nodes whose path is a prefix of this node path, subtract the
        // length of this node.
        final Set<StatsInfo> newStats = new HashSet<>();
        try {
            final TreePath currentPath = TreePath.getPath(node);
            final int currentChars = node.getLength();
            final int currentTokens = AstNodeUtil.tokensCount(sourceCode, node);
            for (final StatsInfo nodeStats : exprStats) {
                final TreePath nodePath = nodeStats.getTreePath();
                if (nodePath.isPrefixOf(currentPath)) {
                    final int chars = nodeStats.getCharCount();
                    final int tokens = nodeStats.getTokenCount();
                    newStats.add(new StatsInfo(nodePath, chars - currentChars, tokens - currentTokens));
                } else {
                    newStats.add(nodeStats);
                }
            }
            exprStats = newStats;
        } catch (InvalidInputException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Failed to tokenize part of the input source code", e);
            }
        }
    }

    private void countAsExpression(final ASTNode node) {
        try {
            final int tokens = AstNodeUtil.tokensCount(sourceCode, node);
            exprStats.add(new StatsInfo(TreePath.getPath(node), node.getLength(), tokens));
        } catch (InvalidInputException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Failed to tokenize part of the input source code", e);
            }
        }
    }


}
