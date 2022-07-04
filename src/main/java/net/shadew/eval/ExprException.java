package net.shadew.eval;

/**
 * A generic exception that is thrown as {@link EvalException} when evaluation of an expression fails, or as a {@link
 * ParseException} when parsing of an expression fails.
 */
public class ExprException extends Exception {
    public ExprException() {
    }

    public ExprException(String message) {
        super(message);
    }

    public ExprException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExprException(Throwable cause) {
        super(cause);
    }
}
