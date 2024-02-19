package model.policy;

import java.lang.reflect.Type;

public class PolicyModuleModel {
    // A policy module contain (AND MUST CONTAIN):
    // A Type enforce file
    // A interface definition file
    // A file context file ( not used by Psychosis )

    // This also determine the file name!
    private String name;

    private TypeEnfModel typeEnfObject;
    private FileContextModel fileContentObject =
            new FileContextModel();
    private InterfaceSetModel interfaceObject =
            new InterfaceSetModel();

    public PolicyModuleModel(String name) {
        this.name = name;
        typeEnfObject = new TypeEnfModel(this.name);
    }

    public PolicyModuleModel(String name, TypeEnfModel t, InterfaceSetModel i, FileContextModel f) {
        this.name = name;
        this.typeEnfObject = t;
        this.interfaceObject = i;
        this.fileContentObject = f;
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
        return res;
    }

    public void addInterface(InterfaceModel i) {
        interfaceObject.addInterface(i);
    }

    public InterfaceModel getInterface(String name) {
        return interfaceObject.getInterface(name);
    }

    public InterfaceSetModel getInterfaceSet() {
        return this.interfaceObject;
    }

    public TypeEnfModel getTypeEnf() {
        return this.typeEnfObject;
    }

    public void removeInterface(String interfaceName) {
        interfaceObject.removeInterface(interfaceName);
    }
}
