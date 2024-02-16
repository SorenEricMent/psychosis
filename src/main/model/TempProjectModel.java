package model;

import model.exception.NotFoundException;
import model.policy.InterfaceModel;
import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

import java.util.ArrayList;

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

    // EFFECT: getter for path, nullified for temp project
    public String getProjectPath() {
        return null;
    }

    public String toString() {
        return super.toString().concat("\nYOU ARE WORKING ON A TEST PROJECT, YOUR PROGRESS WILL NOT BE SAVED.");
    }

    @Override
    public void updateRule() {

    }

    @Override
    public void removeRule() {

    }

    @Override
    public void addInterface(String layerName, String moduleName, String interfaceName, int paramNum)
            throws NotFoundException {
        try {
            this.getLayer(layerName)
                    .getPolicyModule(moduleName)
                    .addInterface(
                            new InterfaceModel(interfaceName, true));

        } catch (NullPointerException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void updateInterface() {

    }

    @Override
    public void removeInterface(String layerName, String moduleName, String interfaceName) {
        this.getLayer(layerName).getPolicyModule(moduleName).removeInterface(interfaceName);
    }

    @Override
    public void removeModule(String layerName, String moduleName) {
        this.getLayer(layerName).removePolicyModule(moduleName);
    }
}
