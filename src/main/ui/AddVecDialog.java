package ui;

import model.policy.AccessVectorModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// EFFECTS: dialog to add a vector(string) to a security class in project's AccessVectorModel definition

public class AddVecDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField secClassName;
    private JTextField vecName;
    private final AccessVectorModel vector;
    private final StatusDisplay sd;
    private final ProjectEditor editor;

    // EFFECTS: init the fields and construct the dialog and bind basic event handlers on OK and cancel button
    public AddVecDialog(AccessVectorModel vector, StatusDisplay sd, ProjectEditor editor) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        this.vector = vector;
        this.sd = sd;
        this.editor = editor;

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

    // EFFECTS: add the vecName to secClassName in the access vector model binded
    private void onOK() {
        vector.addAccessVector(secClassName.getText(), vecName.getText());
        editor.refreshSecClassList();
        sd.modificationHappened();
        dispose();
    }

    // EFFECTS: dispose the dialog
    private void onCancel() {
        dispose();
    }

    // EFFECTS: invoke constructor to create the dialog and display it
    public static void main(AccessVectorModel vector, StatusDisplay sd, ProjectEditor editor) {
        AddVecDialog dialog = new AddVecDialog(vector, sd, editor);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
