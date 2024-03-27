package model;

import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.policy.InterfaceModel;
import model.policy.PolicyModuleModel;

// A temporary project is a project that is not on disk and is for debugging only
// It ships with only one layers and one module added to that layer
public class TempProjectModel extends ProjectModel {

    // EFFECTS: init a temporary project with a layer named test and a module named test_module inside
    public TempProjectModel(String name) {
        super(name, null);
        layers.add(new DummyLayerModel("test"));
        this.getLayer("test").addPolicyModule(
                new PolicyModuleModel("test_module")
        );
    }

    // EFFECTS: init a completely empty temp project, used for loading.
    public TempProjectModel(String name, Boolean ignoredEmptiness) {
        super(name, null);
        // Emptyness is used for polymorph a completely empty TempProject
    }


    // EFFECT: getter for path, nullified for temp project
    public String getProjectPath() {
        return null;
    }

    // EFFECTS: call ProjectModel's toString with additional warning of this being a test project
    @Override
    public String toString() {
        return super.toString().concat("\nYOU ARE WORKING ON A TEST PROJECT, YOUR PROGRESS WILL NOT BE SAVED.");
    }

    // EFFECTS: add an empty interface, no fs flush
    // MODIFIES: this
    @Override
    public void addInterface(String layerName, String moduleName, String interfaceName)
            throws NotFoundException {
        try {
            InterfaceModel toAdd = new InterfaceModel(interfaceName, moduleName, true);
            this.getLayer(layerName)
                    .getPolicyModule(moduleName)
                    .addInterface(toAdd);
            this.getGlobalInterfaceSet().addInterface(toAdd);
        } catch (NullPointerException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    // EFFECTS: remove an interface with specific name, no fs flush
    // MODIFIES: this
    @Override
    public void removeInterface(String layerName, String moduleName, String interfaceName) {
        this.getLayer(layerName).getPolicyModule(moduleName).removeInterface(interfaceName);
    }

    // EFFECTS: add an empty module to a specific layer, no fs flush
    // MODIFIES: this
    @Override
    public void addModule(String layerName, String moduleName) throws DuplicateException {
        this.getLayer(layerName).addPolicyModule(
                new PolicyModuleModel(moduleName)
        );
        // TODO: incorporate newly defined interface to globalSet
    }

    // EFFECTS: remove a module with specific name from a specific layer, no fs flush
    // MODIFIES: this
    @Override
    public void removeModule(String layerName, String moduleName) {
        this.getLayer(layerName).removePolicyModule(moduleName);
        // TODO: remove interface from globalSet
    }
}
