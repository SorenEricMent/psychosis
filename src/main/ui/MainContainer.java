package ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainContainer {
    private JTree projectList;
    private JPanel mainContainer;
    private JButton fileBtn;
    private JToolBar globalToolbar;
    private JButton helpBtn;
    private JPanel mainEditor;
    private ProjectPlaceholder projectPlaceholder;

    private GraphicInterface globalObjects;

    public MainContainer(GraphicInterface globalObjects) {
        this.globalObjects = globalObjects;
        projectPlaceholder.getCreateProjectButton().addActionListener(e -> {
            CreateProjectDialog.main(this.globalObjects);
        });
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
                SaveWorkspaceDialog.main();
            }
        });
        filePopup.add(openProjectPop);
        filePopup.add(openWorkspacePop);
        filePopup.add(saveWorkspacePop);
        filePopup.pack();

        fileBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1) {
                    filePopup.show(e.getComponent(), fileBtn.getX(), fileBtn.getY() + fileBtn.getHeight());
                }
            }
        });

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

    private void createUIComponents() {
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("Projects");
        projectList = new JTree(top);
    }

}
