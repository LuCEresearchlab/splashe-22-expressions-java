package ch.usi.inf.luce.expr.analyzer.stats;


import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"PMD.TooManyMethods"})
public class StatsVisitor extends ASTVisitor {
    
    public static final String CLASS_DECLARATION = "ClassDeclaration";
    public static final String METHOD_DECLARATION = "MethodDeclaration";
    
    // Map from a given ASTNode type (represented as the simple name of the class) to
    // its frequency (number of occurrences in the tree).
    private final Map<String, Integer> stats = new HashMap<>();

    // Overall number of AST nodes in the whole tree.
    private int astNodes;
    // Overall number of tokens in annotations for the whole tree.
    private int annotationsTokens;
    // Overall number of annotation AST nodes for the whole tree.
    private int annotationsNodes;

    private final SourceCode source;

    private final Logger logger = Logger.getLogger("StatsVisitor");

    public StatsVisitor(final SourceCode source) {
        super();
        this.source = source;
    }

    public Map<String, Integer> getStats() {
        return stats;
    }

    public int astNodesCount() {
        return astNodes - annotationsNodes;
    }

    public int annotationsTokensCount() {
        return annotationsTokens;
    }

    /* Explicitly exclude annotations */

    @Override
    public boolean visit(@NotNull MarkerAnnotation node) {
        countAnnotationTokens(node);
        return false;
    }

    @Override
    public boolean visit(@NotNull NormalAnnotation node) {
        countAnnotationTokens(node);
        return false;
    }

    @Override
    public boolean visit(@NotNull SingleMemberAnnotation node) {
        countAnnotationTokens(node);
        return false;
    }

    @Override
    public boolean visit(@NotNull AnnotationTypeDeclaration node) {
        countAnnotationTokens(node);
        return false;
    }

    @Override
    public boolean visit(@NotNull AnnotationTypeMemberDeclaration node) {
        countAnnotationTokens(node);
        return false;
    }

    @Override
    public boolean visit(@NotNull AnonymousClassDeclaration node) {
        countNode(CLASS_DECLARATION);
        return true;
    }
    
    @Override
    public boolean visit(@NotNull TypeDeclaration node) {
        // A TypeDeclaration is either a class declaration or an interface declaration.
        if (!node.isInterface()) {
            countNode(CLASS_DECLARATION);
        }
        return true;
    }

    @Override
    public boolean visit(@NotNull MethodDeclaration node) {
        countNode(METHOD_DECLARATION);
        return true;
    }

    @Override
    public void preVisit(ASTNode node) {
        astNodes++;
    } 

    private void countNode(final String nodeName) {
        final Integer currentCount = stats.getOrDefault(nodeName, 0);
        stats.put(nodeName, currentCount + 1);
    }

    private void countAnnotationTokens(final ASTNode node) {
        try {
            annotationsNodes++;
            annotationsTokens += AstNodeUtil.tokensCount(source, node);
        } catch (InvalidInputException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Failed to tokenize part of the input source code", e);
            }
        }
    }

}
