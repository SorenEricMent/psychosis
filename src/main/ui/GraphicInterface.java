package ui;

import model.Pair;
import model.ProjectModel;
import model.TempProjectModel;
import model.TrackerModel;
import model.policy.LayerModel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;

// The class to create and manage the main window of Psychosis
public class GraphicInterface {
    private ArrayList<
            Pair<ProjectModel, TrackerModel>> loadedProjects = new ArrayList<>();
    private Integer currentWorkIndex = 0;
    // Object 0 is an empty non-saving test only project

    private MainContainer mainContainer;

    public GraphicInterface() {
        splashScreen();
        System.out.println("Starting in GUI.");
        this.loadedProjects.add(
                new Pair<>(new TempProjectModel("Temp"),
                        new TrackerModel()));
        initStyle();
        initWindow();
        DefaultTreeModel projectTreeModel = (DefaultTreeModel) mainContainer.getProjectList().getModel();
        DefaultMutableTreeNode projectTreeRoot = (DefaultMutableTreeNode) projectTreeModel.getRoot();

        for (Pair<ProjectModel, TrackerModel> p : loadedProjects) {
            // Names
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(p.getFirst().getName());
            // Layers
            for (LayerModel l : p.getFirst().getLayers()) {
                child.add(new DefaultMutableTreeNode(l.getName()));
            }
            //
            projectTreeModel.insertNodeInto(child, projectTreeRoot, projectTreeRoot.getChildCount());
            mainContainer.getProjectList().scrollPathToVisible(new TreePath(child.getPath()));
        }
    }

    // EFFECTS: init the main application window
    private void initWindow() {
        JFrame mainWindow = new JFrame("Psychosis Studio " + Main.getVersion());
        mainContainer = new MainContainer();
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}
