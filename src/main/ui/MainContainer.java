package ui;

import ui.closure.StatusDisplay;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

// The container panel that contained all GUI components
public class MainContainer {
    private JTree projectList;
    private JPanel mainContainer;
    private JButton fileBtn;
    private JToolBar globalToolbar;
    private JButton helpBtn;
    private JPanel mainEditor;
    private ProjectPlaceholder projectPlaceholder;
    private JPanel statusPanel;
    private JProgressBar progressBar;
    private JLabel status;
    private JLabel modified;
    private JComboBox langCombo;

    private final GraphicInterface globalObjects;

    private final ResourceBundle bundle;

    // CLOSURES
    private final StatusDisplay statusBoxed;


    // EFFECTS: create all GUI components
    public MainContainer(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        bundle = ResourceBundle.getBundle("resources/PsychosisResource");
        status.setText(getReflectiveResource("status.ready"));
        initProjectPlaceholder();
        topToolbar();
        initLangCombo();

        statusBoxed = new StatusDisplay(getBundle(), statusPanel, progressBar, status, modified);
        globalToolbar.add(Box.createHorizontalGlue(), globalToolbar.getComponentCount() - 1);
    }

    public StatusDisplay getStatusBoxed() {
        return statusBoxed;
    }

    // EFFECTS: Get i18n value from the bundle imported by Intellij
    public String getReflectiveResource(String key) {
        return bundle.getString(key);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public JPanel getMainEditor() {
        return mainEditor;
    }

    public ProjectPlaceholder getProjectPlaceholder() {
        return projectPlaceholder;
    }

    // EFFECTS: init action listener for language switch
    // The reason of the length is that two Callables are created
    // ,and it makes no sense to split as it is already minimal
    private void initLangCombo() {
        langCombo.addItem("Esperanto");
        langCombo.addItem("English");
        langCombo.addItem("Francais");

        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("eo")) {
            langCombo.setSelectedIndex(0);
        } else if (locale.getLanguage().equals("en")) {
            langCombo.setSelectedIndex(1);
        } else if (locale.getLanguage().equals("fr")) {
            langCombo.setSelectedIndex(2);
        }
        langCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                restartWithLocale();
            }
        });
    }

    private void restartWithLocale() {
        WarningDialog.main("This would require a restart! Continue after save.", new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Locale locale = Locale.getDefault();
                if (langCombo.getSelectedItem().toString().equals("Esperanto")) {
                    Main.selfLocaleRestart("eo", "");
                } else if (langCombo.getSelectedItem().toString().equals("English")) {
                    Main.selfLocaleRestart("en", "");
                } else if (langCombo.getSelectedItem().toString().equals("Francais")) {
                    Main.selfLocaleRestart("fr", "");
                }
                return null;
            }
        });
    }

    // EFFECTS: init the event handler for buttons in project placeholder with globalObjects reference
    private void initProjectPlaceholder() {
        projectPlaceholder.getCreateProjectButton().addActionListener(e -> {
            CreateProjectDialog.main(this.globalObjects);
        });
        projectPlaceholder.getLoadProjectButton().addActionListener(e -> {
            new LoadProjectDialog(this.globalObjects);
        });
        projectPlaceholder.getLoadWorkspaceButton().addActionListener(e -> {
            new LoadWorkspaceDialog(this.globalObjects);
        });
    }

    // EFFECTS: call and init the whole toolbar
    private void topToolbar() {
        topToolbarFile();
        topToolbarHelp();
    }

    // EFFECTS: return the file menu
    private JPopupMenu topToolbarFileMenu() {
        JPopupMenu filePopup = new JPopupMenu("");
        filePopup.setOpaque(true);
        JMenuItem openProjectPop = new JMenuItem("Open Project (Compiled)");

        JMenuItem openWorkspacePop = new JMenuItem("Open Workspace");

        JMenuItem saveWorkspacePop = new JMenuItem("Save Workspace (Compiled)");

        topToolBarFileMenuBinding(openProjectPop, openWorkspacePop, saveWorkspacePop);
        filePopup.add(openProjectPop);
        filePopup.add(openWorkspacePop);
        filePopup.add(saveWorkspacePop);
        filePopup.pack();
        return filePopup;
    }

    // EFFECTS: add event listener for file menu popups
    private void topToolBarFileMenuBinding(JMenuItem openProjectPop,
                                           JMenuItem openWorkspacePop, JMenuItem saveWorkspacePop) {
        openProjectPop.addActionListener(actionEvent -> new LoadProjectDialog(globalObjects));
        openWorkspacePop.addActionListener(actionEvent -> new LoadWorkspaceDialog(globalObjects));
        saveWorkspacePop.addActionListener(actionEvent -> new SaveWorkspaceDialog(globalObjects));
    }

    // EFFECTS: add event listener for the file button at top toolbar with
    // menu returned from topToolbarFileMenu
    // MODIFIES: this
    public void topToolbarFile() {
        JPopupMenu filePopup = topToolbarFileMenu();
        fileBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1) {
                    filePopup.show(e.getComponent(), fileBtn.getX(), fileBtn.getY() + fileBtn.getHeight());
                }
            }
        });
    }

    // EFFECTS: add event listener for the help button at top tool bar
    // MODIFIES: this
    public void topToolbarHelp() {
        helpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                HelpDialog.main();
            }
        });
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }

    public JTree getProjectList() {
        return projectList;
    }

    // EFFECTS: make progress bar visible
    public void enableProgressBar() {
        progressBar.setVisible(true);
    }

    // EFFECTS: make progress bar invisible
    public void disableProgressBar() {
        progressBar.setVisible(false);
    }

    // EFFECTS: custom create for GUI builder, init root as Projects
    private void createUIComponents() {
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("Projects");
        projectList = new JTree(top);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        disableProgressBar();
    }
}
