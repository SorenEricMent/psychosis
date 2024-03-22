package ui;

import model.policy.AccessVectorModel;
import model.policy.RuleAddable;

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
    private RuleAddable target;
    private Callable<Void> callback;
    private boolean varCheck;
    private Debouncer<Void> actionFieldDebouncer;
    private AccessVectorModel accessVector;

    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    public AddRuleDialog(RuleAddable target, Callable<Void> callback, AccessVectorModel av, boolean varCheck) {
        initPane();
        this.target = target;
        this.callback = callback;
        this.varCheck = varCheck;
        this.accessVector = av;
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
        classFieldListener();

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

    private void classFieldListener() {
        actionFieldDebouncer = new Debouncer<Void>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                HashSet<String> actions = accessVector.getAccessVector().get(classField.getText());
                DefaultComboBoxModel<String> vecList = new DefaultComboBoxModel<>();
                if (actions == null) {
                    // No action defined for the current state's class name
                    vecList.addElement("Class Not Found");
                } else {
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

    private void onOK() {
        // to remove
        try {
            callback.call();
        } catch (Exception e) {
            WarningDialog.main(e.getMessage());
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(RuleAddable target, Callable<Void> callback, AccessVectorModel av, boolean varCheck) {
        AddRuleDialog dialog = new AddRuleDialog(target, callback, av, varCheck);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
