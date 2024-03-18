package ui;

import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// The editor panel for a specific layer, one instance for every layer
public class LayerEditor {

    private JLabel title1;
    private JPanel layerEditorPanel;
    private JLabel projectName;
    private JLabel layerName;
    private JLabel title2;
    private JList moduleList;
    private JLabel numberModule;
    private LayerModel layer;

    // EFFECTS: create a new editing panel for a layer, update field to it accordingly
    public LayerEditor(LayerModel layer, String project) {
        this.layer = layer;
        projectName.setText(project);
        layerName.setText(layer.getName());

        JPopupMenu modulePopup = new JPopupMenu("");
        modulePopup.setOpaque(true);
        JMenuItem removeModuleOption = new JMenuItem("Remove Module");
        removeModuleOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        moduleList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("modulelist click");
                if (e.getButton() == 3) {
                    modulePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        modulePopup.add(removeModuleOption);
        modulePopup.pack();

        reloadModuleList();
    }

    // EFFECTS: for init the module list with modules in layer
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

    public JPanel getLayerEditorPanel() {
        return layerEditorPanel;
    }
}
