package model.policy;

import java.util.HashMap;
import java.util.HashSet;

// A tracker for SELinux type attributes
// This is fundamentally similar to TrackerModel, except it tag SELinux types.
// Again this should be created with a relational db but that's not in
// my plan for this term's project

// Not currently used, in future possibly ship with ProjectModel
public class AttributeModel {
    private final HashMap<String, HashSet<String>> attributes;

    // EFFECTS: init self with an empty set of attributes
    public AttributeModel() {
        attributes = new HashMap<String, HashSet<String>>();
    }

    // EFFECTS: return the set of types that is associated with attribute name, PLACEHOLDER
    public HashSet<String> queryAttributeWithType(String name) {
        return null;
    }

    // EFFECTS: return the set of attributes that is associated with type name, PLACEHOLDER
    public HashSet<String> queryTypeWithAttributes(String name) {
        return null;
    }
}
