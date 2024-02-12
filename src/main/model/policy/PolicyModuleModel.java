package model.policy;

public class PolicyModuleModel {
    // A policy module contain (AND MUST CONTAIN):
    // A Type enforce file
    // A interface definition file
    // A file context file ( not used by Psychosis )

    // This also determine the file name!
    private String name;

    public PolicyModuleModel(String name) {
        this.name = name;
    }

    // EFFECT: Override Object.toString, for tui command show_module
    public String toString() {
        return ""; //stub
    }
}
