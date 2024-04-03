package model.policy;

// A policy module contain (AND MUST CONTAIN):
// A Type enforce file
// A interface definition file
// A file context file ( not used by Psychosis )

// This also determine the file name!

import model.Event;
import model.EventLog;

public class PolicyModuleModel {
    private final String name;

    private TypeEnfModel typeEnfObject;
    private FileContextModel fileContentObject =
            new FileContextModel();
    private InterfaceSetModel interfaceObject =
            new InterfaceSetModel();

    // EFFECTS: init a completely empty module with a empty te.
    public PolicyModuleModel(String name) {
        this.name = name;
        typeEnfObject = new TypeEnfModel(this.name);
    }

    // EFFECTS: init this PolicyModule with predefined te, if and fc
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
    @Override
    public String toString() {
        String res = "";
        res = res.concat("Module name: " + this.getName() + "\n");
        res = res.concat("Rule statements: " + typeEnfObject.lineCount() + "\n");
        res = res.concat("Declared interfaces: " + interfaceObject.lineCount() + "\n");

        res = res.concat("File context: not implemented. \n");
        return res;
    }

    // EFFECTS: add an interface to the interface set
    // MODIFIES: this
    public void addInterface(InterfaceModel i) {
        EventLog.getInstance().logEvent(new Event(
                "Adding interface " + i.getName() + " to module " + name));
        interfaceObject.addInterface(i);
    }

    // EFFECTS: lookup the interface with name (proxy call to interface set's getInterface)
    public InterfaceModel getInterface(String name) {
        return interfaceObject.getInterface(name);
    }

    // REQUIRES: postcondition: call project's rebuildGlobalInterfaceSet
    public void setInterfaceObject(InterfaceSetModel interfaceObject) {
        EventLog.getInstance().logEvent(new Event(
                "Replacing the interface object for  " + name
                        + " to " + interfaceObject.hashCode() + " from " + this.interfaceObject.hashCode()));
        this.interfaceObject = interfaceObject;
    }

    public InterfaceSetModel getInterfaceSet() {
        return this.interfaceObject;
    }

    public TypeEnfModel getTypeEnf() {
        return this.typeEnfObject;
    }

    public void setTypeEnfObject(TypeEnfModel typeEnfObject) {
        EventLog.getInstance().logEvent(new Event(
                "Replacing the TypeEnf object for  " + name
                        + " to " + typeEnfObject.hashCode() + " from " + this.typeEnfObject.hashCode()));
        this.typeEnfObject = typeEnfObject;
    }

    // EFFECTS: remove the interface with name (proxy call to interface set's removeInterface)
    // MODIFIES: this
    public void removeInterface(String interfaceName) {
        EventLog.getInstance().logEvent(new Event(
                "Removing interface " + interfaceName + " from module " + name));
        interfaceObject.removeInterface(interfaceName);
    }
}
