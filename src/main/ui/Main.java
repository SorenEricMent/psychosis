package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    // EFFECTS: return the version, a static string
    public static String getVersion() {
        return "0.1.0";
    }

    public static final String DEFAULT_ACCESS_VEC_PATH = "./data/fallback/access_vectors";
    public static final String DEFAULT_SEC_CLASS_PATH = "./data/fallback/security_classes";
    private static boolean useTUI = true;

    // EFFECTS: process locale fallback, start TUI/GUI based on flag
    public static void main(String[] args) {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            Locale.setDefault(new Locale(locale.getLanguage(), "US"));
        } else if (locale.getLanguage().equals("fr")) {
            Locale.setDefault(new Locale(locale.getLanguage(), "FR"));
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

    // EFFECTS: restart the application with new locale params on JVM
    // REQUIRES: locale-country be defined in PsychosisResource
    public static void selfLocaleRestart(String locale, String country) {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-Duser.language=" + locale);
        command.add("-Duser.country=" + country);
        command.add("-splash:data/resources/splash.jpg");
        command.add("--add-opens=java.desktop/sun.awt.X11=ALL-UNNAMED");
        command.add("-XX:+UseG1GC");
        command.add("-XX:MaxGCPauseMillis=200");
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(Main.class.getName());
        command.add("-g");

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.exit(0);
    }
}
