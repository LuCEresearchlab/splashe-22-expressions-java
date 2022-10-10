package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.tree.ParseTree;

public class FitsWithinGrammar {

    private static Set<String> diff(final Set<String> source, final Set<String> toRemove) {
        final Set<String> sourceCopy = new HashSet<>(source);
        sourceCopy.removeAll(toRemove);
        return sourceCopy;
    }

    private static void ensureFits(final Set<String> usedRules, final Set<String> usableRules) {
        System.out.println("Usable rules not covered: " + diff(usableRules, usedRules));
        System.out.println("Used rules that CANNOT be used: " + diff(usedRules, usableRules));
    }

    public static void main(final String[] args) {
        try {
            final ParseTree tree = Utils.parse(Files.readString(Path.of(args[0])));
            final Set<String> usableRules = new NoExprGrammar().getRemainingRulesNames();
            Utils.rulesUsedInParseTree(tree).ifPresentOrElse(
                    rules -> ensureFits(rules, usableRules),
                    () -> System.out.println("Error while parsing " + args[0])
            );
        } catch (IOException e) {
            System.out.println("Unable to read " + args[0]);
        }
    }
}
