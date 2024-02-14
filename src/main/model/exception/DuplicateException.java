package model.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String name) {
        super("The name " + name + " already exists");
    }
}
