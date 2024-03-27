package model.exception;

// Generic exception for adding duplicate name to a uniqueness-required set
public class DuplicateException extends RuntimeException {

    // EFFECTS: construct the exception with a message warning the duplicate
    public DuplicateException(String name) {
        super("The name " + name + " already exists");
    }
}
