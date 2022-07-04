package net.shadew.eval;

interface UnaryOp {
    UnaryOp NEG = val -> -val;
    UnaryOp ABS = Math::abs;

    double apply(double val);
}
