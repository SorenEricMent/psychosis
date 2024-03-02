package model.exception;

// Generic exception for not finding an element with key name
public class NotFoundException extends RuntimeException {
    public NotFoundException(String name) {
        super("Trying to operates on a non-exist name: " + name);
    }

}
