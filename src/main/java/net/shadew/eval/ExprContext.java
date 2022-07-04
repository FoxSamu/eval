package net.shadew.eval;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An expression context is mandatory for the evaluation of expressions, as this holds all the variables and functions
 * available in an expression.
 */
public class ExprContext {
    private final Map<String, Double> variables = new HashMap<>();
    private final Map<String, ExprFunction> functions = new HashMap<>();

    /**
     * Returns a variable value, like as it is accessed in an expression.
     *
     * @param var The variable name
     * @return The variable value
     *
     * @throws EvalException When the variable was not defined
     */
    public double get(String var) throws EvalException {
        Double d = variables.get(var);
        if (d == null)
            throw new EvalException("No such variable: '" + var + "'");
        return d;
    }

    /**
     * Calls a function, like as it is called in an expression.
     *
     * @param function The function name
     * @param args     The function arguments
     * @return The computed value
     *
     * @throws EvalException When the function was not defined, or when the function fails itself
     */
    public double call(String function, double... args) throws EvalException {
        ExprFunction fn = functions.get(function);
        if (fn == null) throw new EvalException("No such function '" + function + "'");
        try {
            return fn.compute(args);
        } catch (EvalException exc) {
            throw new EvalException("Function '" + function + "': " + exc.getMessage(), exc);
        }
    }

    /**
     * Returns a defined function
     *
     * @param function The function name
     * @return The function instance, or null if it was not defined
     */
    public ExprFunction func(String function) {
        return functions.get(function);
    }

