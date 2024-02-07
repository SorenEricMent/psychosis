package ui;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import java.util.Scanner;

import model.CustomReader;
import model.ProjectModel;
import model.TempProjectModel;
import model.exception.SyntaxParseException;
import model.policy.*;

public class TerminalInterface {
    //Debugging command line for Phase 1

    private ArrayList<ProjectModel> loadedProjects = new ArrayList<ProjectModel>();
    private Integer currentWorkIndex = 0;
    // Object 0 is an empty non-saving test only project

    private Scanner scanner = new Scanner(System.in);

    private boolean isRunning = true;

    TerminalInterface() {
        this.loadedProjects.add(new TempProjectModel("temp"));
    }

    @SuppressWarnings("methodlength")
    public void startInterface() {
        while (isRunning) {
            System.out.print("Psychosis@" + getFocus().getName() + "$ ");
            String[] inputList = scanner.nextLine().split(" ");

            // Command list:
            // load_access_vectors, load_capability,
            // load_rules. load_interface
            // load_policy

            // lookup_interface, lookup_attribute, lookup_type
            try {
                switch (inputList[0]) {
                    case "create_project": notImplemented();
                        break;
                    case "load_project": notImplemented();
                        break;
                    case "show_project": System.out.println(getFocus().toString());
                        break;
                    case "show_layer": System.out.println(getFocus().lookup(inputList[1]).toString());
                        break;
                    case "add_module": commandAddModule(inputList);
                        break;
                    case "remove_module": commandRemoveModule(inputList);
                        break;
                    case "add_interface": notImplemented();
                        break;
                    case "show_interface": notImplemented();
                        break;
                    case "edit_interface": notImplemented();
                        break;
                    case "edit_typeenf": notImplemented();
                        break;
                    case "edit_filecontext": notImplemented();
                        break;
                    case "tag_add_interface": notImplemented();
                        break;
                    case "tag_rm_interface": notImplemented();
                        break;
                    case "load_access_vectors": commandLoadAccessVectors(inputList);
                        break;
                    case "show_capability": notImplemented();
                        break;
                    case "load_capability": notImplemented();
                        break;
                    case "load_interface": notImplemented();
                        break;
                    case "load_typeenf": notImplemented();
                        break;
                    case "lookup_interface": notImplemented();
                        break;
                    case "exit":
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

    private void commandAddModule(String[] params) {
        // add_module <layer_name> <module_name>
    }

    private void commandRemoveModule(String[] params) {
        // remove_module <layer_name> <module_name>
    }


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

    public static void notImplemented() {
        System.out.println("Not implemented for phase 1.");
    }
}
