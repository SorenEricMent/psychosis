package model.policy;

import model.FileObjectModel;

public class FileContextModel extends FileObjectModel {
    // Psychosis does not have functionality correlated to file context (.fc) files
    // This is a placeholder for file context files.
    public String readRaw() {
        return null;
    }

    public int lineCount() {
        return 0;
    }
}
