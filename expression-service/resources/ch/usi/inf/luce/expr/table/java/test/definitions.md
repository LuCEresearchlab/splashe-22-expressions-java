# Java documentation
- [Array access](#array-access)
- [Array creation](#array-creation)
- [Array creation with initializer](#array-creation-with-initializer)
- [Array creation without dimensions and empty initializer](#array-creation-without-dimensions-and-empty-initializer)
- [Array initializer (empty)](#array-initializer-(empty))
- [Array initializer](#array-initializer)
- [Assignment](#assignment)
- [Binary operation](#binary-operation)
- [Binary operation with more than two operands](#binary-operation-with-more-than-two-operands)
- [Boolean literal](#boolean-literal)
- [Cast expression](#cast-expression)
- [Char literal](#char-literal)
- [Parenthesized expression](#parenthesized-expression)
- [Class instance creation without arguments](#class-instance-creation-without-arguments)
- [Class instance creation](#class-instance-creation)
- [Generic class instance creation](#generic-class-instance-creation)
- [Qualified class instance creation](#qualified-class-instance-creation)
- [Anonymous class instance creation](#anonymous-class-instance-creation)
- [Class literal](#class-literal)
- [Conditional expression](#conditional-expression)
- [Instance creation reference](#instance-creation-reference)
- [Method reference expression](#method-reference-expression)
- [Method reference expression with generics](#method-reference-expression-with-generics)
- [Type reference expression](#type-reference-expression)
- [Field access without this](#field-access-without-this)
- [Field access on variable](#field-access-on-variable)
- [Field access](#field-access)
- [Nested field access](#nested-field-access)
- [Field access on anonymous object](#field-access-on-anonymous-object)
- [Field access using qualified class name](#field-access-using-qualified-class-name)
- [Method call on field access using qualified class name](#method-call-on-field-access-using-qualified-class-name)
- [Enum](#enum)
- [Field on interface](#field-on-interface)
- [Instanceof expression](#instanceof-expression)
- [Lambda expression without parentheses](#lambda-expression-without-parentheses)
- [Lambda expression with no parameters and empty block](#lambda-expression-with-no-parameters-and-empty-block)
- [Lambda expression with empty block](#lambda-expression-with-empty-block)
- [Lambda expression with block](#lambda-expression-with-block)
- [Lambda expression](#lambda-expression)
- [Lambda expression with types](#lambda-expression-with-types)
- [Lambda expression with qualified generic types](#lambda-expression-with-qualified-generic-types)
- [Method invocation](#method-invocation)
- [Static method invocation](#static-method-invocation)
- [Static method invocation with type arguments](#static-method-invocation-with-type-arguments)
- [Static method invocation on qualified name](#static-method-invocation-on-qualified-name)
- [Null literal](#null-literal)
- [Number literal](#number-literal)
- [Postfix expression](#postfix-expression)
- [Prefix expression](#prefix-expression)
- [String literal](#string-literal)
- [String literal with quotes inside](#string-literal-with-quotes-inside)
- [This literal](#this-literal)
- [This literal with qualifier](#this-literal-with-qualifier)
- [Super field access](#super-field-access)
- [Qualified super field access](#qualified-super-field-access)
- [Super method invocation](#super-method-invocation)
- [Super method invocation with arguments](#super-method-invocation-with-arguments)
- [Super method invocation with type arguments](#super-method-invocation-with-type-arguments)
- [Qualified super method invocation](#qualified-super-method-invocation)
- [Super method reference](#super-method-reference)
- [Qualified super method reference](#qualified-super-method-reference)

## Array access
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.10.3)

### Example
```java
int[] a = new int[1]; int b = a[0];
```

#### Expression tree diagram
```
└─ # [ # ] ∈ int
   ├─ a ∈ int[]
   └─ 0 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(3, 0), List.of(new NodeContentElem.NameUse("a")), "int[]", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.Hole(new Plug(2, 1)), new NodeContentElem.OtherContent("["), new NodeContentElem.Hole(new Plug(2, 2)), new NodeContentElem.OtherContent("]")), "int", ""), new Node(new Plug(4, 0), List.of(new NodeContentElem.OtherContent("0")), "int", "")),
    Set.of(new Edge(new Plug(2, 1), new Plug(3, 0)), new Edge(new Plug(2, 2), new Plug(4, 0))),
    new Node(new Plug(2, 0), List.of(new NodeContentElem.Hole(new Plug(2, 1)), new NodeContentElem.OtherContent("["), new NodeContentElem.Hole(new Plug(2, 2)), new NodeContentElem.OtherContent("]")), "int", ""))
```

## Array creation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.10.1)

### Example
```java
int[][] a = new int[2][3];
```

#### Expression tree diagram
```
└─ new   int [ # ] [ # ] ∈ int[][]
   ├─ 2 ∈ int
   └─ 3 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("int"), new NodeContentElem.OtherContent("["), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("]"), new NodeContentElem.OtherContent("["), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent("]")), "int[][]", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("2")), "int", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("3")), "int", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0)), new Edge(new Plug(0, 2), new Plug(2, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("int"), new NodeContentElem.OtherContent("["), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("]"), new NodeContentElem.OtherContent("["), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent("]")), "int[][]", ""))
```

## Array creation with initializer
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-10.html#jls-10.6)

### Example
```java
int[][] a = new int[][] { {3}, {4} };
```

#### Expression tree diagram
```
└─ new   int[][] { # , # } ∈ int[][]
   ├─ { # } ∈ int[]
   │  └─ 3 ∈ int
   └─ { # } ∈ int[]
      └─ 4 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("{"), new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("}")), "int[]", ""), new Node(new Plug(3, 0), List.of(new NodeContentElem.OtherContent("{"), new NodeContentElem.Hole(new Plug(3, 1)), new NodeContentElem.OtherContent("}")), "int[]", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("3")), "int", ""), new Node(new Plug(4, 0), List.of(new NodeContentElem.OtherContent("4")), "int", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("int[][]"), new NodeContentElem.OtherContent("{"), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(","), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent("}")), "int[][]", "")),
    Set.of(new Edge(new Plug(3, 1), new Plug(4, 0)), new Edge(new Plug(0, 2), new Plug(3, 0)), new Edge(new Plug(1, 1), new Plug(2, 0)), new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("int[][]"), new NodeContentElem.OtherContent("{"), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(","), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent("}")), "int[][]", ""))
```

## Array creation without dimensions and empty initializer
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-10.html#jls-10.6)

### Example
```java
int[] a = new int[] {};
```

#### Expression tree diagram
```
└─ new   int[] { } ∈ int[]
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("int[]"), new NodeContentElem.OtherContent("{"), new NodeContentElem.OtherContent("}")), "int[]", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("int[]"), new NodeContentElem.OtherContent("{"), new NodeContentElem.OtherContent("}")), "int[]", ""))
```

## Array initializer (empty)
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-10.html#jls-10.6)

### Example
```java
int[] a = {};
```

#### Expression tree diagram
```
└─ { } ∈ int[]
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("{"), new NodeContentElem.OtherContent("}")), "int[]", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("{"), new NodeContentElem.OtherContent("}")), "int[]", ""))
```

## Array initializer
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-10.html#jls-10.6)

### Example
```java
int[] a = {3, 4};
```

#### Expression tree diagram
```
└─ { # , # } ∈ int[]
   ├─ 3 ∈ int
   └─ 4 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("{"), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(","), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent("}")), "int[]", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("3")), "int", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("4")), "int", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0)), new Edge(new Plug(0, 2), new Plug(2, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("{"), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(","), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent("}")), "int[]", ""))
```

## Assignment
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.26)

### Example
```java
int a; a = 1 + 2;
```

#### Expression tree diagram
```
└─ # = # ∈ int
   ├─ a ∈ int
   └─ # + # ∈ int
      ├─ 1 ∈ int
      └─ 2 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(3, 0), List.of(new NodeContentElem.OtherContent("1")), "int", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.NameUse("a")), "int", ""), new Node(new Plug(4, 0), List.of(new NodeContentElem.OtherContent("2")), "int", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("="), new NodeContentElem.Hole(new Plug(0, 2))), "int", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.Hole(new Plug(2, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(2, 2))), "int", "")),
    Set.of(new Edge(new Plug(2, 2), new Plug(4, 0)), new Edge(new Plug(0, 2), new Plug(2, 0)), new Edge(new Plug(2, 1), new Plug(3, 0)), new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("="), new NodeContentElem.Hole(new Plug(0, 2))), "int", ""))
```

## Binary operation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.17)

### Example
```java
int a = 1 + 2;
```

#### Expression tree diagram
```
└─ # + # ∈ int
   ├─ 1 ∈ int
   └─ 2 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("2")), "int", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("1")), "int", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(0, 2))), "int", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0)), new Edge(new Plug(0, 2), new Plug(2, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(0, 2))), "int", ""))
```

## Binary operation with more than two operands
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.17)

### Example
```java
int a = 1 + 2 + 3 - 4 * 5;
```

#### Expression tree diagram
```
└─ # - # ∈ int
   ├─ # + # ∈ int
   │  ├─ # + # ∈ int
   │  │  ├─ 1 ∈ int
   │  │  └─ 2 ∈ int
   │  └─ 3 ∈ int
   └─ # * # ∈ int
      ├─ 4 ∈ int
      └─ 5 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("1")), "int", ""), new Node(new Plug(6, 0), List.of(new NodeContentElem.Hole(new Plug(6, 1)), new NodeContentElem.OtherContent("*"), new NodeContentElem.Hole(new Plug(6, 2))), "int", ""), new Node(new Plug(7, 0), List.of(new NodeContentElem.OtherContent("4")), "int", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("-"), new NodeContentElem.Hole(new Plug(0, 2))), "int", ""), new Node(new Plug(3, 0), List.of(new NodeContentElem.OtherContent("2")), "int", ""), new Node(new Plug(4, 0), List.of(new NodeContentElem.Hole(new Plug(4, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(4, 2))), "int", ""), new Node(new Plug(8, 0), List.of(new NodeContentElem.OtherContent("5")), "int", ""), new Node(new Plug(5, 0), List.of(new NodeContentElem.OtherContent("3")), "int", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(1, 2))), "int", "")),
    Set.of(new Edge(new Plug(6, 1), new Plug(7, 0)), new Edge(new Plug(4, 1), new Plug(1, 0)), new Edge(new Plug(0, 2), new Plug(6, 0)), new Edge(new Plug(0, 1), new Plug(4, 0)), new Edge(new Plug(1, 2), new Plug(3, 0)), new Edge(new Plug(6, 2), new Plug(8, 0)), new Edge(new Plug(4, 2), new Plug(5, 0)), new Edge(new Plug(1, 1), new Plug(2, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("-"), new NodeContentElem.Hole(new Plug(0, 2))), "int", ""))
```

## Boolean literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.10.3)

### Example
```java
boolean a = true;
```

#### Expression tree diagram
```
└─ true ∈ boolean
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("true")), "boolean", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("true")), "boolean", ""))
```

## Cast expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.16)

### Example
```java
float a = (float) 10;
```

#### Expression tree diagram
```
└─ ( float ) # ∈ float
   └─ 10 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("10")), "int", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameUse("float"), new NodeContentElem.OtherContent(")"), new NodeContentElem.Hole(new Plug(0, 1))), "float", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameUse("float"), new NodeContentElem.OtherContent(")"), new NodeContentElem.Hole(new Plug(0, 1))), "float", ""))
```

## Char literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.10.4)

### Example
```java
char a = 'c';
```

#### Expression tree diagram
```
└─ 'c' ∈ char
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("'c'")), "char", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("'c'")), "char", ""))
```

## Parenthesized expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.8.5)

### Example
```java
int a = (42);
```

#### Expression tree diagram
```
└─ 42 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("42")), "int", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("42")), "int", ""))
```

## Class instance creation without arguments
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.9)

### Example
```java
String a = new String();
```

#### Expression tree diagram
```
└─ new   String ( ) ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "String", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "String", ""))
```

## Class instance creation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.9)

### Example
```java
String a = new String("LuCE");
```

#### Expression tree diagram
```
└─ new   String ( # ) ∈ String
   └─ "LuCE" ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("\"LuCE\"")), "String", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(")")), "String", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(")")), "String", ""))
```

## Generic class instance creation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.9)

### Example
```java
List<List<String>> l = new ArrayList<List<String>>();
```

#### Expression tree diagram
```
└─ new   ArrayList < List < String > > ( ) ∈ ArrayList<List<String>>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("ArrayList"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("List"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "ArrayList<List<String>>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("ArrayList"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("List"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "ArrayList<List<String>>", ""))
```

## Qualified class instance creation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.9)

### Example
```java
class Outer { class Inner {} }   class Other { Outer.Inner i = new Outer().new Inner(); }
```

#### Expression tree diagram
```
└─ # . new   Inner ( ) ∈ Inner
   └─ new   Outer ( ) ∈ Outer
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("Inner"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "Inner", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("Outer"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "Outer", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("Inner"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "Inner", ""))
```

## Anonymous class instance creation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.9)

### Example
```java
Object o = new Object(){ int m() { return 1 + 2;} };
```

#### Expression tree diagram
```
└─ new   Object ( ) { … } ∈ Object
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("Object"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("{ … }")), "Object", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("Object"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("{ … }")), "Object", ""))
```

## Class literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.8.2)

### Example
```java
Class<String> a = String.class;
```

#### Expression tree diagram
```
└─ String . class ∈ Class<String>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("class")), "Class<String>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("class")), "Class<String>", ""))
```

## Conditional expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.25)

### Example
```java
int a = true ? 1 : 0;
```

#### Expression tree diagram
```
└─ # ? # : # ∈ int
   ├─ true ∈ boolean
   ├─ 1 ∈ int
   └─ 0 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("1")), "int", ""), new Node(new Plug(3, 0), List.of(new NodeContentElem.OtherContent("0")), "int", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("true")), "boolean", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("?"), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent(":"), new NodeContentElem.Hole(new Plug(0, 3))), "int", "")),
    Set.of(new Edge(new Plug(0, 2), new Plug(2, 0)), new Edge(new Plug(0, 3), new Plug(3, 0)), new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("?"), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent(":"), new NodeContentElem.Hole(new Plug(0, 3))), "int", ""))
```

## Instance creation reference
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.13)

### Example
```java
java.util.function.Supplier<Object> s = Object::new;
```

#### Expression tree diagram
```
└─ Object :: new ∈ Supplier<Object>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Object"), new NodeContentElem.OtherContent("::"), new NodeContentElem.OtherContent("new")), "Supplier<Object>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Object"), new NodeContentElem.OtherContent("::"), new NodeContentElem.OtherContent("new")), "Supplier<Object>", ""))
```

## Method reference expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.13)

### Example
```java
java.util.function.Supplier<Integer> s = new Object()::hashCode;
```

#### Expression tree diagram
```
└─ # :: hashCode ∈ Supplier<Integer>
   └─ new   Object ( ) ∈ Object
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("::"), new NodeContentElem.NameUse("hashCode")), "Supplier<Integer>", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("Object"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "Object", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("::"), new NodeContentElem.NameUse("hashCode")), "Supplier<Integer>", ""))
```

## Method reference expression with generics
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.13)

### Example
```java
java.util.function.Consumer<List<List<String>>> c = new C()::<List<String>>m; } class C { public <T> void m(List<T> l) {}
```

#### Expression tree diagram
```
└─ # :: < List < String > > m ∈ Consumer<List<List<String>>>
   └─ new   C ( ) ∈ C
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("::"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("List"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("m")), "Consumer<List<List<String>>>", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("C"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "C", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("::"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("List"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("m")), "Consumer<List<List<String>>>", ""))
```

## Type reference expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.13)

### Example
```java
java.util.function.Function<Double, Double> s = Math::sqrt;
```

#### Expression tree diagram
```
└─ Math :: sqrt ∈ Function<Double,Double>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Math"), new NodeContentElem.OtherContent("::"), new NodeContentElem.NameUse("sqrt")), "Function<Double,Double>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Math"), new NodeContentElem.OtherContent("::"), new NodeContentElem.NameUse("sqrt")), "Function<Double,Double>", ""))
```

## Field access without this
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
class C { int f; public int m() { return f; } }
```

#### Expression tree diagram
```
└─ f ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("f")), "int", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("f")), "int", ""))
```

## Field access on variable
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
class C { int f; public int m() { C c = new C(); return c.f; } }
```

#### Expression tree diagram
```
└─ # . f ∈ int
   └─ c ∈ C
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.NameUse("c")), "C", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0))),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", ""))
```

## Field access
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
Object o = new Object() { private int f; public int m() { return this.f; } };
```

#### Expression tree diagram
```
└─ # . f ∈ int
   └─ this
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("this")), "", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0))),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", ""))
```

## Nested field access
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
Object o = new Object() { private Integer f; public int m() { return this.f.MAX_VALUE; } };
```

#### Expression tree diagram
```
└─ # . MAX_VALUE ∈ int
   └─ # . f ∈ Integer
      └─ this
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(3, 0), List.of(new NodeContentElem.OtherContent("this")), "", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.Hole(new Plug(2, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "Integer", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("MAX_VALUE")), "int", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0)), new Edge(new Plug(2, 1), new Plug(3, 0))),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("MAX_VALUE")), "int", ""))
```

## Field access on anonymous object
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
int v = new Object() { public Integer f; }.f.MAX_VALUE;
```

#### Expression tree diagram
```
└─ # . MAX_VALUE ∈ int
   └─ # . f ∈ Integer
      └─ new   Object ( ) { … } ∈ Object
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("Object"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("{ … }")), "Object", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "Integer", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("MAX_VALUE")), "int", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0)), new Edge(new Plug(1, 1), new Plug(2, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("MAX_VALUE")), "int", ""))
```

## Field access using qualified class name
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
int v = java.lang.Integer.MAX_VALUE;
```

#### Expression tree diagram
```
└─ java . lang . Integer . MAX_VALUE ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("lang"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Integer"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("MAX_VALUE")), "int", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("lang"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Integer"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("MAX_VALUE")), "int", ""))
```

## Method call on field access using qualified class name
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
String s = java.lang.Integer.TYPE.getName();
```

#### Expression tree diagram
```
└─ # . getName ( ) ∈ String
   └─ java . lang . Integer . TYPE ∈ Class<Integer>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("getName"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "String", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("lang"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Integer"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("TYPE")), "Class<Integer>", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("getName"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "String", ""))
```

## Enum
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
java.awt.Component.BaselineResizeBehavior o = java.awt.Component.BaselineResizeBehavior.OTHER;
```

#### Expression tree diagram
```
└─ java . awt . Component . BaselineResizeBehavior . OTHER ∈ BaselineResizeBehavior
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("awt"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Component"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("BaselineResizeBehavior"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("OTHER")), "BaselineResizeBehavior", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("awt"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Component"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("BaselineResizeBehavior"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("OTHER")), "BaselineResizeBehavior", ""))
```

## Field on interface
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11)

### Example
```java
int v = java.awt.image.ImageObserver.WIDTH;
```

#### Expression tree diagram
```
└─ java . awt . image . ImageObserver . WIDTH ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("awt"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("image"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("ImageObserver"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("WIDTH")), "int", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("awt"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("image"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("ImageObserver"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("WIDTH")), "int", ""))
```

## Instanceof expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.20.2)

### Example
```java
boolean a = "Hi" instanceof CharSequence;
```

#### Expression tree diagram
```
└─ #   instanceof   CharSequence ∈ boolean
   └─ "Hi" ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("\"Hi\"")), "String", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(" "), new NodeContentElem.OtherContent("instanceof"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("CharSequence")), "boolean", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(" "), new NodeContentElem.OtherContent("instanceof"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("CharSequence")), "boolean", ""))
```

## Lambda expression without parentheses
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-LambdaExpression)

### Example
```java
java.util.function.Function<String, String> a = name -> "Hi " + name;
```

#### Expression tree diagram
```
└─ name -> # ∈ Function<String,String>
   └─ # + # ∈ String
      ├─ "Hi " ∈ String
      └─ name ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("\"Hi \"")), "String", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(1, 2))), "String", ""), new Node(new Plug(3, 0), List.of(new NodeContentElem.NameUse("name")), "String", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "Function<String,String>", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0)), new Edge(new Plug(1, 2), new Plug(3, 0)), new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "Function<String,String>", ""))
```

## Lambda expression with no parameters and empty block
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-LambdaExpression)

### Example
```java
Runnable r = () -> { };
```

#### Expression tree diagram
```
└─ ( ) -> { … } ∈ Runnable
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.OtherContent("{ … }")), "Runnable", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.OtherContent("{ … }")), "Runnable", ""))
```

## Lambda expression with empty block
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-LambdaExpression)

### Example
```java
java.util.function.Consumer<String> a = name -> { };
```

#### Expression tree diagram
```
└─ name -> { … } ∈ Consumer<String>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent("->"), new NodeContentElem.OtherContent("{ … }")), "Consumer<String>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent("->"), new NodeContentElem.OtherContent("{ … }")), "Consumer<String>", ""))
```

## Lambda expression with block
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-LambdaExpression)

### Example
```java
java.util.function.Function<String, String> a = name -> { return "Hi " + name; };
```

#### Expression tree diagram
```
└─ name -> { … } ∈ Function<String,String>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent("->"), new NodeContentElem.OtherContent("{ … }")), "Function<String,String>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent("->"), new NodeContentElem.OtherContent("{ … }")), "Function<String,String>", ""))
```

## Lambda expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-LambdaExpression)

### Example
```java
java.util.function.Function<String, String> a = (name) -> "Hi " + name;
```

#### Expression tree diagram
```
└─ ( name ) -> # ∈ Function<String,String>
   └─ # + # ∈ String
      ├─ "Hi " ∈ String
      └─ name ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("\"Hi \"")), "String", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(1, 2))), "String", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "Function<String,String>", ""), new Node(new Plug(3, 0), List.of(new NodeContentElem.NameUse("name")), "String", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0)), new Edge(new Plug(1, 2), new Plug(3, 0)), new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "Function<String,String>", ""))
```

## Lambda expression with types
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-LambdaExpression)

### Example
```java
java.util.function.BiFunction<String, String, String> a = (String name, String surname) -> name + surname;
```

#### Expression tree diagram
```
└─ ( String   name , String   surname ) -> # ∈ BiFunction<String,String,String>
   └─ # + # ∈ String
      ├─ name ∈ String
      └─ surname ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent(","), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameDef("surname"), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "BiFunction<String,String,String>", ""), new Node(new Plug(3, 0), List.of(new NodeContentElem.NameUse("surname")), "String", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.NameUse("name")), "String", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("+"), new NodeContentElem.Hole(new Plug(1, 2))), "String", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0)), new Edge(new Plug(1, 2), new Plug(3, 0)), new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameDef("name"), new NodeContentElem.OtherContent(","), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameDef("surname"), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "BiFunction<String,String,String>", ""))
```

## Lambda expression with qualified generic types
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-LambdaExpression)

### Example
```java
java.util.function.Function<ArrayList<String>, Integer> f = (ArrayList<String> l) -> l.size();
```

#### Expression tree diagram
```
└─ ( ArrayList < String >   l ) -> # ∈ Function<ArrayList<String>,Integer>
   └─ # . size ( ) ∈ int
      └─ l ∈ ArrayList<String>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.NameUse("l")), "ArrayList<String>", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameUse("ArrayList"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameDef("l"), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "Function<ArrayList<String>,Integer>", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("size"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "int", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0)), new Edge(new Plug(1, 1), new Plug(2, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("("), new NodeContentElem.NameUse("ArrayList"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameDef("l"), new NodeContentElem.OtherContent(")"), new NodeContentElem.OtherContent("->"), new NodeContentElem.Hole(new Plug(0, 1))), "Function<ArrayList<String>,Integer>", ""))
```

## Method invocation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
int d = new java.util.Random().nextInt(1);
```

#### Expression tree diagram
```
└─ # . nextInt ( # ) ∈ int
   ├─ new   java . util . Random ( ) ∈ Random
   └─ 1 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("new"), new NodeContentElem.OtherContent(" "), new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("util"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Random"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "Random", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("1")), "int", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("nextInt"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent(")")), "int", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0)), new Edge(new Plug(0, 2), new Plug(2, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("nextInt"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 2)), new NodeContentElem.OtherContent(")")), "int", ""))
```

## Static method invocation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
double a = Math.sqrt(1.0);
```

#### Expression tree diagram
```
└─ Math . sqrt ( # ) ∈ double
   └─ 1.0 ∈ double
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("1.0")), "double", ""), new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Math"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("sqrt"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(")")), "double", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Math"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("sqrt"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(")")), "double", ""))
```

## Static method invocation with type arguments
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
List<String> l = Collections.<String>emptyList();
```

#### Expression tree diagram
```
└─ Collections . < String > emptyList ( ) ∈ List<String>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Collections"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("emptyList"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "List<String>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Collections"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("emptyList"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "List<String>", ""))
```

## Static method invocation on qualified name
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
double a = java.lang.Math.sqrt(1.0);
```

#### Expression tree diagram
```
└─ java . lang . Math . sqrt ( # ) ∈ double
   └─ 1.0 ∈ double
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("lang"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Math"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("sqrt"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(")")), "double", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("1.0")), "double", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("java"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("lang"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("Math"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("sqrt"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(0, 1)), new NodeContentElem.OtherContent(")")), "double", ""))
```

## Null literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.10.7)

### Example
```java
Object a = null;
```

#### Expression tree diagram
```
└─ null ∈ null
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("null")), "null", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("null")), "null", ""))
```

## Number literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.10.1)

### Example
```java
float a = 3.14f;
```

#### Expression tree diagram
```
└─ 3.14f ∈ float
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("3.14f")), "float", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("3.14f")), "float", ""))
```

## Postfix expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.14)

### Example
```java
int n = 0; int a = n++;
```

#### Expression tree diagram
```
└─ # ++ ∈ int
   └─ n ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("++")), "int", ""), new Node(new Plug(2, 0), List.of(new NodeContentElem.NameUse("n")), "int", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0))),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent("++")), "int", ""))
```

## Prefix expression
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.15)

### Example
```java
boolean a = !false;
```

#### Expression tree diagram
```
└─ ! # ∈ boolean
   └─ false ∈ boolean
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("!"), new NodeContentElem.Hole(new Plug(0, 1))), "boolean", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("false")), "boolean", "")),
    Set.of(new Edge(new Plug(0, 1), new Plug(1, 0))),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("!"), new NodeContentElem.Hole(new Plug(0, 1))), "boolean", ""))
```

## String literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.10.5)

### Example
```java
String a = "Hello world";
```

#### Expression tree diagram
```
└─ "Hello world" ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("\"Hello world\"")), "String", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("\"Hello world\"")), "String", ""))
```

## String literal with quotes inside
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.10.5)

### Example
```java
String a = "Hello \"John\"";
```

#### Expression tree diagram
```
└─ "Hello \"John\"" ∈ String
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("\"Hello \\\"John\\\"\"")), "String", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("\"Hello \\\"John\\\"\"")), "String", ""))
```

## This literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.8.3)

### Example
```java
Object o = new Object() { void m() { Object o = this; } };
```

#### Expression tree diagram
```
└─ this
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("this")), "", "")),
    Set.of(),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("this")), "", ""))
```

## This literal with qualifier
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.8.3)

### Example
```java
class Outer { class Inner { Outer o = Outer.this; } }
```

#### Expression tree diagram
```
└─ Outer . this ∈ Outer
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Outer"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("this")), "Outer", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Outer"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("this")), "Outer", ""))
```

## Super field access
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11.2)

### Example
```java
class Parent { int f; } class Child extends Parent { int a = super.f; }
```

#### Expression tree diagram
```
└─ super . f ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", ""))
```

## Qualified super field access
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.11.2)

### Example
```java
class Parent { int f; } class Child extends Parent { class Inner { int a = Child.super.f; } }
```

#### Expression tree diagram
```
└─ Child . super . f ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Child"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Child"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("f")), "int", ""))
```

## Super method invocation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
class Parent { int m() { return 1; } } class Child extends Parent { int a = super.m(); }
```

#### Expression tree diagram
```
└─ super . m ( ) ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "int", "")),
    Set.of(),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "int", ""))
```

## Super method invocation with arguments
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
class Parent { int m(int x) { return 1; } } class Child extends Parent { int a = super.m(1); }
```

#### Expression tree diagram
```
└─ super . m ( # ) ∈ int
   └─ 1 ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(2, 0), List.of(new NodeContentElem.OtherContent("1")), "int", ""), new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent(")")), "int", "")),
    Set.of(new Edge(new Plug(1, 1), new Plug(2, 0))),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.Hole(new Plug(1, 1)), new NodeContentElem.OtherContent(")")), "int", ""))
```

## Super method invocation with type arguments
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
class Parent { <T> List<T> m() { return null; } } class Child extends Parent { List<String> a = super.<String>m(); }
```

#### Expression tree diagram
```
└─ super . < String > m ( ) ∈ List<String>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "List<String>", "")),
    Set.of(),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("String"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "List<String>", ""))
```

## Qualified super method invocation
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.12)

### Example
```java
class Parent { int m() { return 1; } } class Child extends Parent { class Inner { int a = Child.super.m(); } }
```

#### Expression tree diagram
```
└─ Child . super . m ( ) ∈ int
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.NameUse("Child"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "int", "")),
    Set.of(),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.NameUse("Child"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("."), new NodeContentElem.NameUse("m"), new NodeContentElem.OtherContent("("), new NodeContentElem.OtherContent(")")), "int", ""))
```

## Super method reference
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.13)

### Example
```java
class Parent { <T> List<T> m() { return null; } } class Child extends Parent { java.util.function.Supplier<List<Integer>> s = super::<Integer>m; }
```

#### Expression tree diagram
```
└─ super :: < Integer > m ∈ Supplier<List<Integer>>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("::"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("Integer"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("m")), "Supplier<List<Integer>>", "")),
    Set.of(),
    new Node(new Plug(1, 0), List.of(new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("::"), new NodeContentElem.OtherContent("<"), new NodeContentElem.NameUse("Integer"), new NodeContentElem.OtherContent(">"), new NodeContentElem.NameUse("m")), "Supplier<List<Integer>>", ""))
```

## Qualified super method reference
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.13)

### Example
```java
class Parent { void m(int i){} } class Child extends Parent { class Inner { java.util.function.Consumer<Integer> s = Child.super::m; } }
```

#### Expression tree diagram
```
└─ Child . super :: m ∈ Consumer<Integer>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Child"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("::"), new NodeContentElem.NameUse("m")), "Consumer<Integer>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("Child"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("super"), new NodeContentElem.OtherContent("::"), new NodeContentElem.NameUse("m")), "Consumer<Integer>", ""))
```

## Class literal
See the [reference documentation](https://docs.oracle.com/javase/specs/jls/se11/html/jls-15.html#jls-15.8.2)

### Example
```java
Class<Void> a = void.class;
```

#### Expression tree diagram
```
└─ void . class ∈ Class<Void>
```

#### Source code
```java
new ExprTreeDiagram(
    Set.of(new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("void"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("class")), "Class<Void>", "")),
    Set.of(),
    new Node(new Plug(0, 0), List.of(new NodeContentElem.NameUse("void"), new NodeContentElem.OtherContent("."), new NodeContentElem.OtherContent("class")), "Class<Void>", ""))
```

