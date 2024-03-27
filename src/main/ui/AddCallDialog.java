package ui;

import model.exception.NotFoundException;
import model.policy.InterfaceModel;
import model.policy.InterfaceSetModel;
import model.policy.TypeEnfModel;
import ui.async.Debouncer;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

// The class of adding interface call dialog, ask for name and params
// provide simple name-based search and preview.

public class AddCallDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lookupStatus;
    private JComboBox<String> interfaceCombo;
    private JButton addParamBtn;
    private JTextField paramData;
    private JLabel paramList;
    private JLabel paramCount;
    private JTextArea previewPane;
    private final InterfaceSetModel globalSet;
    private final StatusDisplay sd;
    private final ModuleEditor editor;
    private final TypeEnfModel target;
    private int callCount;
    private ArrayList<String> addParams;

    // EFFECTS: init the add call dialog's pane and add basic event listeners, with call count at 1 and no params.
    public AddCallDialog(StatusDisplay sd, InterfaceSetModel globalSet, ModuleEditor editor, TypeEnfModel target) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.sd = sd;
        this.globalSet = globalSet;
        this.editor = editor;
        this.target = target;
        this.callCount = 1;
        this.addParams = new ArrayList<>();

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        initAddParamBtn();
        initSearchHandler();
        interfaceCombo.addActionListener(actionEvent -> previewHandler());

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // EFFECTS: add the interface call,
    // notice that there is no if interface exists check as it is not syntactically wrong
    // MODIFIES: (side-effect) previewPane, target, editor
    private void onOK() {
        target.addInterfaceCall(interfaceCombo.getEditor().getItem().toString(), addParams.toArray(String[]::new));
        editor.rebuildCallList();
        try {
            previewPane.setText(globalSet
                    .getInterface(interfaceCombo.getEditor().getItem().toString())
                    .call(addParams.stream().toArray(String[]::new))
                    .toString());
        } catch (NotFoundException e) {
            //Do nothing
        }
        sd.modificationHappened();
        dispose();
    }

    // EFFECTS: quit the dialog
    private void onCancel() {
        dispose();
    }

    // EFFECTS: create the event handler for add param button
    // on click: update the list of params and increase the param counter.
    private void initAddParamBtn() {
        addParamBtn.addActionListener(actionEvent -> {
            addParams.add(paramData.getText());
            callCount++;
            paramCount.setText("$" + callCount);
            paramList.setText(String.join(", ", addParams));
        });
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
        InterfaceModel inf;
        try {
            inf = globalSet.getInterface(interfaceCombo.getSelectedItem().toString());
            lookupStatus.setText(inf.getOwner());
            // TODO: update table
        } catch (NotFoundException e) {
            // Clear the
        }
    }

    // EFFECTS: call the constructor to create the dialog and display it at the middle
    public static void main(StatusDisplay sd, InterfaceSetModel globalSet, ModuleEditor editor, TypeEnfModel target) {
        AddCallDialog dialog = new AddCallDialog(sd, globalSet, editor, target);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
