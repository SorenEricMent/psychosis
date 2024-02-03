package model.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AccessVectorModel {

    private HashMap<String, HashSet<String>> accessVector;

    public AccessVectorModel() {
        accessVector = new HashMap<String, HashSet<String>>();
    }

    public void addSecurityClass(String className) {
        if (className != null) {
            this.accessVector.putIfAbsent(className,
                    new HashSet<String>());
        }
    }

    public void addAccessVector(String className, String actionName) {
        this.accessVector.get(className).add(actionName);
    }

    public HashMap<String, HashSet<String>> batchAddAction(HashMap<String, HashSet<String>> from) {
        //SecurityClassDoesNotExistException

        return null; //stub
    }

    public static String securityClassParser(String line) {
        // All lines that started with # are comments
        if (line.strip().charAt(0) == '#') {
            return null;
        } else {
            return line.strip().substring(1);
        }
    }

    public static HashMap<String, HashSet<String>> accessVectorParser(String content) {
        for (int i = 0; i < content.length(); i++) {
            String token1 = null;
            String token2 = null;
            Boolean readLeftBracket;

        }
        return null;
    }
}
