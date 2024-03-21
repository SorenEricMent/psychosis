package ui;

import java.util.Locale;

public class Main {
    // EFFECTS: return the version, a static string
    public static String getVersion() {
        return "0.1.0";
    }

    public static final String DEFAULT_ACCESS_VEC_PATH = "./data/fallback/access_vectors";
    public static final String DEFAULT_SEC_CLASS_PATH =  "./data/fallback/security_classes";
    private static boolean useTUI = true;

    // EFFECTS: process locale fallback, start TUI/GUI based on flag
    public static void main(String[] args) {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            Locale.setDefault(new Locale(locale.getLanguage(), "US"));
        } else {
            if (!locale.getLanguage().equals("eo")) {
                Locale.setDefault(new Locale("eo", ""));
            }
        }

        for (String arg : args) {
            if (arg.equals("-g") || arg.equals("--gui")) {
                // Start in GUI
                GraphicInterface gui = new GraphicInterface();
                useTUI = false;
            }
        }
        if (useTUI) {
            TerminalInterface terminal = new TerminalInterface();
            terminal.startInterface();
        }
    }
}
