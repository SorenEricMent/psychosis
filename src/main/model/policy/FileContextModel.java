package model.policy;

import model.Decodeable;
import model.Encodeable;
import model.FileObjectModel;

// Psychosis does not have functionality correlated to file context (.fc) files
// This is a placeholder for file context files.
public class FileContextModel extends FileObjectModel implements Encodeable, Decodeable {
    public String toString() {
        return null;
    }

    public int lineCount() {
        return 0;
    }

    public static FileContextModel parser(String str) {
        return null;
    }
}
