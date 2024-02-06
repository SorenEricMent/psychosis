package model;

import java.util.ArrayList;
import java.util.HashMap;

public class FileObjectModel<X> {
    // Abstraction of a file
    // Correlate line range to objects using a list of triple

    private HashMap<String, X> objectSet;

    private ArrayList<String> content = new ArrayList<>();

    private ArrayList<Pair<Integer, String>> lookupList =
            new ArrayList<Pair<Integer, String>>();

}
