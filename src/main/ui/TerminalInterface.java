package ui;

import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

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
            String rawInput = scanner.nextLine();
            String[] inputList = rawInput.split(" ");
            String command = inputList[0];

            // Command list:
            // load_access_vectors, load_capability,
            // load_rules. load_interface
            // load_policy

            // lookup_interface, lookup_attribute, lookup_type
            switch (command) {
                case "load_access_vectors":
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

                    }
                    break;
                case "exit":
                    this.isRunning = false;
                    break;
            }
            System.out.println();
        }
    }

    public AccessVectorModel loadAccessVectors(String accessVectorPath, String securityClassPath) {
        return null;
    }

    public PolicyModel loadPolicy(String path) {
        return null; //stub
    }
}
