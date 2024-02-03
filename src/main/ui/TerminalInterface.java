package ui;

import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;

import model.CustomReader;
import model.policy.*;

public class TerminalInterface {
    //Debugging command line for Phase 1

    private ArrayList<PolicyModel> loadedPolicies = new ArrayList<PolicyModel>();
    private Integer currentWorkIndex = -1;

    private Scanner scanner = new Scanner(System.in);

    private boolean isRunning = true;

    TerminalInterface() {

    }

    public void startInterface() {
        while (isRunning) {
            System.out.print("Psychosis$ ");
            String[] inputList = scanner.nextLine().split(" ");

            // Command list:
            // load_access_vectors, load_capability,
            // load_rules. load_interface
            // load_policy

            // lookup_interface, lookup_attribute, lookup_type
            switch (inputList[0]) {
                case "load_access_vectors":
                    commandLoadAccessVectors(inputList);
                    break;
                case "exit":
                    this.isRunning = false;
                    break;
                default:
                    System.out.println("Unknown command.");
            }
            System.out.println();
        }
    }

    private void commandLoadAccessVectors(String[] inputList) {
        if (inputList.length <= 2) {
            System.out.println("Usage:");
            System.out.println("load_access_vectors <access_vectors path> <security_classes path>.");
            System.out.println("Use default to load built-in definition, e.g.");
            System.out.println("load_access_vectors default default");
        } else {
            String accessVectorPath = inputList[1];
            String securityClassPath = inputList[2];

            if (accessVectorPath.equals("default")) {
                accessVectorPath = "";
            }
            if (securityClassPath.equals("default")) {
                securityClassPath = "";
            }
            loadAccessVectors(accessVectorPath, securityClassPath);
        }
    }

    public AccessVectorModel loadAccessVectors(String accessVectorPath, String securityClassPath) {
        File securityClassFile = new File(securityClassPath);
        File accessVectorFile = new File(accessVectorPath);
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(securityClassFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        try {
            accessVectorFileContent = CustomReader.readAsCompact(accessVectorFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        accessVectorModel.batchAddAction(AccessVectorModel.accessVectorParser(accessVectorFileContent));
        return accessVectorModel;
    }

    public PolicyModel loadPolicy(String path) {
        return null; //stub
    }
}
