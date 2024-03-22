package ui;

import model.ProjectModel;
import model.exception.DuplicateException;

import javax.swing.*;
import java.awt.event.*;

// The dialog for adding a Layer to a Project, invoked by ProjectEditor

public class AddLayerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane layerName;

    private final ProjectModel bindedProject;
    private final GraphicInterface globalObjects;
    private final ProjectEditor editor;

    //EFFECTS: init dialog content
    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    // EFFECTS: init fields and call initPane to create the dialog, init basic dialog events
    public AddLayerDialog(ProjectModel bindedProject, GraphicInterface globalObjects, ProjectEditor editor) {
        this.bindedProject = bindedProject;
        this.globalObjects = globalObjects;
        this.editor = editor;

        initPane();

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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }



    // EFFECTS: OK button handler
    // MODIFIES: bindedProject
    private void onOK() {
        String name = layerName.getText();
        if (name.isEmpty()) {
            WarningDialog.main("Layer name cannot be empty.");
            dispose();
        }

        try {
            bindedProject.addLayer(name);
            globalObjects.updateLayerTree(bindedProject.getName(), name);
            this.editor.refreshLayerList();
            dispose();
        } catch (DuplicateException e) {
            WarningDialog.main("Duplicated layer name");
        }
    }

    // EFFECTS: cancel button handler, exit the dialog.
    private void onCancel() {
        dispose();
    }

    // EFFECTS: call constructor to init dialog and display the dialog
    public static void main(ProjectModel bindedProject, GraphicInterface globalObjects, ProjectEditor editor) {
        AddLayerDialog dialog = new AddLayerDialog(bindedProject, globalObjects, editor);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
