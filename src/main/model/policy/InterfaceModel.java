package model.policy;

import java.util.ArrayList;
import java.util.HashSet;

public class InterfaceModel {
    private String name;

    private ArrayList<String> parameters;
    private ArrayList<RuleSetModel> ruleSetModels;
    private HashSet<String> tags;
    private boolean isUserDefined;

    public InterfaceModel(String name) {
        this.name = name;
    }

    public boolean getIsUserDefined() {
        return this.isUserDefined;
    }

    public static InterfaceModel interfaceParser(String content) {
        return null;
    }

}
