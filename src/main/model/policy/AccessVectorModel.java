package model.policy;

import model.CommonUtil;
import model.exception.SyntaxParseException;

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

    public static String securityClassParser(String line) throws SyntaxParseException {
        // Within a line, all content after # is considered a comment;
        // Note: could return null - no definition from the line
        line = line.strip();
        int commentPosition = CommonUtil.commentLocate(line);
        if (commentPosition != -1) {
            line = line.substring(0, commentPosition);
        }
        if (line.isEmpty()) {
            return null;
        }
        if (line.length() <= 7) {
            // All definitions start with class[space], so
            // any line that is shorter than 7 is invalid.
            throw new SyntaxParseException(line);
        }
        String[] tokenized = CommonUtil.reduceSpace(line).split(" ");
        if (tokenized.length == 2) {
            if (tokenized[0].equals("class")) {
                return tokenized[1];
            }
        }
        throw new SyntaxParseException(line);
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
