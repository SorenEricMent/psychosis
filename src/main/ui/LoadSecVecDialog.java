package ui;

import model.policy.AccessVectorModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.io.File;

// File chooser dialog to prompt user to select two files to load access vector definition
// NOT YET USED
public class LoadSecVecDialog {
    private final JFileChooser secChooser;
    private final JFileChooser vecChooser;

    // EFFECTS: construct the Dialog and show them in order, create and bind event handler for the final approve
    public LoadSecVecDialog(StatusDisplay sd, AccessVectorModel av) {
        this.secChooser = new JFileChooser(System.getProperty("user.dir"));
        this.vecChooser = new JFileChooser(System.getProperty("user.dir"));

        sd.enableProgress();
        secChooser.setDialogTitle("Loading project from JSON");
        vecChooser.setSelectedFile(new File("project.json"));
    }
}
