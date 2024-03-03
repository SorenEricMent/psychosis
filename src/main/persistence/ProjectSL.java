package persistence;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;
import model.exception.SyntaxParseException;
import model.policy.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import java.util.HashSet;

// FUTURE TODO: Use AoP to call gc after load
// ik call gc is normally bad practice but makes sense here as a huge object is just loaded
// and user could def wait for some more miliseconds for a load

// Providing methods of loading/saving a project to/from a .pcsp file
public abstract class ProjectSL {

    // Technically project should implement Encodeable and Decodeable, but toString is already used for overview

    // REQUIRES: content is a valid JSON and valid xpcsp file
    // EFFECTS: Load a project from a compiled JSON format
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonCompiled(String content) throws JSONException {
        JSONObject parsed = new JSONObject(content);
        System.out.println("Loaded xpcsp JSON content.");
        Pair<ProjectModel, TrackerModel> res = new Pair<>(new TempProjectModel(parsed.getString("name"), true),
                new TrackerModel());
        parsed.getJSONArray("layers").forEach(i -> {
            JSONObject layerObj = (JSONObject) i;
            res.getFirst().addLayer(layerObj.getString("name"));
            layerObj.getJSONArray("modules").forEach(j -> {
                JSONObject moduleObj = (JSONObject) j;
                String moduleName = moduleObj.getString("name");
                PolicyModuleModel p = new PolicyModuleModel(moduleName,
                        parseTypeEnfFromJsonCompiled(moduleName, moduleObj.getJSONObject("te")),
                        parseIfSetFromJsonCompiled(moduleObj.getJSONObject("if")),
                        parseFileContextFromJsonCompiled(moduleObj.getJSONObject("fc")));
                res.getFirst().getLayer(layerObj.getString("name")).addPolicyModule(p);
            });
        });
        System.out.println("Loaded project structure and content.");
        // FUTURE TODO: regenerate Tracker
        System.out.println("Regenerated Tracker database.");
        System.gc();
        return res;
    }


    // EFFECTS: create a TypeEnfModel from a compiled JSON
    private static TypeEnfModel parseTypeEnfFromJsonCompiled(String name, JSONObject obj) throws JSONException {
        TypeEnfModel res = new TypeEnfModel(name);
        obj.getJSONArray("content").forEach(c -> {
            JSONObject line = (JSONObject) c;
            if (line.getString("type").equals("call")) {
                res.addInterfaceCall(line.getString("name"),
                        line.getJSONArray("params").toList().toArray(String[]::new));
            } else if (line.getString("type").equals("statement")) {
                HashSet<String> actions = new HashSet<>();
                line.getJSONArray("actions").forEach(a -> {
                    actions.add((String) a);
                });
                res.addStatement(
                        new RuleSetModel(
                            RuleSetModel.toRuleType(line.getString("rule")),
                            line.getString("source"),
                            line.getString("target"),
                            line.getString("target_class"),
                            actions));
            } else {
                return; // jump to next iteration, ignore unknown type
            }
        });
        return res; //stub
    }

    // EFFECTS: create a InterfaceSetModel from a compiled JSON
    private static InterfaceSetModel parseIfSetFromJsonCompiled(JSONObject obj) throws JSONException {
        InterfaceSetModel res = new InterfaceSetModel();
        obj.getJSONArray("content").forEach(c -> {
            JSONObject line = (JSONObject) c;
            if (line.getString("type").equals("interface") || line.getString("type").equals("template")) {
                // Currently Psychosis does not treat interface and template differently internally

            } else {
                return;// jump to next iteration, ignore unknown type
            }
        });
        return null; //stub
    }

    // EFFECTS: placeholder for creating a FileContextModel from a compiled JSON
    private static FileContextModel parseFileContextFromJsonCompiled(JSONObject obj) throws JSONException {
        return null; // No functionality about .fc
    }


    // REQUIRES: content is a valid JSON and valid pcsp file
    // EFFECTS: Load a project from a meta JSON format
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonMeta(String content) {

        return null;
    }

    // EFFECTS: convert a ProjectModel to Psychosis .pcsj file (JSON storing project metadata)
    public String saveWholeProjectToJson(ProjectModel proj) {
        return null;
    }

    // EFFECTS: convert a ProjectModel to Psychosis .xpcsj file (JSON containing all project information)
    public String saveWholeProjectToJsonCompiled(ProjectModel proj) {
        return null;
    }
}
