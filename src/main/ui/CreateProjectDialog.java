package ui;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;

import javax.swing.*;
import java.awt.event.*;

public class CreateProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane projectCreateBasisTab;
    private JTextField inputProjectNameEmpty;

    private final GraphicInterface globalObjects;

    private void initPane() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    public CreateProjectDialog(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        initPane();
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
        if (inputProjectNameEmpty.getText().equals("")) {
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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(GraphicInterface globalObjects) {
        CreateProjectDialog dialog = new CreateProjectDialog(globalObjects);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
