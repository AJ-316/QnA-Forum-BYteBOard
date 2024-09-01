/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;

import DataObjects.UserDataObject;
import QnAForumDatabase.Database;
import QnAForumDatabase.EncryptionUtils;

import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author AJ
 */
public class LoginForm extends JPanel {

    private JTextField idField;
    private JLabel idLabel;
    private JButton loginBtn;
    private JLabel loginErrorLabel;
    private JPasswordField pwdField;
    private JLabel pwdLabel;
    private JToggleButton signupBtn;
    private JLabel signupInfoLabel;
    private JLabel titleLabel;
    public LoginForm() {
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        titleLabel = new JLabel();
        idLabel = new JLabel();
        idField = new JTextField();
        pwdLabel = new JLabel();
        pwdField = new JPasswordField();
        loginErrorLabel = new JLabel();
        loginBtn = new JButton();
        signupInfoLabel = new JLabel();
        signupBtn = new JToggleButton();

        setBackground(ResourceManager.getColor("base"));
        setLayout(new GridBagLayout());

        titleLabel.setForeground(ResourceManager.getColor("main"));
        titleLabel.setText("Login");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 1.0;
        add(titleLabel, gridBagConstraints);
        titleLabel.setFont(ResourceManager.getFont("inter_semibold.48"));

        idLabel.setText("Username or Email:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        add(idLabel, gridBagConstraints);
        idLabel.setFont(ResourceManager.getFont("inter_regular.26"));

        idField.setMinimumSize(new Dimension(200, 39));
        idField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 10, 40);
        add(idField, gridBagConstraints);
        idField.setFont(ResourceManager.getFont("inter_regular.24"));

        pwdLabel.setText("Password:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 10, 50, 10);
        add(pwdLabel, gridBagConstraints);
        pwdLabel.setFont(ResourceManager.getFont("inter_regular.26"));

        pwdField.setMinimumSize(new Dimension(200, 29));
        pwdField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 50, 40);
        add(pwdField, gridBagConstraints);
        pwdField.setFont(ResourceManager.getFont("inter_regular.18"));

        loginErrorLabel.setForeground(new Color(210, 0, 0));
        loginErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(loginErrorLabel, gridBagConstraints);
        loginErrorLabel.setFont(ResourceManager.getFont("inter_regular.18"));

        loginBtn.setForeground(new Color(10, 120, 120));
        loginBtn.setIcon(ResourceManager.getIcon("login_default", 32)
        );
        loginBtn.setText("Log In");
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setPressedIcon(ResourceManager.getIcon("login_pressed", 32)
        );
        loginBtn.setRolloverIcon(ResourceManager.getIcon("login_rollover", 32)
        );
        loginBtn.addActionListener(evt -> loginBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(50, 0, 20, 0);
        add(loginBtn, gridBagConstraints);
        loginBtn.setFont(ResourceManager.getFont("inter_regular.26"));

        signupInfoLabel.setText("Don't have an account?");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(20, 10, 10, 10);
        add(signupInfoLabel, gridBagConstraints);
        signupInfoLabel.setFont(ResourceManager.getFont("inter_regular.20"));

        signupBtn.setForeground(ResourceManager.getColor("main_dark"));
        signupBtn.setIcon(ResourceManager.getIcon("signup_default", 32)
        );
        signupBtn.setText("Sign Up");
        signupBtn.setBorderPainted(false);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setFocusPainted(false);
        signupBtn.setPressedIcon(ResourceManager.getIcon("signup_pressed", 32)
        );
        signupBtn.setRolloverIcon(ResourceManager.getIcon("signup_rollover", 32)
        );
        signupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                signupBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new Insets(5, 10, 25, 10);
        add(signupBtn, gridBagConstraints);
        signupBtn.setFont(ResourceManager.getFont("inter_regular.20"));
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(ActionEvent evt) {
        boolean isIDEmail = EncryptionUtils.isValidEmail(idField.getText());

        if (!EncryptionUtils.isValidPassword(pwdField.getPassword())) {
            loginErrorLabel.setText("Incorrect Password");
        }

        // For Registration
        /*if(!EncryptionUtils.isValidPassword(pwdField.getPassword())) {
            if(pwdField.getPassword().length < 8 || pwdField.getPassword().length > 20) {
                loginErrorLabel.setText("Password length must be 8-20");
                return;
            }
            loginErrorLabel.setText("Include Uppercase, Lowercase, Digit, and Special Character");
            return;
        }*/

        UserDataObject userData = Database.getUserInfo(idField.getText(), isIDEmail);
        if (userData == null) {
            loginErrorLabel.setText(isIDEmail ? "Invalid Email" : "Invalid Username");
            return;
        }

        if (!EncryptionUtils.checkPwd(pwdField.getPassword(), userData.get("password"))) {
            loginErrorLabel.setText("Incorrect Password");
            return;
        }

        loginErrorLabel.setText("");

        String id = idField.getText();

        if (isIDEmail) {
            id = Database.getData(UserDataObject.TABLE, UserDataObject.usernameKey(),
                    UserDataObject.emailKey(), id, true)[0];
        }

        AuthenticationForm.authenticateUser(id);
    }

    private void signupBtnActionPerformed(ActionEvent evt) {
        AuthenticationForm.setRegisterUser(true);
    }
}
