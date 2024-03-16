package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class SaveWorkspaceDialog extends JDialog {
    private JPanel contentPane;
    private JFileChooser saveWorkspaceChooser;

    public SaveWorkspaceDialog() {
        setContentPane(contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        saveWorkspaceChooser.setDialogTitle("Saving all states to a workspace.json");
        saveWorkspaceChooser.setSelectedFile(new File("workspace.json"));

        int userSelection = saveWorkspaceChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveWorkspaceChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main() {
        SaveWorkspaceDialog dialog = new SaveWorkspaceDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

}
