package ui;

import model.policy.TypeEnfModel;

import javax.swing.*;
import java.io.File;

public class ExportTEDialog {
    protected final JFileChooser loadProjectChooser;
    protected TypeEnfModel te;

    public ExportTEDialog(TypeEnfModel te) {
        loadProjectChooser = new JFileChooser();
    }

    // EFFECTS: init the file chooser dialog
    private void initDialog() {

    }

}
