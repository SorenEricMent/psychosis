package ui;

import model.ProjectModel;
import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProjectEditor {

    private JLabel title;
    private JLabel name;
    private JPanel projectEditorPanel;
    private JTable capabilityTable;
    private JList list1;
    private JList list2;
    private JList layerList;
    private JButton addVectorButton;
    private JButton addSecurityClassButton;
    private JButton addLayerButton;
    private JLabel numberLayer;
    private JLabel numberModule;
    private JPanel numberPanel;

    private ProjectModel bindedProject;

    public ProjectEditor(ProjectModel p) {
        bindedProject = p;
        this.name.setText(p.getName());
        this.numberLayer.setText(String.valueOf(p.getLayers().size()));
        this.numberModule.setText(String.valueOf(p.totalModules()));

        initAddLayerBtn();
        initAddSecClassBtn();
        initAddVecBtn();

        updateLayerList();
    }

    public JPanel getProjectEditorPanel() {
        return projectEditorPanel;
    }

    private void initAddLayerBtn() {
        addLayerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddLayerDialog.main(bindedProject);
            }
        });
    }

    private void initAddSecClassBtn() {
        addSecurityClassButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddSecClassDialog.main(bindedProject);
            }
        });
    }

    private void initAddVecBtn() {
        addVectorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddVecDialog.main(bindedProject);
            }
        });
    }

    // EFFECTS: refresh the list of layers on GUI
    private void updateLayerList() {
        DefaultListModel<String> newLayerList = new DefaultListModel<>();
        for (LayerModel l : this.bindedProject.getLayers()) {
            newLayerList.addElement(l.getName());
        }
        layerList.setModel(newLayerList);
    }

    // EFFECTS: rebuild the capability list on GUI
    private void updateCapabilityList() {
        String[] columns = {"Capability", "Status"};
        ArrayList<String[]> data = new ArrayList<>();

        for (ProjectModel.PolicyCapabilities c : ProjectModel.PolicyCapabilities.values()) {
            String[] tmp = new String[2];
            tmp[0] = c.toString();
            tmp[1] = bindedProject.checkCapability(c).toString();
            data.add(tmp);
        }
        capabilityTable = new JTable(data.stream().toArray(String[][]::new), columns);
        capabilityTable.setDefaultEditor(Object.class, null);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        updateCapabilityList();
    }
}
