package ui;

import model.ProjectModel;
import model.exception.SyntaxParseException;
import model.policy.LayerModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

// The editor panel for projects
public class ProjectEditor {

    private JLabel title;
    private JLabel name;
    private JPanel projectEditorPanel;
    private JTable capabilityTable;
    private JList<String> secClassList;
    private JList<String> vecList;
    private JList<String> layerList;
    private JButton addVectorButton;
    private JButton addSecClassButton;
    private JButton addLayerButton;
    private JLabel numberLayer;
    private JLabel numberModule;
    private JPanel numberPanel;
    private JButton loadBuiltinBtn;
    private JButton loadExVecBtn;

    private final ProjectModel bindedProject;
    private final StatusDisplay sd;
    private final GraphicInterface globalObjects;
    private final ProjectEditor self;


    // EFFECTS: init fields defined, update GUI accordingly and call init functions to init
    // event listeners for buttons, reload the layer list finally
    public ProjectEditor(ProjectModel p, StatusDisplay sd, GraphicInterface g) {
        self = this; // Use global scope to override anonymous scope
        this.bindedProject = p;
        this.globalObjects = g;
        this.sd = sd;
        this.name.setText(p.getName());
        this.numberLayer.setText(String.valueOf(p.getLayers().size()));
        this.numberModule.setText(String.valueOf(p.totalModules()));

        initAddLayerBtn();
        initAddSecClassBtn();
        initAddVecBtn();
        initLoadBuiltinBtn();
        initSecVecListBinding();
        initAddSecVecBtn();

        refreshSecClassList();
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
        addSecClassButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddSecClassDialog.main(bindedProject.getAccessVectors(),  sd, self);
            }
        });
    }

    // EFFECTS: add event listener for add vector button
    private void initAddVecBtn() {
        addVectorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddVecDialog.main(bindedProject.getAccessVectors(), sd, self);
            }
        });
    }

    // EFFECTS: add event listener for loading builtin definition button
    private void initLoadBuiltinBtn() {
        loadBuiltinBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    bindedProject.setAccessVectors(TerminalInterface
                            .loadAccessVectors(Main.DEFAULT_ACCESS_VEC_PATH, Main.DEFAULT_SEC_CLASS_PATH));
                    sd.modificationHappened();
                } catch (IOException ex) {
                    WarningDialog.main("Failed to load built-in file, check ./data , error: " + ex.getMessage());
                } catch (SyntaxParseException ex) {
                    WarningDialog.main("Broken syntax in built-in file, should not happen! " + ex.getMessage());
                }
                refreshSecClassList();
            }
        });
    }

    // EFFECTS: init the event handler for security class list's click action
    private void initSecVecListBinding() {
        secClassList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String selected = secClassList.getSelectedValue().toString();
                DefaultListModel<String> vecModel = new DefaultListModel<>();
                if (selected.equals("None")) {
                    vecModel.addElement("None");
                } else {
                    for (String vec : bindedProject.getAccessVectors().getAccessVector().get(selected)) {
                        vecModel.addElement(vec);
                    }
                }
                vecList.setModel(vecModel);
            }
        });
    }

    // EFFECTS: completely reload the list for security classes with bindedProject's definition
    public void refreshSecClassList() {
        DefaultListModel<String> resModel = new DefaultListModel<>();
        if (bindedProject.getAccessVectors().getAccessVector().isEmpty()) {
            resModel.addElement("None");
            vecList.setModel(resModel); // No element, overwrite with same placeholder
        } else {
            for (String secClassName : bindedProject.getAccessVectors().getAccessVector().keySet()) {
                resModel.addElement(secClassName);
            }
        }
        secClassList.setModel(resModel);
    }

    // EFFECTS: bind the event handler for add security class button and add vector button
    private void initAddSecVecBtn() {
        addSecClassButton.addActionListener(actionEvent
                -> AddSecClassDialog.main(bindedProject.getAccessVectors(), sd, self));
        addVectorButton.addActionListener(actionEvent -> AddVecDialog.main(bindedProject.getAccessVectors(), sd, self));
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
