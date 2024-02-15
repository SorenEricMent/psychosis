package model.policy;

import model.FileObjectModel;

import java.util.ArrayList;
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

    public static TypeEnfModel typeEnfParser(String content) {
        return null;
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
