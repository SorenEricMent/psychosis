package model.policy;

import model.CommonUtil;
import model.exception.NotFoundException;

import java.util.HashSet;

public class RuleSetModel {
    // A rule is a three-tuple and a hashset

    public enum RuleType {
        allow,
        dontaudit,
        // The following are not planned to be implemented.
        neverallow,
        constrain,
        mlsconstrain
    }

    private RuleType ruleType;
    private String sourceContext;
    private String targetContext;
    private String targetClass;
    private HashSet<String> actions = new HashSet<String>();

    public RuleSetModel(RuleType ruleType, String source, String target, String targetClass, HashSet<String> actions) {
        this.ruleType = ruleType;
        this.sourceContext = source;
        this.targetContext = target;
        this.targetClass = targetClass;
        this.actions = actions;
    }

    public void addAction(String action) {
        this.actions.add(action);
    }

    public void addAction(HashSet<String> actions) {
        this.actions.addAll(actions);
    }

    public boolean existAction(String str) {
        return actions.contains(str);
    }

    public RuleType getRuleType() {
        return this.ruleType;
    }

    public String getSourceContext() {
        return this.sourceContext;
    }

    public String getTargetContext() {
        return this.targetContext;
    }

    public String getTargetClass() {
        return this.targetClass;
    }

    public HashSet<String> getActions() {
        return actions;
    }

    // EFFECTS: convert rule type from string to Enum type
    public static RuleType toRuleType(String str) {
        switch (str) {
            case "allow":
                return RuleType.allow;
            case "dontaudit":
                return RuleType.dontaudit;
            case "neverallow":
                return RuleType.neverallow;
            case "constrain":
                return RuleType.constrain;
            case "mlsconstrain":
                return RuleType.mlsconstrain;
            default:
                throw new NotFoundException("Unknown rule type" + str);
        }
    }

    // EFFECTS: judge if two RuleSetModel is same in rule type
    // source context, target context and target class.
    public static boolean isEquvStatement(RuleSetModel a, RuleSetModel b) {
        return a.getRuleType() == b.getRuleType()
                && a.getSourceContext().equals(b.getSourceContext())
                && a.getTargetContext().equals(b.getTargetContext())
                && a.getTargetClass().equals(b.getTargetClass());
    }

    // EFFECTS: judge if a RuleSetModel is completely equivalent to self.
    public boolean equals(RuleSetModel b) {
        return isEquvStatement(this, b) && this.actions.equals(b.getActions());
    }

    // EFFECTS: parse a line of text to RuleSetModel
    // TODO
    public static RuleSetModel ruleSetParser(String str) {
        String[] tokenized = CommonUtil.basicTokenizer(str);
        return null; //stub
    }

    // EFFECTS: convert self to SELinux rule format.
    @Override
    public String toString() {
        return this.ruleType.toString() + " "
                + this.sourceContext + " "
                + this.targetContext + ":"
                + this.targetClass + " "
                + "{ " + String.join(" ", this.actions) + " };";
    }
}
