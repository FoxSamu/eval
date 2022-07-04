package net.shadew.eval;

record EError(String err) implements Expression {
    @Override
    public double eval(double... params) throws EvalException {
        throw new EvalException(err);
    }
}
