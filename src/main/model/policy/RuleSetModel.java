package model.policy;

import java.util.HashSet;

public class RuleSetModel {
    // A rule is a three-tuple and a hashset

    enum RuleType {
        allow,
        dontaudit,
        neverallow,
        constrain,
        mlsconstrain
    }

    private RuleType ruleType;
    private String sourceContext;
    private String targetContext;
    private String targetClass;
    private HashSet<String> actions = new HashSet<String>();

    RuleSetModel(String ruleType, String source, String target, String targetClass, HashSet<String> actions) {

    }

    public void addAction() {

    }

    public boolean existAction() {
        return false;//TODO
    }
}
