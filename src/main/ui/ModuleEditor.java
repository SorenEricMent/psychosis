package ui;

import model.policy.AccessVectorModel;
import model.policy.PolicyModuleModel;
import model.policy.RuleSetModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JList statementList;
    private JButton addInterface;
    private JButton addStatementIf;
    private final PolicyModuleModel bindedModule;
    private String layer;
    private String project;
    private AccessVectorModel accessVector;
    private StatusDisplay statusDisplay;

    // EFFECTS: create this new module editor panel from a module and its belonging
    public ModuleEditor(StatusDisplay sd, PolicyModuleModel p, AccessVectorModel av, String layer, String project) {
        this.statusDisplay = sd;
        this.bindedModule = p;
        this.accessVector = av;
        this.layer = layer;
        this.project = project;
        moduleName.setText(bindedModule.getName());
        masterName.setText(layer + "." + project);
        teAddRuleBtnHandler();
        teAddCallBtnHandler();
    }

    public JPanel getModuleEditorPanel() {
        return moduleEditorPanel;
    }

    // EFFECTS: init click handler for add rule button in TE tab
    private void teAddRuleBtnHandler() {
        teAddRuleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddRuleDialog.main(statusDisplay, bindedModule.getTypeEnf(), new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        return rebuildTeRuleList();
                    }
                }, accessVector, true);
            }
        });
    }

    // EFFECTS: init click handler for add call button in TE tab
    private void teAddCallBtnHandler() {
        addCallBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddCallDialog.main();
            }
        });

    }

    private Void rebuildTeRuleList() {
        String[] columns = {"Type", "Source", "Target", "Class", "Actions"};
        DefaultTableModel res = new DefaultTableModel(columns, 0);

        for (RuleSetModel r : bindedModule.getTypeEnf().getStatementsFO()) {
            String[] data = new String[5];
            data[0] = r.getRuleType().toString();
            data[1] = r.getSourceContext();
            data[2] = r.getTargetContext();
            data[3] = r.getTargetClass();
            data[4] = r.getActions().toString();
            res.addRow(data);
        }

        ruleTable.setModel(res);
//        ruleTable.setDefaultEditor(Object.class, null);
        return null;
    }

    private void rebuildCallList() {

    }

    // EFFECTS: custom create hook, rebuild the rule list and call list
    // at the first load
    private void createUIComponents() {
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
        rebuildCallList();
    }
}
