# A simple expression evaluator
This small Java library can evaluate simple mathematical expressions involving only numbers, such as `(1 + sqrt(5))/2`.

## Usage
What you will need is an `ExprContext`. This holds all the variables and functions available in your expressions and can be reused for multiple expressions. You can define your own variables and functions in here, but it can also define a set of default variables and functions on demand.

```java
ExprContext ctx = new ExprContext();
// Provide standard math functions such as sin() and sqrt()
// For a full list, see the documentation of this method
ctx.addBasicMath();

// Now parse your expression
CtxExpression expr = CtxExpression.parse("3 + 5 * 5");

// And then evaluate
System.out.println(expr.eval(ctx));
// Will print 28
```

You can install the artifact from my Maven repository, via Gradle:
```gradle
repositories {
    maven { url "https://maven.shadew.net/" }
}

dependencies {
    implementation "net.shadew:eval:0.1"
}
```

## Formal expression grammar
Expressions follow the following formal grammar

- expression = expression `+` multiply
- expression = expression `-` multiply
- multiply = multiply `*` power
- multiply = multiply `/` power
- multiply = multiply `%` power
- power = power `^` base
- base = `(` expression `)`
- base = `|` expression `|`
- base = `+` base
- base = `-` base
- base = **Number**
- base = **Identifier**
- base = **Identifier** `(` args `)`
- args = **\[empty]**
- args = expression
- args = expression `,` args

The terminals **Number** and **Identifier** refer respectively to Java-like numbers and Java-like identifiers. The **\[empty]** symbol refers to the empty string.

Whitespaces are not included in the grammar, but are valid terminals which are ignored wherever they are encountered between the grammar.

See the documentation of `CtxExpression.parse(...)` for a more detailed description
of the syntax.
