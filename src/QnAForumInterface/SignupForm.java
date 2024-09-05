/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;

import DatabasePackage.DBUser;
import DatabasePackage.EncryptionUtils;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author AJ
 */
public class SignupForm extends JPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField emailField;
    private JLabel emailLabel;
    private JButton loginBtn;
    private JLabel loginInfoLabel;
    private JPasswordField pwdCheckField;
    private JLabel pwdCheckLabel;
    private JPasswordField pwdField;
    private JLabel pwdLabel;
    private JButton signupBtn;
    private JLabel signupErrorLabel;
    private JLabel titleLabel;
    private JTextField usernameField;
    private JLabel usernameLabel;
    /**
     * Creates new form LoginForm
     */
    public SignupForm() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        titleLabel = new JLabel();
        usernameLabel = new JLabel();
        usernameField = new JTextField();
        emailLabel = new JLabel();
        emailField = new JTextField();
        pwdLabel = new JLabel();
        pwdField = new JPasswordField();
        pwdCheckLabel = new JLabel();
        pwdCheckField = new JPasswordField();
        signupErrorLabel = new JLabel();
        signupBtn = new JButton();
        loginInfoLabel = new JLabel();
        loginBtn = new JButton();

        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setLayout(new GridBagLayout());

        titleLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        titleLabel.setText("Register");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 1.0;
        add(titleLabel, gridBagConstraints);
        titleLabel.setFont(ResourceManager.getFont("inter_semibold.48"));

        usernameLabel.setText("Username:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        add(usernameLabel, gridBagConstraints);
        usernameLabel.setFont(ResourceManager.getFont("inter_regular.26"));
        usernameLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        usernameField.setMinimumSize(new Dimension(200, 39));
        usernameField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        add(usernameField, gridBagConstraints);
        usernameField.setFont(ResourceManager.getFont("inter_regular.24"));
        usernameField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        usernameField.setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        usernameField.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        emailLabel.setText("Email-ID:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        add(emailLabel, gridBagConstraints);
        emailLabel.setFont(ResourceManager.getFont("inter_regular.26"));
        emailLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        emailField.setMinimumSize(new Dimension(200, 39));
        emailField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        add(emailField, gridBagConstraints);
        emailField.setFont(ResourceManager.getFont("inter_regular.24"));
        emailField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        emailField.setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        emailField.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        pwdLabel.setText("Password:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        add(pwdLabel, gridBagConstraints);
        pwdLabel.setFont(ResourceManager.getFont("inter_regular.26"));
        pwdLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        pwdField.setMinimumSize(new Dimension(200, 29));
        pwdField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        add(pwdField, gridBagConstraints);
        pwdField.setFont(ResourceManager.getFont("inter_regular.18"));
        pwdField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        pwdField.setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        pwdField.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        pwdCheckLabel.setText("Re- Enter Password:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        add(pwdCheckLabel, gridBagConstraints);
        pwdCheckLabel.setFont(ResourceManager.getFont("inter_regular.26"));
        pwdCheckLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        pwdCheckField.setMinimumSize(new Dimension(200, 29));
        pwdCheckField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        add(pwdCheckField, gridBagConstraints);
        pwdCheckField.setFont(ResourceManager.getFont("inter_regular.18"));
        pwdCheckField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        pwdCheckField.setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        pwdCheckField.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        signupErrorLabel.setForeground(new Color(210, 0, 0));
        signupErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(signupErrorLabel, gridBagConstraints);
        signupErrorLabel.setFont(ResourceManager.getFont("inter_regular.18"));

        signupBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        signupBtn.setText("Sign Up");
        signupBtn.setBorderPainted(false);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setFocusPainted(false);
        ResourceManager.setButtonIcons(signupBtn, "signup", ResourceManager.MINI);
        signupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                signupBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new Insets(50, 0, 20, 0);
        add(signupBtn, gridBagConstraints);
        signupBtn.setFont(ResourceManager.getFont("inter_regular.26"));

        loginInfoLabel.setText("Already have an account?");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new Insets(20, 10, 10, 10);
        add(loginInfoLabel, gridBagConstraints);
        loginInfoLabel.setFont(ResourceManager.getFont("inter_regular.20"));
        loginInfoLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        loginBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        loginBtn.setText("Log In");
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setFocusPainted(false);
        ResourceManager.setButtonIcons(loginBtn, "login", ResourceManager.MINI);
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new Insets(5, 10, 25, 10);
        add(loginBtn, gridBagConstraints);
        loginBtn.setFont(ResourceManager.getFont("inter_regular.20"));
    }// </editor-fold>//GEN-END:initComponents

    private void signupBtnActionPerformed(ActionEvent evt) {
        String username = usernameField.getText();
        String email = emailField.getText();

        if (username.length() <= 3) {
            signupErrorLabel.setText("Username too small");
            return;
        }

        if (!DBUser.isValueAvailable(DBUser.K_USER_NAME, username)) {
            signupErrorLabel.setText("Username already taken");
            return;
        }

        if (!EncryptionUtils.isValidEmail(email)) {
            signupErrorLabel.setText("Invaild Email");
            return;
        }

        if (!isPasswordConfirmed()) {
            signupErrorLabel.setText("Passwords do not match");
            return;
        }

        if (!EncryptionUtils.isValidPassword(pwdField.getPassword())) {
            if (pwdField.getPassword().length < 8 || pwdField.getPassword().length > 20) {
                signupErrorLabel.setText("Password length must be 8-20");
                return;
            }
            signupErrorLabel.setText("Include Uppercase, Lowercase, Digit, and Special Character");
            return;
        }

        // UserDataObject userData = new UserDataObject(username, password, email.toLowerCase());
        // DECRYPTED Database.insertData(userData);

        DBUser.addUser(username, EncryptionUtils.encryptPwd(pwdField.getPassword()), email.toLowerCase());
        AuthenticationForm.authenticateUser(username);
    }

    private boolean isPasswordConfirmed() {
        if (pwdField.getPassword().length != pwdCheckField.getPassword().length)
            return false;

        for (int i = 0; i < pwdField.getPassword().length; i++) {
            if (pwdField.getPassword()[i] != pwdCheckField.getPassword()[i])
                return false;
        }

        return true;
    }

    private void loginBtnActionPerformed(ActionEvent evt) {
        AuthenticationForm.setRegisterUser(false);
    }
    // End of variables declaration//GEN-END:variables


    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        usernameField.setText("");
        pwdCheckField.setText("");
        emailField.setText("");
        pwdField.setText("");
        signupErrorLabel.setText("");
    }
}
