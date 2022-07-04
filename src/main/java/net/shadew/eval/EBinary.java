package net.shadew.eval;

record EBinary(Expression el, Expression er, BinaryOp op) implements Expression {
    @Override
    public double eval(double... params) throws EvalException {
        return op.apply(el.eval(params), er.eval(params));
    }
}
