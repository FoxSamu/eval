package net.shadew.eval;

record EUnary(Expression e, UnaryOp op) implements Expression {
    @Override
    public double eval(double... params) throws EvalException {
        return op.apply(e.eval(params));
    }
}
