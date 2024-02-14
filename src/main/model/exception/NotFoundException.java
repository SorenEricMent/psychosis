package model.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String name) {
        super("Trying to operates on a non-exist name: " + name);
    }

}
