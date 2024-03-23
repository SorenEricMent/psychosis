package model;

import model.exception.DuplicateException;

public interface UniqueEmptyAddable {
    void add() throws DuplicateException;
}
