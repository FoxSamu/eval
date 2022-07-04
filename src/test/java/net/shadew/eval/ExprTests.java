package net.shadew.eval;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExprTests {
    ExprContext context;

    @BeforeEach
    void beforeEach() {
        context = new ExprContext();
        context.addBasicMath();
    }

    private double eval(String str) throws Exception {
        Expression e = CtxExpression.parse(str).toContextless(context);
        return e.eval();
    }

    private double eval(String str, String param, double paramValue) throws Exception {
        Expression e = CtxExpression.parse(str).toContextless(context, param);
        return e.eval(paramValue);
    }

    @Test
    void testAdd() throws Exception {
        assertEquals(5, eval("2 + 3"), 0.00001);
    }

    @Test
    void testSub() throws Exception {
        assertEquals(-1, eval("2 - 3"), 0.00001);
    }

    @Test
    void testMul() throws Exception {
        assertEquals(6, eval("2 * 3"), 0.00001);
    }

    @Test
    void testDiv() throws Exception {
        assertEquals(2, eval("6 / 3"), 0.00001);
    }

    @Test
    void testMod() throws Exception {
        assertEquals(1, eval("7 % 3"), 0.00001);
    }

    @Test
    void testHex() throws Exception {
        assertEquals(0xE621, eval("0xE621"), 0.00001);
    }

    @Test
    void testBin() throws Exception {
        assertEquals(0b1100110011, eval("0b1100110011"), 0.00001);
    }

    @Test
    void testDec() throws Exception {
        assertEquals(621, eval("621"), 0.00001);
    }

    @Test
    void testVar() throws Exception {
        assertEquals(Math.PI, eval("PI"), 0.00001);
    }

    @Test
    void testInf() throws Exception {
        assertEquals(Double.POSITIVE_INFINITY, eval("inf"));
    }

    @Test
    void testNeg() throws Exception {
        assertEquals(-3, eval("-3"), 0.00001);
    }

    @Test
    void testAbs() throws Exception {
        assertEquals(3, eval("|-3|"), 0.00001);
        assertEquals(3, eval("|3|"), 0.00001);
    }

    @Test
    void testFunc() throws Exception {
        assertEquals(2, eval("sqrt(4)"), 0.00001);
    }

    @Test
    void testFuncMultiple() throws Exception {
        assertEquals(8, eval("max(2, 3, 4, 8)"), 0.00001);
    }

    @Test
    void testEvalError() {
        assertThrows(EvalException.class, () -> eval("unknown_variable"));
    }

    @Test
    void testGoldenRatio() throws Exception {
        assertEquals(1.618033988749, eval("(1 + sqrt(5)) / 2"), 0.00001);
    }

    @Test
    void testParam() throws Exception {
        assertEquals(4, eval("2 * a", "a", 2), 0.00001);
    }
}
