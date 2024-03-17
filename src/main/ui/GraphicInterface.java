package ui;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;
import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.PanelUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// The class to create and manage the main window of Psychosis
public class GraphicInterface {
    private final ArrayList<
            Pair<ProjectModel, TrackerModel>> loadedProjects = new ArrayList<>();
    private final Integer currentWorkIndex = 0;
    // Object 0 is an empty non-saving test only project

    private MainContainer mainContainer;

    public ArrayList<Pair<ProjectModel, TrackerModel>> getLoadedProjects() {
        return loadedProjects;
    }

    public Integer getCurrentWorkIndex() {
        return currentWorkIndex;
    }

    public MainContainer getMainContainer() {
        return mainContainer;
    }

    // EFFECTS: update project tree with a new project at the end
    // REQUIRES: must not call rebuildWholeProjectTree before or duplicate will be created
    public void updateProjectTree(ProjectModel p) {
        DefaultTreeModel projectTreeModel = (DefaultTreeModel) mainContainer.getProjectList().getModel();
        DefaultMutableTreeNode projectTreeRoot = (DefaultMutableTreeNode) projectTreeModel.getRoot();
        // Names
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(p.getName());
        // Layers
        for (LayerModel l : p.getLayers()) {
            // Modules
            DefaultMutableTreeNode layerChild = new DefaultMutableTreeNode(l.getName());
            for (PolicyModuleModel m : l.getPolicyModules().values()) {
                layerChild.add(new DefaultMutableTreeNode(m.getName()));
            }
            child.add(layerChild);
        }
        projectTreeModel.insertNodeInto(child, projectTreeRoot, projectTreeRoot.getChildCount());
    }

    // EFFECTS: polymorph call to stripe Pair
    public void updateProjectTree(Pair<ProjectModel, TrackerModel> p) {
        updateProjectTree(p.getFirst());
    }

    // EFFECTS: completely regenerate the content in project tree from loadedProjects;
    public void rebuildWholeProjectTree() {
        DefaultTreeModel projectTreeModel = (DefaultTreeModel) mainContainer.getProjectList().getModel();
        DefaultMutableTreeNode projectTreeRoot = (DefaultMutableTreeNode) projectTreeModel.getRoot();
        projectTreeRoot.removeAllChildren();
        projectTreeModel.reload();
        for (Pair<ProjectModel, TrackerModel> p : loadedProjects) {
            // Names
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(p.getFirst().getName());
            // Layers
            for (LayerModel l : p.getFirst().getLayers()) {
                // Modules
                DefaultMutableTreeNode layerChild = new DefaultMutableTreeNode(l.getName());
                for (PolicyModuleModel m : l.getPolicyModules().values()) {
                    layerChild.add(new DefaultMutableTreeNode(m.getName()));
                }
                child.add(layerChild);
            }
            projectTreeModel.insertNodeInto(child, projectTreeRoot, projectTreeRoot.getChildCount());
            mainContainer.getProjectList().scrollPathToVisible(new TreePath(child.getPath()));
            mainContainer.getProjectList().getSelectionModel()
                    .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            mainContainer.getProjectList().addTreeSelectionListener(projectTreeEvent(mainContainer.getProjectList()));
        }
    }

    // EFFECTS: handle GUI update triggered on project tree click
    public TreeSelectionListener projectTreeEvent(JTree tree) {
        return new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                TreePath node = tree.getSelectionPath();
                if (node == null) {
                    return;
                }
                // This is dirty, but this tree's structure is determined.
                if (node.getPath().length == 1) {
                    mainContainer.getMainEditor().remove(0);
                    mainContainer.getMainEditor()
                            .add(mainContainer.getProjectPlaceholder().getProjectPlaceholderContainer());
                    mainContainer.getMainEditor().revalidate();
                    mainContainer.getMainEditor().repaint();
                } else if (node.getPath().length == 2) {
                    System.out.println("Clicked spec proj");
                    LayerEditor tmp = new LayerEditor(new LayerModel("testtemp"));
                    mainContainer.getMainEditor().remove(0);
                    mainContainer.getMainEditor().add(tmp.getLayerEditorPanel());
                    mainContainer.getMainEditor().revalidate();
                    mainContainer.getMainEditor().repaint();
                }
            }
        };
    }

    // EFFECTS: init the GUI and create global objects storing working information
    public GraphicInterface() {
        splashScreen();
        System.out.println("Starting in GUI.");
        this.loadedProjects.add(
                new Pair<>(new TempProjectModel("Temp"),
                        new TrackerModel()));
        initStyle();
        initWindow();
        rebuildWholeProjectTree();
    }

    // EFFECTS: init the main application window
    private void initWindow() {
        JFrame mainWindow = new JFrame("Psychosis Studio " + Main.getVersion());
        mainContainer = new MainContainer(this);
        ImageIcon img = new ImageIcon("./data/resources/logo.jpg");

        mainWindow.setIconImage(img.getImage());

        mainWindow.setContentPane(mainContainer.getMainContainer());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null);
    }

    // EFFECTS: draw a splash screen
    private static void splashScreen() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        try {
            Thread.sleep(1000); // My banner looks so good it needs to last longer
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: set application menu bar title and use system ui style
    private static void initStyle() {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Psychosis Studio");
        Toolkit defToolkit = Toolkit.getDefaultToolkit();
        java.lang.reflect.Field awtAppClassNameField = null;

        try {
            awtAppClassNameField = defToolkit.getClass().getDeclaredField("awtAppClassName");
            awtAppClassNameField.setAccessible(true);
            awtAppClassNameField.set(defToolkit, "Psychosis Studio");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Using style " + UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        UIManager.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);
    }
}
