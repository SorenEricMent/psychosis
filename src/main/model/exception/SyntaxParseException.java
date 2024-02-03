package model.exception;

public class SyntaxParseException extends Exception {
    public SyntaxParseException(String line) {
        super("Failed to parse syntax! \nLine content: " + line);
    }
}
