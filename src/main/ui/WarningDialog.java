package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The class to create a simple, centered warning dialog
public class WarningDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel warningInfo;

    // EFFECTS: create the dialog with info as the content
    public WarningDialog(String info) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        warningInfo.setText(info);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    // EFFECTS: OK button click handler, do nothing
    private void onOK() {
        // add your code here
        dispose();
    }

    // EFFECTS: create a new warning dialog, call constructor and do basic sizing
    public static void main(String info) {
        WarningDialog dialog = new WarningDialog(info);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
