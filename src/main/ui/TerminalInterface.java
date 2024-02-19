package ui;

import model.CommonUtil;
import model.CustomReader;
import model.ProjectModel;
import model.TempProjectModel;
import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.exception.SyntaxParseException;
import model.policy.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TerminalInterface {
    //Debugging command line for Phase 1

    private final ArrayList<ProjectModel> loadedProjects = new ArrayList<>();
    private final Integer currentWorkIndex = 0;
    // Object 0 is an empty non-saving test only project

    private final Scanner scanner = new Scanner(System.in);

    private boolean isRunning = true;

    TerminalInterface() {
        this.loadedProjects.add(new TempProjectModel("temp"));
    }

    @SuppressWarnings("methodlength")
    public void startInterface() {
        while (isRunning) {
            System.out.print("Psychosis@" + getFocus().getName() + "$ ");
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
                    case "create_project":
                        notImplemented();
                        break;
                    case "select_project":
                        notImplemented();
                        break;
                    case "load_project":
                        notImplemented();
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
                        notImplemented();
                        break;
                    case "load_capability":
                        notImplemented();
                        break;
                    case "load_typeenf":
                        notImplemented();
                        break;
                    case "exit":
                        System.out.println("Goodbye");
                        this.isRunning = false;
                        break;
                    default:
                        System.out.println("Unknown command.");
                }
            } catch (SyntaxParseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }

    // EFFECT: return the project that is currently working on
    private ProjectModel getFocus() {
        return loadedProjects.get(currentWorkIndex);
    }

    private void commandCreateProject() {
        // create_project <basis (test/refpolicy/custom)> [custom]:<path> name
        // the latter is not yet implemented in Phase 1, TODO
    }

    private void commandShowProject() {
        System.out.println(getFocus().toString());
    }

    private void commandShowLayer(String[] params) {
        try {
            System.out.println(getFocus().getLayer(params[1]).toString());
        } catch (NotFoundException e) {
            System.out.println("Failed to show layer detail.");
            System.out.println(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        }
    }

    private void commandCreateLayer(String[] params) {
        // create_layer <layer_name>
        try {
            this.getFocus().addLayer(params[1]);
        } catch (DuplicateException e) {
            System.out.println("Error when adding layer to project " + this.getFocus().getName());
            System.out.println(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        }
    }

    private void commandRemoveLayer(String[] params) {
        if (reassure()) {
            try {
                this.getFocus().removeLayer(params[1]);
            } catch (NotFoundException e) {
                System.out.println("Error when removing layer.");
                System.out.println(e);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Not enough params.");
            }
        }
    }

    private void commandListProject() {
        System.out.print("Loaded projects: ");
        for (ProjectModel project : loadedProjects) {
            System.out.print(project.getName() + " ");
        }
    }

    private void commandLoadModule(String[] params) {
        // load_module <to layer> <name> <te path> <if path> <fc path>
        try {
            InterfaceSetModel i = InterfaceSetModel.parser(CustomReader.readAsWholeCode(new File(params[4])));
            for (InterfaceModel x : i.getInterfaces()) {
                this.getFocus().getGlobalInterfaceSet().addInterface(x);
            }
            TypeEnfModel t = TypeEnfModel.parser(CustomReader.readAsWholeCode(new File(params[3])));
            FileContextModel f = new FileContextModel();
            PolicyModuleModel m = new PolicyModuleModel(params[2], t, i, f);
            this.getFocus().getLayer(params[1]).addPolicyModule(m);
        } catch (SyntaxParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void commandAddModule(String[] params) {
        // add_module <layer_name> <module_name>
        try {
            this.getFocus().getLayer(params[1]).addPolicyModule(
                    new PolicyModuleModel(params[2])
            );
        } catch (NotFoundException e) {
            System.out.println("Failed to add module to layer.");
            System.out.println(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        }
    }

    private void commandShowModule(String[] params) {
        // show_module <layer_name> <module_name>
        try {
            System.out.println(this
                    .getFocus()
                    .getLayer(params[1])
                    .getPolicyModule(params[2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        }
    }

    private void commandExportModule(String[] params) {
        // export_module <text/file> <te/if/fc> [layer_name] [module_name]
        // Phase 1: only option text implemented
        if (params[1].equals("text")) {
            if (params[2].equals("te")) {
                System.out.println(this.getFocus().getLayer(params[3]).getPolicyModule(params[4]).getTypeEnf());
            } else if (params[2].equals("if")) {
                System.out.println(this.getFocus().getLayer(params[3]).getPolicyModule(params[4]).getInterfaceSet());
            } else if (params[2].equals("fc")) {
                notImplemented();
            } else {
                System.out.println("Unknown export component option.");
            }
        } else if (params[1].equals("file")) {
            notImplemented();
        } else {
            System.out.println("Unknown export option.");
        }
    }

    private void commandRemoveModule(String[] params) {
        // remove_module <layer_name> <module_name>
        try {
            this.getFocus().removeModule(
                    params[1],
                    params[2]
            );
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        }
    }

    private void commandAddInterface(String[] params) {
        // add_interface <layer_name> <module_name> <interface_name>
        try {
            this.getFocus().addInterface(
                    params[1],
                    params[2],
                    params[3]
            );
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        } catch (NotFoundException e) {
            System.out.println("The layer/module you are trying to access doesn't exist");
        }
    }

    private void commandEditInterface(String[] params) {
        // edit_interface <layer_name> <module_name> <interface_name>
        // <add/remove> <allow/dontaudit> <source_label> <target_label> <target_class> [action_list]

        // VALIDATE WITH WEAK!
        switch (params[1]) {
            case "add":
                break;
            case "remove":
                break;
        }
    }

    private void commandLookUpInterface(String[] params) {
        // lookup_interface <unspec/userdefined> name=name tag=tag1,tag2

    }

    private void commandShowInterface(String[] params) {
        // show_interface <layer_name> <module_name> <interface_name>
        try {
            System.out.println(this
                    .getFocus()
                    .getLayer(params[1])
                    .getPolicyModule(params[2])
                    .getInterface(params[3]));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        }
    }

    private void commandRemoveInterface(String[] params) {
        // remove_interface <layer_name> <module_name> <interface_name>
        try {
            this.getFocus().removeInterface(
                    params[1],
                    params[2],
                    params[3]
            );
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough params.");
        } catch (NotFoundException e) {
            System.out.println("The layer/module you are trying to access doesn't exist");
        }
    }

    private void commandEditTypeEnf(String[] params) {
        // edit_typeenf <layer_name> <module_name> <add/remove/add_inf/remove_inf> <RuleType>
        // <source context> <target_context> <target_class> [listof actions] (add_inf/remove_inf infname [listof args])
        HashSet<String> actions = new HashSet<String>();
        if (params[3].equals("add")) {
            Collections.addAll(actions, Arrays.copyOfRange(params, 8, params.length));
            this.getFocus().getLayer(params[1]).getPolicyModule(params[2]).getTypeEnf().addStatement(
                    new RuleSetModel(RuleSetModel.toRuleType(
                            params[4]), params[5], params[6], params[7],
                            actions));
        } else if (params[3].equals("remove")) {
            // Output if exists such rules
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
            System.out.println("Access vector definition loaded, total " + getFocus().getAccessVectors().size()
                    + " classes.");
        }
    }


    public static AccessVectorModel loadAccessVectors(String accessVectorPath, String securityClassPath)
            throws IOException, SyntaxParseException {
        File securityClassFile = new File(securityClassPath);
        File accessVectorFile = new File(accessVectorPath);
        Scanner fileReader = null;

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

        accessVectorFileContent = CustomReader.readAsWholeCode(accessVectorFile);

        accessVectorModel.batchAddAction(AccessVectorModel.accessVectorParser(accessVectorFileContent));
        return accessVectorModel;
    }

    public static PolicyModuleModel loadPolicy(String path) {
        return null; //stub
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
