package ui;

import model.CustomReader;
import model.exception.SyntaxParseException;
import model.policy.PolicyModuleModel;
import model.policy.TypeEnfModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

// file chooser dialog to import a te file and overwrite the original
// TypeEnf definition
public class ImportTEDialog extends JDialog  {
    private final JFileChooser fileChooser;
    private final StatusDisplay sd;
    private final PolicyModuleModel target;

    // EFFECTS: init the fields and create the file chooser dialog, bind approve event handler
    public ImportTEDialog(StatusDisplay sd, PolicyModuleModel target) {
        this.sd = sd;
        this.target = target;
        fileChooser = new JFileChooser(System.getProperty("user.dir"));

        fileChooser.setDialogTitle("Loading SELinux Type Enforcement file");
        fileChooser.setSelectedFile(new File("example.te"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            approveHandler(fileChooser.getSelectedFile());
        }
    }

    // EFFECTS: load the TE file and parse it to TypeEnfModel and use the result to override the module's one
    private void approveHandler(File path) {
        try {
            target.setTypeEnfObject(TypeEnfModel.parser(CustomReader.readAsWhole(path)));
            sd.modificationHappened();
        } catch (SyntaxParseException | IOException e) {
            WarningDialog.main(e.getMessage());
        }
    }
}
