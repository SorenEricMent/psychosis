package ui;

import model.CustomReader;
import model.Pair;
import persistence.Workspace;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

// Dialog to save workspace data to a .json
// Although this is a Dialog, it is not managed by GUI designer as JFileChooser pop its own
public class SaveWorkspaceDialog extends JDialog {
    private final JFileChooser saveWorkspaceChooser;
    private final GraphicInterface globalObjects;

    // EFFECTS: init the workspace dialog and init the handling logic for approve/cancel
    public SaveWorkspaceDialog(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        saveWorkspaceChooser = new JFileChooser(System.getProperty("user.dir"));
        saveWorkspaceChooser.setDialogTitle("Saving all states to a workspace.json");
        saveWorkspaceChooser.setSelectedFile(new File("workspace.json"));

        int userSelection = saveWorkspaceChooser.showSaveDialog(this);
        //TODO

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveWorkspaceChooser.getSelectedFile();
            Workspace workspaceToSave = generateWorkspace(this.globalObjects);
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            try {
                CustomReader.writeToFile(fileToSave, workspaceToSave.toStringCompiled());
            } catch (IOException e) {
                new WarningDialog(e.getMessage());
                globalObjects.getMainContainer().disableProgressBar();
            }
        }
    }

    private Workspace generateWorkspace(GraphicInterface g) {
        return new Workspace("", 0, g.getLoadedProjects().stream().map(Pair::getFirst)
                .collect(Collectors.toCollection(ArrayList::new)));
    }
}
