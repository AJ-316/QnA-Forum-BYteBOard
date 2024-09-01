/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package QnAForumInterface;

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
    private QnAForumInterface.AskBoard askBoard;

    public QnAForum() {
        initComponents();
    }

    public static void logout() {
        AuthenticationForm.init();
        instance.dispose();
    }

    public static void init(JPanel panel) {
        EventQueue.invokeLater(() -> {
            instance = new QnAForum();
            instance.setVisible(true);
            instance.setLocationRelativeTo(null);
            setContent(panel);
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

        askBoard = new QnAForumInterface.AskBoard();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        getContentPane().add(askBoard, BorderLayout.CENTER);
    }
}
