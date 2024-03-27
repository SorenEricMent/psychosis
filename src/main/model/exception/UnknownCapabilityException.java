package model.exception;

// Legacy codebase, will be replaced with NotFoundException
public class UnknownCapabilityException extends Exception {

    // EFFECTS: construct the exception with a message warning the non-existence of a capability
    // this will be reokaced with NotFoundException

    public UnknownCapabilityException(String line) {
        super("Undefined policy capability: " + line);
    }
}
