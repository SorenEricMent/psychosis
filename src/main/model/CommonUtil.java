package model;

public class CommonUtil {
    public static int commentLocate(String line) {
        int commentPosition = -1;
        for (int i = 0; i < line.length(); i++) {
            commentPosition += 1;
            if (line.charAt(i) == '#') {
                // Found the first comment character
                break;
            }
        }
        return commentPosition;
    }
}
