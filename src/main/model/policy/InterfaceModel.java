package model.policy;

import model.MacroProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InterfaceModel {
    private final String name;

    private ArrayList<String> parameters;

    private String description;

    private String paramsDescription;
    private ArrayList<RuleSetModel> ruleSetModels;
    private HashSet<String> tags;
    private final boolean isUserDefined;

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
        String res = "interface(`" + getName() + "',`\n";
        for (RuleSetModel r : ruleSetModels) {
            res = res.concat(r.toString() + "\n");
        }
        res = res + "')";
        return res;
    }

    public String getName() {
        return this.name;
    }

    public TypeEnfModel call(String[] args) {
        // Replace args into Interface / template to create a
        // pseudo TypeEnfModel (a set of rules)
        TypeEnfModel res = new TypeEnfModel("_");
        MacroProcessor variableSubstitute = new MacroProcessor();
        for (int i = 0; i < args.length; i++) {
            variableSubstitute.addMacro("$".concat(Integer.toString(i + 1)), args[i]);
        }
        for (RuleSetModel r : ruleSetModels) {
            RuleSetModel parsed = new RuleSetModel(
                    r.getRuleType(),
                    variableSubstitute.process(r.getSourceContext()),
                    variableSubstitute.process(r.getTargetContext()),
                    variableSubstitute.process(r.getTargetClass()),
                    r.getActions()
                    // Technically get Actions should also be parsed with macro
                    // But I've never seen such usage in Refpolicy
            );
            res.addStatement(
                    parsed
            );
        }
        return res;
    }
}
