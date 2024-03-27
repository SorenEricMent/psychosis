package ui;

import model.exception.NotFoundException;
import model.policy.InterfaceModel;
import model.policy.InterfaceSetModel;
import model.policy.PolicyModuleModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Dialog to add a new empty interface
public class AddInterfaceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField interfaceName;
    private final StatusDisplay sd;
    private final InterfaceSetModel interfaceSet;
    private final InterfaceSetModel globalSet;
    private final ModuleEditor editor;
    private final String owner;

    // EFFECTS: init the add interface dialog and basic listeners
    public AddInterfaceDialog(StatusDisplay sd, InterfaceSetModel interfaceSet, InterfaceSetModel globalSet,
                              String owner, ModuleEditor editor) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.interfaceSet = interfaceSet;
        this.globalSet = globalSet;
        this.sd = sd;
        this.owner = owner;
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

    // EFFECTS: check if interface with such name exists in project, if so, alert the user,
    // if not so, create an empty interface, add it to the interfaceSet given and the globalSet
    // MODIFIES: interfaceSet, globalSet
    private void onOK() {
        // check duplicate on globalSet first
        try {
            this.globalSet.getInterface(interfaceName.getText());
            WarningDialog.main("Interface with this name already exists within project.");

        } catch (NotFoundException e) {
            // could continue
            InterfaceModel toAddInf = new InterfaceModel(interfaceName.getText(), owner,true);
            interfaceSet.addInterface(toAddInf);
            globalSet.addInterface(toAddInf);
            // Update GUI
            sd.modificationHappened();
            editor.rebuildIfList();
            dispose();
        }
    }

    // EFFECTS: close the dialog for cancel button
    private void onCancel() {
        dispose();
    }

    // EFFECTS: construct the add interface dialog and display it
    public static void main(StatusDisplay sd, InterfaceSetModel interfaceSet, InterfaceSetModel globalSet,
                            String owner, ModuleEditor editor) {
        AddInterfaceDialog dialog = new AddInterfaceDialog(sd, interfaceSet, globalSet, owner, editor);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
