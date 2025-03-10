package ui.closure;

import javax.swing.*;
import java.util.ResourceBundle;

// Reference to the status and provide methods to mutate it
public class StatusDisplay {
    private final JPanel boxed;
    private final JProgressBar bar;
    private final JLabel status;
    private final JLabel modify;
    private final ResourceBundle bundle;

    // Possible states of modification-save
    public enum ModifyStatus {
        UNMODIFIED,
        MODIFIED,
        MODIFIED_SAVED,
        UNMODIFIED_SAVED //???
    }

    private ModifyStatus modifyStatus = ModifyStatus.UNMODIFIED;

    // EFFECTS: init the fields for StatusDisplay class
    public StatusDisplay(ResourceBundle b, JPanel statusPanel, JProgressBar bar, JLabel status, JLabel modify) {
        this.boxed = statusPanel;
        this.bar = bar;
        this.status = status;
        this.modify = modify;
        this.bundle = b;
    }

    public ModifyStatus getModifyStatus() {
        return modifyStatus;
    }

    // EFFECTS: return if Psychosis should alarm User of unsaved changes;
    public boolean shouldAlarmUnsave() {
        return this.modifyStatus == ModifyStatus.MODIFIED;
    }

    // EFFECTS: update the modify status to modified
    // MODIFIES: this
    public void modificationHappened() {
        this.modifyStatus = ModifyStatus.MODIFIED;
        this.modify.setText(bundle.getString("status.modified"));
    }

    // EFFECTS: update the modify status to the SAVED version of the current status
    // MODIFIES: this
    public void saveHappened() {
        this.modifyStatus =
                modifyStatus == ModifyStatus.UNMODIFIED ? ModifyStatus.UNMODIFIED_SAVED : ModifyStatus.MODIFIED_SAVED;
        this.modify.setText(modifyStatus == ModifyStatus.UNMODIFIED_SAVED
                ? bundle.getString("status.unmodified_saved") : bundle.getString("status.saved"));
    }

    // EFFECTS: set the status to writing
    // MODIFIES: this, (side-effect) progress bar
    public void onWrite() {
        this.status.setText("Writing...");
        enableProgress();
    }

    // EFFECTS: set the status to Ready
    // MODIFIES: this
    public void ready() {
        this.status.setText("Ready");
    }

    // EFFECTS: display the progress bar
    // MODIFIES: (side-effect) progress bar
    public void enableProgress() {
        bar.setVisible(true);
    }

    // EFFECTS: hide the progress bar
    // MODIFIES: (side-effect) progress bar
    public void disableProgress() {
        bar.setVisible(false);
    }
}
