package model;

import model.exception.DuplicateException;

// Class could be added an empty child with a unique name
public interface UniqueEmptyAddable {
    void add() throws DuplicateException;
}
