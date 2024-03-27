package model.exception;

// Generic exception for not finding an element with key name
public class NotFoundException extends RuntimeException {

    // EFFECTS: construct the exception with a message warning the non-existent of a name
    public NotFoundException(String name) {
        super("Trying to operates on a non-exist name: " + name);
    }

}
