package model.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InterfaceModel {
    private String name;

    private ArrayList<String> parameters;

    private String description;

    private String paramsDescription;
    private ArrayList<RuleSetModel> ruleSetModels;
    private HashSet<String> tags;
    private boolean isUserDefined;

    public InterfaceModel(String name, boolean isUserDefined) {
        this.name = name;
        this.isUserDefined = isUserDefined;
        this.description = "";
        this.ruleSetModels = new ArrayList<>();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIsUserDefined() {
        return this.isUserDefined;
    }


    // EFFECTS: parse .if interface definition file
    // This parser is actually a lexer for now.
    // For now, only first-order statements are supported!
    // Conditionals(IFDEF) and Calls will be skipped.
    public static HashMap<String, InterfaceModel> interfaceParserFO(String content) {
        HashMap<String, InterfaceModel> results = new HashMap<String, InterfaceModel>();
        HashSet<String> typeCheck = new HashSet<String>();
        // interface(`name', `content');

        return null;
    }

    public void addRuleSetModels(RuleSetModel rule) {
        // First, check if the statement-source-target-targetclass tuple
        // already exists
        for (RuleSetModel r : ruleSetModels) {
            if (RuleSetModel.isEquvStatement(r, rule)) {
                r.addAction(rule.getActions());
            }
        }
    }

    public void setRuleSetModels(ArrayList<RuleSetModel> rules) {
        this.ruleSetModels = rules;
    }
}
