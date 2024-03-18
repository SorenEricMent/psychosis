package ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

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

    private final GraphicInterface globalObjects;

    private ResourceBundle bundle;

    // EFFECTS: create all GUI components
    public MainContainer(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        bundle = ResourceBundle.getBundle("resources/PsychosisResource");
        status.setText(getReflectiveResource("status.ready"));
        initProjectPlaceholder();
        topToolbar();
    }

    // EFFECTS: Get i18n value from the bundle imported by Intellij
    public String getReflectiveResource(String key) {
        return bundle.getString(key);
    }

    public JPanel getMainEditor() {
        return mainEditor;
    }

    public ProjectPlaceholder getProjectPlaceholder() {
        return projectPlaceholder;
    }

    // EFFECTS: init the event handler for buttons in project placeholder with globalObjects reference
    private void initProjectPlaceholder() {
        projectPlaceholder.getCreateProjectButton().addActionListener(e -> {
            CreateProjectDialog.main(this.globalObjects);
        });
        projectPlaceholder.getLoadProjectButton().addActionListener(e -> {
            LoadProjectDialog.main(this.globalObjects);
        });
        projectPlaceholder.getLoadWorkspaceButton().addActionListener(e -> {
            LoadWorkspaceDialog.main(this.globalObjects);
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

        JMenuItem openProjectPop = new JMenuItem("Open Project");
        openProjectPop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        JMenuItem openWorkspacePop = new JMenuItem("Open Workspace");
        openWorkspacePop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        JMenuItem saveWorkspacePop = new JMenuItem("Save Workspace");
        saveWorkspacePop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new SaveWorkspaceDialog();
            }
        });
        //temp TODO split
//        filePopup.add(openProjectPop);
//        filePopup.add(openWorkspacePop);
        filePopup.add(saveWorkspacePop);
        filePopup.pack();
        return filePopup;
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

    // EFFECTS: custom create for GUI builder, init root as Projects
    private void createUIComponents() {
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("Projects");
        projectList = new JTree(top);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
    }
}
