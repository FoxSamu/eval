package net.shadew.eval;

/**
 * An exception that is thrown when parsing of an expression fails.
 */
public class ParseException extends ExprException {
    private final String errorInput;
    private final int pos;

    public ParseException(String errorInput, int pos, String message) {
        super(message);
        this.errorInput = errorInput;
        this.pos = pos;
    }

    /**
     * Returns the erroneous input string
     */
    public String errorInput() {
        return errorInput;
    }

    /**
     * Returns the position of the error in the string
     */
    public int pos() {
        return pos;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder(super.getMessage());
        builder.append(System.lineSeparator());
        builder.append(errorInput).append(System.lineSeparator());
        for (int i = 0, pos = this.pos; i < pos; i ++) {
            builder.append(" ");
        }
        builder.append("^");
        return builder.toString();
    }
}
