package ui;

import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;

import model.CustomReader;
import model.exception.SyntaxParseException;
import model.policy.*;

public class TerminalInterface {
    //Debugging command line for Phase 1

    private ArrayList<PolicyModel> loadedPolicies = new ArrayList<PolicyModel>();
    private Integer currentWorkIndex = -1;

    private Scanner scanner = new Scanner(System.in);

    private boolean isRunning = true;

    TerminalInterface() {

    }

    @SuppressWarnings("methodlength")
    public void startInterface() {
        while (isRunning) {
            System.out.print("Psychosis$ ");
            String[] inputList = scanner.nextLine().split(" ");

            // Command list:
            // load_access_vectors, load_capability,
            // load_rules. load_interface
            // load_policy

            // lookup_interface, lookup_attribute, lookup_type
            try {
                switch (inputList[0]) {
                    case "load_access_vectors":
                        commandLoadAccessVectors(inputList);
                        break;
                    case "load_capability":
                        break;
                    case "lookup_interface":
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

            loadAccessVectors(accessVectorPath, securityClassPath);
            // System.out.println("Loaded " + )
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

        accessVectorFileContent = CustomReader.readAsCompact(accessVectorFile);

        accessVectorModel.batchAddAction(AccessVectorModel.accessVectorParser(accessVectorFileContent));
        return accessVectorModel;
    }

    public static PolicyModel loadPolicy(String path) {
        return null; //stub
    }
}
