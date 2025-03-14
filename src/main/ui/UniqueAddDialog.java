package ui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// All add operations that enforce uniqueness could be achieved with this
// Dialog, it warns for duplication.

// EXPERIMENTAL, NOT USED
public class UniqueAddDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField addName;
    private final String title;
    private final String duplicateInfo;

    // EFFECTS: init the content pane and fields, bind event handler for buttons
    public UniqueAddDialog(String title, String duplicateInfo) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.title = title;
        this.duplicateInfo = duplicateInfo;

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
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // EFFECTS: ok button handler, dispose the dialog
    private void onOK() {
        dispose();
    }

    // EFFECTS: cancel button handler, dispose the dialog
    private void onCancel() {
        dispose();
    }

    // EFFECTS: call constructor to create dialog and display it
    public static void main(String title, String info) {
        UniqueAddDialog dialog = new UniqueAddDialog(title, info);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
