package ui;

public class Main {
    public static void main(String[] args) {
        for (int i = 0;i < args.length;i++) {
            if (args[i].equals("-g") || args[i].equals("--gui")) {
                // Start in GUI
            }
        }
        TerminalInterface terminal = new TerminalInterface();
        terminal.startInterface();
    }
}
