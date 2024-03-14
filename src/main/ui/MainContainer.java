package ui;

import javax.swing.*;
import java.awt.*;

public class MainContainer {
    private JList list1;
    private JPanel mainContainer;
    private JButton fileBtn;
    private JToolBar globalToolbar;
    private JButton helpBtn;

    public static void main() {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Psychosis Studio");
        Toolkit defToolkit = Toolkit.getDefaultToolkit();
        java.lang.reflect.Field awtAppClassNameField = null;

        try {
            awtAppClassNameField = defToolkit.getClass().getDeclaredField("awtAppClassName");
            awtAppClassNameField.setAccessible(true);
            awtAppClassNameField.set(defToolkit, "Psychosis Studio");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        JFrame mainContainer = new JFrame("Psychosis Studio " + Main.getVersion());
        ImageIcon img = new ImageIcon("./data/resources/logo.jpg");

        mainContainer.setIconImage(img.getImage());

        mainContainer.setContentPane(new MainContainer().mainContainer);
        mainContainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainContainer.pack();
        mainContainer.setVisible(true);
        mainContainer.setLocationRelativeTo(null);
    }
}
