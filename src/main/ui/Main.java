package ui;

public class Main {
    public static String getVersion() {
        return "0.1.0";
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-g") || args[i].equals("--gui")) {
                // Start in GUI
            }
        }
        TerminalInterface terminal = new TerminalInterface();
        terminal.startInterface();
    }
}
