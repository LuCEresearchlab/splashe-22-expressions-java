package ch.usi.inf.luce.expr.analyzer.stats;

import ch.usi.inf.luce.expr.analyzer.core.ParserException;
import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import ch.usi.inf.luce.expr.analyzer.java.AstNodeUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"PMD.AvoidCatchingNPE", "PMD.AvoidCatchingGenericException"})
public class JavaStatsAnalyzer {

    private final Path directory;
    private final FileStatsTable fileStatsTable;
    private final ExprStatsTable exprStatsTable;
    private final ProjectsTable projectsTable;
    
    private final Logger logger = Logger.getLogger("JavaStatsAnalyzer");

    public JavaStatsAnalyzer(@NotNull Path directory, @NotNull JavaStatsDbManager dbManager) {
        this.directory = directory;
        this.fileStatsTable = new FileStatsTable(dbManager);
        this.exprStatsTable = new ExprStatsTable(dbManager);
        this.projectsTable = new ProjectsTable(dbManager);
    }

    private void logSevere(final String msg, final Exception e) {
        if (logger.isLoggable(Level.SEVERE)) {
            logger.log(Level.SEVERE, msg, e);
        }
    }

    public void analyze() {
        try (Stream<Path> files = Files.list(directory)) {
            final String[] paths = files.map(file -> file.toAbsolutePath().toString())
                                        .toArray(String[]::new);
            final FileASTRequestor fileAstRequestor = new FileASTRequestor() { 
                @Override
                public void acceptAST(String sourceFilePath, CompilationUnit ast) {
                    final Path filePath = Path.of(sourceFilePath);
                    final String fileName = filePath.getFileName().toString();
                    try { 
                        try {
                            AstNodeUtil.assertNoErrors(ast);
                            final SourceCode sourceCode = new SourceCode(fileName, Files.readString(filePath));
                            final StatsVisitor visitor = new StatsVisitor(sourceCode);
                            ast.accept(visitor);
                            final ExpressionsStatsVisitor exprVisitor = new ExpressionsStatsVisitor(sourceCode);
                            ast.accept(exprVisitor);
                            projectsTable.saveFileInProject(directory, fileName);
                            fileStatsTable.saveCompiledFileStats(fileName, sourceCode.code.length(),
                                    tokensCount(visitor, sourceCode, ast),
                                    getNodeCount(visitor, StatsVisitor.CLASS_DECLARATION),
                                    getNodeCount(visitor, StatsVisitor.METHOD_DECLARATION),
                                    visitor.astNodesCount());
                            exprStatsTable.saveExpressionsStats(fileName, exprVisitor.getExpressionsStats());
                        } catch (IOException e) {
                            fileStatsTable.saveProblematicFileStats(fileName);
                            logSevere("Failed to read input source file", e);
                        } catch (ParserException e) {
                            fileStatsTable.saveProblematicFileStats(fileName);
                            logSevere("Failed to parse input source file", e);
                        } catch (InvalidInputException e) {
                            fileStatsTable.saveProblematicFileStats(fileName);
                            logSevere("Failed to tokenize input source file", e);
                        } catch (NullPointerException e) {
                            fileStatsTable.saveProblematicFileStats(fileName);
                            logSevere("NPE while visiting AST, probably some bindings have not been resolved", e);
                        }
                    } catch (SQLException e) {
                        logSevere("Failed to store data in the database", e);
                    }
                }
            };
            final ASTParser parser = AstNodeUtil.getJava11ParserWithBindings(new String[0]);
            parser.createASTs(paths, null, new String[0], fileAstRequestor, null);
        } catch (IOException e) {
            logSevere("Failed to read input source file", e);
        } catch (InvalidPathException e) {
            logSevere("Error with input source file, possibly due to a NUL byte", e);
        }
    }

    private int getNodeCount(final StatsVisitor visitor, final String nodeName) {
        return visitor.getStats().getOrDefault(nodeName, 0);
    }

    private int tokensCount(final StatsVisitor visitor, final SourceCode source, final ASTNode ast)
            throws InvalidInputException {
        // Total number of tokens in the whole file (excluding comments),
        // minus number of tokens in annotations.
        return AstNodeUtil.tokensCount(source, ast) - visitor.annotationsTokensCount();
    }

}
