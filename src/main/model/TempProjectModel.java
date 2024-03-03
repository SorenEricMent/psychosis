package model;

import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.policy.InterfaceModel;
import model.policy.PolicyModuleModel;

public class TempProjectModel extends ProjectModel {

    // A temporary project is a project that is not on disk and is for debugging only
    // It ships with only one layers and one module added to that layer

    public TempProjectModel(String name) {
        super(name, null);
        layers.add(new DummyLayerModel("test"));
        this.getLayer("test").addPolicyModule(
                new PolicyModuleModel("test_module")
        );
    }

    public TempProjectModel(String name, Boolean ignoredEmptiness) {
        super(name, null);
        // Emptyness is used for polymorph a completely empty TempProject
    }


    // EFFECT: getter for path, nullified for temp project
    public String getProjectPath() {
        return null;
    }

    public String toString() {
        return super.toString().concat("\nYOU ARE WORKING ON A TEST PROJECT, YOUR PROGRESS WILL NOT BE SAVED.");
    }

    @Override
    public void addInterface(String layerName, String moduleName, String interfaceName)
            throws NotFoundException {
        try {
            InterfaceModel toAdd = new InterfaceModel(interfaceName, true);
            this.getLayer(layerName)
                    .getPolicyModule(moduleName)
                    .addInterface(toAdd);
            this.getGlobalInterfaceSet().addInterface(toAdd);
        } catch (NullPointerException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void removeInterface(String layerName, String moduleName, String interfaceName) {
        this.getLayer(layerName).getPolicyModule(moduleName).removeInterface(interfaceName);
    }

    @Override
    public void addModule(String layerName, String moduleName) throws DuplicateException {
        this.getLayer(layerName).addPolicyModule(
                new PolicyModuleModel(moduleName)
        );
        // TODO: incorporate newly defined interface to globalSet
    }

    @Override
    public void removeModule(String layerName, String moduleName) {
        this.getLayer(layerName).removePolicyModule(moduleName);
        // TODO: remove interface from globalSet
    }
}
