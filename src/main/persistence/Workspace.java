package persistence;

import model.ProjectModel;
import model.Encodeable;
import model.Decodeable;
import org.json.JSONObject;

import java.util.ArrayList;

// A Workspace is an abstract to the intermediate form of persistent saving/loading of the entire state
public class Workspace implements Encodeable, Decodeable {
    private ArrayList<ProjectModel> projects;
    private String name;

    public Workspace(String name) {
        this.name = name;
        this.projects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<ProjectModel> getProjects() {
        return projects;
    }

    public int getProjectNum() {
        return projects.size();
    }

    // EFFECTS: Encode the workspace to a .pcsw file
    public String toString() {
        return "";
    }

    // EFFECTS: Load workspace from a .pcsw file
    public void parser(String content) {
        JSONObject parsed = new JSONObject(content);
    }
}
