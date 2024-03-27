package ui;

import model.CustomReader;
import model.policy.InterfaceSetModel;
import model.policy.TypeEnfModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

// FileChooser dialog to export a te file
public class ExportTEDialog extends JDialog {
    protected final JFileChooser fileChooser;
    protected StatusDisplay sd;
    protected InterfaceSetModel projectIfSet;
    protected TypeEnfModel te;

    // REQUIRES: projectIfSet should be globalSet
    // EFFECTS: construct the file chooser, compiled determine if projectIfSet will be used to compile variables in te
    // , update sd on save
    public ExportTEDialog(StatusDisplay sd, InterfaceSetModel projectIfSet, TypeEnfModel te, boolean compiled) {
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setDialogTitle("Exporting .te file");
        fileChooser.setSelectedFile(new File("policy.te"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                sd.onWrite();
                CustomReader.writeToFile(fileToSave, te.toString());
                sd.ready();
            } catch (IOException e) {
                new WarningDialog(e.getMessage());
            }
            sd.disableProgress();
        }

    }

}
