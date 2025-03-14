package ui;

import model.CustomReader;
import ui.closure.StatusDisplay;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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
    private JButton quitApplicationBtn;
    private JLabel appTitle;
    private JPanel titlePanel;
    private JLabel seStatus;
    private JButton minimizeBtn;
    private JLabel logo;

    private final GraphicInterface globalObjects;

    private final ResourceBundle bundle;

    // CLOSURES
    private final StatusDisplay statusBoxed;
    private Point mouseDownCompCoords = null;


    // EFFECTS: create all GUI components
    public MainContainer(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        bundle = ResourceBundle.getBundle("resources/PsychosisResource");
        status.setText(getReflectiveResource("status.ready"));
        initProjectPlaceholder();
        topToolbar();
        initLangCombo();

        initTitleHide();

        statusBoxed = new StatusDisplay(getBundle(), statusPanel, progressBar, status, modified);
        globalToolbar.add(Box.createHorizontalGlue(), globalToolbar.getComponentCount() - 4);
    }

    public StatusDisplay getStatusBoxed() {
        return statusBoxed;
    }

    // EFFECTS: Get i18n value from the bundle imported by IntelliJ
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

    // EFFECTS: init action listener for language switch that restart the program with new locale
    // after switch
    private void initLangCombo() {
        langCombo.addItem("Esperanto");
        langCombo.addItem("English");
        langCombo.addItem("Francais"); // Imaging not having non-ASCII support in autograder

        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("eo")) {
            langCombo.setSelectedIndex(0);
        } else if (locale.getLanguage().equals("en")) {
            langCombo.setSelectedIndex(1);
        } else if (locale.getLanguage().equals("fr")) {
            langCombo.setSelectedIndex(2);
        }
        langCombo.addActionListener(actionEvent -> restartWithLocale());
    }

    // EFFECTS: Warn the user that updating language will require a restart, call Main.selfLocaleRestart on agree
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
        appTitle.setText("Psychosis Studio " + Main.getVersion());
        initWindowMove();
        topToolbarFile();
        topToolbarHelp();
        topToolbarSeStat();
        initQuitBtn();
    }

    // EFFECTS: update seStatus based on selinux status and operating system
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void topToolbarSeStat() {
        if (System.getProperty("os.name").toLowerCase().equals("linux")) {
            try {
                if (CustomReader.readAsWhole(new File("/sys/kernel/security/lsm")).contains("selinux")) {
                    if (CustomReader.readAsWholeCode(new File("/sys/fs/selinux/enforce")).getFirst().equals("1")) {
                        seStatus.setIcon(new ImageIcon("./data/resources/icons/security-custom-green.png"));
                        seStatus.setText("SELinux Enforced");
                        seStatus.setForeground(new Color(126, 211, 33));
                    } else {
                        seStatus.setIcon(new ImageIcon("./data/resources/icons/security-custom-yellow.png"));
                        seStatus.setText("SELinux Permissive");
                        seStatus.setForeground(new Color(248, 231, 38));
                    }
                } else {
                    // Linux with no SELinux
                    seStatus.setIcon(new ImageIcon("./data/resources/icons/shield-off-custom-red.png"));
                    seStatus.setText("SELinux Disabled");
                    seStatus.setForeground(new Color(208, 2, 27));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            seStatus.setIcon(new ImageIcon("./data/resources/icons/shield-off-custom-green.png"));
            seStatus.setText("SELinux Not Available");
            seStatus.setForeground(new Color(126, 211, 33));
        }
    }

    // EFFECTS: Add event handler of quitting the app to quitApplicationBtn
    private void initQuitBtn() {
        quitApplicationBtn.addActionListener(actionEvent -> {
            if (statusBoxed.shouldAlarmUnsave()) {
                SaveWarningDialog.main(statusBoxed, globalObjects);
            } else {
                System.exit(0);
            }
        });
        minimizeBtn.addActionListener(actionEvent -> globalObjects.getMainWindow().setState(Frame.ICONIFIED));
    }

    // EFFECTS: add drag event listener to topToolbar and make the main window update position
    // on mouse press and mouse motion
    // Original from StackOverflow 16046824, edited to adapt on component
    private void initWindowMove() {
        globalToolbar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }

        });

        globalToolbar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                globalObjects.getMainWindow()
                        .setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });
    }

    // EFFECTS: create the event listener for titlePanel that trigger title display hide
    private void initTitleHide() {
        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                appTitle.setVisible(!appTitle.isVisible());
            }
        });
    }

    // EFFECTS: return the file menu constructed out of openProjectPop, openWorkspacePop and saveWorkspacePop
    private JPopupMenu topToolbarFileMenu() {
        JPopupMenu filePopup = new JPopupMenu("");
        filePopup.setOpaque(true);
        JMenuItem openProjectPop = new JMenuItem(getReflectiveResource("Toolbar.file.open_project_comp"));

        JMenuItem openWorkspacePop = new JMenuItem(getReflectiveResource("Toolbar.file.open_workspace"));

        JMenuItem saveWorkspacePop = new JMenuItem(getReflectiveResource("Toolbar.file.save_workspace"));

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
        saveWorkspacePop.addActionListener(actionEvent -> new SaveWorkspaceDialog(getStatusBoxed(), globalObjects));
    }

    // EFFECTS: add event listener for the file button at top toolbar with
    // menu returned from topToolbarFileMenu
    // MODIFIES: this
    public void topToolbarFile() {
        JPopupMenu filePopup = topToolbarFileMenu();
        fileBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1) {
                    filePopup.show(e.getComponent(), fileBtn.getX() - titlePanel.getWidth(),
                            fileBtn.getY() + fileBtn.getHeight());
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
    // MODIFIES: (side-effect) progressBar
    public void enableProgressBar() {
        progressBar.setVisible(true);
    }

    // EFFECTS: make progress bar invisible
    // MODIFIES: (side-effect) progressBar
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
