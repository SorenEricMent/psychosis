package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelpDialog extends JDialog {
    private static final int[] YUUTA_SEQ = { KeyEvent.VK_Y, KeyEvent.VK_U, KeyEvent.VK_U, KeyEvent.VK_T, KeyEvent.VK_A };

    private JPanel contentPane;
    private JButton buttonClose;
    private JTabbedPane helpTabs;
    private JLabel debugContent;
    private JLabel logo;
    private JLabel title;
    private int yuutaIndex = 0;

    private final KeyEventDispatcher fnEaster = (keyEvent) -> {
        if (keyEvent.getID() != KeyEvent.KEY_PRESSED) {
            return false;
        }
        if (keyEvent.getKeyCode() == YUUTA_SEQ[yuutaIndex]) {
            yuutaIndex++;
        } else {
            yuutaIndex = 0;
        }
        if (yuutaIndex > 4) {
            yuutaIndex = 0;
            logo.setIcon(new ImageIcon(new ImageIcon("./data/resources/yuuta.jpg")
                    .getImage().getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_DEFAULT)));
            title.setText("EatYuuta Studio");
        }
        return false;
    };

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

        // Easter egg for the cutest Yuuta
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(fnEaster);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(fnEaster);
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                yuutaIndex = 0;
            }
        });
        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent windowEvent) {
                yuutaIndex = 0;
            }

            @Override
            public void windowLostFocus(WindowEvent windowEvent) {
                yuutaIndex = 0;
            }
        });
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
