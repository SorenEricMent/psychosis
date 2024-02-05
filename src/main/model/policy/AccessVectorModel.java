package model.policy;

import model.CommonUtil;
import model.Pair;
import model.exception.SyntaxParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

public class AccessVectorModel {

    private HashMap<String, HashSet<String>> accessVector;

    public AccessVectorModel() {
        accessVector = new HashMap<String, HashSet<String>>();
    }

    public HashMap<String, HashSet<String>> getValue() {
        return accessVector;
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

    // REQUIRE: content must be derived of comment - read from readAsWholeCode.
    // Effect: parse SELinux access_vectors to a HashMap
    public static HashMap<String, HashSet<String>> accessVectorParser(String content)  throws SyntaxParseException {
        HashMap<String, HashSet<String>> results = new HashMap<String, HashSet<String>>();
        // Process common(parents) first
        Pair<String[], HashMap<String, HashSet<String>>> commonParsed = accessVectorParserCommon(content);
        // Alright I know I should do this in the first place but I don't want to
        // rewrite accessVectorParser for now;

        // Split with newline and any number of whitespace to tokenize code
        // This should be done with a DFA with memory but LINE LIMIT
        return commonParsed.getSecond();
    }

    private static Pair<String[], HashMap<String, HashSet<String>>> accessVectorParserCommon(String content)
            throws SyntaxParseException {
        HashMap<String, HashSet<String>> commons = new HashMap<String, HashSet<String>>();
        String[] tokenized = CommonUtil.basicTokenizer(content);
        boolean readingActions = false;
        String currentCommonName = "";
        for (int i = 0; i < tokenized.length; i++) {
            if (readingActions) {
                if (tokenized[i].equals("}")) {
                    readingActions = false;
                } else {
                    commons.get(currentCommonName).add(tokenized[i]);
                }
            } else {
                if (tokenized[i].equals("common")) {
                    // next element is name and next-next should be {
                    commons.putIfAbsent((currentCommonName = tokenized[i + 1]), new HashSet<String>());
                    if (tokenized[i + 2].equals("{")) {
                        readingActions = true;
                        i += 2;
                    } else {
                        throw new SyntaxParseException("No following { after common name");
                    }
                }
            }
        }
        return new Pair<String[], HashMap<String, HashSet<String>>>(tokenized, commons);
    }
}