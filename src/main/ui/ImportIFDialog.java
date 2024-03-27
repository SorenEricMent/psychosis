package ui;

import model.CustomReader;
import model.ProjectModel;
import model.exception.SyntaxParseException;
import model.policy.InterfaceModel;
import model.policy.InterfaceSetModel;
import model.policy.PolicyModuleModel;
import model.policy.TypeEnfModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

// file chooser dialog to import an if file and overwrite the content of the original interface and regenerate the
// projects' globalSet
public class ImportIFDialog extends JDialog {
    private final JFileChooser fileChooser;
    private final StatusDisplay sd;
    private final PolicyModuleModel target;
    private final ProjectModel project;
    private final ModuleEditor editor;

    // EFFECTS: init the fields and approve event handler and create the file chooser dialog
    public ImportIFDialog(StatusDisplay sd, PolicyModuleModel target,
                          InterfaceSetModel interfaceSet, ProjectModel proj, ModuleEditor editor) {
        this.sd = sd;
        this.target = target;
        this.project = proj;
        this.editor = editor;
        fileChooser = new JFileChooser(System.getProperty("user.dir"));

        fileChooser.setDialogTitle("Loading SELinux Interface file");
        fileChooser.setSelectedFile(new File("example.if"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            approveHandler(fileChooser.getSelectedFile());
        }
    }

    // EFFECTS: load the If file and parse it to a InterfaceSet and use the result to override the module's one
    // and regenerate project's set
    private void approveHandler(File path) {
        try {
            InterfaceSetModel inf = InterfaceSetModel.parser(CustomReader.readAsWholeCode(path).getFirst());
            inf.setOwners(target.getName());
            target.setInterfaceObject(inf);
            project.rebuildGlobalInterfaceSet();
            editor.rebuildIfList();
            sd.modificationHappened();
        } catch (SyntaxParseException | IOException e) {
            WarningDialog.main(e.getMessage());
        }
    }
}
