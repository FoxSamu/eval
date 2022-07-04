package net.shadew.eval;

record ECVariable(String var) implements CtxExpression {
    @Override
    public double eval(ExprContext ctx) throws EvalException {
        return ctx.get(var);
    }

    @Override
    public Expression toContextless(ExprContext context, String... paramNames) {
        int i = 0;
        for (String s : paramNames) {
            if (s.equals(var))
                return new EParam(i);
            i++;
        }

        if (context.hasVar(var)) {
            try {
                return new EConstant(context.get(var));
            } catch (EvalException e) {
                assert false; // Never happens since we checked hasVar already
            }
        }

        return new EError("No such variable: '" + var + "'");
    }
}
