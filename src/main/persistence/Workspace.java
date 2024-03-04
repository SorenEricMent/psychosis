package persistence;

import model.Decodeable;
import model.Encodeable;
import model.ProjectModel;
import org.json.JSONObject;

import java.util.ArrayList;

// A Workspace is an abstract to the intermediate form of persistent saving/loading of the entire state
public class Workspace implements Encodeable, Decodeable {
    private ArrayList<ProjectModel> projects;
    private final String name;
    private final int index;

    // EFFECTS: init a workspace with name and index and an empty list of projects
    public Workspace(String name, int index) {
        this.name = name;
        this.index = index;
        this.projects = new ArrayList<>();
    }

    public void setProjects(ArrayList<ProjectModel> projects) {
        this.projects = projects;
    }

    public ArrayList<ProjectModel> getProjects() {
        return projects;
    }

    public String getName() {
        return name;
    }

    public int getProjectNum() {
        return projects.size();
    }

    // EFFECTS: Encode the workspace to a .pcsw file
    public String toString() {
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("index", index);
        return res.toString();
    }

    // EFFECTS: Load workspace from a .pcsw file
    public void parser(String content) {
        JSONObject parsed = new JSONObject(content);

    }
}
