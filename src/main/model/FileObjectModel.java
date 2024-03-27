package model;

import java.util.ArrayList;

// Abstraction of a file
// A placeholder, psychosis has no relevant functionalities.
public abstract class FileObjectModel {
    private final ArrayList<String> content = new ArrayList<>();

    public abstract String toString();

    public abstract int lineCount();

}