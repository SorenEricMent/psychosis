package persistence;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;
import model.policy.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.stream.Collectors;

// FUTURE TODO: Use AoP to call gc after load
// ik call gc is normally bad practice but makes sense here as a huge object is just loaded
// and user could def wait for some more miliseconds for a load

// Providing methods of loading/saving a project to/from a .pcsp file
public class ProjectSL {

    // Technically project should implement Encodeable and Decodeable, but toString is already used for overview

    // EFFECTS: proxy caller for loadProjectFromJsonCompiled with String to JSON conversion
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonCompiled(String content) throws JSONException {
        return loadProjectFromJsonCompiled(new JSONObject(content));
    }

    // REQUIRES: content is a JSON/ valid JSON String and valid xpcsp file
    // EFFECTS: Load a project from a compiled JSON format
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonCompiled(JSONObject parsed) {
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
        res.getFirst().rebuildGlobalInterfaceSet();
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
                // Ignore the IntelliJ warning of replacing String[]::new with Object[]::new, bug in type checking
            } else if (line.getString("type").equals("statement")) {
                HashSet<String> actions = new HashSet<>();
                line.getJSONArray("actions").forEach(a -> actions.add((String) a));
                res.addStatement(
                        new RuleSetModel(
                                RuleSetModel.toRuleType(line.getString("rule")),
                                line.getString("source"),
                                line.getString("target"),
                                line.getString("target_class"), actions));
            } else {
                // jump to next iteration, ignore unknown type
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
                InterfaceModel ifToAdd = new InterfaceModel(line.getString("name"), false);
                line.getJSONArray("statements").forEach(s -> {
                    JSONObject statement = (JSONObject) s;
                    ifToAdd.addRuleSetModels(new RuleSetModel(
                            RuleSetModel.toRuleType(statement.getString("rule")),
                            statement.getString("source"),
                            statement.getString("target"),
                            statement.getString("target_class"),
                            statement.getJSONArray("actions").toList().stream()
                                    .map(x -> (String) x).collect(Collectors.toCollection(HashSet::new))));
                });
                res.addInterface(ifToAdd);
            } else {
                // jump to next iteration, ignore unknown type
            }
        });
        return res; //stub
    }

    // EFFECTS: placeholder for creating a FileContextModel from a compiled JSON
    private static FileContextModel parseFileContextFromJsonCompiled(JSONObject obj) throws JSONException {
        return new FileContextModel(); // No functionality about .fc
    }

    // EFFECTS: proxy caller for loadProjectFromJsonMeta with String to JSON conversion
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonMeta(String content) {
        return null;
    }

    // REQUIRES: content is a valid JSON and valid pcsp file
    // EFFECTS: Load a project from a meta JSON format
    public static Pair<ProjectModel, TrackerModel> loadProjectFromJsonMeta(JSONObject obj) {
        return new Pair<ProjectModel, TrackerModel>(null, null); //stub
    }

    // EFFECTS: convert a ProjectModel to Psychosis .pcsj file (JSON storing project metadata)
    public static String saveProjectToJsonMeta(ProjectModel proj) {
        return null;
    }

    // EFFECTS: convert a ProjectModel to Psychosis .xpcsj file (JSON containing all project information)
    public static String saveProjectToJsonCompiled(ProjectModel proj) {
        JSONObject res = new JSONObject();
        res.put("name", proj.getName());
        res.put("layers", new JSONArray());
        for (LayerModel layer : proj.getLayers()) {
            JSONObject newLayer = new JSONObject().put("name", layer.getName());
            JSONArray modules = new JSONArray();
            for (String k : layer.getPolicyModules().keySet()) {
                PolicyModuleModel policyModule = layer.getPolicyModule(k);
                JSONObject newModule = new JSONObject().put("name", policyModule.getName());
                JSONArray teContent = saveTEToJsonArray(policyModule.getTypeEnf());
                JSONArray ifContent = saveIfSetToJsonArray(policyModule.getInterfaceSet());

                newModule.put("te", new JSONObject().put("content", teContent));
                newModule.put("if", new JSONObject().put("content", ifContent));
                newModule.put("fc", new JSONObject()); // placeholder

                modules.put(newModule);
            }
            newLayer.put("modules", modules);
            res.getJSONArray("layers").put(newLayer);
        }
        return res.toString();
    }

    // EFFECTS: serialize a TypeEnfModel to a list of JSON
    private static JSONArray saveTEToJsonArray(TypeEnfModel t) {
        JSONArray res = new JSONArray();
        // Process first-order statements
        for (RuleSetModel rule : t.getStatementsFO()) {
            res.put(new JSONObject()
                    .put("type", "statement")
                    .put("rule", rule.getRuleType().toString())
                    .put("source", rule.getSourceContext())
                    .put("target", rule.getTargetContext())
                    .put("target_class", rule.getTargetClass())
                    .put("actions", new JSONArray(rule.getActions())));
        }
        for (Pair<String, String[]> c : t.getInterfaceCall()) {
            res.put(new JSONObject()
                    .put("type", "call")
                    .put("name", c.getFirst())
                    .put("params", new JSONArray(c.getSecond())));
        }
        // Process interface calls
        return res;
    }

    // EFFECTS: serialize a InterfaceSetModel to a list of JSON
    private static JSONArray saveIfSetToJsonArray(InterfaceSetModel t) {
        JSONArray res = new JSONArray();
        // Does not differentiate template from interface for now
        for (InterfaceModel i : t.getInterfaces()) {
            JSONArray statements = new JSONArray();
            for (RuleSetModel rule : i.getRuleSetModels()) {
                statements.put(new JSONObject()
                        .put("rule", rule.getRuleType().toString())
                        .put("source", rule.getSourceContext())
                        .put("target", rule.getTargetContext())
                        .put("target_class", rule.getTargetClass())
                        .put("actions", new JSONArray(rule.getActions())));
            }
            res.put(new JSONObject()
                    .put("type", "interface")
                    .put("name", i.getName())
                    .put("statements", statements));
        }
        return res;
    }
}
