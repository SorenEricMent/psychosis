package ui;

import model.policy.PolicyModuleModel;

import javax.swing.*;
import java.awt.*;

// The editor panel for modules, consist of editor plane for te, if and fc in a tabbed pane
public class ModuleEditor {

    private JPanel moduleEditorPanel;
    private JLabel title1;
    private JLabel masterName;
    private JLabel moduleName;
    private JLabel title2;
    private JList list1;
    private JTabbedPane moduleEditorTabs;
    private JButton exportTeButton;

    // EFFECTS: create this new module editor panel from a module and its belonging
    public ModuleEditor(PolicyModuleModel p, String layer, String project) {
        this.moduleName.setText(p.getName());
        this.masterName.setText(layer + "." + project);
    }

    public JPanel getModuleEditorPanel() {
        return moduleEditorPanel;
    }

}
