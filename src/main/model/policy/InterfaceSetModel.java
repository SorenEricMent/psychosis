package model.policy;

import model.FileObjectModel;
import model.Pair;

import java.io.File;
import java.util.ArrayList;

public class InterfaceSetModel extends FileObjectModel {

    private ArrayList<InterfaceModel> interfaces = new ArrayList<>();

    public String toString() {
        String res = "";
        for (InterfaceModel i : interfaces) {
            res = res.concat(interfaces.toString() + "\n");
        }
        return res;
    }

    // EFFECTS: return number of interfaces
    public int lineCount() {
        return interfaces.size();
    }
}
