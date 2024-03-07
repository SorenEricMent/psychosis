package model.policy;

import model.CommonUtil;
import model.exception.NotFoundException;
import model.exception.SyntaxParseException;

import java.util.HashSet;

// A RuleSetModel is a SELinux first-order rule - a four-tuple and a hashset

public class RuleSetModel {

    public enum RuleType {
        allow,
        dontaudit,
        // The following are not planned to be implemented.
        neverallow,
        constrain,
        mlsconstrain
    }

    private final RuleType ruleType;
    private final String sourceContext;
    private final String targetContext;
    private final String targetClass;
    private final HashSet<String> actions;

    // EFFECTS: construct a RuleSetModel out of all related params, notice that the 4 tuple is not mutable
    // for that should be done with a new rule
    public RuleSetModel(RuleType ruleType, String source, String target, String targetClass, HashSet<String> actions) {
        this.ruleType = ruleType;
        this.sourceContext = source;
        this.targetContext = target;
        this.targetClass = targetClass;
        this.actions = actions;
    }

    // EFFECTS: add an action to the action set
    public void addAction(String action) {
        this.actions.add(action);
    }

    // EFFECTS: add a set of actions to the action set
    public void addAction(HashSet<String> actions) {
        this.actions.addAll(actions);
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
        return this.actions;
    }

    // EFFECTS: convert rule type from string to Enum type
    public static RuleType toRuleType(String str) {
        switch (str) {
            case "allow":
                return RuleType.allow;
            case "dontaudit":
                return RuleType.dontaudit;
            // THE REST STATEMENTS ARE NOT IMPLEMENTED
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

    // EFFECTS: judge if a string is a processed statement type
    public static boolean isProcessed(String token) {
        return token.equals("allow") || token.equals("dontaudit");
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

    // EFFECTS: judge if a string is a starting of a statement
    public static boolean isStatement(String str) {
        return str.equals("allow") || str.equals("dontaudit") || str.equals("neverallow") || str.equals("constrain")
                || str.equals("mlsconstrain");
    }

    // REQUIRES: last element must be ";"
    // EFFECTS: parse a line of text to RuleSetModel
    public static RuleSetModel ruleSetParser(String[] tokenized) throws SyntaxParseException {
        RuleType rt = toRuleType(tokenized[0]);
        String sourceContext = tokenized[1];
        String targetContext = tokenized[2].split(":")[0];
        String targetClass = tokenized[2].split(":")[1];

        HashSet<String> actions = new HashSet<>();
        CommonUtil.Balancer balancer = new CommonUtil.Balancer();

        if (!tokenized[tokenized.length - 1].equals(";")) {
            throw new SyntaxParseException("Statement without ending delimiter.");
        }

        for (int i = 3; i < tokenized.length - 1; i++) {
            if (CommonUtil.Balancer.isOtherToken(tokenized[i])) {
                actions.add(tokenized[i]);
            } else {
                balancer.push(tokenized[i]);
                if (balancer.isSyntaxError()) {
                    throw new SyntaxParseException("Unbalanced {}");
                }
            }
        }
        if (!balancer.check()) {
            throw new SyntaxParseException("Unbalanced {}, stack content: " + balancer);
        }

        return new RuleSetModel(rt, sourceContext, targetContext, targetClass, actions);
    }

    // EFFECTS: proxy caller for parsing a string (tokenize and pass to the actual parser)
    public static RuleSetModel ruleSetParser(String str) throws SyntaxParseException {
        String[] tokenized = CommonUtil.strongTokenizer(str);
        return ruleSetParser(tokenized);
    }

    // EFFECTS: convert self to SELinux statement format.
    @Override
    public String toString() {
        return this.ruleType.toString() + " "
                + this.sourceContext + " "
                + this.targetContext + ":"
                + this.targetClass + " "
                + "{ " + String.join(" ", this.actions) + " };";
    }
}
