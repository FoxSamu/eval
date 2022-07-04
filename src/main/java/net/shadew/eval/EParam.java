package net.shadew.eval;

record EParam(int i) implements Expression {
    @Override
    public double eval(double... params) throws EvalException {
        if (i < 0 || i >= params.length)
            throw new EvalException("Parameter " + i + " not defined");
        return params[i];
    }
}
