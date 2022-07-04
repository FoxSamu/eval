package net.shadew.eval;

record ECUnary(CtxExpression e, UnaryOp op) implements CtxExpression {
    @Override
    public double eval(ExprContext ctx) throws EvalException {
        return op.apply(e.eval(ctx));
    }

    @Override
    public Expression toContextless(ExprContext context, String... paramNames) {
        Expression l = e.toContextless(context, paramNames);
        if (l instanceof EError)
            return l;
        if (l instanceof EConstant c)
            return new EConstant(op.apply(c.val()));

        return new EUnary(l, op);
    }
}
