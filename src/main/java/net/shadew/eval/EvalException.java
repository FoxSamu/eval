package net.shadew.eval;

/**
 * An exception thrown when evaluation of an expression fails.
 */
public class EvalException extends ExprException {
    public EvalException() {
    }

    public EvalException(String message) {
        super(message);
    }

    public EvalException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvalException(Throwable cause) {
        super(cause);
    }
}
