package model;

import model.exception.NotFoundException;
import model.policy.LayerModel;

public class TempProjectModel extends ProjectModel {

    // A temporary project is a project that is not on disk and is for debugging only
    // It ships with only one layers

    public TempProjectModel(String name) {
        super(name, null);
        layers.add(new LayerModel("test"));
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
                    .getPolicyModule(moduleName);// TODO

        } catch (NullPointerException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void updateInterface() {

    }

    @Override
    public void removeInterface(String layerName, String moduleName) {

    }
}
