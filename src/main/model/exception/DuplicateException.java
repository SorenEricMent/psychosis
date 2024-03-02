package model.exception;

// Generic exception for adding duplicate name to a uniqueness-required set
public class DuplicateException extends RuntimeException {
    public DuplicateException(String name) {
        super("The name " + name + " already exists");
    }
}
