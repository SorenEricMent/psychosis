package model.exception;

// Legacy codebase, will be replaced with NotFoundException
public class UnknownCapabilityException extends Exception {
    public UnknownCapabilityException(String line) {
        super("Undefined policy capability: " + line);
    }
}
