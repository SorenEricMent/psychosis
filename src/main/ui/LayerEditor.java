package ui;

import model.policy.LayerModel;

import javax.swing.*;

// The editor panel for a specific layer, one instance for every layer
public class LayerEditor {

    private JLabel title1;
    private JPanel layerEditorPanel;
    private JLabel projectName;
    private JLabel layerName;
    private JLabel title2;

    // EFFECTS: create a new editing panel for a layer, update field to it accordingly
    public LayerEditor(LayerModel layer, String project) {
        projectName.setText(project);
        layerName.setText(layer.getName());
    }

    public JPanel getLayerEditorPanel() {
        return layerEditorPanel;
    }
}
