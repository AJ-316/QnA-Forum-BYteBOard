package BYteBOardInterface.BoardsPackage.AuthenticationPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBUser;
import BYteBOardDatabase.EncryptionUtils;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnAForumMainFrame;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import java.awt.*;

public class SignupFormPanel extends BoardPanel {

    private BoardTextField usernameField;
    private BoardTextField emailField;
    private BoardPasswordField passwordField;
    private BoardPasswordField rePasswordField;

    public SignupFormPanel(Frame frame) {
        super(frame);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.insets(0, 0, 0, 0);
        builder.weightY(1);

        // Title
        BoardLabel signupTitle = new BoardLabel("Signup");
        signupTitle.addInsets(50, 0, 0, 0);
        signupTitle.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 40);
        signupTitle.setFGMain();
        builder.addToNextCell(signupTitle);

        // Container for all input fields
        BoardPanel fieldsContainer = new BoardPanel(frame, ByteBoardTheme.MAIN);
        initFields(frame, fieldsContainer);
        builder.addToNextCell(fieldsContainer);

        // Login Option
        BoardPanel loginContainer = getLoginOptionContainer(frame);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.addToNextCell(loginContainer);
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
        requestSwitchMainFrame(QnAForumMainFrame.class, userData.getValue(DBUser.K_USER_ID));
    }

    private void initFields(Frame frame, BoardPanel fieldsContainer) {
        fieldsContainer.addInsets(20, 40, 20, 40);
        fieldsContainer.setCornerRadius(90);

        //GridBagBuilder builder = new GridBagBuilder(fieldsContainer, 2);

//        builder.fill(GridBagConstraints.BOTH);
//        builder.insets(5, 13, 5, 14);

        // Username Label
        BoardLabel usernameLabel = new BoardLabel("Username");
        usernameLabel.setFGLight();
        usernameLabel.setAlignmentTrailing();
        usernameLabel.addInsets(10);
//        builder.addToNextCell(usernameLabel);
        // Username field
        usernameField = new BoardTextField(frame, ByteBoardTheme.MAIN_DARK, 20);
        usernameField.setHintText("Display Name");
//        builder.addToNextCell(usernameField.getTextFieldContainer());

        // Email Label
        BoardLabel emailLabel = new BoardLabel("Email");
        emailLabel.setFGLight();
        emailLabel.setAlignmentTrailing();
        emailLabel.addInsets(10);
//        builder.addToNextCell(emailLabel);
        // Email field
        emailField = new BoardTextField(frame, ByteBoardTheme.MAIN_DARK, 20);
        emailField.setHintText("example@gmail.com");
//        builder.addToNextCell(emailField.getTextFieldContainer());

        // Password Label
        BoardLabel passwordLabel = new BoardLabel("Password");
        passwordLabel.setFGLight();
        passwordLabel.setAlignmentTrailing();
        passwordLabel.addInsets(10);
//        builder.addToNextCell(passwordLabel);
        // Password field
        passwordField = new BoardPasswordField(frame, ByteBoardTheme.MAIN_DARK, 20);
        passwordField.setHintText("New Password");
//        builder.addToNextCell(passwordField.getTextFieldContainer());

        // RePassword Label
        BoardLabel rePasswordLabel = new BoardLabel("Re-Password");
        rePasswordLabel.setFGLight();
        rePasswordLabel.setAlignmentTrailing();
        rePasswordLabel.addInsets(10);
//        builder.addToNextCell(rePasswordLabel);
        // RePassword field
        rePasswordField = new BoardPasswordField(frame, ByteBoardTheme.MAIN_DARK, 20);
        rePasswordField.setHintText("Repeat New Password");
//        builder.addToNextCell(rePasswordField.getTextFieldContainer());

        // Signup Button
        BoardButton signupButton = new BoardButton("Signup", "add");
        signupButton.setFGLight();
        signupButton.addActionListener(e -> initFormSubmission());
//        builder.fill(GridBagConstraints.NONE);
//        builder.insets(20, 10, 10, 10);
//        builder.addToNextCell(signupButton, 2, 1);

        GridBagBuilder builder = new GridBagBuilder(fieldsContainer, 2);
        builder.insets(5, 13, 5, 14).fillBoth()
                .addToNextCell(usernameLabel).addToNextCell(usernameField.getTextFieldContainer())
                .addToNextCell(emailLabel).addToNextCell(emailField.getTextFieldContainer())
                .addToNextCell(passwordLabel).addToNextCell(passwordField.getTextFieldContainer())
                .addToNextCell(rePasswordLabel).addToNextCell(rePasswordField.getTextFieldContainer());

        builder.gridSize(2, 1).fillNone().insets(20, 10, 10, 10)
                .addToNextCell(signupButton);
    }

    private BoardPanel getLoginOptionContainer(Frame frame) {
        BoardPanel loginContainer = new BoardPanel(frame);
        loginContainer.addInsets(0, 0, 50, 0);

        GridBagBuilder builder = new GridBagBuilder(loginContainer);

        // Login Label
        BoardLabel loginLabel = new BoardLabel("Already a BYteBOard user?");
        loginLabel.addInsets(0, 0, 10, 0);
        builder.addToNextCell(loginLabel);

        // Login Button
        BoardButton loginButton = new BoardButton("Login", "login");
        loginButton.addActionListener(e -> {
            setPanelVisibility(LoginFormPanel.class, true);
            setVisible(false);
            clearFieldErrors();
        });
        builder.addToNextCell(loginButton);
        return loginContainer;
    }

    public void clearFieldErrors() {
        usernameField.setErrorLabel("");
        emailField.setErrorLabel("");
        passwordField.clearError();
        rePasswordField.clearError();
    }

    public void clearFields() {
        clearFieldErrors();
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        rePasswordField.setText("");
    }

}