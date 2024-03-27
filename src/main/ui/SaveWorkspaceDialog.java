package ui;

import model.CustomReader;
import model.Pair;
import persistence.Workspace;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

// file chooser dialog to save workspace data to a .json
public class SaveWorkspaceDialog extends JDialog {
    private final JFileChooser saveWorkspaceChooser;
    private final GraphicInterface globalObjects;
    private final StatusDisplay statusDisplay;


    // EFFECTS: init the workspace dialog and init the handling logic for approve/cancel
    public SaveWorkspaceDialog(StatusDisplay sd, GraphicInterface globalObjects) {
        this.statusDisplay = sd;
        this.globalObjects = globalObjects;
        saveWorkspaceChooser = new JFileChooser(System.getProperty("user.dir"));
        saveWorkspaceChooser.setDialogTitle("Saving all states to a workspace.json");
        saveWorkspaceChooser.setSelectedFile(new File("workspace.json"));

        int userSelection = saveWorkspaceChooser.showSaveDialog(this);
        //TODO

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveWorkspaceChooser.getSelectedFile();
            Workspace workspaceToSave = generateWorkspace(this.globalObjects);
            try {
                CustomReader.writeToFile(fileToSave, workspaceToSave.toStringCompiled());
                statusDisplay.saveHappened();
            } catch (IOException e) {
                new WarningDialog(e.getMessage());
                globalObjects.getMainContainer().disableProgressBar();
            }
        }
    }

    // EFFECTS: create a workspace object with information in the shared global object GraphicalInterface
    private Workspace generateWorkspace(GraphicInterface g) {
        return new Workspace("", 0, g.getLoadedProjects().stream().map(Pair::getFirst)
                .collect(Collectors.toCollection(ArrayList::new)));
    }
}
