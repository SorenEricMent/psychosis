package model.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String name) {
        super("Trying to operates on a non-exist name: " + name);
    }

}
