package model.policy;

import java.util.HashMap;
import java.util.HashSet;

public class AttributeModel {
    // This is fundamentally similar to TrackerModel, except it tag SELinux types.
    // Again this should be created with a relational db but that's not in
    // my plan for this term's project
    private HashMap<String, HashSet<String>> attributes;

    AttributeModel() {
        attributes = new HashMap<String, HashSet<String>>();
    }
}
