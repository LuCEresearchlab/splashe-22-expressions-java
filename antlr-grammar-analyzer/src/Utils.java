package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.antlr.v4.tool.Rule;

public class Utils {
    private final static String RESOURCES_DIR = "resources";
    public final static String STARTING_RULE = "compilationUnit";

    private static Set<String> loadRules(final String fileName) {
        final Set<String> rules = new HashSet<>();
        try {
            rules.addAll(Files.readAllLines(Path.of(RESOURCES_DIR, fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rules;
    }

    public static Set<String> loadExpressionRules() {
        return loadRules("java9_expressions.txt");
    }

    public static Set<String> loadAnnotationRules() {
        return loadRules("java9_annotations.txt");
    }

    public static Grammar loadJava9Grammar() {
        return Grammar.load(Path.of(RESOURCES_DIR,"java9", "Java9Parser.g4").toString());
    }

    public static LexerGrammar loadJava9LexerGrammar() {
        return (LexerGrammar) Grammar.load(Path.of(RESOURCES_DIR,"java9", "Java9Lexer.g4").toString());
    }

    public static ParseTree parse(final String source) {
        final LexerGrammar lg = loadJava9LexerGrammar();
        final Grammar g = loadJava9Grammar();
        final CharStream input = CharStreams.fromString(source);
        final LexerInterpreter lexEngine = lg.createLexerInterpreter(input);
        final CommonTokenStream tokens = new CommonTokenStream(lexEngine);
        final ParserInterpreter parser = g.createParserInterpreter(tokens);
        return parser.parse(g.getRule(STARTING_RULE).index);
    }

    /**
     * Finds the names of all rules used in a parse tree.
     * Returns Optional.empty() when the parse tree contains errors.
     */
    public static Optional<Set<String>> rulesUsedInParseTree(final ParseTree tree) {
        final Grammar grammar = loadJava9Grammar();
        final Set<String> rules = new HashSet<>();
        final Set<ErrorNode> errors = new HashSet<>();
        ParseTreeWalker.DEFAULT.walk(new ParseTreeListener() {
            @Override
            public void visitTerminal(final TerminalNode terminalNode) { /* skip */ }

            @Override
            public void visitErrorNode(final ErrorNode errorNode) {
                errors.add(errorNode);
            }

            @Override
            public void enterEveryRule(final ParserRuleContext parserRuleContext) {
                final Rule r = grammar.getRule(parserRuleContext.getRuleIndex());
                rules.add(r.name);
            }

            @Override
            public void exitEveryRule(final ParserRuleContext parserRuleContext) { /* skip */ }
        }, tree);
        return errors.isEmpty() ? Optional.of(rules) : Optional.empty();
    }

}
