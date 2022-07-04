package net.shadew.eval;

record ECBinary(CtxExpression el, CtxExpression er, BinaryOp op) implements CtxExpression {
    @Override
    public double eval(ExprContext ctx) throws EvalException {
        return op.apply(el.eval(ctx), er.eval(ctx));
    }

    @Override
    public Expression toContextless(ExprContext context, String... paramNames) {
        Expression l = el.toContextless(context, paramNames);
        if (l instanceof EError)
            return l;

        Expression r = er.toContextless(context, paramNames);
        if (r instanceof EError)
            return r;

        if (l instanceof EConstant cl && r instanceof EConstant cr)
            return new EConstant(op.apply(cl.val(), cr.val()));

        return new EBinary(l, r, op);
    }
}
