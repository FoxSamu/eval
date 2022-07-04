package net.shadew.eval;

record EFunction(ExprFunction function, Expression... exprs) implements Expression {
    @Override
    public double eval(double... params) throws EvalException {
        double[] results = new double[exprs.length];
        for (int i = 0, l = exprs.length; i < l; i ++) {
            results[i] = exprs[i].eval(params);
        }
        return function.compute(results);
    }
}
