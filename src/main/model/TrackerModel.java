package model;

import model.policy.InterfaceModel;

import java.util.HashMap;
import java.util.ArrayList;

public class TrackerModel {
    // This serves as a tracker for Psychosis to lookup
    // things speedy

    // This should be replaced with a relational database
    // but... after the term project is finished.
    private HashMap<String, ArrayList<InterfaceModel>> mapSLabelIf;
    private HashMap<String, ArrayList<InterfaceModel>> mapTLabelIf;

    public TrackerModel() {
        mapSLabelIf = new HashMap<String, ArrayList<InterfaceModel>>();
        mapTLabelIf = new HashMap<String, ArrayList<InterfaceModel>>();
    }

    public void insertInterfaceWithSLabel(String key, InterfaceModel val) {
        if (!mapSLabelIf.containsKey(key)) {
            mapSLabelIf.put(key, new ArrayList<InterfaceModel>());
        }
        mapSLabelIf.get(key).add(val);
    }

    public void insertInterfaceWithTLabel(String key, InterfaceModel val) {
        if (!mapTLabelIf.containsKey(key)) {
            mapTLabelIf.put(key, new ArrayList<InterfaceModel>());
        }
        mapTLabelIf.get(key).add(val);
    }

    public ArrayList<InterfaceModel> queryInterfaceWithSLabel(String key) {
        return mapSLabelIf.getOrDefault(key, null);
    }

    public ArrayList<InterfaceModel> queryInterfaceWithTLabel(String key) {
        return mapTLabelIf.getOrDefault(key, null);
    }
}
