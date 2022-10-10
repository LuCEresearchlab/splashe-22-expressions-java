package ch.usi.inf.luce.expr.analyzer.java;

import ch.usi.inf.luce.expr.analyzer.core.SourceCode;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public final class JavaSourceCodeUtil {

    private static final String CU_PREFIX = "public class __ { public void __() { ";
    private static final String CU_POSTFIX = "} }";
    private static final Logger LOGGER = Logger.getLogger("SourceCode");
    private static final CodeFormatter FORMATTER = ToolFactory.createCodeFormatter(
            DefaultCodeFormatterConstants.getEclipseDefaultSettings());

    private JavaSourceCodeUtil() {
    }

    /**
     * Returns a copy of the given {@link SourceCode} with its
     * <code>code</code> value wrapped in a CU.
     */
    public static SourceCode stmtsToCompilationUnit(SourceCode sourceCode) {
        return new SourceCode(sourceCode.fileName, CU_PREFIX + sourceCode.code + CU_POSTFIX);
    }

    /**
     * Performs code formatting on a sequence of statements using Eclipse's
     * default settings.
     */
    public static SourceCode formatStmts(SourceCode sourceCode) {
        final String code = sourceCode.code;
        final TextEdit edit = FORMATTER.format(
                CodeFormatter.K_STATEMENTS,
                code,
                0, // start
                code.length(), // end
                0, // initial indentation
                null // default line separator
        );
        final IDocument document = new Document(code);
        try {
            edit.apply(document);
        } catch (BadLocationException | MalformedTreeException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while formatting example source code", e);
            }
        }
        return new SourceCode(sourceCode.fileName, document.get());
    }
}
