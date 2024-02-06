package model.policy;

import java.util.ArrayList;

public class ProjectModel {
    private String name;

    private String projectPath;

    protected ArrayList<LayerModel> layers = new ArrayList<LayerModel>();

    public ProjectModel(String name, String projectPath) {
        this.name = name;
        this.projectPath = projectPath;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<LayerModel> getLayers() {
        return this.layers;
    }

    // EFFECT: explain the project detail, override default toString
    public String toString() {
        String result = "";
        result = result.concat("Project name: " + getName() + "\nLayers: ");
        for (LayerModel layer : layers) {
            result = result.concat(layer.getName() + "\n");
        }
        return result;
    }
}
