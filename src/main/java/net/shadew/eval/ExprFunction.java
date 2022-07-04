package net.shadew.eval;

/**
 * A function definition to be called by an expression.
 */
public interface ExprFunction {
    /**
     * Calls the function
     *
     * @param args The arguments
     * @return The computed value
     *
     * @throws EvalException If evaluation fails
     */
    double compute(double... args) throws EvalException;

    /**
     * Returns a new function that is marked as non-mutating. See {@link #noMutation(ExprFunction)}.
     *
     * @return A new function that does not mutate.
     */
    default ExprFunction noMutation() {
        return noMutation(this);
    }

    /**
     * Returns whether the given function instance is marked as non-mutating. See {@link #noMutation(ExprFunction)}.
     *
     * @param fn The function to check
     * @return Whether the function does not mutate
     */
    static boolean doesNotMutate(ExprFunction fn) {
        return fn instanceof NonMutatingFunction;
    }

    /**
     * Returns a new function that copies the given function, but that is marked as non-mutating. This means that for
     * every combination of inputs, there is only one output which is always the same. Functions marked as non-mutating
     * are simplified when making an expression context-free, where possible.
     *
     * @return A new function that does not mutate.
     */
    static ExprFunction noMutation(ExprFunction fn) {
        if (fn instanceof NonMutatingFunction)
            return fn;
        return (NonMutatingFunction) fn::compute;
    }

    /**
     * Creates a zero-argument function.
     *
     * @param fn The function implementation
     * @return A function
     */
    static ExprFunction zero(Zero fn) {
        return args -> fn.compute();
    }

    /**
     * A zero-argument function.
     */
    interface Zero {
        double compute() throws EvalException;
    }

    /**
     * Creates a one-argument function.
     *
     * @param fn The function implementation
     * @return A function
     */
    static ExprFunction one(One fn) {
        return args -> {
            if (args.length < 1)
                throw new EvalException("Expected 1 argument");
            return fn.compute(args[0]);
        };
    }

    /**
     * Creates an overloaded function.
     *
     * @param a The function implementation for no arguments
     * @param b The function implementation for one argument
     * @return A function
     */
    static ExprFunction zeroOrOne(Zero a, One b) {
        return args -> {
            if (args.length < 1)
                return a.compute();
            return b.compute(args[0]);
        };
    }

    /**
     * A one-argument function.
     */
    interface One {
        double compute(double a) throws EvalException;
    }

    /**
     * Creates a two-argument function.
     *
     * @param fn The function implementation
     * @return A function
     */
    static ExprFunction two(Two fn) {
        return args -> {
            if (args.length < 2)
                throw new EvalException("Expected 2 arguments");
            return fn.compute(args[0], args[1]);
        };
    }

    /**
     * Creates an overloaded function.
     *
     * @param a The function implementation for no arguments
     * @param b The function implementation for two arguments
     * @return A function
     */
    static ExprFunction zeroOrTwo(Zero a, Two b) {
        return args -> {
            if (args.length < 2)
                return a.compute();
            return b.compute(args[0], args[1]);
        };
    }

    /**
     * Creates an overloaded function.
     *
     * @param a The function implementation for one argument
     * @param b The function implementation for two arguments
     * @return A function
     */
    static ExprFunction oneOrTwo(One a, Two b) {
        return args -> {
            if (args.length < 1)
                throw new EvalException("Expected 1 argument");
            if (args.length < 2)
                return a.compute(args[0]);
            return b.compute(args[0], args[1]);
        };
    }

    /**
     * A two-argument function.
     */
    interface Two {
        double compute(double a, double b) throws EvalException;
    }

    /**
     * Creates a three-argument function.
     *
     * @param fn The function implementation
     * @return A function
     */
    static ExprFunction three(Three fn) {
        return args -> {
            if (args.length < 3)
                throw new EvalException("Expected 3 arguments");
            return fn.compute(args[0], args[1], args[2]);
        };
    }

    /**
     * Creates an overloaded function.
     *
     * @param a The function implementation for no arguments
     * @param b The function implementation for three arguments
     * @return A function
     */
    static ExprFunction zeroOrThree(Zero a, Three b) {
        return args -> {
            if (args.length < 3)
                return a.compute();
            return b.compute(args[0], args[1], args[2]);
        };
    }

    /**
     * Creates an overloaded function.
     *
     * @param a The function implementation for one argument
     * @param b The function implementation for three arguments
     * @return A function
     */
    static ExprFunction oneOrThree(One a, Three b) {
        return args -> {
            if (args.length < 1)
                throw new EvalException("Expected 1 argument");
            if (args.length < 3)
                return a.compute(args[0]);
            return b.compute(args[0], args[1], args[2]);
        };
    }

    /**
     * Creates an overloaded function.
     *
     * @param a The function implementation for two arguments
     * @param b The function implementation for three arguments
     * @return A function
     */
    static ExprFunction twoOrThree(Two a, Three b) {
        return args -> {
            if (args.length < 2)
                throw new EvalException("Expected 2 arguments");
            if (args.length < 3)
                return a.compute(args[0], args[1]);
            return b.compute(args[0], args[1], args[2]);
        };
    }

    /**
     * A three-argument function.
     */
    interface Three {
        double compute(double a, double b, double c) throws EvalException;
    }

    /**
     * Creates a function that expects a certain amount of arguments. If the given function implementation is
     * non-mutating then the returned function is also non-mutating.
     *
     * @param fn           The function implementation
     * @param requiredArgs The required amount of arguments
     * @return A function
     */
    static ExprFunction args(ExprFunction fn, int requiredArgs) {
        if (fn instanceof NonMutatingFunction)
            return (NonMutatingFunction) args -> {
                if (args.length < requiredArgs)
                    throw new EvalException("Expected " + requiredArgs + " argument" + (requiredArgs == 1 ? "" : "s"));
                return fn.compute(args);
            };
        else
            return args -> {
                if (args.length < requiredArgs)
                    throw new EvalException("Expected " + requiredArgs + " argument" + (requiredArgs == 1 ? "" : "s"));
                return fn.compute(args);
            };
    }
}
