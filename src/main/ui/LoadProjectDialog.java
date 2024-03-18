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

    public LoadProjectDialog(GraphicInterface globalObjects) {
        ArrayList<Pair<ProjectModel, TrackerModel>> loadedProjects = globalObjects.getLoadedProjects();
        loadProjectChooser = new JFileChooser(System.getProperty("user.dir"));
        loadProjectChooser.setDialogTitle("Loading project from JSON");
        loadProjectChooser.setSelectedFile(new File("project.json"));

        int userSelection = loadProjectChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = loadProjectChooser.getSelectedFile();
            if (fileToLoad.exists()) {
                System.out.println("Load from: " + fileToLoad.getAbsolutePath());
            } else {
                WarningDialog.main("File does not exist.");
            }
            try {
                Pair<ProjectModel, TrackerModel> proj = ProjectSL
                        .loadProjectFromJsonCompiled(CustomReader.readAsWhole(fileToLoad));
                if (globalObjects.isProjectNameDuplicated(proj.getFirst().getName())) {
                    DuplicatedProjectDialog.main(proj, globalObjects);
                } else {
                    loadedProjects.add(proj);
                    globalObjects.updateProjectTree(proj);
                }
            } catch (IOException e) {
                WarningDialog.main(e.getMessage());
            }
        }
    }
}
