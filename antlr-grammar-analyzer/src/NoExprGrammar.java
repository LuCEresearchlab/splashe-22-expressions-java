package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.tool.Alternative;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.Rule;
import org.antlr.v4.tool.ast.GrammarAST;

public class NoExprGrammar {

    private record RuleWithRuleRefs(String name, Set<String> refRulesNames) {}

    private final Grammar grammar = Utils.loadJava9Grammar();

    // Map from already (at least partially) processed rule name to outcome (whether to keep it or not)
    private final Map<String, Boolean> processedRules = new HashMap<>();
    private final Set<RuleWithRuleRefs> remainingRules = new HashSet<>();

    private void avoidRules(final Set<String> rules) {
        processedRules.putAll(rules.stream().collect(Collectors.toMap(r -> r, r -> false)));
    }

    public NoExprGrammar() {
        // Pre-mark all expression rules as "not to keep"
        avoidRules(Utils.loadExpressionRules());
        // And also avoid annotation rules
        avoidRules(Utils.loadAnnotationRules());
        // And also modules
        avoidRules(Set.of("moduleName"));
        // Process rules starting from the root entrypoint
        shouldKeepRule(grammar.getRule(Utils.STARTING_RULE));
    }

    /**
     * A RuleRef is "optional" when it's not required to be included
     * to complete the alternative (i.e., because it is inside a "*" block
     * and therefore can occur 0 or multiple times, or inside a "?" block,
     * and therefore can occur 0 or 1 times).
     */
    private boolean isRuleRefOptional(final GrammarAST ruleRefAST) {
        final GrammarAST alt = ruleRefAST.getOutermostAltNode();
        // Find nodes from `ruleRefAST` up to the root of this alternative,
        // check whether one of them indicates optionality.
        return Stream.iterate(ruleRefAST, n -> n != alt, n -> (GrammarAST) n.parent).anyMatch(
                n -> Set.of(ANTLRParser.CLOSURE,   // EBNF *, (0 or more)
                            ANTLRParser.OPTIONAL)  // EBNF ?, (0 or 1)
                        .contains(n.getType()));
    }

    private boolean isRuleRefMandatory(final GrammarAST ruleRefAST) {
        return !isRuleRefOptional(ruleRefAST);
    }

    private String refRuleName(final GrammarAST ruleRefAST) {
        return ruleRefAST.getText();
    }

    private List<GrammarAST> getRuleRefs(final Alternative a) {
        return a.ruleRefs.values().stream().flatMap(List::stream).toList();
    }

    /**
     * A RuleRef is kept when the referenced rule is kept.
     */
    public boolean shouldKeepRuleRef(final GrammarAST ruleRefAST) {
        return shouldKeepRule(grammar.getRule(refRuleName(ruleRefAST)));
    }

    /**
     * An Alternative contains Token(s) and RuleRef(s). We care about the latter.
     * The Alternative survives iff all its non-optional RuleRef survive.
     */
    public boolean shouldKeepAlternative(final Alternative a) {
        // Important: given that shouldKeepRuleRef calls shouldKeepRule that has (wanted) side effects,
        // we need to evaluate shouldKeepRuleRef for every RuleRef.
        // At that point we can consider only the mandatory ones:
        // we can keep this alternative iff all those mandatory rule refs can all be kept.
        record RuleRefInfo(boolean shouldKeep, boolean mandatory) {}
        return getRuleRefs(a)
                .stream()
                .map(rr -> new RuleRefInfo(shouldKeepRuleRef(rr),
                                           isRuleRefMandatory(rr)))
                .filter(RuleRefInfo::mandatory)
                .allMatch(RuleRefInfo::shouldKeep);
    }

    /**
     * A Rule contains a list of Alternative(s). We keep it around if it is not an expression rule
     * and there is at least one of its alternatives that survives the traversal.
     * Finally, we store the rule together with the set of referenced rules in the surviving alternatives.
     */
    public boolean shouldKeepRule(final Rule r) {
        if (processedRules.containsKey(r.name)) {
            return processedRules.get(r.name);
        }
        processedRules.put(r.name, true);
        final List<Alternative> alts = Arrays.stream(r.alt).skip(1).filter(this::shouldKeepAlternative).toList();
        if (alts.isEmpty()) {
            processedRules.put(r.name, false);
            return false;
        } else {
            final Set<String> refRulesNames = alts.stream()
                    .map(this::getRuleRefs)
                    .flatMap(List::stream)
                    .map(this::refRuleName)
                    .collect(Collectors.toSet());
            remainingRules.add(new RuleWithRuleRefs(r.name, refRulesNames));
            return true;
        }
    }

    public void printAsDot() {
        System.out.println("digraph {");
        final Set<String> remainingRulesNames = remainingRules.stream().map(RuleWithRuleRefs::name).collect(Collectors.toSet());
        for (final RuleWithRuleRefs rule : remainingRules) {
            for (final String ruleRef : rule.refRulesNames) {
                if (remainingRulesNames.contains(ruleRef)) {
                    System.out.println(rule.name + " -> " + ruleRef);
                }
            }
        }
        System.out.println("}");
    }

    public Set<String> getRemainingRulesNames() {
        return remainingRules.stream().map(RuleWithRuleRefs::name).collect(Collectors.toSet());
    }

    public static void main(final String[] args) {
        new NoExprGrammar().printAsDot();
    }
}
