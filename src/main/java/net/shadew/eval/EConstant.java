package net.shadew.eval;

record EConstant(double val) implements Expression, CtxExpression {
    @Override
    public double eval(ExprContext ctx) {
        return val;
    }

    @Override
    public Expression toContextless(ExprContext context, String... paramNames) {
        return this;
    }

    @Override
    public double eval(double... params) {
        return val;
    }
}
