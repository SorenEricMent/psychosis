package model.policy;

import model.CommonUtil;
import model.FileObjectModel;
import model.exception.SyntaxParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class TypeEnfModel extends FileObjectModel {
    // SELinux type enforcement (.te) file

    // A statement could be a method call or statement
    // but for now only first order statement is supported

    private ArrayList<String> requiredType = new ArrayList<>();
    private ArrayList<RuleSetModel> statementsFO = new ArrayList<>();

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

    public void addInterfaceCall() {
        // TODO: not implemented for p1
    }

    public static TypeEnfModel typeEnfParser(String content) throws SyntaxParseException {
        String[] tokenized = CommonUtil.strongTokenizer(content);
        TypeEnfModel res = new TypeEnfModel();
        // First line has to be policy_module(name)
        if (!tokenized[0].equals("policy_module") || !tokenized[1].equals("(") || !tokenized[3].equals(")")) {
            throw new SyntaxParseException("Bad syntax for name declaration.");
        }
        for (int i = 4; i < tokenized.length; i++) {
            if (RuleSetModel.isStatement(tokenized[i])) {
                // Find the end of this statement and feed to RuleSetModel parser
                int end = i + 4; // Minimal ; token distance
                for (int j = i + 4; j < tokenized.length; j++) {
                    if (tokenized[j].equals(";")) {
                        break;
                    }
                }
                res.addStatement(RuleSetModel.ruleSetParser(Arrays.copyOfRange(tokenized, i, end)));
            }
        }
        return res;
    }

    // EFFECTS: export content in string;
    public String toString() {
        String res = "";
        // FUTURE TODO: REQUIRE STATEMENT
        for (RuleSetModel r : statementsFO) {
            res = res.concat(r.toString());
        }
        return res;
    }

    public int lineCount() {
        return statementsFO.size();
    }
}
