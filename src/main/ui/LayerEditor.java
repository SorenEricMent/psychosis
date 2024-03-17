package ui;

import model.policy.LayerModel;

import javax.swing.*;
import java.awt.*;

public class LayerEditor {

    private JLabel title;
    private JPanel layerEditorPanel;
    private JLabel name;

    public LayerEditor(LayerModel layer) {
        name.setText(layer.getName());
    }

    public JPanel getLayerEditorPanel() {
        return layerEditorPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        layerEditorPanel = new JPanel();
    }
}
