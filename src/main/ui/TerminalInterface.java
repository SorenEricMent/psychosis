package ui;

import model.*;
import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.exception.SyntaxParseException;
import model.policy.*;
import org.json.JSONException;
import persistence.ProjectSL;
import persistence.Workspace;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

// Define the commands for Psychosis shell
public class TerminalInterface {
    private ArrayList<
            Pair<ProjectModel, TrackerModel>> loadedProjects = new ArrayList<>();
    private Integer currentWorkIndex = 0;
    // Object 0 is an empty non-saving test only project

    private final Scanner scanner = new Scanner(System.in);

    private boolean isRunning = true;

    // EFFECTS: init a set of commands operating on the selected project
    // with only a temp empty project available in list.
    TerminalInterface() {
        this.loadedProjects.add(
                new Pair<>(new TempProjectModel("temp"),
                        new TrackerModel()));
    }

    // EFFECTS: Start a scanner to receive command input from user
    @SuppressWarnings("methodlength")
    public void startInterface() {
        System.out.println(" _______                         __                      _          \n"
                + "|_   __ \\                       [  |                    (_)         \n"
                + "  | |__) |.--.    _   __  .---.  | |--.   .--.   .--.   __   .--.   \n"
                + "  |  ___/( (`\\]  [ \\ [  ]/ /'`\\] | .-. |/ .'`\\ \\( (`\\] [  | ( (`\\]  \n"
                + " _| |_    `'.'.   \\ '/ / | \\__.  | | | || \\__. | `'.'.  | |  `'.'.  \n"
                + "|_____|  [\\__) )[\\_:  /  '.___.'[___]|__]'.__.' [\\__) )[___][\\__) ) \n"
                + "                 \\__.'                                              ");
        System.out.println("Psychosis Studio Shell version " + Main.getVersion());
        while (isRunning) {
            System.out.print("\u001B[34mPsychosis\u001B[0m@\u001B[36m" + getFocus().getName() + "\u001B[0m$ ");
            String[] inputList = scanner.nextLine().split(" ");

            try {
                switch (inputList[0]) {
                    case "list":
                        System.out.println("create_project select_project load_project list_project show_project"
                                + " show_layer create_layer remove_layer show_module add_module remove_module"
                                + " add_interface show_interface edit_interface remove_interface lookup_interface"
                                + " load_interface edit_typeenf"
                                + " edit_filecontext tag_add_interface tag_rm_interface load_access_vectors"
                                + " show_capability load_capability");
                        break;
                    case "help":
                        commandLoadHelp(inputList[1]);
                        break;
                    case "load_workspace":
                        commandLoadWorkspace(inputList);
                        break;
                    case "export_workspace":
                        commandExportWorkspace(inputList);
                        break;
                    case "create_project":
                        commandCreateProject(inputList);
                        break;
                    case "select_project":
                        commandSelectProject(inputList);
                        break;
                    case "load_project":
                        commandLoadProject(inputList);
                        break;
                    case "export_project":
                        commandExportProject(inputList);
                        break;
                    case "list_project":
                        commandListProject();
                        break;
                    case "show_project":
                        commandShowProject();
                        break;
                    case "show_layer":
                        commandShowLayer(inputList);
                        break;
                    case "create_layer":
                        commandCreateLayer(inputList);
                        break;
                    case "remove_layer":
                        commandRemoveLayer(inputList);
                        break;
                    case "load_module": // Have to load module in complete as .te might rely on .if
                        commandLoadModule(inputList);
                        break;
                    case "show_module":
                        commandShowModule(inputList);
                        break;
                    case "export_module":
                        commandExportModule(inputList);
                        break;
                    case "add_module":
                        commandAddModule(inputList);
                        break;
                    case "remove_module":
                        commandRemoveModule(inputList);
                        break;
                    case "add_interface":
                        commandAddInterface(inputList);
                        break;
                    case "lookup_interface":
                        commandLookUpInterface(inputList);
                        break;
                    case "show_interface":
                        commandShowInterface(inputList);
                        break;
                    case "edit_interface":
                        commandEditInterface(inputList);
                        break;
                    case "remove_interface":
                        commandRemoveInterface(inputList);
                        break;
                    case "edit_typeenf":
                        commandEditTypeEnf(inputList);
                        break;
                    case "edit_filecontext":
                        notImplemented();
                        break;
                    case "tag_add_interface":
                        notImplemented();
                        break;
                    case "tag_rm_interface":
                        notImplemented();
                        break;
                    case "load_access_vectors":
                        commandLoadAccessVectors(inputList);
                        break;
                    case "show_capability":
                        commandShowCapability(inputList);
                        break;
                    case "load_capability":
                        notImplemented();
                        break;
                    case "load_typeenf":
                        notImplemented();
                        break;
                    case "show_class":
                        commandShowClass(inputList);
                        break;
                    case "show_vectors":
                        commandShowVectors(inputList);
                        break;
                    case "exit":
                        System.out.println("Goodbye");
                        this.isRunning = false;
                        break;
                    default:
                        System.out.println("Unknown command.");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Not enough params for command execution, please check help <command>.");
            } catch (Exception e) {
                System.out.println("Unknown exception happened during the command's execution: " + e);
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    // EFFECTS: return the project that is currently working on
    private ProjectModel getFocus() {
        return loadedProjects.get(currentWorkIndex).getFirst();
    }

    // EFFECTS: lookup the help information for a command
    private void commandLoadHelp(String commandName) {
        switch (commandName) {
            case "export_project":
                System.out.println("export_project: <compiled/meta> <path>");
                System.out.println("Export a project to <path> in <compiled/meta> format.");
                System.out.println("For phase 2, temp project could only be exported as compiled.");
                break;
            case "default":
                System.out.println("Unknown command you are looking up");
        }
    }

    // EFFECTS: export the current project to a JSON
    private void commandExportProject(String[] params) {
        // export_project: <compiled/meta> <path>
        String content;
        if (params[1].equals("compiled")) {
            content = ProjectSL.saveProjectToJsonCompiled(this.getFocus());
        } else if (params[1].equals("meta")) {
            content = ProjectSL.saveProjectToJsonMeta(this.getFocus());
        } else {
            System.out.println("Unknown export format, valid options: compiled/meta");
            return;
        }
        File target = new File(params[2]);
        try {
            CustomReader.writeToFile(target, content);
        } catch (IOException e) {
            System.out.println("Error when saving to file at " + target.getAbsolutePath() + ", " + e);
        }
        System.out.println("Saved project.");
    }

    // EFFECTS: export all current projects into a workspace file (name and a list of projects / path)
    private void commandExportWorkspace(String[] params) {
        // export_workspace <compiled/meta> <workspace_name> <path> [compiled_list]

        // Compiled: export everything in compiled format
        // meta: export in meta format when possible, use [compiled_list] for compiled export

        // Currently only compiled is supported
        String content =
                params[1].equals("compiled")
                        ? ((new Workspace(params[2], this.currentWorkIndex, new ArrayList<>(this.loadedProjects.stream()
                        .map(Pair::getFirst).collect(Collectors.toList())))).toStringCompiled())
                        : ((new Workspace(params[2], this.currentWorkIndex, new ArrayList<>(this.loadedProjects.stream()
                        .map(Pair::getFirst).collect(Collectors.toList())))).toString());
        File target = new File(params[3]);
        try {
            CustomReader.writeToFile(target, content);
        } catch (IOException e) {
            System.out.println("Error when reading file at location "
                    + target.getAbsolutePath() + " : " + e);
        }
        System.gc();
    }

    // EFFECTS: overwrite the current workspace completely with a Workspace loaded from a JSON
    private void commandLoadWorkspace(String[] params) {
        // load_workspace <path>
        String fileContent = "";
        File target = new File(params[1]);
        try {
            fileContent = CustomReader.readAsWhole(target);
        } catch (IOException e) {
            System.out.println("Error when reading file: " + e);
        }
        try {
            Workspace temp = new Workspace(fileContent);
            this.loadedProjects =
                    temp.getProjects().stream()
                            .map(x -> new Pair<>(x, new TrackerModel()))
                            .collect(Collectors.toCollection(ArrayList::new));
            // TrackerModel is currently placeholding
            this.currentWorkIndex = temp.getIndex();
            System.out.println("Loaded workspace named " + temp.getName() + " at path " + target.getAbsolutePath());
            System.out.println("Totally " + temp.getProjectNum()
                    + "projects, currently at index " + this.currentWorkIndex);
        } catch (JSONException e) {
            System.out.println("Invalid JSON, " + e);
        }
    }

    // EFFECTS: load a project from a JSON and add it to the project list
    private void commandLoadProject(String[] params) {
        // load_project <compiled/meta> <path>
        try {
            String fileContent = CustomReader.readAsWhole(
                    new File(params[2]));
            Pair<ProjectModel, TrackerModel> projLoaded;
            if (params[1].equals("compiled")) {
                projLoaded = ProjectSL.loadProjectFromJsonCompiled(fileContent);
            } else if (params[1].equals("meta")) {
                projLoaded = ProjectSL.loadProjectFromJsonMeta(fileContent);
            } else {
                System.out.println("Unknown load format, valid values: compiled/meta.");
                return;
            }
            this.loadedProjects.add(projLoaded);
            System.out.println("New project loaded to workspace.");
        } catch (IOException e) {
            System.out.println("Error when reading file: " + e);
        }
    }

    // EFFECTS: create an empty / refpolicy-based / empty-in-mem-only project
    // and add it to the project list
    private void commandCreateProject(String[] params) {
        // create_project <basis (test/refpolicy/empty)> [custom]:<path> name
        // the latter is not yet implemented in Phase 1, TODO

        //TODO
        ProjectModel proj;
        if (params[1].equals("test")) {
            proj = new TempProjectModel(params[2]);
            System.out.println("Created new test project (not on filesystem!) " + params[2]);
            loadedProjects.add(
                    new Pair<>(proj, new TrackerModel()));
        } else if (params[1].equals("refpolicy")) {
            notImplemented();
            System.out.println("Psychosis dropped plan for refpolicy support ");
        } else if (params[1].equals("empty")) {
            proj = new ProjectModel(params[2], params[3]);
            System.out.println("Created new empty project " + params[2] + "at filesystem location " + params[3]);
            loadedProjects.add(
                    new Pair<>(proj, new TrackerModel()));
        } else {
            System.out.println("Unknown project-creation basis");
        }

    }

    // EFFECTS: update which project to be selected
    private void commandSelectProject(String[] params) {
        this.currentWorkIndex = Integer.parseInt(params[1]) - 1;
    }

    // EFFECTS: show an overview of a project
    private void commandShowProject() {
        System.out.println(getFocus().toString());
    }

    // EFFECTS: show an overview of a layer
    private void commandShowLayer(String[] params) {
        try {
            System.out.println(getFocus().getLayer(params[1]).toString());
        } catch (NotFoundException e) {
            System.out.println("Failed to show layer detail.");
            System.out.println(e);
        }
    }

    // EFFECTS: create a empty layer with no modules
    private void commandCreateLayer(String[] params) {
        // create_layer <layer_name>
        try {
            this.getFocus().addLayer(params[1]);
        } catch (DuplicateException e) {
            System.out.println("Error when adding layer to project " + this.getFocus().getName());
            System.out.println(e);
        }
    }

    // EFFECTS: remove a layer completely
    private void commandRemoveLayer(String[] params) {
        if (reassure()) {
            try {
                this.getFocus().removeLayer(params[1]);
            } catch (NotFoundException e) {
                System.out.println("Error when removing layer.");
                System.out.println(e);
            }
        }
    }

    // EFFECTS: list the loaded projects
    private void commandListProject() {
        System.out.print("Loaded projects: ");
        for (Pair<ProjectModel, TrackerModel> project : loadedProjects) {
            System.out.print(project.getFirst().getName() + " ");
        }
    }

    // EFFECTS: load a module from file to a layer
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void commandLoadModule(String[] params) {
        // load_module <to layer> <name> <te path> <if path> <fc path>
        try {
            if (params[3].endsWith("fc") || params[3].endsWith("if")) {
                System.out.println("Warning: you seem to have loaded a .fc/.if file as your .te");
            }
            if (params[4].endsWith("fc") || params[4].endsWith("te")) {
                System.out.println("Warning: you seem to have loaded a .fc/.te file as your .if");
            }
            if (params[5].endsWith("te") || params[3].endsWith("if")) {
                System.out.println("Warning: you seem to have loaded a .te/.if file as your .fc");
            }
            InterfaceSetModel i = InterfaceSetModel.parser(CustomReader.readAsWholeCode(new File(params[4]))
                    .getFirst());
            for (InterfaceModel x : i.getInterfaces()) {
                this.getFocus().getGlobalInterfaceSet().addInterface(x);
            }
            TypeEnfModel t = TypeEnfModel.parser(CustomReader.readAsWholeCode(new File(params[3])).getFirst());
            FileContextModel f = new FileContextModel();
            PolicyModuleModel m = new PolicyModuleModel(params[2], t, i, f);
            this.getFocus().getLayer(params[1]).addPolicyModule(m);
        } catch (SyntaxParseException e) {
            System.out.println("Error in syntax: " + e);
        } catch (NotFoundException e) {
            System.out.println("Failed to load module to the layer: " + e);
        } catch (IOException e) {
            System.out.println("Exception in reading file: " + e);
        }
    }

    // EFFECTS: add an empty module to a layer
    private void commandAddModule(String[] params) {
        // add_module <layer_name> <module_name>
        try {
            this.getFocus().getLayer(params[1]).addPolicyModule(
                    new PolicyModuleModel(params[2])
            );
        } catch (NotFoundException e) {
            System.out.println("Failed to add module to layer.");
            System.out.println(e);
        }
    }

    // EFFECTS: show an overview of a module
    private void commandShowModule(String[] params) {
        // show_module <layer_name> <module_name>

        System.out.println(this
                .getFocus()
                .getLayer(params[1])
                .getPolicyModule(params[2]));

    }

    // EFFECTS: export the result content of a module, might be .te/.if/.fc or compiled first-order .te
    private void commandExportModule(String[] params) {
        // export_module <text/file> <te/if/fc/compiled> [layer_name] [module_name]
        // Phase 1: only option text implemented
        if (params[1].equals("text")) {
            if (params[2].equals("te")) {
                System.out.println(this.getFocus().getLayer(params[3]).getPolicyModule(params[4]).getTypeEnf());
            } else if (params[2].equals("if")) {
                System.out.println(this.getFocus().getLayer(params[3]).getPolicyModule(params[4]).getInterfaceSet());
            } else if (params[2].equals("fc")) {
                notImplemented();
            } else if (params[2].equals("compiled")) {
                // WHERE DID MY PREV CALL GO I DEFINITELY DID IT
                System.out.println(this.getFocus().getLayer(params[3]).getPolicyModule(params[4]).getTypeEnf().toString(
                        this.getFocus().getGlobalInterfaceSet()
                ));
            } else {
                System.out.println("Unknown export component option.");
            }
        } else if (params[1].equals("file")) {
            notImplemented();
        } else {
            System.out.println("Unknown export option.");
        }
    }

    // EFFECTS: remove a policy module
    private void commandRemoveModule(String[] params) {
        // remove_module <layer_name> <module_name>

        this.getFocus().removeModule(
                params[1],
                params[2]
        );

    }

    // EFFECTS: add an empty interface to a module
    private void commandAddInterface(String[] params) {
        // add_interface <layer_name> <module_name> <interface_name>
        try {
            this.getFocus().addInterface(
                    params[1],
                    params[2],
                    params[3]
            );
        } catch (NotFoundException e) {
            System.out.println("The layer/module you are trying to access doesn't exist");
        }
    }

    // EFFECTS: add/remove statements on an interface
    private void commandEditInterface(String[] params) {
        // edit_interface <interface_name>
        // <add/remove> <allow/dontaudit> <source_label> <target_label> <target_class> [action_list]

        // VALIDATE WITH WEAK!
        HashSet<String> actions = new HashSet<String>();
        switch (params[2]) {
            case "add":
                Collections.addAll(actions, Arrays.copyOfRange(params, 7, params.length));
                this.getFocus().getGlobalInterfaceSet().getInterface(params[2]).addRuleSetModels(
                        new RuleSetModel(RuleSetModel.toRuleType(
                                params[3]), params[4], params[5], params[6],
                                actions)
                );
                break;
            case "remove":
                Collections.addAll(actions, Arrays.copyOfRange(params, 7, params.length));
//                this.getFocus().getGlobalInterfaceSet().getInterface(params[2]).removeRuleSetModels(
//                        new RuleSetModel(RuleSetModel.toRuleType(
//                                params[3]), params[4], params[5], params[6],
//                                actions)
//                );
//                break;
                // TODO
        }
    }

    // EFFECTS: show the list of interfaces matching the criteria given.
    private void commandLookUpInterface(String[] params) {
        // lookup_interface <unspec/userdefined> name=name sl=<name/none> tl=<name/none> tag=tag1,tag2

        // lookup in global interface object
        HashSet<InterfaceModel> search = new HashSet<>();
        Collections.addAll(search,
                this.getFocus().getGlobalInterfaceSet().getInterfaces().toArray(InterfaceModel[]::new));

        // getInterfaces has the guarantee of uniqueness
        // Truncate search range corresponding to source label / target label
        if (!params[2].equals("none")) {
            // intersection set with Tracker-sl
            // search.retainAll();
        }
        if (!params[3].equals("none")) {
            // intersection set with Tracker-tl
        }

//            for (InterfaceModel i : this.getFocus().getGlobalInterfaceSet().getInterfaces()) {
//                if ((params[2].equals("unspec") && !i.getIsUserDefined())
//                        || (params[2].equals("userdefined") && i.getIsUserDefined())) {
//                    // TODO: tags
//                    res.add(i.getName());
//                }
//            }

        // User-defined & tag filter
        search = (HashSet<InterfaceModel>) search.stream().filter(x ->
                (params[2].equals("userdefined") && x.getIsUserDefined())
                        || (params[2].equals("unspec") && x.getIsUserDefined())).collect(Collectors.toSet());

        System.out.println("Interfaces matches your search: " + String.join(" ",
                search.stream().map(InterfaceModel::getName).toArray(String[]::new)));

    }

    // EFFECTS: show the content of an interface
    private void commandShowInterface(String[] params) {
        // show_interface <layer_name> <module_name> <interface_name>
        System.out.println(this
                .getFocus()
                .getLayer(params[1])
                .getPolicyModule(params[2])
                .getInterface(params[3]));
    }

    // EFFECTS: remove a single interface
    private void commandRemoveInterface(String[] params) {
        // remove_interface <layer_name> <module_name> <interface_name>
        try {
            this.getFocus().removeInterface(
                    params[1],
                    params[2],
                    params[3]
            );
        } catch (NotFoundException e) {
            System.out.println("The layer/module you are trying to access doesn't exist");
        }
    }

    // EFFECTS: edit .te file of the current policy, add/remove statement or interface call
    private void commandEditTypeEnf(String[] params) {
        // edit_typeenf <layer_name> <module_name> <add/remove/add_inf/remove_inf> <RuleType>
        // <source context> <target_context> <target_class> [listof actions] (add_inf/remove_inf infname [listof args])
        HashSet<String> actions = new HashSet<>();
        if (params[3].equals("add")) {
            Collections.addAll(actions, Arrays.copyOfRange(params, 8, params.length));
            this.getFocus().getLayer(params[1]).getPolicyModule(params[2]).getTypeEnf().addStatement(
                    new RuleSetModel(RuleSetModel.toRuleType(
                            params[4]), params[5], params[6], params[7],
                            actions));
        } else if (params[3].equals("remove")) {
            // Output if exists such rules
            Collections.addAll(actions, Arrays.copyOfRange(params, 8, params.length));
            HashSet<String> res =
                    this.getFocus().getLayer(params[1]).getPolicyModule(params[2]).getTypeEnf().removeStatement(
                            new RuleSetModel(RuleSetModel.toRuleType(
                                    params[4]), params[5], params[6], params[7],
                                    actions));
            System.out.println("Success. The following actions in the original type enforcement are preserved: "
                    + String.join(" ", res));
        } else if (params[3].equals("add_inf")) {
            // Output if exists such rules
            this.getFocus().getLayer(params[1]).getPolicyModule(params[2]).getTypeEnf().addInterfaceCall(params[3],
                    Arrays.copyOfRange(params, 4, params.length));
        } else if (params[3].equals("remove_inf")) {
            // Output if exists such rules
            // this.getFocus().getLayer(params[1]).
            // getPolicyModule(params[2]).getTypeEnf().removeInterfaceCall(params[3]);
            //todo
        } else {
            System.out.println("Unknown actions");
        }

    }

    // EFFECTS: command: parse and load access vector and class definition from filesystem
    private void commandLoadAccessVectors(String[] inputList) throws SyntaxParseException, IOException {
        if (inputList.length <= 2) {
            System.out.println("Usage:");
            System.out.println("load_access_vectors <access_vectors path> <security_classes path>.");
            System.out.println("Use default to load built-in definition, e.g.");
            System.out.println("load_access_vectors default default");
        } else {
            String accessVectorPath = inputList[1];
            String securityClassPath = inputList[2];

            if (accessVectorPath.equals("default")) {
                accessVectorPath = "./data/fallback/access_vectors";
            }
            if (securityClassPath.equals("default")) {
                securityClassPath = "./data/fallback/security_classes";
            }

            this.getFocus().setAccessVectors(loadAccessVectors(accessVectorPath, securityClassPath));
            System.gc();
            System.out.println("Access vector definition loaded, total " + getFocus().getAccessVectors().size()
                    + " classes.");
        }
    }

    // EFFECTS: show the list of enabled capabilities
    private void commandShowCapability(String[] params) {
        // show_capability
        System.out.println("Current capabilities: ");
        for (ProjectModel.PolicyCapabilities p : this.getFocus().getCapabilities()) {
            System.out.println(p.toString());
        }
    }

    // EFFECTS: show the list of security classes defined
    private void commandShowClass(String[] params) {
        System.out.println("Security class list: ");
        HashMap<String, HashSet<String>> res = this.getFocus().getAccessVectors().getAccessVector();
        for (String k : res.keySet()) {
            System.out.println(k + " ");
        }
    }

    // EFFECTS: show the action set for a security class
    private void commandShowVectors(String[] params) {
        // show_vector <class>
        HashSet<String> res = this.getFocus().getAccessVectors().getAccessVector().get(params[1]);
        if (res == null) {
            System.out.println("This class is not defined.");
        } else {
            System.out.println(res);
        }
    }


    // EFFECTS: load access_vectors and security_classes and overwrite current project's field
    public static AccessVectorModel loadAccessVectors(String accessVectorPath, String securityClassPath)
            throws IOException, SyntaxParseException {
        File securityClassFile = new File(securityClassPath);
        File accessVectorFile = new File(accessVectorPath);
        Scanner fileReader;

        fileReader = new Scanner(securityClassFile);

        AccessVectorModel accessVectorModel = new AccessVectorModel();
        while (fileReader.hasNextLine()) {
            // The format of security_classes allowed line-by-line parsing
            // while access_vectors does not.
            accessVectorModel.addSecurityClass(
                    AccessVectorModel.securityClassParser(fileReader.nextLine())
            );
        }
        fileReader.close();
        String accessVectorFileContent;

        accessVectorFileContent = CustomReader.readAsWholeCode(accessVectorFile).getFirst();

        accessVectorModel.batchAddAction(AccessVectorModel.accessVectorParser(accessVectorFileContent));
        return accessVectorModel;
    }

    // EFFECTS: placeholder for commands not yet implememnted
    public static void notImplemented() {
        System.out.println("Not implemented for phase 1.");
    }


    // EFFECTS: create additional confirm prompt for sensitive commands.
    public boolean reassure() {
        System.out.println("Are you sure to proceed? (y/n)");
        return scanner.nextLine().equalsIgnoreCase("y");
    }
}
