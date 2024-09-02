package QnAForumInterface;

import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author AJ
 */
public class QnAForum extends JFrame {

    private static QnAForum instance;

    public QnAForum(String title) {
        setTitle(title);
        initComponents();
    }

    public static void logout(boolean isRepeatAuthentication) {
        AuthenticationForm.init(isRepeatAuthentication);
        instance.dispose();
    }

    public static void restart() {
        Object[] userData = InterfaceEventManager.invokeRequest("UserData.ProfileBoard");
        logout(false);
        EventQueue.invokeLater(() -> {
            AuthenticationForm.authenticateUser((String) userData[1]);
            AuthenticationForm.getInstance().setVisible(false);
        });
    }

    public static void init(JPanel panel) {
        EventQueue.invokeLater(() -> {
            instance = new QnAForum("BYteBOard QnA-Forum");
            instance.setLocationRelativeTo(null);
            setContent(panel);
            instance.setVisible(true);
        });
    }

    public static void setContent(JPanel panel) {
        if (panel == null) return;
        if (instance.getContentPane().getComponentCount() != 0) {
            instance.getContentPane().removeAll();
        }

        instance.getContentPane().add(panel, BorderLayout.CENTER);
        instance.getContentPane().repaint();
        instance.getContentPane().revalidate();
    }

    public static void printUIManagerKeys() {
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keysEnumeration = defaults.keys();
        ArrayList<Object> keysList = Collections.list(keysEnumeration);
        for (Object key : keysList) {
            System.out.println(key);
        }
    }

    private void initComponents() {
        AskBoard askBoard = new AskBoard();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        getContentPane().add(askBoard, BorderLayout.CENTER);
    }
}
