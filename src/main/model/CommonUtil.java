package model;

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
}
