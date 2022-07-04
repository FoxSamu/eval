package net.shadew.eval;

import java.util.ArrayList;
import java.util.List;

class Parser {
    static final ThreadLocal<Parser> parser = ThreadLocal.withInitial(Parser::new);

    private int pos;
    private String string;

    void input(String str) {
        pos = 0;
        string = str;
    }

    ParseException error(String message) {
        return new ParseException(string, pos, message);
    }

    int lookahead() {
        return lookahead(0);
    }

    int lookahead(int off) {
        if (pos + off >= string.length())
            return -1;
        return string.charAt(pos + off);
    }

    void advance() {
        if (pos != string.length())
            pos++;
    }

    void skipWhitespace() {
        while (Character.isWhitespace(lookahead())) {
            advance();
        }
    }

    boolean hasIdentifier() {
        skipWhitespace();
        return Character.isJavaIdentifierStart(lookahead());
    }

    String readIdentifier() throws ParseException {
        if (!hasIdentifier()) {
            throw error("Expected identifier");
        }

        int p = pos;
        int o = 1;
        while (Character.isJavaIdentifierPart(lookahead(o))) {
            o++;
        }

        pos += o;
        return string.substring(p, p + o);
    }

    boolean hasNumber() {
        skipWhitespace();
        int c = lookahead();
        return c >= '0' && c <= '9' || c == '.';
    }

    double readNumber() throws ParseException {
        if (!hasNumber()) {
            throw error("Expected number");
        }

        int p = pos;
        int o = 1;
        int nc;
        boolean e = false;
        while (Character.isJavaIdentifierPart(nc = lookahead(o)) || e && (nc == '+' || nc == '-')) {
            e = nc == 'e' || nc == 'E';
            o++;
        }

        String ns = string.substring(p, p + o);

        double val;
        if (ns.startsWith("0x") || ns.startsWith("0X")) {
            if (ns.length() < 3)
                throw error("Illegal number");

            double v = 0;
            for (int i = 2, l = ns.length(); i < l; i++) {
                char c = ns.charAt(i);
                v *= 16;
                if (c >= '0' && c <= '9')
                    v += c - '0';
                else if (c >= 'a' && c <= 'f')
                    v += c - 'a' + 10;
                else if (c >= 'A' && c <= 'F')
                    v += c - 'A' + 10;
                else
                    throw error("Illegal number");
            }
            val = v;
        } else if (ns.startsWith("0b") || ns.startsWith("0B")) {
            if (ns.length() < 3)
                throw error("Illegal number");

            double v = 0;
            for (int i = 2, l = ns.length(); i < l; i++) {
                char c = ns.charAt(i);
                v *= 2;
                if (c >= '0' && c <= '1')
                    v += c - '0';
                else
                    throw error("Illegal number");
            }
            val = v;
        } else {
            try {
                val = Double.parseDouble(ns);
            } catch (NumberFormatException exc) {
                throw error("Illegal number");
            }
        }

        pos += o;
        return val;
    }

    boolean hasToken(String exp) {
        skipWhitespace();
        return string.startsWith(exp, pos);
    }

    void token(String exp) throws ParseException {
        skipWhitespace();
        if (string.startsWith(exp, pos)) {
            pos += exp.length();
        } else {
            throw error("Expected '" + exp + "'");
        }
    }

    CtxExpression parseExpr() throws ParseException {
        CtxExpression e = parseMultiply();

        while (true) {
            if (hasToken("+")) {
                token("+");
                CtxExpression r = parseMultiply();
                e = new ECBinary(e, r, BinaryOp.ADD);
            } else if (hasToken("-")) {
                token("-");
                CtxExpression r = parseMultiply();
                e = new ECBinary(e, r, BinaryOp.SUB);
            } else {
                return e;
            }
        }
    }

    CtxExpression parseMultiply() throws ParseException {
        CtxExpression e = parsePower();

        while (true) {
            if (hasToken("*")) {
                token("*");
                CtxExpression r = parsePower();
                e = new ECBinary(e, r, BinaryOp.MUL);
            } else if (hasToken("/")) {
                token("/");
                CtxExpression r = parsePower();
                e = new ECBinary(e, r, BinaryOp.DIV);
            } else if (hasToken("%")) {
                token("%");
                CtxExpression r = parsePower();
                e = new ECBinary(e, r, BinaryOp.MOD);
            } else {
                return e;
            }
        }
    }

    CtxExpression parsePower() throws ParseException {
        CtxExpression e = parsePrimitive();

        while (true) {
            if (hasToken("^")) {
                token("^");
                CtxExpression r = parsePrimitive();
                e = new ECBinary(e, r, BinaryOp.POW);
            } else {
                return e;
            }
        }
    }

    CtxExpression parsePrimitive() throws ParseException {
        if (hasIdentifier()) {
            String id = readIdentifier();

            if (hasToken("(")) {
                token("(");
                List<CtxExpression> args = new ArrayList<>();

                if (hasToken(")")) {
                    token(")");
                    return new ECFunction(id, args.toArray(CtxExpression[]::new));
                }

                while (true) {
                    args.add(parseExpr());

                    if (hasToken(","))
                        token(",");
                    else if (hasToken(")")) {
                        token(")");
                        return new ECFunction(id, args.toArray(CtxExpression[]::new));
                    } else {
                        throw error("Expected ',' or ')'");
                    }
                }
            } else {
                return new ECVariable(id);
            }
        } else if (hasNumber()) {
            double num = readNumber();
            return new EConstant(num);
        } else if (hasToken("(")) {
            token("(");
            CtxExpression e = parseExpr();
            token(")");
            return e;
        } else if (hasToken("|")) {
            token("|");
            CtxExpression e = parseExpr();
            token("|");
            return new ECUnary(e, UnaryOp.ABS);
        } else if (hasToken("-")) {
            token("-");
            CtxExpression e = parsePrimitive();
            return new ECUnary(e, UnaryOp.NEG);
        } else if (hasToken("+")) {
            token("+");
            return parsePrimitive();
        } else {
            throw error("Expected identifier, number, '+', '-', '|' or '('");
        }
    }
}
