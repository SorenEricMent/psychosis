package model.policy;

import model.FileObjectModel;
import model.Pair;

import java.io.File;
import java.util.ArrayList;

public class InterfaceSetModel extends FileObjectModel {
    protected ArrayList<Pair<Integer, String>> lookupList =
            new ArrayList<Pair<Integer, String>>();

    public String toString() {
        return null;
    }

    public int lineCount() {
        return 0;
    }
}
