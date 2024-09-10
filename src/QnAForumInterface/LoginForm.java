package QnAForumInterface;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBUser;
import BYteBOardDatabase.EncryptionUtils;

import Resources.ByteBoardTheme;
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
    private JButton signupBtn;
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
        signupBtn = new JButton();

        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setLayout(new GridBagLayout());

        titleLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN));
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
        idLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        idField.setMinimumSize(new Dimension(200, 39));
        idField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 10, 40);
        add(idField, gridBagConstraints);
        idField.setFont(ResourceManager.getFont("inter_regular.24"));
        idField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        idField.setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        idField.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        pwdLabel.setText("Password:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 10, 50, 10);
        add(pwdLabel, gridBagConstraints);
        pwdLabel.setFont(ResourceManager.getFont("inter_regular.26"));
        pwdLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        pwdField.setMinimumSize(new Dimension(200, 29));
        pwdField.setPreferredSize(new Dimension(200, 29));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 50, 40);
        add(pwdField, gridBagConstraints);
        pwdField.setFont(ResourceManager.getFont("inter_regular.18"));
        pwdField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        pwdField.setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        pwdField.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        loginErrorLabel.setForeground(new Color(210, 0, 0));
        loginErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(loginErrorLabel, gridBagConstraints);
        loginErrorLabel.setFont(ResourceManager.getFont("inter_regular.18"));

        loginBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        loginBtn.setText("Log In");
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setFocusPainted(false);
        ResourceManager.setButtonIcons(loginBtn, "login", ResourceManager.MINI);
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
        signupInfoLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        signupBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
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
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new Insets(5, 10, 25, 10);
        add(signupBtn, gridBagConstraints);
        signupBtn.setFont(ResourceManager.getFont("inter_regular.20"));
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(ActionEvent evt) {
        boolean isIDEmail = EncryptionUtils.isValidEmail(idField.getText());

        if (EncryptionUtils.isInvalidPassword(pwdField.getPassword())) {
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

        DBDataObject userData = DBUser.accessUser(idField.getText(), false, false);
        if (userData == null) {
            loginErrorLabel.setText(isIDEmail ? "Invalid Email" : "Invalid Username");
            return;
        }

        if (!EncryptionUtils.checkPwd(pwdField.getPassword(), userData.getValue(DBUser.K_PASSWORD))) {
            loginErrorLabel.setText("Incorrect Password");
            return;
        }

        loginErrorLabel.setText("");

        String id = idField.getText();

        AuthenticationForm.authenticateUser(userData.getValue(DBUser.K_USER_NAME));
    }

    private void signupBtnActionPerformed(ActionEvent evt) {
        AuthenticationForm.setRegisterUser(true);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        idField.setText("");
        pwdField.setText("");
        loginErrorLabel.setText("");
    }
}
