package ui;

import model.policy.RuleAddable;

import javax.swing.*;
import java.awt.event.*;
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

    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    public AddRuleDialog(RuleAddable target, Callable<Void> callback, boolean varCheck) {
        initPane();
        this.target = target;
        this.callback = callback;
        this.varCheck = varCheck;
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

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

    public static void main(RuleAddable target, Callable<Void> callback, boolean varCheck) {
        AddRuleDialog dialog = new AddRuleDialog(target, callback, varCheck);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
