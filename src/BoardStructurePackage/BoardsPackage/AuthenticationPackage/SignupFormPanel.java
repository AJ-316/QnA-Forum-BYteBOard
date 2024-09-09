package BoardStructurePackage.BoardsPackage.AuthenticationPackage;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.BoardsPackage.QnAForumPackage.QnAForumMainFrame;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.*;
import DatabasePackage.DBDataObject;
import DatabasePackage.DBUser;
import DatabasePackage.EncryptionUtils;
import Resources.ByteBoardTheme;

import java.awt.*;

public class SignupFormPanel extends BoardPanel {

    private BoardTextField usernameField;
    private BoardTextField emailField;
    private BoardPasswordField passwordField;
    private BoardPasswordField rePasswordField;

    public SignupFormPanel(MainFrame main, Frame frame) {
        super(main, frame);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.insets(0, 0, 0, 0);
        builder.gridWeightY(1);

        // Title
        BoardLabel signupTitle = new BoardLabel("Signup");
        signupTitle.addInsets(50, 0, 0, 0);
        signupTitle.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 40);
        signupTitle.setFGMain();
        builder.add(signupTitle);

        // Container for all input fields
        BoardPanel fieldsContainer = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        initFields(main, frame, fieldsContainer);
        builder.add(fieldsContainer);

        // Login Option
        BoardPanel loginContainer = getLoginOptionContainer(main, frame);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.add(loginContainer);
    }

    private void initFormSubmission() {
        String username = usernameField.getText();
        String email = emailField.getText();

        if (username.length() <= 3) {
            clearFieldErrors();
            usernameField.setErrorLabel("Username too small");
            return;
        }

        if (DBUser.isValueAvailable(DBUser.K_USER_NAME, username)) {
            clearFieldErrors();
            usernameField.setErrorLabel("Username already taken");
            return;
        }

        if (!EncryptionUtils.isValidEmail(email)) {
            clearFieldErrors();
            emailField.setErrorLabel("Invalid Email");
            return;
        }

        if (!EncryptionUtils.isPasswordMatching(passwordField.getPassword(), rePasswordField.getPassword())) {
            clearFieldErrors();
            rePasswordField.setErrorLabel("Passwords do not match");
            return;
        }

        String passwordFeedback = EncryptionUtils.getPasswordFeedback(passwordField.getPassword());
        if (passwordFeedback != null) {
            clearFieldErrors();
            passwordField.setErrorLabel(passwordFeedback);
            return;
        }

        clearFieldErrors();
        DBUser.addUser(username, EncryptionUtils.encryptPwd(passwordField.getPassword()), email.toLowerCase());
        DBDataObject userData = DBUser.accessUser(username, false, true);
        requestSwitchMainFrame(QnAForumMainFrame.ID, userData.getValue(DBUser.K_USER_ID));
    }

    private void initFields(MainFrame main, Frame frame, BoardPanel fieldsContainer) {
        fieldsContainer.addInsets(20, 40, 20, 40);
        fieldsContainer.setCornerRadius(90);

        GridBagBuilder builder = new GridBagBuilder(fieldsContainer, 2);

        builder.fill(GridBagConstraints.BOTH);
        builder.insets(5, 13, 5, 14);

        // Username Label
        BoardLabel usernameLabel = new BoardLabel("Username");
        usernameLabel.setFGLight();
        usernameLabel.setAlignmentTrailing();
        usernameLabel.addInsets(10);
        builder.add(usernameLabel);
        // Username field
        usernameField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        usernameField.setHintText("Display Name");
        builder.add(usernameField.getTextFieldContainer());

        // Email Label
        BoardLabel emailLabel = new BoardLabel("Email");
        emailLabel.setFGLight();
        emailLabel.setAlignmentTrailing();
        emailLabel.addInsets(10);
        builder.add(emailLabel);
        // Email field
        emailField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        emailField.setHintText("example@gmail.com");
        builder.add(emailField.getTextFieldContainer());

        // Password Label
        BoardLabel passwordLabel = new BoardLabel("Password");
        passwordLabel.setFGLight();
        passwordLabel.setAlignmentTrailing();
        passwordLabel.addInsets(10);
        builder.add(passwordLabel);
        // Password field
        passwordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        passwordField.setHintText("New Password");
        builder.add(passwordField.getTextFieldContainer());

        // RePassword Label
        BoardLabel rePasswordLabel = new BoardLabel("Re-Password");
        rePasswordLabel.setFGLight();
        rePasswordLabel.setAlignmentTrailing();
        rePasswordLabel.addInsets(10);
        builder.add(rePasswordLabel);
        // RePassword field
        rePasswordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        rePasswordField.setHintText("Repeat New Password");
        builder.add(rePasswordField.getTextFieldContainer());

        // Signup Button
        BoardButton signupButton = new BoardButton("Signup", "signup");
        signupButton.setFGLight();
        signupButton.addActionListener(e -> initFormSubmission());
        builder.fill(GridBagConstraints.NONE);
        builder.insets(20, 10, 10, 10);
        builder.add(signupButton, 2, 1);
    }

    private BoardPanel getLoginOptionContainer(MainFrame main, Frame frame) {
        BoardPanel loginContainer = new BoardPanel(main, frame);
        loginContainer.addInsets(0, 0, 50, 0);

        GridBagBuilder builder = new GridBagBuilder(loginContainer);

        // Login Label
        BoardLabel loginLabel = new BoardLabel("Already a BYteBOard user?");
        loginLabel.addInsets(0, 0, 10, 0);
        builder.add(loginLabel);

        // Login Button
        BoardButton loginButton = new BoardButton("Login", "login");
        loginButton.addActionListener(e -> {
            setPanelVisibility("loginForm", true);
            setVisible(false);
            clearFieldErrors();
        });
        builder.add(loginButton);
        return loginContainer;
    }

    private void clearFieldErrors() {
        usernameField.setErrorLabel("");
        emailField.setErrorLabel("");
        passwordField.clearError();
        rePasswordField.clearError();
    }

}