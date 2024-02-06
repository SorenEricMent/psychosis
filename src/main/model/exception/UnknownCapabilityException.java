package model.exception;

public class UnknownCapabilityException extends Exception {
    public UnknownCapabilityException(String line) {
        super("Undefined policy capability: " + line);
    }
}
