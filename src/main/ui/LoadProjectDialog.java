package ui;

import model.CustomReader;
import model.Pair;
import model.ProjectModel;
import model.TrackerModel;
import persistence.ProjectSL;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Dialog to load project from a .json
// Although this is a Dialog, it is not managed by GUI designer as JFileChooser pop its own
public class LoadProjectDialog extends JDialog {
    private final JFileChooser loadProjectChooser;
    private final GraphicInterface globalObjects;
    private final ArrayList<Pair<ProjectModel, TrackerModel>> loadedProjects;

    // EFFECTS: init the fields and call initDialog to create the load project dialog
    // bind handler for user selection
    public LoadProjectDialog(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        this.loadedProjects = globalObjects.getLoadedProjects();
        this.loadProjectChooser = new JFileChooser(System.getProperty("user.dir"));

        initDialog();
        int userSelection = loadProjectChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            approveHandler(loadProjectChooser.getSelectedFile());
        } else {
            globalObjects.getMainContainer().disableProgressBar();
        }
    }

    // EFFECTS: init the file chooser dialog
    private void initDialog() {
        globalObjects.getMainContainer().enableProgressBar();
        loadProjectChooser.setDialogTitle("Loading project from JSON");
        loadProjectChooser.setSelectedFile(new File("project.json"));
    }

    // EFFECTS: add the project to loadedProjects, add the corresponding element
    // to project tree ans remove progress bar finally
    // MODIFIES: loadedProject, globalObject(GUI Component)
    private void loadProject(ArrayList<Pair<ProjectModel, TrackerModel>> loadedProjects,
                             Pair<ProjectModel, TrackerModel> proj) {
        loadedProjects.add(proj);
        globalObjects.updateProjectTree(proj);
        globalObjects.getMainContainer().disableProgressBar();
    }

    // EFFECTS: create warning dialog and remove progress bar
    private void exceptionHandler(String msg) {
        globalObjects.getMainContainer().disableProgressBar();
        WarningDialog.main(msg);
    }

    // EFFECTS: handle the logic for user approve save, call DuplicatedProjectDialog
    // if the name duplicated, call exceptionHandler for exception, add Project
    // and update GUI if no exception
    private void approveHandler(File fileToLoad) {
        if (fileToLoad.exists()) {
            System.out.println("Load from: " + fileToLoad.getAbsolutePath());
            try {
                Pair<ProjectModel, TrackerModel> proj = ProjectSL
                        .loadProjectFromJsonCompiled(CustomReader.readAsWhole(fileToLoad));
                if (globalObjects.isProjectNameDuplicated(proj.getFirst().getName())) {
                    DuplicatedProjectDialog.main(proj, globalObjects);
                } else {
                    loadProject(loadedProjects, proj);
                }
            } catch (IOException e) {
                exceptionHandler(e.getMessage());
            }
        } else {
            exceptionHandler("File does not exist.");
        }
    }
}
