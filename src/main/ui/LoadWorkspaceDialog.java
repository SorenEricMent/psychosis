package ui;

import model.CustomReader;
import model.Pair;
import model.ProjectModel;
import model.TrackerModel;
import org.json.JSONException;
import persistence.Workspace;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

// File chooser dialog for loading workspace and overwrite the current globalObject core fields
// and reload GUI after, also remove all panel mapping
public class LoadWorkspaceDialog extends JDialog {
    private final JFileChooser fileChooser;
    private final GraphicInterface globalObjects;

    // EFFECTS: init the fields and call initDialog to create the dialog, also init the
    // Approve button handling logic
    public LoadWorkspaceDialog(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        this.fileChooser = new JFileChooser(System.getProperty("user.dir"));
        initDialog();

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                Workspace w = new Workspace("", 0, new ArrayList<ProjectModel>());
                w.parser(CustomReader.readAsWhole(fileChooser.getSelectedFile()));
                loadWorkspace(w);
                globalObjects.getMainContainer().disableProgressBar();
            } catch (IOException e) {
                WarningDialog.main(e.getMessage());
                globalObjects.getMainContainer().disableProgressBar();
            } catch (JSONException e) {
                WarningDialog.main("Not a valid workspace file, " + e.getMessage());
                globalObjects.getMainContainer().disableProgressBar();
            }
        } else {
            globalObjects.getMainContainer().disableProgressBar();
        }
    }

    // EFFECTS: load fields from a workspace to the current globalObjects and regenerate the GUI
    private void loadWorkspace(Workspace w) {
        this.globalObjects.setLoadedProjects(w.getProjects().stream().map(x -> {
            return new Pair<ProjectModel, TrackerModel>(x, new TrackerModel());
        }).collect(Collectors.toCollection(ArrayList::new)));
        this.globalObjects.rebuildWholeProjectTree();
        //todo: use index for selection
        //for now, just switch back to placeholder
        this.globalObjects.replaceMainEditor(
                this.globalObjects.getMainContainer().getProjectPlaceholder().getProjectPlaceholderContainer());
        this.globalObjects.emptyEditorMap();
    }

    // EFFECTS: init the file chooser dialog (title and default selection), enable progress bar
    // MODIFIES: (side-effect) progressBar
    private void initDialog() {
        globalObjects.getMainContainer().enableProgressBar();
        fileChooser.setDialogTitle("Loading workspace from JSON");
        fileChooser.setSelectedFile(new File("workspace.json"));
    }
}
