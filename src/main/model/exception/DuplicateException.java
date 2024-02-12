package model.exception;

public class DuplicateException extends Exception {
    public DuplicateException(String name) {
        super("The name " + name + " already exists");
    }
}
