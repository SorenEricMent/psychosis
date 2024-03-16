package ui;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

public class CreateProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane projectCreateBasisTab;
    private JTextField inputProjectNameTextField;

    private GraphicInterface globalObjects;

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
        this.globalObjects.getLoadedProjects().add(
                new Pair<>(new TempProjectModel("test", true), new TrackerModel())
        );
        this.globalObjects.rebuildProjectTree();
        // add your code here
        dispose();
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
