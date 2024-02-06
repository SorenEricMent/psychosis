package model;

import model.ProjectModel;
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
}
