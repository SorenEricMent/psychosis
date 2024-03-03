package model.policy;

import model.CommonUtil;
import model.exception.SyntaxParseException;

import java.util.HashMap;
import java.util.HashSet;

// The model of refpolicy's type system (access_vectors and security_classes)
// not currently enforced
// TODO: enforce security_classes matches with access_vectors and lookup method for future enf system
public class AccessVectorModel {

    private final HashMap<String, HashSet<String>> accessVector;

    // EFFECTS: init AccessVectorModel with a empty set of accessVector
    public AccessVectorModel() {
        accessVector = new HashMap<String, HashSet<String>>();
    }

    public HashMap<String, HashSet<String>> getValue() {
        return accessVector;
    }

    // EFFECTS: update access vectors with new class
    // MODIFIES: this
    public void addSecurityClass(String className) {
        if (className != null) {
            this.accessVector.putIfAbsent(className,
                    new HashSet<String>());
        }
    }

    // EFFECTS: getter for class number
    public int size() {
        return accessVector.size();
    }

    public HashMap<String, HashSet<String>> getAccessVector() {
        return accessVector;
    }

    // EFFECTS: update specific access vector class with action
    // MODIFIES: this
    public void addAccessVector(String className, String actionName) {
        this.accessVector.get(className).add(actionName);
    }

    // EFFECTS: add all actions from "from" to access vectors with key as the key
    public void batchAddAction(HashMap<String, HashSet<String>> from) {
        for (String s : from.keySet()) {
            this.accessVector.putIfAbsent(s, new HashSet<String>());
            this.accessVector.get(s).addAll(from.get(s));
        }
    }

    // REQUIRES: this parser does not specially require readAsWholeCode like others as it is implemented differently
    // EFFECTS: parse a line of security class definition to the class's name
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

    // REQUIRE: content must be derived of comment - e.g. read from readAsWholeCode.
    // EFFECTS: parse SELinux access_vectors to a HashMap
    @SuppressWarnings("methodlength")
    // Explained at https://canvas.ubc.ca/courses/130128/external_tools/48698
    // TLDR: it doesn't make sense to split this parser
    public static HashMap<String, HashSet<String>> accessVectorParser(String content) throws SyntaxParseException {
        HashMap<String, HashSet<String>> results = new HashMap<String, HashSet<String>>();
        HashMap<String, HashSet<String>> commons = new HashMap<String, HashSet<String>>();
        String[] tokenized = CommonUtil.basicTokenizer(content);
        boolean readingActions = false;
        boolean commonOrClass = false; // True for class, false for common
        String currentCommonName = "";
        String currentClassName = "";
        for (int i = 0; i < tokenized.length; i++) {
            if (readingActions) {
                if (tokenized[i].equals("}")) {
                    readingActions = false;
                } else {
                    if (!CommonUtil.tokenValidate(tokenized[i])) {
                        throw new SyntaxParseException("Invalid action token: " + tokenized[i]);
                    }
                    if (commonOrClass) {
                        results.get(currentClassName).add(tokenized[i]);
                    } else {
                        commons.get(currentCommonName).add(tokenized[i]);
                    }
                }
            } else {
                if (tokenized[i].equals("{")) {
                    readingActions = true;
                } else if (tokenized[i].equals("common")) {
                    // next element is name and next-next should be {
                    commons.putIfAbsent((currentCommonName = tokenized[i + 1]), new HashSet<String>());
                    if (!CommonUtil.tokenValidate(currentCommonName)) {
                        throw new SyntaxParseException("Invalid common name token: " + currentCommonName);
                    }
                    commonOrClass = false;
                    i = i + 1;
                } else if (tokenized[i].equals("class")) {
                    results.putIfAbsent((currentClassName = tokenized[i + 1]), new HashSet<String>());
                    if (!CommonUtil.tokenValidate(currentClassName)) {
                        throw new SyntaxParseException("Invalid class name token: " + currentClassName);
                    }
                    if (tokenized[i + 2].equals("inherits")) {
                        results.putIfAbsent(currentClassName, new HashSet<String>());
                        results.get(currentClassName).addAll(commons.get(tokenized[i + 3]));
                        i = i + 3;
                    } else {
                        i = i + 1;
                    }
                    commonOrClass = true;
                } else {
                    throw new SyntaxParseException("Unknown token.");
                }
            }
        }
        return results;
    }
}