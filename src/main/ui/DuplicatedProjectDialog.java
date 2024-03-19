package ui;

import model.Pair;
import model.ProjectModel;
import model.TrackerModel;

import javax.swing.*;
import java.awt.event.*;

public class DuplicatedProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane newName;
    private Pair<ProjectModel, TrackerModel> projectToAdd;
    private GraphicInterface globalObjects;


    public DuplicatedProjectDialog(Pair<ProjectModel, TrackerModel> p, GraphicInterface globalObjects) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.projectToAdd = p;
        this.globalObjects = globalObjects;
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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

    private void onCancel() {
        // add your code here if necessary
        globalObjects.getMainContainer().disableProgressBar();
        dispose();
    }

    public static void main(Pair<ProjectModel, TrackerModel> p, GraphicInterface globalObjects) {
        DuplicatedProjectDialog dialog = new DuplicatedProjectDialog(p, globalObjects);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
