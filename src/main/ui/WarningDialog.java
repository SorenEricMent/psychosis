package ui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Callable;

// The class to create a simple, centered warning dialog
public class WarningDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel warningInfo;
    private JButton buttonCancel;
    private Callable<Void> ok;

    // EFFECTS: create the dialog with info as the content
    public WarningDialog(String info) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onCancel());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });


        warningInfo.setText(info);
    }

    // EFFECTS: constructor for with-callback call
    public WarningDialog(String info, Callable<Void> ok) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        warningInfo.setText(info);
        this.ok = ok;

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // EFFECTS: OK button click handler, call callback if exists
    private void onOK() {
        if (this.ok != null) {
            try {
                ok.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        dispose();
    }

    // EFFECTS: quit the dialog
    private void onCancel() {
        dispose();
    }

    // EFFECTS: create a new warning dialog, call constructor and do basic sizing
    public static void main(String info) {
        WarningDialog dialog = new WarningDialog(info);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void main(String info, Callable<Void> ok) {
        WarningDialog dialog = new WarningDialog(info, ok);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
