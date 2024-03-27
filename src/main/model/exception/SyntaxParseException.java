package model.exception;

// Generic exception for parser's failure
public class SyntaxParseException extends Exception {
    // EFFECTS: construct the exception with a message warning user the surrounding context of the
    // error content;
    public SyntaxParseException(String line) {
        super("Failed to parse syntax! \nLine content: " + line);
    }
}
