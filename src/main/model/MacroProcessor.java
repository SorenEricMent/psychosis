package model;

import java.util.regex.*;
import java.util.ArrayList;

public class MacroProcessor {
    // Process SELinux .spt macros

    private ArrayList<Pattern> macros;

    MacroProcessor() {
        macros = new ArrayList<Pattern>();
    }

    public void addMacro(String expr) {
        macros.add(Pattern.compile(expr));
    }

    public String process(String text) {
        return text;
    }

    public static MacroProcessor macroRuleParser() {
        return null;
    }
}
