package ui;

import model.policy.PolicyModuleModel;

import javax.swing.*;

public class ModuleEditor {

    private JPanel moduleEditorPanel;
    private JLabel title1;
    private JLabel masterName;
    private JLabel moduleName;
    private JLabel title2;

    // EFFECTS: create this new module editor panel from a module and its belonging
    public ModuleEditor(PolicyModuleModel p, String layer, String project) {
        this.moduleName.setText(p.getName());
        this.masterName.setText(layer + "." + project);
    }

    public JPanel getModuleEditorPanel() {
        return moduleEditorPanel;
    }
}
