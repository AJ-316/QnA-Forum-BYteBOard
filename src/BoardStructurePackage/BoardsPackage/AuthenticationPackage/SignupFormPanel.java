package BoardStructurePackage.BoardsPackage.AuthenticationPackage;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.*;
import DatabasePackage.DBDataObject;
import DatabasePackage.DBUser;
import DatabasePackage.EncryptionUtils;
import QnAForumInterface.AuthenticationForm;
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
        builder.insets(new Insets(20, 20, 20, 20));

        // Title
        BoardLabel signupTitle = new BoardLabel("Signup");
        signupTitle.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 38);
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
            usernameField.setErrorLabel("Username too small");
            return;
        }

        if (!DBUser.isValueAvailable(DBUser.K_USER_NAME, username)) {
            usernameField.setErrorLabel("Username already taken");
            return;
        }

        usernameField.setErrorLabel("");

        if (!EncryptionUtils.isValidEmail(email)) {
            emailField.setErrorLabel("Invalid Email");
            return;
        }

        emailField.setErrorLabel("");

        if (!isPasswordConfirmed()) {
            rePasswordField.setErrorLabel("Passwords do not match");
            return;
        }

        rePasswordField.setErrorLabel("");

        String passwordFeedback = EncryptionUtils.getPasswordFeedback(passwordField.getPassword());
        if (passwordFeedback != null) {
            passwordField.setErrorLabel(passwordFeedback);
            return;
        }

        passwordField.setErrorLabel("");

        // fixme - register and authenticate user
        // DBUser.addUser(username, EncryptionUtils.encryptPwd(passwordField.getPassword()), email.toLowerCase());
        // AuthenticationForm.authenticateUser(username);
    }

    private boolean isPasswordConfirmed() {
        if (passwordField.getPassword().length != rePasswordField.getPassword().length)
            return false;

        for (int i = 0; i < passwordField.getPassword().length; i++) {
            if (passwordField.getPassword()[i] != rePasswordField.getPassword()[i])
                return false;
        }

        return true;
    }

    private void initFields(MainFrame main, Frame frame, BoardPanel fieldsContainer) {
        fieldsContainer.addInsets(20, 40, 20, 40);
        fieldsContainer.setCornerRadius(90);

        GridBagBuilder builder = new GridBagBuilder(fieldsContainer, 2);

        builder.fill(GridBagConstraints.BOTH);
        builder.insets(new Insets(5, 5, 5, 5));

        // Username Label
        BoardLabel usernameLabel = new BoardLabel("Username");
        usernameLabel.setFGLight();
        usernameLabel.setAlignmentTrailing();
        usernameLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        usernameLabel.addInsets(10);
        builder.add(usernameLabel);
        // Username field
        usernameField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        usernameField.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        builder.add(usernameField.getTextFieldContainer());

        // Email Label
        BoardLabel emailLabel = new BoardLabel("Email");
        emailLabel.setFGLight();
        emailLabel.setAlignmentTrailing();
        emailLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        emailLabel.addInsets(10);
        builder.add(emailLabel);
        // Email field
        emailField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        emailField.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        builder.add(emailField.getTextFieldContainer());

        // Password Label
        BoardLabel passwordLabel = new BoardLabel("Password");
        passwordLabel.setFGLight();
        passwordLabel.setAlignmentTrailing();
        passwordLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        passwordLabel.addInsets(10);
        builder.add(passwordLabel);
        // Password field
        passwordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        passwordField.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        builder.add(passwordField.getTextFieldContainer());

        // RePassword Label
        BoardLabel rePasswordLabel = new BoardLabel("Repeat Password");
        rePasswordLabel.setFGLight();
        rePasswordLabel.setAlignmentTrailing();
        rePasswordLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        rePasswordLabel.addInsets(10);
        builder.add(rePasswordLabel);
        // RePassword field
        rePasswordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        rePasswordField.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        builder.add(rePasswordField.getTextFieldContainer());

        // Login Button
        BoardButton signupButton = new BoardButton("Signup", "signup");
        signupButton.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
        signupButton.setFGLight();
        signupButton.addActionListener(e -> initFormSubmission());
        builder.fill(GridBagConstraints.NONE);
        builder.insets(new Insets(20, 10, 10, 10));
        builder.add(signupButton, 2, 1);
    }

    private BoardPanel getLoginOptionContainer(MainFrame main, Frame frame) {
        BoardPanel loginContainer = new BoardPanel(main, frame);
        loginContainer.setLayout(new GridLayout(2, 1));

        // Signup Label
        BoardLabel loginLabel = new BoardLabel("Already a BYteBOard user?");
        loginLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        loginContainer.add(loginLabel);

        // Signup Button
        BoardButton loginButton = new BoardButton("Login", "login");
        loginButton.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
        loginButton.addActionListener(e -> {
            setPanelVisibility("loginForm", true);
            setVisible(false);
            clearFieldErrors();
        });
        loginContainer.add(loginButton);
        return loginContainer;
    }

    private void clearFieldErrors() {
        usernameField.setErrorLabel("");
        passwordField.setErrorLabel("");
    }

}