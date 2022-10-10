package ch.usi.inf.luce.expr.analyzer.core;

import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown while parsing the source code.
 */
public class ParserException extends Exception {

    public ParserException(int line, @NotNull String message) {
        super("Error at line " + line + ": " + message);
    }

    public ParserException(@NotNull String message) {
        super(message);
    }
}
