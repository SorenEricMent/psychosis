package persistence;

import model.Pair;
import model.ProjectModel;
import model.TrackerModel;

// Providing methods of loading/saving a project to/from a .pcsp file
public abstract class ProjectSL {

    // Technically project should implement Encodeable and Decodeable, but toString is already used for overview

    // REQUIRES: content is a valid JSON and valid xpcsp file
    // EFFECTS: Load a project from a compiled JSON format
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonCompiled(String content) {

        return null;
    }

    // REQUIRES: content is a valid JSON and valid pcsp file
    // EFFECTS: Load a project from a meta JSON format
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonMeta(String content) {

        return null;
    }

    public String saveWholeProjectToJson(ProjectModel proj) {
        return null;
    }
}
