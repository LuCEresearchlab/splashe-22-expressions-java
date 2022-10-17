# anltr-grammar-analyzer

Tool(s) to analyze an ANTLR grammar.

## Resources

- `resources/java9_expressions.txt`: list of rules, one per line, belonging to Chapter 15 (Expressions) of the JLS for Java 9 (with the addition of `expressionName`, as detailed in [JLS Forms of Expressions](https://docs.oracle.com/javase/specs/jls/se9/html/jls-15.html#jls-15.2)).
- `resources/java9/*`: ANTLR grammar for Java 9 taken from https://github.com/antlr/grammars-v4/tree/master/java/java9

## How to use

This project is managed using [Bazel](https://bazel.build/).

- Clone the repository
- Build it using Bazel (which will fetch the necessary dependencies): `bazel build //...`

### Analyzer
- Execute the analyzer using `./bazel-bin/antlr-grammar-analyzer`
```
{variableInitializer=[expression], throwStatement=[expression], resource=[expression], explicitConstructorInvocation=[argumentList, expressionName, primary], basicForStatementNoShortIf=[expression], statementExpression=[classInstanceCreationExpression, assignment, methodInvocation, preDecrementExpression, preIncrementExpression, postIncrementExpression, postDecrementExpression], returnStatement=[expression], ifThenStatement=[expression], synchronizedStatement=[expression], ifThenElseStatementNoShortIf=[expression], switchStatement=[expression], basicForStatement=[expression], enhancedForStatementNoShortIf=[expression], variableAccess=[expressionName, fieldAccess], whileStatement=[expression], whileStatementNoShortIf=[expression], doStatement=[expression], enumConstant=[argumentList], assertStatement=[expression], switchLabel=[constantExpression], elementValue=[conditionalExpression], ifThenElseStatement=[expression], enhancedForStatement=[expression]}
===== Non-expression rules that refer to expression rules =====
assertStatement
basicForStatement
basicForStatementNoShortIf
doStatement
elementValue
enhancedForStatement
enhancedForStatementNoShortIf
enumConstant
explicitConstructorInvocation
ifThenElseStatement
ifThenElseStatementNoShortIf
ifThenStatement
resource
returnStatement
statementExpression
switchLabel
switchStatement
synchronizedStatement
throwStatement
variableAccess
variableInitializer
whileStatement
whileStatementNoShortIf
===== Expression rules referenced =====
argumentList
assignment
classInstanceCreationExpression
conditionalExpression
constantExpression
expression
expressionName
fieldAccess
methodInvocation
postDecrementExpression
postIncrementExpression
preDecrementExpression
preIncrementExpression
primary
```

### Grammar without expressions

Recursively removes rules that are expressions or cannot be "completed" without expressions. Produces a Dot graph.
Execute it using using `./bazel-bin/antlr-grammar-noexpr`.

![Grammar without expressions](grammar-noexpr.png)

### Check whether sample file fits grammar without expressions

Check whether a sample Java source fits within the grammar without expressions:

- whether it exhaustively covers all the rules
- whether it does not contain rules outside the grammar

```sh
./bazel-bin/fits-grammar-noexpr resources/C.java
```
