package ch.usi.inf.luce.expr.analyzer.java;

import ch.usi.inf.luce.expr.analyzer.core.ParserException;
import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

public final class AstNodeUtil {

    private AstNodeUtil() {
    }

    @NotNull
    @VisibleForTesting
    public static CompilationUnit getCompilationUnit(@NotNull String sourceCode,
                                                     @Nullable String[] sourcePathEntries,
                                                     @NotNull String unitName) {
        final ASTParser parser = getJava11ParserWithBindings(sourcePathEntries);
        parser.setSource(sourceCode.toCharArray());
        parser.setUnitName(unitName);
        return (CompilationUnit) parser.createAST(null);
    }

    public static ASTParser getJava11ParserWithBindings(final String[] sourcepathEntries) {
        //noinspection deprecation
        final ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setEnvironment(new String[0], sourcepathEntries, null, true);
        parser.setResolveBindings(true);
        // Set compiler to Java 11
        final Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);
        return parser;
    }
    
    public static void assertNoErrors(@NotNull CompilationUnit cu) throws ParserException {
        for (final IProblem problem : cu.getProblems()) {
            // We ignore the problem "public type X should be declared in its
            // own file" because we are directly feeding the source code as an
            // array of Char to the parser, ignoring the filename.
            if (problem.isError() && problem.getID() != IProblem.PublicClassMustMatchFileName) {
                throw new ParserException(problem.getSourceLineNumber(), problem.getMessage());
            }
        }
    }
    
    public static int tokensCount(final SourceCode sourceCode, final ASTNode node) throws InvalidInputException {
        final String source = sourceCode.getCodeSubstring(node.getStartPosition(), node.getLength());
        return tokenize(source).size();
    }

    public static List<String> tokenize(final String source) throws InvalidInputException {
        // Ignores (silently consumes) comments.
        final IScanner scanner = ToolFactory.createScanner(false, false, false, JavaCore.VERSION_11);
        scanner.setSource(source.toCharArray());
        final List<String> tokens = new ArrayList<>();
        while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
            tokens.add(String.valueOf(scanner.getCurrentTokenSource()));
        }
        return tokens;
    }

    public static int tokensInSource(final SourceCode sourceCode) throws InvalidInputException {
        final CompilationUnit cu = getCompilationUnit(sourceCode.code,
                null,
                sourceCode.fileName);
        return tokensCount(sourceCode, cu);
    }

    /**
     * Qualified field access refers to a field access that is not just a SimpleName.
     * Checks if node is a field name of a such a field access node.
     *
     * @return true iff node is on the right of a dot in
     *         a parent of the form 'foo . node'
     */
    public static boolean isFieldNameOfQualifiedFieldAccess(final SimpleName node) {
        final ASTNode parent = node.getParent();

        // Attention:
        // if `node` is a child of a node of the form
        // 'foo . node' (where `node` is on the right of the dot)
        // then `node` is not an expression.
        return (parent.getNodeType() == ASTNode.FIELD_ACCESS       && node.equals(((FieldAccess) parent).getName()))
                || (parent.getNodeType() == ASTNode.QUALIFIED_NAME     && node.equals(((QualifiedName) parent).getName()))
                || (parent.getNodeType() == ASTNode.SUPER_FIELD_ACCESS && node.equals(((SuperFieldAccess) parent).getName()));
    }

}
