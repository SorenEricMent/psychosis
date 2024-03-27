package model;

import model.exception.SyntaxParseException;

// The class can be decoded from a file
public interface Decodeable {
    // The return type is meant to be overrided
    static Object parser(String str) throws SyntaxParseException {
        return null;
    }
}
