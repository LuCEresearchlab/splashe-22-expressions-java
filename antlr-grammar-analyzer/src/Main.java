package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.tool.Grammar;

public class Main {

    private static Map<String, Set<String>> computeRuleToRuleRefs() {
        final Grammar g = Utils.loadJava9Grammar();
        return g.rules.values().stream().collect(Collectors.toMap(
                rule -> rule.name,
                rule -> Arrays.stream(rule.alt)  // find all alternatives in a rule
                        .skip(1)  // skip first element (null), array is 1-based
                        .flatMap(alt -> alt.ruleRefs.keySet().stream()) // get names of referenced rules
                        .collect(Collectors.toSet())
        ));
    }

    private static Map<String, List<String>> findRulesReferencingExpressionEntryPoints(
            final Map<String, Set<String>> ruleToRuleRefs, final Set<String> expressionRules) {
        final Map<String, List<String>> ruleToExprRuleRefs = ruleToRuleRefs.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().filter(expressionRules::contains).toList()));
        return ruleToExprRuleRefs.entrySet().stream()
                .filter(e -> !e.getKey().contains("_lf")) // remove "ANTLR specific" rules (not from the grammar in the specification)
                .filter(e -> !expressionRules.contains(e.getKey())) // remove rules that are themselves expr rules
                .filter(e -> !e.getValue().isEmpty()) // remove rules without references to expression rules
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static void printSorted(final Collection<String> values) {
        final List<String> l = new ArrayList<>(values);
        Collections.sort(l);
        l.forEach(System.out::println);
    }

    /**
     * Determines the set of rules in the ANTLR Java 9 grammar
     * that are not themselves "expression rules" but reference "expression rules".
     * We consider a rule to be an "expression rule" when is part of Chapter 15 (Expressions) of the JLS.
     * Moreover, finds which are those expression rules that are referenced by non-expression rules;
     * in other words, what are the "entry points" for expressions.
     */
    public static void main(final String[] args) {
        final Set<String> expressionRules = Utils.loadExpressionRules();
        final Map<String, Set<String>> ruleToRuleRefs = computeRuleToRuleRefs();
        final Map<String, List<String>> rulesReferencingExpr = findRulesReferencingExpressionEntryPoints(ruleToRuleRefs, expressionRules);
        System.out.println(rulesReferencingExpr);
        System.out.println("===== Non-expression rules that refer to expression rules =====");
        printSorted(rulesReferencingExpr.keySet());
        System.out.println("===== Expression rules referenced =====");
        printSorted(rulesReferencingExpr.values().stream().flatMap(List::stream).collect(Collectors.toSet()));
    }
    
}
