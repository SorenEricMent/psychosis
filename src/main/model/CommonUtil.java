package model;

import java.util.Arrays;
import java.util.Stack;

// Helper functions that is used (or expected to be used) commonly across Psychosis
public class CommonUtil {
    // EFFECT: return the first # position, if none, return -1
    public static final int commentLocate(String line) {
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
    public static final String reduceSpace(String text) {
        return text.replaceAll("\\s{2,}", " ").trim();
    }

    // EFFECTS: tokenize the text with any number of newline or space
    public static final String[] basicTokenizer(String text) {
        String[] res = text.split("(\\r\\n|[\\r\\n])+|\\s+");
        if (res.length >= 2) { // Remove possible empty first element
            if (res[0].equals("")) {
                res = Arrays.copyOfRange(res, 1, res.length);
            }
        }
        return res;
    }

    // EFFECTS: tokenize the text with any number of newline or space or symbols
    public static final String[] strongTokenizer(String text) {
        // "((?=@)|(?<=@))" to preserve delimiter
        if (text.endsWith(";")) {
            text = text.replaceFirst(";$", " ;");
        }
        String[] res = text.split(
                "(((\\r\\n|[\\r\\n])+|\\s+)|((?=\\{)|(?<=\\{))"
                        + "|((?=\\})|(?<=\\}))|((?=\\()|(?<=\\())|((?=\\))|(?<=\\))))"
                        + "|((?=,)|(?<=,))|((?=;)|(?<=;))|((?=`)|(?<=`))|((?=')|(?<='))");
        return Arrays.stream(res).filter(t -> !t.equals("")).toArray(String[]::new);
    }

    // EFFECTS: return if the string is a valid selinux name (no reserved word, a-zA-Z0-9 and _)
    public static final boolean tokenValidate(String text) {
        return text.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    // EFFECTS: return if the string is a valid selinux variable name (no reserved word, a-zA-Z0-9 and _, can include $)
    public static final boolean tokenValidateWeak(String text) {
        return text.matches("([a-zA-Z_]|(\\$\\d))([a-zA-Z0-9_]|(\\$\\d))*");
    }

    // Those are statement keywords not planned to be supported
    // by Psychosis for now
    private static final String[] ignoredKeyword = {
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


    // EFFECTS: return if the str is in ignoredKeyword
    public static final boolean isIgnored(String str) {
        for (String s : ignoredKeyword) {
            if (str.equals(s)) {
                return true;
            }
        }
        return false;
    }

    // Checker for balancing with a stack
    public static class Balancer {
        private final Stack<String> stack = new Stack<String>();
        private boolean readingString = false;

        private boolean syntaxError = false;

        // ` match '
        // ( match )
        // { match }
        // ` override ( { check

        public boolean isSyntaxError() {
            return syntaxError;
        }

        public String toString() {
            return readingString + " " + syntaxError + " " + stack.toString();
        }

        // EFFECTS: return if the stack is empty;
        public boolean check() {
            return stack.isEmpty();
        }

        @SuppressWarnings({"checkstyle:methodLength", "checkstyle:SuppressWarnings"})

        // REQUIRES: should be a single letter
        // EFFECTS: push a value to stack
        public final void push(String val) {
            if (readingString) {
                if (val.equals("`")) {
                    stack.push("`");
                } else if (val.equals("'")) {
                    stack.pop();
                    if (stack.isEmpty() || !stack.peek().equals("`")) {
                        this.readingString = false;
                    }
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
                    case "(":
                    case "{":
                        stack.push(val);
                    default:
                        break;
                }
            }
        }

        // EFFECTS: helper for calling push with a char
        public final void push(char val) {
            this.push(Character.toString(val));
        }

        // EFFECTS: judge if a String is a char processed by balancer
        public static final boolean isOtherToken(String str) {
            return !str.equals("`") && !str.equals("'")
                    && !str.equals("{") && !str.equals("}")
                    && !str.equals("(") && !str.equals(")");
        }
    }
}