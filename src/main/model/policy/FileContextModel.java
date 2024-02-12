package model.policy;

public class FileContextModel {
    // Psychosis does not have functionality correlated to file context (.fc) files
    // This is a placeholder for file context files.
    private String content;

    public FileContextModel(String content) {
        this.content = content;
    }

    // EFFECT: getter for content;
    public String getContent() {
        return content;
    }
}
