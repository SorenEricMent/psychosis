package ui;

import java.util.Locale;

public class Main {
    public static String getVersion() {
        return "0.1.0";
    }

    private static boolean useTUI = true;

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
