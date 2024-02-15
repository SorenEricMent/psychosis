package model;

import model.exception.SyntaxParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;

import java.util.Stack;

public class CommonUtil {

    // EFFECT: return the first # position, if none, return -1
    public static int commentLocate(String line) {
        int commentPosition = -1;
        for (int i = 0; i < line.length(); i++) {
            commentPosition += 1;
            if (line.charAt(i) == '#') {
                // Found the first comment character
                return commentPosition;
            }
        }
        return -1;
    }

    // EFFECTS: replace all continuous space in the string with a single space
    public static String reduceSpace(String text) {
        return text.replaceAll("\\s{2,}", " ").trim();
    }

    // EFFECTS: tokenize the text with any number of newline or space
    public static String[] basicTokenizer(String text) {
        String[] res = text.split("(\\r\\n|[\\r\\n])+|\\s+");
        if (res.length >= 2) { // Remove possible empty first element
            if (res[0].equals("")) {
                res = Arrays.copyOfRange(res, 1, res.length);
            }
        }
        return res;
    }

    // EFFECTS: return if the string is a valid selinux name (no reserved word, a-zA-Z0-9 and _)
    public static boolean tokenValidate(String text) {
        return text.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    // EFFECTS: return if the string is a valid selinux variable name (no reserved word, a-zA-Z0-9 and _, can include $)
    public static boolean tokenValidateWeak(String text) {
        return text.matches("([a-zA-Z_]|(\\$\\d))([a-zA-Z0-9_]|(\\$\\d))*");
    }

    // Those are statement keywords not planned to be supported
    // by Psychosis for now
    private static String[] ignoredKeyword = {
            "netifcon",
            "nodecon",
            "portcon",
            "allowxperm",
            "auditallowxperm",
            "ibendportcon",
            "neverallowxperm",
            "dontauditxperm",
            "ibpkeycon",
            "typebounds",
            "userbounds",
            "rolebounds",
            "attribute_role",
            "role_transition",
            "type_member",
            "type_transition",
            "range_transition",
            "bool"
    };

    public static String[] ignored() {
        return ignoredKeyword;
    }

    public static ArrayList<String> seTokenizer(String content) throws SyntaxParseException {
        // TODO
        ArrayList<String> results = new ArrayList<String>();
        String token = "";
        Balancer balancer = new Balancer();
        for (char chr : content.toCharArray()) {
            switch (chr) {
                case '`': case '\'': case '{': case '}': case '(': case ')':
                    balancer.push(chr);
                    if (!balancer.check()) {
                        throw new SyntaxParseException("Unmatching character.");
                    }
                default:
                    token = token.concat(Character.toString(chr));
            }
        }
        return results;
    }

    public static class Balancer {
        // Checker for balancing with a stack
        private Stack<String> stack = new Stack<String>();
        private boolean readingString = false;

        private boolean syntaxError = false;

        // ` match '
        // ( match )
        // { match }
        // ` override ( { check

        public boolean isSyntaxError() {
            return syntaxError;
        }

        public boolean check() {
            return stack.isEmpty();
        }

        @SuppressWarnings({"checkstyle:methodLength", "checkstyle:SuppressWarnings"})

        // EFFECTS: push a value to stack
        public void push(String val) {
            if (readingString) {
                if (val.equals("'")) {
                    stack.pop();
                    readingString = false;
                }
            } else {
                switch (val) {
                    case "'":
                        // readingString must be false
                        this.syntaxError = true;
                        break;
                    case ")":
                        if (this.check() || !stack.pop().equals("(")) {
                            this.syntaxError = true;
                        }
                        break;
                    case "}":
                        if (this.check() || !stack.pop().equals("{")) {
                            this.syntaxError = true;
                        }
                        break;
                    case "`":
                        this.readingString = true;
                    case "(": case "{":
                        stack.push(val);
                    default:
                        break;
                }
            }
        }

        public void push(char val) {
            this.push(Character.toString(val));
        }
    }
}