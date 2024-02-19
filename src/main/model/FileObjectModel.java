package model;

import java.util.ArrayList;

public abstract class FileObjectModel {
    // Abstraction of a file

    private final ArrayList<String> content = new ArrayList<>();

    public abstract String toString();

    public abstract int lineCount();

}