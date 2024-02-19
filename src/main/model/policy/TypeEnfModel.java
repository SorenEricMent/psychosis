package model.policy;

import model.*;
import model.exception.SyntaxParseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TypeEnfModel extends FileObjectModel implements Encodeable, Decodeable {
    // SELinux type enforcement (.te) file

    // A statement could be a method call or statement
    // but for now only first order statement is supported

    private final String name;
    private final ArrayList<String> requiredType = new ArrayList<>();
    private final ArrayList<RuleSetModel> statementsFO = new ArrayList<>();

    private final ArrayList<Pair<String, String[]>> interfaceCall = new ArrayList<>();

    public ArrayList<RuleSetModel> getStatementsFO() {
        return statementsFO;
    }

    public TypeEnfModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addInterfaceCall(String i, String[] args) {
        interfaceCall.add(new Pair<String, String[]>(i, args));
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

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public static TypeEnfModel parser(String content) throws SyntaxParseException {
        String[] tokenized = CommonUtil.strongTokenizer(content);
        // First line has to be policy_module(name)
        if (tokenized.length < 4
                || !tokenized[0].equals("policy_module") || !tokenized[1].equals("(") || !tokenized[3].equals(")")) {
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
                i = end;
            } else if (tokenized[i].equals("require")) {
                int end = getEnd(i + 1, tokenized);
                i = end;
            } else if (tokenized[i].equals("gen_require")) {
                int end = getEnd(i + 1, tokenized);
                i = end;
            } else {
                int end = getEnd(i + 1, tokenized) + 2;
                res.addInterfaceCall(tokenized[i], Arrays.copyOfRange(tokenized, i + 2, end - 2));
                i = end;
            }
        }
        return res;
    }

    private static int getEnd(int i, String[] tokenized) throws SyntaxParseException {
        int end = -1;
        CommonUtil.Balancer findEnd = new CommonUtil.Balancer();
        for (int j = i; j < tokenized.length; j++) {
            findEnd.push(tokenized[j]);
            if (findEnd.isSyntaxError()) {
                throw new SyntaxParseException("Unbalanced parenthesis in block: stack: " + findEnd.toString());
            }
            if (findEnd.check()) {
                end = j;
                break;
            }
        }
        if (end == -1) {
            throw new SyntaxParseException("Cannot end sequence: stack: " + findEnd.toString());
        }
        return end;
    }

    // EFFECTS: export the content without process interface call
    public String toString() {
        String res = "";
        res = res.concat("policy_module(" + this.getName() + ")\n\n");

        // FUTURE TODO: REQUIRE STATEMENT

        // Concat rules
        for (RuleSetModel r : statementsFO) {
            res = res.concat(r.toString() + "\n");
        }

        // Concat interface calls
        for (Pair<String, String[]> p : interfaceCall) {
            if (p.getSecond().length == 0) {
                res = res.concat(
                        p.getFirst() + "()\n"
                );
            } else {
                res = res.concat(
                        p.getFirst() + "(" + String.join(",", p.getSecond()) + ")\n"
                );
            }
        }
        return res;
    }

    // REQUIRES: i need to be the global interface list
    // EFFECTS: export content in string, compiled
    public String toString(InterfaceSetModel i) {
        String res = "";
        res = res.concat("policy_module(" + this.getName() + ")\n\n");

        // FUTURE TODO: REQUIRE STATEMENT

        // Concat rules
        for (RuleSetModel r : statementsFO) {
            res = res.concat(r.toString() + "\n");
        }

        // Concat interface calls
        for (Pair<String, String[]> p : interfaceCall) {
            String compiled = i.getInterface(p.getFirst()).call(p.getSecond()).toString();
            res = res.concat(
                    compiled.substring(18) + "\n" // Remove the policy_module line as it return TypeEnf
            );
        }
        return res;
    }

    // EFFECTS: export only first order statement
    public String toStringRuleOnly() {
        String res = "";
        for (RuleSetModel r : statementsFO) {
            res = res.concat(r.toString() + "\n");
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
