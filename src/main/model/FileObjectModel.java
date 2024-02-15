package model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class FileObjectModel {
    // Abstraction of a file

    private ArrayList<String> content = new ArrayList<>();

    public abstract String toString();

    public abstract int lineCount();

}