    /**
     * Defines a function
     *
     * @param function The function name
     * @param fn       The function
     * @return This instance for chain calls
     *
     * @throws NullPointerException If either the function name or value is null
     */
    public ExprContext defineFunc(String function, ExprFunction fn) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(fn);
        functions.put(function, fn);
        return this;
    }

    /**
     * Removes a function, if it exists
     *
     * @param function The function name
     * @return This instance for chain calls
     */
    public ExprContext removeFunc(String function) {
        functions.remove(function);
        return this;
    }

    /**
     * Returns whether a variable is defined
     *
     * @param var The variable name
     * @return True if it is defined
     */
    public boolean hasVar(String var) {
        return variables.containsKey(var);
    }

    /**
     * Returns whether a function is defined
     *
     * @param function The function name
     * @return True if it is defined
     */
    public boolean hasFunc(String function) {
        return functions.containsKey(function);
    }

    /**
     * Defines a variable
     *
     * @param var The variable name
     * @param val The value
     * @return This instance for chain calls
     *
     * @throws NullPointerException If the name is null
     */
    public ExprContext defineVar(String var, double val) {
        Objects.requireNonNull(var);
        variables.put(var, val);
        return this;
    }

    /**
     * Removes a variable, if it exists
     *
     * @param var The variable name
     * @return This instance for chain calls
     */
    public ExprContext removeVar(String var) {
        functions.remove(var);
        return this;
    }


    private static final double LOG_2 = Math.log(2);

    /**
     * Defines a bunch of basic functions and variables for basic mathematical computations. The following variables and
     * functions are included
     *
     * <ul>
     * <li>{@link Double#POSITIVE_INFINITY inf}</li>
     * <li>{@link Double#NaN NaN}</li>
     * <li>{@link Math#PI PI}</li>
     * <li>{@link Math#E E}</li>
     * <li>{@link Math#abs(double) abs(x)}, although the expression syntax allows {@code |x|} to be written</li>
     * <li>{@link Math#signum(double) sign(x)}</li>
     * <li>{@link Math#sqrt(double) sqrt(x)}</li>
     * <li>{@link Math#cbrt(double) cbrt(x)}</li>
     * <li>{@code rt(x, y)}, the yth-power root of x ({@link Math#pow(double, double) Math.pow(x, 1/y)})</li>
     * <li>{@link Math#ceil(double) ceil(x)}</li>
     * <li>{@link Math#floor(double) floor(x)}</li>
     * <li>{@link Math#round(double) round(x)}</li>
     * <li>{@link Math#sin(double) sin(x)}</li>
     * <li>{@link Math#cos(double) cos(x)}</li>
     * <li>{@link Math#tan(double) tan(x)}</li>
     * <li>{@link Math#asin(double) asin(x)}</li>
     * <li>{@link Math#acos(double) acos(x)}</li>
     * <li>{@link Math#atan(double) atan(x)}</li>
     * <li>{@link Math#sinh(double) sinh(x)}</li>
     * <li>{@link Math#cosh(double) cosh(x)}</li>
     * <li>{@link Math#tanh(double) tanh(x)}</li>
     * <li>{@link Math#exp(double) exp(x)}</li>
     * <li>{@link Math#log(double) ln(x)}</li>
     * <li>{@link Math#log10(double) log10(x)}</li>
     * <li>{@code log2(x)}, log base 2 of x ({@link Math#log(double) Math.log(x) / Math.log(2)}</li>
     * <li>{@code log(x, y)}, log base y of x ({@link Math#log(double) Math.log(x) / Math.log(y)}</li>
     * <li>{@code max(...)}, max value of all arguments</li>
     * <li>{@code min(...)}, min value of all arguments</li>
     * <li>{@code avg(...)}, average of all arguments</li>
     * <li>{@code sum(...)}, sum of all arguments</li>
     * <li>{@code prod(...)}, product of all arguments</li>
     * </ul>
     *
     * @return This instance for chain calls
     */
    public ExprContext addBasicMath() {
        defineVar("inf", Double.POSITIVE_INFINITY);
        defineVar("NaN", Double.NaN);
        defineVar("PI", Math.PI);
        defineVar("E", Math.E);
        defineFunc("abs", ExprFunction.noMutation(ExprFunction.one(Math::abs)));
        defineFunc("sign", ExprFunction.noMutation(ExprFunction.one(Math::signum)));
        defineFunc("sqrt", ExprFunction.noMutation(ExprFunction.one(Math::sqrt)));
        defineFunc("cbrt", ExprFunction.noMutation(ExprFunction.one(Math::cbrt)));
        defineFunc("rt", ExprFunction.noMutation(ExprFunction.two((a, b) -> Math.pow(a, 1 / b))));
        defineFunc("ceil", ExprFunction.noMutation(ExprFunction.one(Math::ceil)));
        defineFunc("floor", ExprFunction.noMutation(ExprFunction.one(Math::floor)));
        defineFunc("round", ExprFunction.noMutation(ExprFunction.one(Math::round)));
        defineFunc("sin", ExprFunction.noMutation(ExprFunction.one(Math::sin)));
        defineFunc("cos", ExprFunction.noMutation(ExprFunction.one(Math::cos)));
        defineFunc("tan", ExprFunction.noMutation(ExprFunction.one(Math::tan)));
        defineFunc("asin", ExprFunction.noMutation(ExprFunction.one(Math::asin)));
        defineFunc("acos", ExprFunction.noMutation(ExprFunction.one(Math::acos)));
        defineFunc("atan", ExprFunction.noMutation(ExprFunction.one(Math::atan)));
        defineFunc("sinh", ExprFunction.noMutation(ExprFunction.one(Math::sinh)));
        defineFunc("cosh", ExprFunction.noMutation(ExprFunction.one(Math::cosh)));
        defineFunc("tanh", ExprFunction.noMutation(ExprFunction.one(Math::tanh)));
        defineFunc("exp", ExprFunction.noMutation(ExprFunction.one(Math::exp)));
        defineFunc("ln", ExprFunction.noMutation(ExprFunction.one(Math::log)));
        defineFunc("log10", ExprFunction.noMutation(ExprFunction.one(Math::log10)));
        defineFunc("log2", ExprFunction.noMutation(ExprFunction.one(a -> Math.log(a) / LOG_2)));
        defineFunc("log", ExprFunction.noMutation(ExprFunction.two((a, b) -> Math.log(a) / Math.log(b))));
        defineFunc("max", ExprFunction.noMutation(args -> {
            double v = Double.NEGATIVE_INFINITY;
            for (double d : args)
                v = Math.max(d, v);
            return v;
        }));
        defineFunc("min", ExprFunction.noMutation(args -> {
            double v = Double.POSITIVE_INFINITY;
            for (double d : args)
                v = Math.min(d, v);
            return v;
        }));
        defineFunc("avg", ExprFunction.noMutation(args -> {
            double v = 0;
            for (double d : args)
                v += d / args.length;
            return v;
        }));
        defineFunc("sum", ExprFunction.noMutation(args -> {
            double v = 0;
            for (double d : args)
                v += d;
            return v;
        }));
        defineFunc("prod", ExprFunction.noMutation(args -> {
            double v = 0;
            for (double d : args)
                v *= d;
            return v;
        }));
        return this;
    }
}
