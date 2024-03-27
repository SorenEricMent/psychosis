package ui;

import model.exception.DuplicateException;
import model.policy.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Dialog to add a new empty module
public class AddModuleDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField moduleName;
    private final LayerModel layer;
    private final GraphicInterface globalObject;
    private final String projectName;


    //EFFECTS: init dialog content
    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    public AddModuleDialog(LayerModel layer, GraphicInterface globalObject, String projectName) {
        initPane();
        this.layer = layer;
        this.globalObject = globalObject;
        this.projectName = projectName;
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

    // EFFECTS: try to add new empty module, do not dispose self if duplicate
    private void onOK() {
        try {
            String name = moduleName.getText();
            layer.addPolicyModule(new PolicyModuleModel(name,
                    new TypeEnfModel(name),
                    new InterfaceSetModel(),
                    new FileContextModel()));
            this.globalObject.updateModuleTree(projectName, layer.getName(), name);
            dispose();
        } catch (DuplicateException e) {
            WarningDialog.main("Duplicated name. " + e.getMessage());
        }
    }

    // EFFECTS: dispose the dialog
    private void onCancel() {
        dispose();
    }

    // EFFECTS: invoke constructor to create the dialog and display it
    public static void main(LayerModel layer, GraphicInterface globalObject, String proj) {
        AddModuleDialog dialog = new AddModuleDialog(layer, globalObject, proj);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
