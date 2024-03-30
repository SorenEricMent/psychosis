package model;

import model.exception.NotFoundException;
import model.policy.InterfaceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// This serves as a tracker for Psychosis to lookup
// things speedy

// This should be replaced with a relational database
// but... after the term project is finished.

public class TrackerModel {
    private final HashMap<String, ArrayList<InterfaceModel>> mapSLabelIf;
    private final HashMap<String, ArrayList<InterfaceModel>> mapTLabelIf;

    private final HashMap<String, ArrayList<InterfaceModel>> tagTracker;

    private final HashSet<InterfaceModel> userDefined;

    // EFFECTS: init the tracker with no tracking info added
    public TrackerModel() {
        EventLog.getInstance().logEvent(new Event("Initialized Tracker"));
        mapSLabelIf = new HashMap<String, ArrayList<InterfaceModel>>();
        mapTLabelIf = new HashMap<String, ArrayList<InterfaceModel>>();
        tagTracker = new HashMap<String, ArrayList<InterfaceModel>>();
        userDefined = new HashSet<>();
    }

    // MODIFIES: this
    // EFFECTS: assign an interface to user-defined list
    public void insertInterfaceUserDefined(InterfaceModel i) {
        EventLog.getInstance().logEvent(new Event("Interface"
                + i.getOwner() + "." + i.getName() + "is marked as user-defined"));
        userDefined.add(i);
    }

    public HashSet<InterfaceModel> queryInterfaceUserDefined() {
        return userDefined;
    }

    // MODIFIES: this
    // EFFECTS: assign an interface into a tag tracker, create the tag if the tag doesn't exist
    public void insertInterfaceWithTag(String key, InterfaceModel i) {
        EventLog.getInstance().logEvent(new Event("Interface"
                + i.getOwner() + "." + i.getName() + "is marked with log " + key));
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

    // EFFECTS: insert a interface that has key as the source label of one of its statement to corresponding Tracker map
    public void insertInterfaceWithSLabel(String key, InterfaceModel val) {
        mapSLabelIf.putIfAbsent(key, new ArrayList<InterfaceModel>());
        mapSLabelIf.get(key).add(val);
    }

    // EFFECTS: insert a interface that has key as the target label of one of its statement to corresponding Tracker map
    public void insertInterfaceWithTLabel(String key, InterfaceModel val) {
        mapTLabelIf.putIfAbsent(key, new ArrayList<InterfaceModel>());
        mapTLabelIf.get(key).add(val);
    }

    // EFFECTS: return the list of interfaces that has key as the source label of one of its statement
    public ArrayList<InterfaceModel> queryInterfaceWithSLabel(String key) {
        return mapSLabelIf.getOrDefault(key, null);
    }

    // EFFECTS: return the list of interfaces that has key as the target label of one of its statement
    public ArrayList<InterfaceModel> queryInterfaceWithTLabel(String key) {
        return mapTLabelIf.getOrDefault(key, null);
    }
}
