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

    private TypeEnfModel typeEnfObject = new TypeEnfModel();
    private FileContextModel fileContentObject =
            new FileContextModel();
    private InterfaceSetModel interfaceObject =
            new InterfaceSetModel();

    public PolicyModuleModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // EFFECT: Override Object.toString, for tui command show_module
    public String toString() {
        String res = "";
        res = res.concat("Module name: " + this.getName() + "\n");
        res = res.concat("Rule statements: " + typeEnfObject.lineCount() + "\n");
        res = res.concat("Declared interfaces: " + interfaceObject.lineCount() + "\n");

        res = res.concat("File context: not implemented. \n");
        return res; //TODO
    }

    public void addInterface(InterfaceModel i) {
        interfaceObject.addInterface(i);
    }

    public InterfaceModel findInterface(String name) {
        return null; //stub
    }

    public void removeInterface(String interfaceName) {
        interfaceObject.removeinterface(interfaceName);
    }
}
