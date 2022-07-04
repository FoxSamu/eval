package net.shadew.eval;

/**
 * An expression that requires an {@link ExprContext} to evaluate.
 */
public interface CtxExpression {
    /**
     * Evaluates this expression.
     *
     * @param ctx The expression context
     * @return The resulting value
     *
     * @throws EvalException If evaluation fails
     */
    double eval(ExprContext ctx) throws EvalException;

    /**
     * Converts this expression to an optimised, context-free {@link Expression} instance. The given parameter names
     * define what variable names refer to which input parameter to {@link Expression#eval} (in order). Other variable
     * references are converted to constant values by looking up the variable values in the given context.
     * <p>
     * Note that errors are imitated in the returned expression. For example, if a variable is not found, the returned
     * expression will imitate the error by immediately throwing it upon evaluation.
     *
     * @param context    The context to read variables from
     * @param paramNames The parameter names
     * @return A context-free expression
     */
    Expression toContextless(ExprContext context, String... paramNames);

    /**
     * Parses an expression from a string. The syntax allows the following:
     * <ul>
     * <li>Decimal Java-like floating point numbers: {@code 3.141592}, {@code 4.3e-8}</li>
     * <li>Hexadecimal integral numbers: {@code 0xABCDEF}</li>
     * <li>Binary integral numbers: {@code 0b1100111}</li>
     * <li>Variable names matching legal Java identifiers: {@code foo}</li>
     * <li>Parentheses: {@code (a)}</li>
     * <li>Absolute value using vertical bars: {@code |a|}</li>
     * <li>Negation: {@code -a}</li>
     * <li>Unary plus (although it's useless): {@code +a}</li>
     * <li>Addition/subtraction: {@code a + b}, {@code a - b}</li>
     * <li>Multiplication/division/modulo: {@code a * b}, {@code a / b}, {@code a % b}</li>
     * <li>Power: {@code a ^ b}</li>
     * <li>Function calls: {@code foo(bar, baz)}</li>
     * <li>Whitespaces, which are ignored (any character matched by {@link Character#isWhitespace(int)} is valid)</li>
     * </ul>
     *
     * Operator precedence is as follows (higher entries have higher precedence):
     * <ul>
     * <li>{@code (a)}, {@code |a|}</li>
     * <li>{@code +a}, {@code -a}</li>
     * <li>{@code a^b}</li>
     * <li>{@code a*b}, {@code a/b}, {@code a%b}</li>
     * <li>{@code a+b}, {@code a-b}</li>
     * </ul>
     *
     * @param expr The expression to parse
     * @return The parsed expression
     *
     * @throws ParseException If the expression syntax is correct.
     */
    static CtxExpression parse(String expr) throws ParseException {
        Parser p = Parser.parser.get();
        p.input(expr);
        return p.parseExpr();
    }
}
