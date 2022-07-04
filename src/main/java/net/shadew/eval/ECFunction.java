package net.shadew.eval;

record ECFunction(String function, CtxExpression... exprs) implements CtxExpression {
    @Override
    public double eval(ExprContext ctx) throws EvalException {
        double[] results = new double[exprs.length];
        for (int i = 0, l = exprs.length; i < l; i++) {
            results[i] = exprs[i].eval(ctx);
        }
        return ctx.call(function, results);
    }

    @Override
    public Expression toContextless(ExprContext context, String... paramNames) {
        Expression[] es = new Expression[exprs.length];
        for (int i = 0, l = exprs.length; i < l; i++) {
            Expression e = es[i] = exprs[i].toContextless(context, paramNames);
            if (e instanceof EError)
                return e;
        }

        if (!context.hasFunc(function))
            return new EError("No such function '" + function + "'");

        ExprFunction func = context.func(function);

        simplify:
        if (ExprFunction.doesNotMutate(func)) {
            double[] res = new double[es.length];
            for (int i = 0, l = es.length; i < l; i++) {
                if (es[i] instanceof EConstant c)
                    res[i] = c.val();
                else break simplify;
            }

            try {
                double val = func.compute(res);
                return new EConstant(val);
            } catch (EvalException e) {
                return new EError(e.getMessage());
            }
        }

        return new EFunction(func, es);
    }
}
