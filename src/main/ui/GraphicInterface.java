package ui;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;
import model.policy.LayerModel;
import model.policy.PolicyModuleModel;
import ui.closure.StatusDisplay;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// The class to create and manage the main window of Psychosis, also contained some
// global states shared across components
public class GraphicInterface {
    private ArrayList<
            Pair<ProjectModel, TrackerModel>> loadedProjects = new ArrayList<>();
    private Integer currentWorkIndex = 0;
    // Object 0 is an empty non-saving test only project

    private MainContainer mainContainer;

    private final HashMap<ProjectModel, ProjectEditor> projectEditorMap = new HashMap<>();
    private final HashMap<LayerModel, LayerEditor> layerEditorMap = new HashMap<>();
    private final HashMap<PolicyModuleModel, ModuleEditor> moduleEditorMap = new HashMap<>();
    private final GraphicInterface self;
    private JFrame mainWindow;

    // EFFECTS: init the GUI and create global objects storing working information
    public GraphicInterface() {
        this.self = this;
        splashScreen();
        System.out.println("Starting in GUI.");
        this.loadedProjects.add(
                new Pair<>(new TempProjectModel("Temp"),
                        new TrackerModel()));
        initStyle();
        initWindow();
        rebuildWholeProjectTree();
    }

    public ArrayList<Pair<ProjectModel, TrackerModel>> getLoadedProjects() {
        return loadedProjects;
    }

    public Integer getCurrentWorkIndex() {
        return currentWorkIndex;
    }

    public MainContainer getMainContainer() {
        return mainContainer;
    }

    // EFFECTS: get the StatusDisplay Object from MainContainer
    public StatusDisplay getStatusDisplay() {
        return this.mainContainer.getStatusBoxed();
    }

    public JFrame getMainWindow() {
        return mainWindow;
    }

    public void setCurrentWorkIndex(Integer currentWorkIndex) {
        this.currentWorkIndex = currentWorkIndex;
    }

    public void setLoadedProjects(ArrayList<Pair<ProjectModel, TrackerModel>> loadedProjects) {
        this.loadedProjects = loadedProjects;
    }

    // EFFECTS: remove all cached editor panel
    // MODIFIES: this
    public void emptyEditorMap() {
        projectEditorMap.clear();
        layerEditorMap.clear();
        moduleEditorMap.clear();
    }

    // EFFECTS: update project tree with a new layer added to the end of a project's child
    // REQUIRES: must not call rebuildWholeProjectTree before together or duplicate will be created
    public void updateModuleTree(String projName, String layerName, String moduleName) {
        DefaultTreeModel projectTreeModel = (DefaultTreeModel) mainContainer.getProjectList().getModel();
        DefaultMutableTreeNode projectTreeRoot = (DefaultMutableTreeNode) projectTreeModel.getRoot();
        DefaultMutableTreeNode projectNode = null;
        DefaultMutableTreeNode layerNode = null;
        for (int i = 0; i < projectTreeModel.getChildCount(projectTreeRoot); i++) {
            if (projectTreeModel.getChild(projectTreeRoot, i).toString().equals(projName)) {
                projectNode = (DefaultMutableTreeNode) projectTreeModel.getChild(projectTreeRoot, i);
            }
        }

        for (int i = 0; i < projectNode.getChildCount(); i++) {
            if (projectNode.getChildAt(i).toString().equals(layerName)) {
                layerNode = (DefaultMutableTreeNode) projectNode.getChildAt(i);
                break;
            }
        }
        projectTreeModel.insertNodeInto(
                new DefaultMutableTreeNode(moduleName), layerNode, layerNode.getChildCount());
    }

    // EFFECTS: update project tree with a new layer added to the end of a project's child
    // REQUIRES: must not call rebuildWholeProjectTree before together or duplicate will be created
    public void updateLayerTree(String projName, String layerName) {
        DefaultTreeModel projectTreeModel = (DefaultTreeModel) mainContainer.getProjectList().getModel();
        DefaultMutableTreeNode projectTreeRoot = (DefaultMutableTreeNode) projectTreeModel.getRoot();
        DefaultMutableTreeNode projectNode = null;
        for (int i = 0; i < projectTreeModel.getChildCount(projectTreeRoot); i++) {
            if (projectTreeModel.getChild(projectTreeRoot, i).toString().equals(projName)) {
                projectNode = (DefaultMutableTreeNode) projectTreeModel.getChild(projectTreeRoot, i);
            }
        }
        projectTreeModel.insertNodeInto(
                new DefaultMutableTreeNode(layerName), projectNode, projectNode.getChildCount());
    }

