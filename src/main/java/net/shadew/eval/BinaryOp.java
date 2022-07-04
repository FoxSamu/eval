package net.shadew.eval;

interface BinaryOp {
    BinaryOp ADD = Double::sum;
    BinaryOp SUB = (l, r) -> l - r;
    BinaryOp MUL = (l, r) -> l * r;
    BinaryOp DIV = (l, r) -> l / r;
    BinaryOp MOD = (l, r) -> l % r;
    BinaryOp POW = Math::pow;

    double apply(double l, double r);
}
