package ui;

import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

// The editor panel for a specific layer, one instance for every layer
public class LayerEditor {

    private JLabel title1;
    private JPanel layerEditorPanel;
    private JLabel projectName;
    private JLabel layerName;
    private JLabel title2;
    private JList<String> moduleList;
    private JLabel numberModule;
    private JButton addModuleBtn;
    private final LayerModel layer;
    private final GraphicInterface globalObject;
    private final String project;

    // EFFECTS: create a new editing panel for a layer, update field to it accordingly
    public LayerEditor(LayerModel layer, GraphicInterface globalObject, String project) {
        this.layer = layer;
        this.project = project;
        this.globalObject = globalObject;
        projectName.setText(project);
        layerName.setText(layer.getName());

        initAddModuleBtn();

        JPopupMenu modulePopup = new JPopupMenu("");
        modulePopup.setOpaque(true);
        JMenuItem removeModuleOption = new
                JMenuItem(globalObject.getMainContainer().getReflectiveResource("LayerEditor.popup.remove_module"));

        moduleList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    modulePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        initRemoveModulePopup(removeModuleOption);

        modulePopup.add(removeModuleOption);
        modulePopup.pack();

        reloadModuleList();
    }

    // EFFECTS: init the event handler for module list right-click
    private void initRemoveModulePopup(JMenuItem target) {
        target.addActionListener(actionEvent -> {
            String moduleName = moduleList.getSelectedValue();
            layer.removePolicyModule(moduleName);
            TreePath prevPath = globalObject.getMainContainer().getProjectList().getSelectionPath();
            globalObject.rebuildWholeProjectTree();
            globalObject.getMainContainer().getProjectList().scrollPathToVisible(new TreePath(
                    Arrays.copyOfRange(prevPath.getPath(), 0, prevPath.getPath().length - 1)
            ));
            reloadModuleList();
        });
    }

    // EFFECTS: reload the module list with the modules in the layer
    // MODIFIES: (side-effect) moduleList
    private void reloadModuleList() {
        DefaultListModel<String> newModuleList = new DefaultListModel<>();
        for (PolicyModuleModel p : this.layer.getPolicyModules().values()) {
            newModuleList.addElement(p.getName());
        }
        moduleList.setModel(newModuleList);
        moduleList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                System.out.println(listSelectionEvent);
            }
        });
//        moduleList.repaint();
//        moduleList.revalidate();
        this.numberModule.setText(String.valueOf(newModuleList.size()));
    }

    // EFFECTS: init event handler for add module button, on click - dialog the user with add module dialog
    // and reload module list after dialog disposed
    private void initAddModuleBtn() {
        addModuleBtn.addActionListener(actionEvent -> {
            AddModuleDialog.main(layer, globalObject, project);
            reloadModuleList();
        });
    }

    public JPanel getLayerEditorPanel() {
        return layerEditorPanel;
    }
}
