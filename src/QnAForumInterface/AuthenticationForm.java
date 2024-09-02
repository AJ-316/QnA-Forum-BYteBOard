/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package QnAForumInterface;


import CustomControls.RoundedJPanel;
import QnAForumInterface.ProfileBoardPackage.ProfileBoard;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author AJ
 */
public class AuthenticationForm extends JFrame {

    private static AuthenticationForm instance;
    private QnAForumInterface.LoginForm loginForm;
    private QnAForumInterface.SignupForm signupForm;

    public AuthenticationForm() {
        initComponents();
    }

    public static void directAccess(String userName) {
        authenticateUser(userName);
    }

    public static void init() {
        EventQueue.invokeLater(() -> {
            instance = new AuthenticationForm();
            instance.setVisible(true);
            instance.setLocationRelativeTo(null);
            setRegisterUser(false);
        });
    }

    public static void authenticateUser(String username) {
        QnAForum.init(ProfileBoard.init(username));
        if(instance != null)
            instance.dispose();
    }

    public static void setRegisterUser(boolean isRegister) {
        instance.loginForm.setVisible(!isRegister);
        instance.signupForm.setVisible(isRegister);
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        JPanel contentPane = new JPanel();
        RoundedJPanel roundedJPanel1 = new RoundedJPanel();
        signupForm = new QnAForumInterface.SignupForm();
        loginForm = new QnAForumInterface.LoginForm();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(ResourceManager.getColor("base"));
        setMinimumSize(new Dimension(1280, 720));

        contentPane.setBackground(ResourceManager.getColor("base"));
        contentPane.setLayout(new GridBagLayout());

        roundedJPanel1.setBackground(ResourceManager.getColor("main"));
        roundedJPanel1.setCornerRadius(120);
        roundedJPanel1.setLayout(new BorderLayout());

        JLabel name = new JLabel();
        name.setBackground(ResourceManager.getColor("base"));
        name.setForeground(ResourceManager.getColor("text_fg_light"));
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setIcon(ResourceManager.getIcon("byteboard/byteboard-logo-transparent", -384));
        name.setBorder(BorderFactory.createEmptyBorder(75, 75, 75, 75));
        name.setFont(ResourceManager.getFont("carltine_bold_italic.48"));
        roundedJPanel1.add(name, BorderLayout.CENTER);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        contentPane.add(roundedJPanel1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        contentPane.add(signupForm, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        contentPane.add(loginForm, gridBagConstraints);

        getContentPane().add(contentPane, BorderLayout.CENTER);

        pack();
    }
}
