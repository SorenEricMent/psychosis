package ui;

public class Main {
    public static String getVersion() {
        return "0.1.0";
    }

    private static boolean useTUI = true;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-g") || args[i].equals("--gui")) {
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
