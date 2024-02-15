package model;

import model.exception.SyntaxParseException;

import java.util.regex.*;
import java.util.ArrayList;

public class MacroProcessor {
    // Process SELinux .spt macros

    // SPT Macros are simple text replacements, nothing fancy
    private ArrayList<Pair<Pattern, String>> macros;

    MacroProcessor() {
        macros = new ArrayList<Pair<Pattern, String>>();
    }

    public void addMacro(String from, String to) {
        macros.add(new Pair<Pattern, String>(Pattern.compile(from), to));
    }

    // EFFECTS: apply all compiled macros to the string.
    public String process(String text) {
        for (Pair<Pattern, String> p : macros) {
            text = p.getFirst()
                    .matcher(text)
                    .replaceAll(p.getSecond());
        }
        return text;
    }

    // EFFECTS: parse a .spt file to compiled macros
    // TODO
    public static MacroProcessor macroRuleParser() throws SyntaxParseException {
        return null;
    }
}
