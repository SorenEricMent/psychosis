package ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProjectPlaceholder {
    private JPanel projectPlaceholderContainer;
    private JButton createProjectButton;
    private JButton loadProjectButton;
    private JButton loadWorkspaceButton;

    public JButton getCreateProjectButton() {
        return createProjectButton;
    }

    public JButton getLoadProjectButton() {
        return loadProjectButton;
    }

    public JButton getLoadWorkspaceButton() {
        return loadWorkspaceButton;
    }
}
