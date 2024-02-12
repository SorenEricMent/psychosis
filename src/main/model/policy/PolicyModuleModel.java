package model.policy;

import model.FileObjectModel;

import java.io.File;

public class PolicyModuleModel {
    // A policy module contain (AND MUST CONTAIN):
    // A Type enforce file
    // A interface definition file
    // A file context file ( not used by Psychosis )

    // This also determine the file name!
    private String name;

    private FileObjectModel<TypeEnfModel> typeEnfObject = new FileObjectModel<TypeEnfModel>();
    private FileObjectModel<FileContextModel> fileContentObject =
            new FileObjectModel<FileContextModel>();
    // Interface is a bit more complicated



    public PolicyModuleModel(String name) {
        this.name = name;
    }

    // EFFECT: Override Object.toString, for tui command show_module
    public String toString() {
        return ""; //stub
    }
}
