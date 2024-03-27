package ui;

import javax.swing.*;

// The page to display at main editor on load or if the root node is selected on project tree
public class ProjectPlaceholder {
    private JPanel projectPlaceholderContainer;
    private JButton createProjectButton;
    private JButton loadProjectButton;
    private JButton loadWorkspaceButton;

    public JPanel getProjectPlaceholderContainer() {
        return projectPlaceholderContainer;
    }

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
