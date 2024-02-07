package model;

import java.util.Arrays;
import java.util.regex.*;

public class CommonUtil {
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
}