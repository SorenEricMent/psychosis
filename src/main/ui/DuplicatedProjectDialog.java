package ui;

import model.Pair;
import model.ProjectModel;
import model.TrackerModel;

import javax.swing.*;
import java.awt.event.*;

// Dialog to ask user to input a new name for the project on duplicate
public class DuplicatedProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane newName;
    private final Pair<ProjectModel, TrackerModel> projectToAdd;
    private final GraphicInterface globalObjects;

    //EFFECTS: init dialog content
    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    // EFFECTS: create event binding for buttons
    private void initButtonEvent() {
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
    }

    // EFFECTS: init fields and call initPane to create the dialog, call initButtonEvent to create event
    // binding for buttons, init basic dialog events
    public DuplicatedProjectDialog(Pair<ProjectModel, TrackerModel> p, GraphicInterface globalObjects) {
        initPane();
        this.projectToAdd = p;
        this.globalObjects = globalObjects;
        initButtonEvent();
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

    // EFFECTS: handler for OK button, warn user if the name given is still a duplication,
    // otherwise add the project, update the gui, disable progress bar and exit.
    private void onOK() {
        // add your code here
        if (globalObjects.isProjectNameDuplicated(newName.getText())) {
            WarningDialog.main("Still duplicated.");
        } else {
            projectToAdd.getFirst().setName(newName.getText());
            globalObjects.getLoadedProjects().add(projectToAdd);
            globalObjects.updateProjectTree(projectToAdd.getFirst());
            globalObjects.getMainContainer().disableProgressBar();
            dispose();
        }
    }

    // EFFECTS: handler for cancel button, disable progress bar and exit
    private void onCancel() {
        // add your code here if necessary
        globalObjects.getMainContainer().disableProgressBar();
        dispose();
    }

    // EFFECTS: call constructor to init dialog and display the dialog
    public static void main(Pair<ProjectModel, TrackerModel> p, GraphicInterface globalObjects) {
        DuplicatedProjectDialog dialog = new DuplicatedProjectDialog(p, globalObjects);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
