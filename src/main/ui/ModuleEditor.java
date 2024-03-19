package ui;

import model.policy.PolicyModuleModel;
import model.policy.RuleSetModel;

import javax.swing.*;
import java.util.ArrayList;

// The editor panel for modules, consist of editor plane for te, if and fc in a tabbed pane
public class ModuleEditor {

    private JPanel moduleEditorPanel;
    private JLabel title1;
    private JLabel masterName;
    private JLabel moduleName;
    private JLabel title2;
    private JTabbedPane moduleEditorTabs;
    private JButton addRuleBtn;
    private JButton addCallBtn;
    private JTable ruleTable;
    private JList interfaceList;
    private JButton exportTeBtn;
    private JButton exportTeCompBtn;
    private final PolicyModuleModel bindedModule;
    private String layer;
    private String project;

    // EFFECTS: create this new module editor panel from a module and its belonging
    public ModuleEditor(PolicyModuleModel p, String layer, String project) {
        this.bindedModule = p;
        this.layer = layer;
        this.project = project;
        moduleName.setText(bindedModule.getName());
        masterName.setText(layer + "." + project);
    }

    public JPanel getModuleEditorPanel() {
        return moduleEditorPanel;
    }

    private void rebuildRuleList() {
        String[] columns = {"Type", "Source", "Target", "Class", "Actions"};
        ArrayList<String[]> data = new ArrayList<>();

        for (RuleSetModel r : bindedModule.getTypeEnf().getStatementsFO()) {
            data.add(new String[5]);
            data.get(data.size() - 1)[0] = r.getRuleType().toString();
            data.get(data.size() - 1)[1] = r.getSourceContext();
            data.get(data.size() - 1)[2] = r.getTargetContext();
            data.get(data.size() - 1)[3] = r.getTargetClass();
            data.get(data.size() - 1)[4] = r.getActions().toString();
        }

        ruleTable = new JTable(data.toArray(String[][]::new), columns);
        ruleTable.setDefaultEditor(Object.class, null);
    }

    private void rebuildCallList() {

    }

    // EFFECTS: custom create hook, rebuild the rule list and call list
    // at the first load
    private void createUIComponents() {
        rebuildRuleList();
        rebuildCallList();
    }
}
