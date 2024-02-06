package model;

import model.policy.InterfaceModel;

import java.util.HashMap;

public class TrackerModel {
    // This serves as a tracker for Psychosis to lookup
    // things speedy

    private HashMap<String, InterfaceModel> mapSLabelIf;
    private HashMap<String, InterfaceModel> mapTLabelIf;

    public TrackerModel() {
        mapSLabelIf = new HashMap<String, InterfaceModel>();
        mapTLabelIf = new HashMap<String, InterfaceModel>();
    }
}
