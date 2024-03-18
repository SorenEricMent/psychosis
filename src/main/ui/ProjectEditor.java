package ui;

import model.ProjectModel;

import javax.swing.*;

public class ProjectEditor {

    private JLabel title;
    private JLabel name;
    private JPanel projectEditorPanel;
    private JTable capabilityTable;
    private JList list1;
    private JList list2;
    private JList layerList;
    private JButton addVectorButton;
    private JButton addSecurityClassButton;
    private JButton addLayerButton;
    private JLabel numberLayer;
    private JLabel numberModule;

    private ProjectModel bindedProject;

    public ProjectEditor(ProjectModel p) {
        bindedProject = p;
        this.name.setText(p.getName());
        this.numberLayer.setText(String.valueOf(p.getLayers().size()));
        this.numberModule.setText(String.valueOf(p.totalModules()));
    }

    public JPanel getProjectEditorPanel() {
        return projectEditorPanel;
    }
}
