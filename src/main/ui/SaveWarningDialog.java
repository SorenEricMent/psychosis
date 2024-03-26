package ui;

import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.*;

public class SaveWarningDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFileChooser fileChooser;
    private final GraphicInterface globalObjects;

    public SaveWarningDialog(StatusDisplay sd, GraphicInterface globalObjects) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.globalObjects = globalObjects;
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
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        new SaveWorkspaceDialog(this.globalObjects);
        dispose();
        System.exit(0);
    }

    private void onCancel() {
        System.exit(0);
        dispose();
    }

    // EFFECTS: call constructor to create dialog and display it
    public static void main(StatusDisplay sd, GraphicInterface globalObjects) {
        SaveWarningDialog dialog = new SaveWarningDialog(sd, globalObjects);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
