package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CustomReader {
    // REQUIRES: target is a File object that exists in the filesystem.
    // EFFECTS: Read the target file as it is, including line breaks;
    public static String readAsWhole(File target) throws IOException {
        Scanner scan = new Scanner(target);
        scan.useDelimiter("\\Z");
        String content;
        try {
            content = scan.next();
        } catch (NoSuchElementException e) {
            // Target file is empty
            content = "";
        }
        scan.close();
        return content;
    }

    // REQUIRES: target is a File object that exists in the filesystem.
    // EFFECTS: Same as readAsWhole, but remove all comments and
    // extra spaces at head/end.
    // comments start with # and end with line breaks.
    // comments will be moved to the second element of the returned tuple with {read index, content}
    public static Pair<String, ArrayList<Pair<Integer, String>>> readAsWholeCode(File target) throws IOException {
        Scanner scan = new Scanner(target);
        String content = "";
        ArrayList<Pair<Integer, String>> comments = new ArrayList<>();
        int i = 0;
        while (scan.hasNextLine()) {
            String temp = scan.nextLine();
            int commentLocation = CommonUtil.commentLocate(temp);
            if (commentLocation != -1) {
                temp = temp.substring(0, commentLocation);
                comments.add(new Pair<Integer, String>(i, temp.substring(commentLocation, temp.length())));
            }
            temp = temp.strip();
            content = content.concat(temp + "\n");
            i++;
        }
        if (content.endsWith("\n")) {
            content = content.substring(0, content.length() - 1);
        }
        scan.close();
        return new Pair<String, ArrayList<Pair<Integer, String>>>(content, comments);
    }

    // DEPRECATED CODE

    // REQUIRES: target is a File object that exists in the filesystem.
    // EFFECTS: Read the target file without preserving line breaks
//    public static String readAsCompact(File target) throws IOException {
//        BufferedReader bfr = new BufferedReader(new FileReader(target));
//        String content = "";
//        String temp;
//        while ((temp = bfr.readLine()) != null) {
//            content = content.concat(temp);
//        }
//        return content;
//    }
}
