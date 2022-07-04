package net.shadew.eval;

/**
 * An expression that, other than some potential input parameters, does not require a context to evaluate. {@link
 * CtxExpression}s can be converted in to instances of this interface via {@link CtxExpression#toContextless}.
 */
public interface Expression {
    /**
     * Evaluates this expression, given the input parameters.
     *
     * @param params The input parameters
     * @return The resulting value
     *
     * @throws EvalException When evaluation fails
     */
    double eval(double... params) throws EvalException;
}
