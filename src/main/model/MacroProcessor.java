package model;

import model.exception.SyntaxParseException;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MacroProcessor {
    // Process SELinux .spt macros

    // SPT Macros are simple text replacements, nothing fancy
    private final ArrayList<Pair<Pattern, String>> macros;

    public MacroProcessor() {
        macros = new ArrayList<Pair<Pattern, String>>();
    }

    public void addMacro(String from, String to) {
        // REQUIRES: to cannot contain from
        macros.add(new Pair<Pattern, String>(Pattern.compile(from, Pattern.LITERAL), to));
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
            str = str.strip();
            if (!str.startsWith("define(`") || !str.endsWith("')")) {
                throw new SyntaxParseException("Broken define syntax. Content: " + str);
            }
            String from = str.substring(str.indexOf("`") + 1, str.indexOf("'"));
            String rest = str.substring(str.indexOf("'") + 1);
            String to = rest.substring(rest.indexOf("`") + 1, rest.length() - 2);
            res.addMacro(from, to);
        }
        return res;
    }
}
