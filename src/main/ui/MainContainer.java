package ui;

import javax.swing.*;

public class MainContainer {
    private JList list1;
    private JPanel mainContainer;

    public static void main() {
        JFrame mainContainer = new JFrame("Psychosys Studio " + Main.getVersion());
        mainContainer.setContentPane(new MainContainer().mainContainer);
        mainContainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainContainer.pack();
        mainContainer.setVisible(true);
        mainContainer.setLocationRelativeTo(null);
    }
}
