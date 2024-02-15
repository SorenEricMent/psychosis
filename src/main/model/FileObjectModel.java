package model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class FileObjectModel {
    // Abstraction of a file
    // Correlate line range to objects using a list of triple

    protected ArrayList<String> content = new ArrayList<>();

    public abstract String readRaw();

    public abstract int lineCount();

}