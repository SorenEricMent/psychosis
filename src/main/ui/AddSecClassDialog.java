package ui;

import model.ProjectModel;
import model.exception.DuplicateException;
import model.policy.AccessVectorModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Dialog to add a new security class
public class AddSecClassDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField secClassName;
    private final AccessVectorModel vector;
    private final StatusDisplay sd;
    private final ProjectEditor editor;

    // EFFECTS: init the fields and create the dialog's content pane, bind event handler for buttons
    public AddSecClassDialog(AccessVectorModel vector, StatusDisplay sd, ProjectEditor editor) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.vector = vector;
        this.sd = sd;
        this.editor = editor;

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

    // EFFECTS: ok handler, add the security class-vec and dispose the dialog, warn the user on exception
    private void onOK() {
        try {
            vector.addSecurityClass(secClassName.getText());
            sd.modificationHappened();
            editor.refreshSecClassList();
            dispose();
        } catch (DuplicateException e) {
            WarningDialog.main(e.getMessage());
        }
    }

    // EFFECTS: dispose the dialog
    private void onCancel() {
        dispose();
    }

    // EFFECTS: invoke constructor to create the dialog and display it
    public static void main(AccessVectorModel vector, StatusDisplay sd, ProjectEditor editor) {
        AddSecClassDialog dialog = new AddSecClassDialog(vector, sd, editor);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
