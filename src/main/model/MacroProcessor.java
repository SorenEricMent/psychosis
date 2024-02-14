package model;

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

    public String process(String text) {
        return text;
    }

    public static MacroProcessor macroRuleParser() {
        return null;
    }
}
