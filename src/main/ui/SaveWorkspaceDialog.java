package ui;

import javax.swing.*;
import java.io.File;

// Dialog to save workspace data to a .json
// Although this is a Dialog, it is not managed by GUI designer as JFileChooser pop its own
public class SaveWorkspaceDialog extends JDialog {
    private final JFileChooser saveWorkspaceChooser;

    public SaveWorkspaceDialog(GraphicInterface globalObjects) {
        saveWorkspaceChooser = new JFileChooser(System.getProperty("user.dir"));
        saveWorkspaceChooser.setDialogTitle("Saving all states to a workspace.json");
        saveWorkspaceChooser.setSelectedFile(new File("workspace.json"));

        int userSelection = saveWorkspaceChooser.showSaveDialog(this);
        //TODO

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveWorkspaceChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        }
    }
}
