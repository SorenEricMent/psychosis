package persistence;

import model.Decodeable;
import model.Encodeable;
import model.policy.LayerModel;

import java.util.ArrayList;

public class Workspace implements Encodeable, Decodeable {

    int index;
    String name;
    ArrayList<LayerModel> layers;

    public Workspace() {
        index = 0;
        name = "workspace";
        layers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<LayerModel> getLayers() {
        return layers;
    }

    public Workspace parser(String content) {
        return null; //stub
    }

    public String toString() {
        return "";
    }
}