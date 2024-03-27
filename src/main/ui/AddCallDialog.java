package ui;

import model.policy.InterfaceModel;
import model.policy.InterfaceSetModel;
import ui.async.Debouncer;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

// The class of adding interface call dialog, ask for name and params
// provide simple name-based search and preview.

public class AddCallDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lookupStatus;
    private JComboBox<String> interfaceCombo;
    private JTable previewTable;
    private JButton button1;
    private JTextField textField1;
    private JLabel paramList;
    private final InterfaceSetModel globalSet;

    // EFFECTS: init the add call dialog's pane and add basic event listeners.
    public AddCallDialog(StatusDisplay sd, InterfaceSetModel globalSet) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.globalSet = globalSet;
        buttonOK.addActionListener(e -> onOK());

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

        initSearchHandler();
        interfaceCombo.addActionListener(actionEvent -> previewHandler());

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // EFFECTS: add the interface call,
    // notice that there is no if interface exists check as it is not syntactically wrong
    private void onOK() {
        // add your code here
        dispose();
    }

    // EFFECTS: quit the dialog
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    // EFFECTS: add the event listener for updating the interface list on type, debounced.
    private void initSearchHandler() {
        Debouncer<Void> searchDebouncer = new Debouncer<Void>(() -> {
            String name = interfaceCombo.getEditor().getItem().toString();
            DefaultComboBoxModel<String> newOptions = new DefaultComboBoxModel<String>();
            newOptions.addElement(name);
            newOptions.addAll(globalSet.getInterfaces().stream()
                    .filter(x -> x.getName().startsWith(name) && !x.getName().equals(name))
                    .map(InterfaceModel::getName).collect(Collectors.toList()));
            interfaceCombo.setModel(newOptions);
            if (newOptions.getSize() >= 2) {
                interfaceCombo.showPopup();
                lookupStatus.setText("...");
            } else {
                previewHandler(); // Selection is certain
            }
            return null;
        }, 180);

        interfaceCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                interfaceCombo.hidePopup();
                searchDebouncer.fire();
            }
        });
    }

    // REQUIRES: a selection event happened on interfaceCombo
    // EFFECTS: update preview with params and selected interface, empty if interface not found
    // MODIFIES: (side effect) previewTable
    private void previewHandler() {
        InterfaceModel inf = globalSet.getInterface(interfaceCombo.getSelectedItem().toString());
        lookupStatus.setText(inf.getOwner());
        // TODO: update table
    }

    // EFFECTS: call the constructor to create the dialog and display it at the middle
    public static void main(StatusDisplay sd, InterfaceSetModel globalSet) {
        AddCallDialog dialog = new AddCallDialog(sd, globalSet);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
