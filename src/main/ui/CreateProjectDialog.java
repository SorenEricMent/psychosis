package ui;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;

import javax.swing.*;
import java.awt.event.*;

// The dialog to create a new project, could be empty or from refpolicy or Psychosis format
public class CreateProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane projectCreateBasisTab;
    private JTextField inputProjectNameEmpty;

    private final GraphicInterface globalObjects;

    // EFFECTS: init the content pane and its default button
    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    // EFFECTS: create the dialog with globalObjects the shared state, add actionListeners for buttons
    public CreateProjectDialog(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
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

    // EFFECTS: warn user on empty/duplicated project name, otherwise add the project to the globalObjects
    // and update the project tree ui
    // MODIFIES: this, (side-effect) globalObjects.getProjectTree, globalObjects
    private void onOK() {
        if (inputProjectNameEmpty.getText().isEmpty()) {
            WarningDialog.main("Name must not be empty");
        } else if (globalObjects.isProjectNameDuplicated(inputProjectNameEmpty.getText())) {
            WarningDialog.main("Duplicated project name");
        } else {
            ProjectModel project = new TempProjectModel(inputProjectNameEmpty.getText());
            this.globalObjects.getLoadedProjects().add(
                    new Pair<>(project, new TrackerModel())
            );
            this.globalObjects.updateProjectTree(project);
            // add your code here
            dispose();
        }
    }

    // EFFECTS: dispose the dialog
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    // EFFECTS: invoke constructor to create the dialog and display it
    public static void main(GraphicInterface globalObjects) {
        CreateProjectDialog dialog = new CreateProjectDialog(globalObjects);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
