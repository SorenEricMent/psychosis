package model.policy;

import model.MacroProcessor;

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

    public int getRuleNum() {
        return ruleSetModels.size();
    }

    public void addRuleSetModels(RuleSetModel rule) {
        // First, check if the statement-source-target-targetclass tuple
        // already exists
        Boolean containEquv = false;
        for (RuleSetModel r : ruleSetModels) {
            if (RuleSetModel.isEquvStatement(r, rule)) {
                containEquv = true;
                r.addAction(rule.getActions());
                break;
            }
        }
        if (!containEquv) {
            ruleSetModels.add(rule);
        }
    }

    public void setRuleSetModels(ArrayList<RuleSetModel> rules) {
        this.ruleSetModels = rules;
    }

    public String toString() {
        String res = "";
        for (RuleSetModel r : ruleSetModels) {
            res = res.concat(r.toString() + "\n");
        }
        return res;
    }

    public String getName() {
        return this.name;
    }

    public TypeEnfModel call(String[] args) {
        // Replace args into Interface / template to create a
        // pseudo TypeEnfModel (a set of rules)
        TypeEnfModel res = new TypeEnfModel("__psychosis_interface_expand__");
        MacroProcessor variableSubstitute = new MacroProcessor();
        for (int i = 0; i < args.length; i++) {
            variableSubstitute.addMacro("$".concat(Integer.toString(i+1)), args[i]);
        }
        for (RuleSetModel r : ruleSetModels) {
            res.addStatement(
                    new RuleSetModel(
                            r.getRuleType(),
                            variableSubstitute.process(r.getSourceContext()),
                            variableSubstitute.process(r.getTargetContext()),
                            variableSubstitute.process(r.getTargetClass()),
                            r.getActions()
                            // Technically get Actions should also be parsed with macro
                            // But I've never seen such usage in Refpolicy
                    )
            );
        }
        return res;
    }
}