    // EFFECTS: update project tree with a new project at the end
    // REQUIRES: must not call rebuildWholeProjectTree before together or duplicate will be created
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
                    replaceMainEditor(mainContainer.getProjectPlaceholder().getProjectPlaceholderContainer());
                } else if (node.getPath().length == 2) {
                    // The following non-null promise is given by projectTree's way of updating
                    switchProjectEditor(Arrays.stream(node.getPath()).map(Object::toString).toArray(String[]::new));
                } else if (node.getPath().length == 3) {
                    switchLayerEditor(Arrays.stream(node.getPath()).map(Object::toString).toArray(String[]::new));
                } else if (node.getPath().length == 4) {
                    switchModuleEditor(Arrays.stream(node.getPath()).map(Object::toString).toArray(String[]::new));
                }
            }
        };
    }

    // EFFECTS: look up if a project editor panel is already created
    //   if not: create and replace the main editor content the panel
    //   if yes: replace the panel and update the map
    private void switchProjectEditor(String[] args) {
        ProjectModel proj = findProjectWithName(args[1]);
        if (projectEditorMap.containsKey(proj)) {
            replaceMainEditor(projectEditorMap.get(proj).getProjectEditorPanel());
        } else {
            ProjectEditor tmp = new ProjectEditor(proj, getStatusDisplay(), this);
            projectEditorMap.put(proj, tmp);
            replaceMainEditor(tmp.getProjectEditorPanel());
        }
    }

    // EFFECTS: look up if a layer editor panel is already created
    //   if not: create and replace the main editor content the panel
    //   if yes: replace the panel and update the map
    private void switchLayerEditor(String[] args) {
        ProjectModel proj = findProjectWithName(args[1]);
        LayerModel layer = proj.getLayer(args[2]);
        if (layerEditorMap.containsKey(layer)) {
            replaceMainEditor(layerEditorMap.get(layer).getLayerEditorPanel());
        } else {
            LayerEditor tmp = new LayerEditor(layer, this, proj.getName());
            layerEditorMap.put(layer, tmp);
            replaceMainEditor(tmp.getLayerEditorPanel());
        }
    }

    // EFFECTS: look up if a module editor panel is already created
    //   if not: create and replace the main editor content the panel
    //   if yes: replace the panel and update the map
    private void switchModuleEditor(String[] args) {
        ProjectModel proj = findProjectWithName(args[1]);
        LayerModel layer = proj.getLayer(args[2]);
        PolicyModuleModel module = layer.getPolicyModule(args[3]);
        if (moduleEditorMap.containsKey(module)) {
            replaceMainEditor(moduleEditorMap.get(module).getModuleEditorPanel());
        } else {
            ModuleEditor tmp = new ModuleEditor(getStatusDisplay(),
                    module, proj.getAccessVectors(), proj.getGlobalInterfaceSet(), proj.getName(), layer.getName());
            moduleEditorMap.put(module, tmp);
            replaceMainEditor(tmp.getModuleEditorPanel());
        }
    }

    // EFFECTS: return the project with name, null if not found
    public ProjectModel findProjectWithName(String name) {
        for (Pair<ProjectModel, TrackerModel> p : loadedProjects) {
            if (p.getFirst().getName().equals(name)) {
                return p.getFirst();
            }
        }
        return null;
    }

    // EFFECTS: return if the name for the new project duplicated
    public boolean isProjectNameDuplicated(String name) {
        for (Pair<ProjectModel, TrackerModel> p : loadedProjects) {
            if (p.getFirst().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: replace the content of the main editor panel
    public void replaceMainEditor(JPanel content) {
        mainContainer.getMainEditor().remove(0);
        mainContainer.getMainEditor().add(content);
        mainContainer.getMainEditor().revalidate();
        mainContainer.getMainEditor().repaint();
    }

    // EFFECTS: init the main application window
    private void initWindow() {
        mainWindow = new JFrame("Psychosis Studio " + Main.getVersion());
        mainContainer = new MainContainer(this);
        ImageIcon img = new ImageIcon("./data/resources/logo.jpg");

        mainWindow.setIconImage(img.getImage());

        mainWindow.setContentPane(mainContainer.getMainContainer());
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (getStatusDisplay().getModifyStatus() == StatusDisplay.ModifyStatus.MODIFIED) {
                    SaveWarningDialog.main(getStatusDisplay(), self);
                } else {
                    System.exit(0);
                }
            }
        });

        mainWindow.setUndecorated(true);
        mainWindow.pack();
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
        mainWindow.setLocationRelativeTo(null);
    }

    // EFFECTS: draw a splash screen, path defined in JVM param
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
        Field awtAppClassNameField = null;
        try {
            awtAppClassNameField = defToolkit.getClass().getDeclaredField("awtAppClassName");
            awtAppClassNameField.setAccessible(true);
            awtAppClassNameField.set(defToolkit, "Psychosis Studio");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        UIManager.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);
    }
}
