package persistence;

import model.Decodeable;
import model.Encodeable;
import model.ProjectModel;
import model.TempProjectModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// A Workspace is an abstract to the intermediate form of persistent saving/loading of the entire state
public class Workspace implements Encodeable, Decodeable {
    private ArrayList<ProjectModel> projects;
    private String name;
    private int index;

    // EFFECTS: init a workspace with name and index and a list of projects
    public Workspace(String name, int index, ArrayList<ProjectModel> projects) {
        this.name = name;
        this.index = index;
        this.projects = projects;
    }

    // EFFECTS: init a workspace from json data
    public Workspace(String jsonData) {
        parser(jsonData);
    }

    public ArrayList<ProjectModel> getProjects() {
        return projects;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getProjectNum() {
        return projects.size();
    }

    // EFFECTS: Encode the workspace to a .pcsw file
    // By default, meta when possible
    public String toString() {
        return null; //stub
    }

    // EFFECTS: Encode the workspace to a .pcsw file, use meta when possible,
    // but compile for the index in compiledIndex
    public String toStringAuto(int[] compiledIndex) {
        // Convert compiledIndex to a Set for easier lookup
        Set<Integer> compiledIndexSet = Arrays.stream(compiledIndex).collect(HashSet::new, Set::add, Set::addAll);
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("index", index);
        for (int i = 0; i < this.getProjects().size(); i++) {
            ProjectModel proj = this.getProjects().get(i);
            if (proj.getClass().equals(TempProjectModel.class)) {
                // Use reflection to judge if the project cannot be meta

            } else if (proj.getClass().equals(ProjectModel.class)) {
                // Class must be ProjectModel
                if (compiledIndexSet.contains(i)) {
                    // use compiled
                } else {

                }
            } else {
                throw new RuntimeException("Reflection received a unknown class when encoding project.");
            }
        }
        return res.toString();
    }

    // EFFECTS: Encode the workspace to a .pcsw file, fully compiled
    public String toStringCompiled() {
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("index", index);
        JSONArray projects = new JSONArray();
        for (ProjectModel p : this.projects) {
            projects.put(ProjectSL.saveProjectToJsonCompiled(p));
        }
        res.put("projects", projects);
        return res.toString();
    }

    // EFFECTS: Load workspace from a .pcsw file
    public void parser(String content) {
        JSONObject parsed = new JSONObject(content);
        this.name = parsed.getString("name");
        this.index = parsed.getInt("index");
        ArrayList<ProjectModel> projects = new ArrayList<>();
        for (Object p : parsed.getJSONArray("projects")) {
            JSONObject project = (JSONObject) p;
            if (project.get("type").equals("compiled")) {
                // Tracker is currently discarded, TODO: after incorporation of tracker inside project, use it here
                projects.add(ProjectSL.loadProjectFromJsonCompiled(project.getJSONObject("data")).getFirst());
            } else if (project.get("type").equals("meta")) {
                projects.add(ProjectSL.loadProjectFromJsonMeta(project.getJSONObject("data")).getFirst());
            } else {
                continue; // Skip unknown type
            }
        }
        this.projects = projects;
    }
}
