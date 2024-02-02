package model.policy;

import java.util.HashMap;
import java.util.HashSet;

public class AccessVectorModel {

    private HashMap<String, HashSet<String>> securityClasses = new HashMap<String, HashSet<String>>();

    AccessVectorModel(String accessVectorPath, String securityClassPath) {

    }

    HashSet<String> accessVectorParser() {
        return null;
    }

    HashSet<String> securityClassParser() {
        return null;
    }
}
