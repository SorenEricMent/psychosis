package ui;

import model.Pair;
import model.ProjectModel;
import model.policy.*;
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
    private JList<String> interfaceList;
    private JButton exportTeBtn;
    private JButton exportTeCompBtn;
    private JButton addInterface;
    private JButton addStatementIf;
    private JList<String> callList;
    private JButton infImportBtn;
    private JTable infRuleTable;
    private final PolicyModuleModel bindedModule;
    private final String layer;
    private final String project;
    private final AccessVectorModel accessVector;
    private final StatusDisplay statusDisplay;
    private final InterfaceSetModel globalInfSet;
    private final ProjectModel proj;
    private final ModuleEditor self = this;

    // EFFECTS: create this new module editor panel from a module and its belonging
    public ModuleEditor(StatusDisplay sd, PolicyModuleModel p, AccessVectorModel av,
                        InterfaceSetModel ifs, String layer, String project, ProjectModel proj) {
        this.statusDisplay = sd;
        this.bindedModule = p;
        this.accessVector = av;
        this.globalInfSet = ifs;
        this.layer = layer;
        this.project = project;
        this.proj = proj;
        moduleName.setText(bindedModule.getName());
        masterName.setText(layer + "." + project);
        teAddRuleBtnHandler();
        teAddCallBtnHandler();
        ifAddEmptyIfHandler();
        rebuildTeRuleList();
        rebuildCallList();
        rebuildIfList();
        initExportTeBtn();
        interfaceCallAssociation();
        initIfAddRuleBtn();
        initImportIfBtn();
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

    // EFFECTS: init click handler for add interface button in interface tab
    private void ifAddEmptyIfHandler() {
        addInterface.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddInterfaceDialog.main(statusDisplay, bindedModule.getInterfaceSet(), globalInfSet,
                        bindedModule.getName(), self);
            }
        });
    }

    // EFFECTS: init click handler for add call button in TE tab
    private void teAddCallBtnHandler() {
        addCallBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddCallDialog.main(statusDisplay, globalInfSet, self, bindedModule.getTypeEnf());
            }
        });

    }

    // EFFECTS: rebuild the list showing all first-order statements
    public Void rebuildTeRuleList() {
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

    // EFFECTS: rebuild the list showing the interface calls from the interface
    public void rebuildCallList() {
        DefaultListModel<String> res = new DefaultListModel<String>();
        for (Pair<String, String[]> call : this.bindedModule.getTypeEnf().getInterfaceCall()) {
            res.addElement(call.getFirst() + "(" + String.join(",", call.getSecond()) + ")");
        }
        callList.setModel(res);
    }

    // EFFECTS: bind the event handler to make the right a preview of clicked interface's rules
    // MODIFIES: statementList
    public void interfaceCallAssociation() {
        interfaceList.addListSelectionListener(listSelectionEvent -> {
            if (interfaceList.getSelectedValue() != null) {
                rebuildIfCallList(bindedModule.getInterface(interfaceList.getSelectedValue()));
            } else {
                rebuildIfCallList(new InterfaceModel("_internal_clear", "_internal_clear", false));
            }
        });
    }

    // EFFECTS: rebuild the call table preview with an InterfaceModel
    public void rebuildIfCallList(InterfaceModel inf) {
        DefaultTableModel res = new DefaultTableModel(0, 5);
        for (RuleSetModel r : inf.getRuleSetModels()) {
            String[] data = new String[5];
            data[0] = r.getRuleType().toString();
            data[1] = r.getSourceContext();
            data[2] = r.getTargetContext();
            data[3] = r.getTargetClass();
            data[4] = r.getActions().toString();
            res.addRow(data);
        }
        infRuleTable.setModel(res);
    }

    // EFFECTS: rebuild the interface list from the binded module, with InterfaceSetModule's order
    public void rebuildIfList() {
        DefaultListModel<String> res = new DefaultListModel<String>();
        for (InterfaceModel call : this.bindedModule.getInterfaceSet().getInterfaces()) {
            res.addElement(call.getName());
        }
        interfaceList.setModel(res);
    }

    // EFFECTS: init the event handler for add rule button in if tab.
    // If none selected, warn the user, else call AddRuleDialog
    private void initIfAddRuleBtn() {
        addStatementIf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (interfaceList.getSelectedIndex() == -1) {
                    WarningDialog.main("No interface selected.");
                } else {
                    InterfaceModel addTarget = bindedModule.getInterface(interfaceList.getSelectedValue());
                    AddRuleDialog.main(statusDisplay,
                            addTarget,
                            new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    statusDisplay.modificationHappened();
                                    rebuildIfCallList(addTarget);
                                    return null;
                                }
                            }, accessVector, false);
                }
            }
        });
    }

    // EFFECTS: create the event handler for export type enf file button and bind it
    private void initExportTeBtn() {
        exportTeBtn.addActionListener(actionEvent ->
                new ExportTEDialog(statusDisplay, globalInfSet, bindedModule.getTypeEnf(), false));
        exportTeCompBtn.addActionListener(actionEvent ->
                new ExportTEDialog(statusDisplay, globalInfSet, bindedModule.getTypeEnf(), true));
    }

    // EFFECTS: create and bind the event handler for import interface button
    private void initImportIfBtn() {
        infImportBtn.addActionListener(actionEvent -> {
            new ImportIFDialog(statusDisplay, bindedModule, bindedModule.getInterfaceSet(), proj, self);
        });
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
    }
}
