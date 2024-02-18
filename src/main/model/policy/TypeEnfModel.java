package model.policy;

import model.CommonUtil;
import model.Decodeable;
import model.Encodeable;
import model.FileObjectModel;
import model.exception.SyntaxParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TypeEnfModel extends FileObjectModel implements Encodeable, Decodeable {
    // SELinux type enforcement (.te) file

    // A statement could be a method call or statement
    // but for now only first order statement is supported

    private String name;
    private ArrayList<String> requiredType = new ArrayList<>();
    private ArrayList<RuleSetModel> statementsFO = new ArrayList<>();

    public ArrayList<RuleSetModel> getStatementsFO() {
        return statementsFO;
    }

    public TypeEnfModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addStatement(RuleSetModel r) {
        RuleSetModel target = null;
        for (RuleSetModel s : statementsFO) {
            // Check if mergible
            if (RuleSetModel.isEquvStatement(s, r)) {
                target = s;
                break;
            }
        }
        if (target == null) {
            statementsFO.add(r);
        } else {
            target.addAction(r.getActions());
        }
    }

    // EFFECTS: remove the intersective rules and return rules that are not intersected.
    public HashSet<String> removeStatement(RuleSetModel r) {
        RuleSetModel target = null;
        for (RuleSetModel s : statementsFO) {
            if (RuleSetModel.isEquvStatement(s, r)) {
                target = s;
                break;
            }
        }
        if (target == null) {
            return null;
        }
        HashSet<String> original = (HashSet<String>) target.getActions().clone();
        target.getActions().removeAll(r.getActions());
        original.removeAll(r.getActions());
        return original;
    }

    public void addInterfaceCall() {
        // TODO: not implemented for p1
    }

    public static TypeEnfModel parser(String content) throws SyntaxParseException {
        String[] tokenized = CommonUtil.strongTokenizer(content);
        // First line has to be policy_module(name)
        if (!tokenized[0].equals("policy_module") || !tokenized[1].equals("(") || !tokenized[3].equals(")")) {
            throw new SyntaxParseException("Bad syntax for name declaration.");
        }
        TypeEnfModel res = new TypeEnfModel(tokenized[2]);
        for (int i = 4; i < tokenized.length; i++) {
            if (RuleSetModel.isStatement(tokenized[i])) {
                // Find the end of this statement and feed to RuleSetModel parser
                int end = -1;
                for (int j = i + 4; j < tokenized.length; j++) {
                    if (tokenized[j].equals(";")) {
                        end = j;
                        break;
                    }
                }
                if (end == -1) {
                    throw new SyntaxParseException("Cannot end sequence.");
                }
                res.addStatement(RuleSetModel.ruleSetParser(Arrays.copyOfRange(tokenized, i, end + 1)));
                i = end + 1;
            } else if (tokenized[i].equals("require") || tokenized[i].equals("gen_require")) {
                // For now, don't do require Type Check;
            }
            // For now, ignore not-first-order statements even if they have syntax errors.
        }
        return res;
    }

    // EFFECTS: export content in string;
    public String toString() {
        String res = "";
        res = res.concat("policy_module(" + this.getName() + ")\n\n");

        // FUTURE TODO: REQUIRE STATEMENT

        // Concat rules
        for (RuleSetModel r : statementsFO) {
            res = res.concat(r.toString());
        }
        return res;
    }

    public int lineCount() {
        return statementsFO.size();
    }

    // EFFECTS: Compare if two TypeEnfModel is equivalent
    // Used only for testing
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public boolean equals(TypeEnfModel t) {
        HashMap<RuleSetModel.RuleType, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>> compare = new
                HashMap<RuleSetModel.RuleType, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>>();
        for (RuleSetModel r : t.getStatementsFO()) {
            compare.putIfAbsent(r.getRuleType(),
                    new HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>());
            compare.get(r.getRuleType()).putIfAbsent(r.getSourceContext(),
                    new HashMap<String, HashMap<String, HashSet<String>>>());
            compare.get(r.getRuleType()).get(r.getSourceContext()).putIfAbsent(r.getTargetContext(),
                    new HashMap<String, HashSet<String>>());
            compare.get(r.getRuleType()).get(r.getSourceContext()).get(r.getTargetContext()).put(r.getTargetClass(),
                    new HashSet<String>());
            compare.get(r.getRuleType()).get(r.getSourceContext()).get(r.getTargetContext())
                    .get(r.getTargetClass()).addAll(r.getActions());
        }
        for (RuleSetModel r : this.getStatementsFO()) {
            try {
                if (!compare.get(r.getRuleType()).get(r.getSourceContext())
                        .get(r.getTargetContext()).get(r.getTargetClass())
                        .equals(r.getActions())) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }
        return true;
    }
}
