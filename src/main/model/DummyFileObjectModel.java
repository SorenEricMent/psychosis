package model;

public class DummyFileObjectModel extends FileObjectModel {
    // The file object module for temp objects

    // Override all methods related to edit/read
    // This should be achieved with higher-kinded type but Java.

    public DummyFileObjectModel() {

    }

    public String toString() {
        return null;
    }

    public int lineCount() {
        return 0;
    }
}
