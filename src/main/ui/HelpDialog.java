package ui;

import javax.swing.*;
import java.awt.event.*;

public class HelpDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonClose;
    private JTabbedPane helpTabs;
    private JLabel debugContent;

    public HelpDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonClose);

        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        debugContent.setText(debugInfo());
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main() {
        HelpDialog dialog = new HelpDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // EFFECTS: return debug info
    private String debugInfo() {
        String debugText = "<html>";
        debugText = debugText.concat("<strong>The following content is for debug only.</strong><br/>");
        debugText = debugText.concat("Version " + Main.getVersion()
                + ", running on " + System.getProperty("os.name") + "<br/>");
        debugText = debugText.concat("Working dir: " + System.getProperty("user.dir") + "<br/>");
        debugText = debugText.concat("Look and feel theme: " + UIManager.getSystemLookAndFeelClassName() + "<br/>");
        debugText = debugText.concat("</html>");
        return debugText;
    }
}
