package model.policy;

import model.MacroProcessor;
import ui.ModuleEditor;

import java.util.ArrayList;
import java.util.HashSet;

// A model for SELinux interface/templates
// Syntactically templates is just interfaces with more possible statements
// So Psychosis does not currently differ them internally.

public class InterfaceModel implements RuleAddable {
    private final String name;

    private ArrayList<String> parameters;

    private String description;

    // Placeholder for SELinux XML format documentation
    private String paramsDescription;
    private final ArrayList<RuleSetModel> ruleSetModels;
    private HashSet<String> tags;
    private final boolean isUserDefined;
    private String owner;

    // EFFECTS: initialize new InterfaceModel
    public InterfaceModel(String name, String owner, boolean isUserDefined) {
        this.name = name;
        this.isUserDefined = isUserDefined;
        this.description = "";
        this.owner = owner;
        this.ruleSetModels = new ArrayList<>();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIsUserDefined() {
        return this.isUserDefined;
    }

    // EFFECTS: return the number of first-order rules this interface contained
    public int getRuleNum() {
        return ruleSetModels.size();
    }

    // EFFECTS: add a rule to ruleset, combine based on action when possible
    // MODIFIES: this
    public void addStatement(RuleSetModel rule) {
        // First, check if the statement-source-target-targetclass tuple already exists
        boolean containEquv = false;
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

    public void setOwner(String owner) {
        this.owner = owner;
    }

    // For testing only
    public ArrayList<RuleSetModel> getRuleSetModels() {
        return ruleSetModels;
    }

    // EFFECTS: override the default toString, convert self to common selinux interface format
    // TODO: distinguish template/interface in the future
    @Override
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


    // EFFECTS: Replace args into Interface / template to create a
    //      pseudo TypeEnfModel (a set of rules)
    public TypeEnfModel call(String[] args) {
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
            res.addStatement(parsed);
        }
        return res;
    }
}
