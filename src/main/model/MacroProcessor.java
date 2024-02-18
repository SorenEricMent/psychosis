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
    // REQUIRE: content should be read from readAsWholeCode
    public static MacroProcessor macroRuleParser(String content) throws SyntaxParseException {
        // Macros are defined with define(`from', `to')
        MacroProcessor res = new MacroProcessor();
        String[] byLine = content.split("\n");
        for (String str : byLine) {
            if (!str.startsWith("define(`") || !str.endsWith("')")) {
                throw new SyntaxParseException("Broken define syntax.");
            }
            String from = str.substring(str.indexOf("`"), str.indexOf("'"));
            String to = str.substring(str.indexOf("'") + 1, str.length() - 2);
            res.addMacro(from, to);
        }
        return res;
    }
}
