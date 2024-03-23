package ui;

import model.policy.AccessVectorModel;
import model.policy.RuleAddable;
import model.policy.RuleSetModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.concurrent.Callable;

// The general dialog to add a rule to te or if
public class AddRuleDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox ruleTypeCombo;
    private JTextField sourceField;
    private JTextField classField;
    private JTextField targetField;
    private JButton addActionBtn;
    private JComboBox actionCombo;
    private JTextPane ruleVisual;
    private final RuleAddable target;
    private final Callable<Void> callback;
    private final boolean varCheck;
    private Debouncer<Void> actionFieldDebouncer;
    private Debouncer<Void> ruleTextDebouncer;
    private final AccessVectorModel accessVector;
    private RuleSetModel newRule;
    private final HashSet<String> actions;
    private final StatusDisplay statusDisplay;

    // EFFECTS: init content pane
    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    // EFFECTS: init the dialog with empty actions and params given
    public AddRuleDialog(StatusDisplay sd,
                         RuleAddable target, Callable<Void> callback, AccessVectorModel av, boolean varCheck) {
        initPane();
        this.statusDisplay = sd;
        this.target = target;
        this.callback = callback;
        this.varCheck = varCheck;
        this.accessVector = av;
        this.actions = new HashSet<>();

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
        updateRuleTextListener();
        classFieldActionListener();
        initAddActionBtn();

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // EFFECTS: bind event handler to update the preview on change, debounced
    private void updateRuleTextListener() {
        ruleTextDebouncer = new Debouncer<Void>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ruleVisual.setText(ruleFieldToPreview(ruleTypeCombo.getSelectedItem().toString(), sourceField.getText(),
                        targetField.getText(), classField.getText(), actions));
                okButtonUpdateStatus();
                return null;
            }
        }, 200);
        KeyAdapter ruleTextUpdateAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                ruleTextDebouncer.fire();
            }
        };
        sourceField.addKeyListener(ruleTextUpdateAdapter);
        classField.addKeyListener(ruleTextUpdateAdapter);
        targetField.addKeyListener(ruleTextUpdateAdapter);
    }

    // EFFECTS: convert the fields in the dialog to the preview of the rule
    private String ruleFieldToPreview(String ruleType, String source,
                                      String target, String classStr, HashSet<String> actions) {
        String tmp = actions.toString();
        return ruleType + " "
                + (source.isEmpty() ? "[Source]" : source) + " "
                + (target.isEmpty() ? "[Target]" : target) + ":"
                + (classStr.isEmpty() ? "[Class]" : classStr) + " "
                + (actions.isEmpty() ? "{ Actions }" : "{ "
                + tmp.substring(1).substring(0, tmp.length() - 2) + " };");
    }

    // EFFECTS: disable the ok button if any field is empty and vice versa
    private void okButtonUpdateStatus() {
        buttonOK.setEnabled(!sourceField.getText().isEmpty() && !targetField.getText().isEmpty()
                && !classField.getText().isEmpty() && !actions.isEmpty());
    }

    // EFFECTS: create and bind the debounced action listener for updating list of actions by class field
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void classFieldActionListener() {
        actionFieldDebouncer = new Debouncer<Void>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                HashSet<String> actions = accessVector.getAccessVector().get(classField.getText());
                DefaultComboBoxModel<String> vecList = new DefaultComboBoxModel<>();
                if (actions == null) {
                    // No action defined for the current state's class name
                    addActionBtn.setEnabled(false);
                    vecList.addElement("Class Not Found");
                } else {
                    addActionBtn.setEnabled(true);
                    for (String s : actions) {
                        vecList.addElement(s);
                    }
                }
                actionCombo.setModel(vecList);
                return null;
            }
        }, 300);
        classField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                actionFieldDebouncer.fire();
            }
        });
    }

    // EFFECTS: init the event handler for clicking the add action button
    private void initAddActionBtn() {
        addActionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                actions.add(actionCombo.getSelectedItem().toString());
                ruleVisual.setText(ruleFieldToPreview(ruleTypeCombo.getSelectedItem().toString(), sourceField.getText(),
                        targetField.getText(), classField.getText(), actions));
                okButtonUpdateStatus();
            }
        });
    }

    // EFFECTS: function to be fired on ok click (want to add the current rule)
    private void onOK() {
        // to remove
        try {
            target.addStatement(new RuleSetModel(
                    RuleSetModel.toRuleType(ruleTypeCombo.getSelectedItem().toString()),
                    sourceField.getText(),
                    targetField.getText(),
                    classField.getText(),
                    actions));
            callback.call();
            statusDisplay.modificationHappened();
        } catch (Exception e) {
            WarningDialog.main(e.getMessage());
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(StatusDisplay sd,
                            RuleAddable target, Callable<Void> callback, AccessVectorModel av, boolean varCheck) {
        AddRuleDialog dialog = new AddRuleDialog(sd, target, callback, av, varCheck);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
