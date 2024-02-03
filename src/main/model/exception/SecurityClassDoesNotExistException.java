package model.exception;

public class SecurityClassDoesNotExistException extends Exception {
    public SecurityClassDoesNotExistException(String className) {
        super("Trying to add access vectors to a security class that is not defined.\nClass name:" + className);
    }
}
