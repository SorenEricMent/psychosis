package ui;

import model.ProjectModel;
import model.policy.LayerModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// The editor panel for projects
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

    private final ProjectModel bindedProject;
    private final GraphicInterface globalObjects;
    private final ProjectEditor self;


    // EFFECTS: init fields defined, update GUI accordingly and call init functions to init
    // event listeners for buttons, reload the layer list finally
    public ProjectEditor(ProjectModel p, GraphicInterface g) {
        self = this;
        this.bindedProject = p;
        this.globalObjects = g;
        this.name.setText(p.getName());
        this.numberLayer.setText(String.valueOf(p.getLayers().size()));
        this.numberModule.setText(String.valueOf(p.totalModules()));

        initAddLayerBtn();
        initAddSecClassBtn();
        initAddVecBtn();

        refreshLayerList();
    }

    public JPanel getProjectEditorPanel() {
        return projectEditorPanel;
    }

    // EFFECTS: add event listener for add layer button
    private void initAddLayerBtn() {
        addLayerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddLayerDialog.main(bindedProject, globalObjects, self);
            }
        });
    }

    // EFFECTS: add event listener for add security class button
    private void initAddSecClassBtn() {
        addSecurityClassButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddSecClassDialog.main(bindedProject);
            }
        });
    }

    // EFFECTS: add event listener for add vector button
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
    // MODIFIES: the JList for layers on the current project editor(side effect)
    public void refreshLayerList() {
        DefaultListModel<String> newLayerList = new DefaultListModel<>();
        for (LayerModel l : this.bindedProject.getLayers()) {
            newLayerList.addElement(l.getName());
        }
        this.numberLayer.setText(String.valueOf(bindedProject.getLayers().size()));
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
        capabilityTable = new JTable(data.toArray(String[][]::new), columns);
        capabilityTable.setDefaultEditor(Object.class, null);
    }

    // EFFECTS: custom create to update the capabilityTable with the capabilities declared
    // by the project binded to this editor on its(table's) creation
    private void createUIComponents() {
        updateCapabilityList();
    }
}
