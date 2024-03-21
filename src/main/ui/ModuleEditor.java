package ui;

import model.policy.PolicyModuleModel;
import model.policy.RuleSetModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;

// The editor panel for modules, consist of editor plane for te, if and fc in a tabbed pane
public class ModuleEditor {

    private JPanel moduleEditorPanel;
    private JLabel title1;
    private JLabel masterName;
    private JLabel moduleName;
    private JLabel title2;
    private JTabbedPane moduleEditorTabs;
    private JButton teAddRuleBtn;
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
        teAddRuleBtnHandler();
    }

    public JPanel getModuleEditorPanel() {
        return moduleEditorPanel;
    }

    private void teAddRuleBtnHandler() {
        teAddRuleBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddRuleDialog.main(bindedModule.getTypeEnf(), new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        return rebuildTeRuleList();
                    }
                }, true);
            }
        });
    }

    private Void rebuildTeRuleList() {
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
        return null;
    }

    private void rebuildCallList() {

    }

    // EFFECTS: custom create hook, rebuild the rule list and call list
    // at the first load
    private void createUIComponents() {
        rebuildTeRuleList();
        rebuildCallList();
    }
}
