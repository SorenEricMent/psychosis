package model;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    // EFFECTS: Read the target file without preserving line breaks;
    public static String readAsCompact(File target) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(target));
        String content = "";
        String temp;
        while ((temp = bfr.readLine()) != null) {
            content = content.concat(temp);
        }
        return content;
    }
}
