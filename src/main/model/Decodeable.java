package model;

import model.exception.SyntaxParseException;

public interface Decodeable {
    // The return type is meant to be overrided
    static Object parser(String str) throws SyntaxParseException {
        return null;
    }
}
