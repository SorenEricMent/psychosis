package model;

import model.exception.NotFoundException;
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

    private HashMap<String, ArrayList<InterfaceModel>> tagTracker;


    public TrackerModel() {
        mapSLabelIf = new HashMap<String, ArrayList<InterfaceModel>>();
        mapTLabelIf = new HashMap<String, ArrayList<InterfaceModel>>();
        tagTracker = new HashMap<String, ArrayList<InterfaceModel>>();
    }

    // MODIFIES: this
    // EFFECTS: assign an interface into a tag tracker, create the tag if the tag doesn't exists
    public void insertInterfaceWithTag(String key, InterfaceModel i) {
        tagTracker.putIfAbsent(key, new ArrayList<InterfaceModel>());
        tagTracker.get(key).add(i);
    }

    // EFFECTS: return the list of interfaces with a specific tag
    public ArrayList<InterfaceModel> queryInterfaceWithTag(String key) {
        if (!this.tagTracker.containsKey(key)) {
            throw new NotFoundException("Tag not found");
        }
        return this.tagTracker.get(key);
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
