package BYteBOardInterface.BoardsPackage.AuthenticationPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBUser;
import BYteBOardDatabase.EncryptionUtils;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnAForumMainFrame;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.BoardButton;
import BoardControls.BoardLabel;
import BoardControls.BoardPasswordField;
import BoardControls.BoardTextField;
import BoardControls.UIPackage.GridBagBuilder;
import BoardResources.ByteBoardTheme;

import java.awt.*;

public class LoginFormPanel extends BoardPanel {

    protected BoardTextField usernameOrEmailField;
    private BoardPasswordField passwordField;

    public LoginFormPanel(Frame frame) {
        super(frame);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.weightY(1);

        // Title
        BoardLabel loginTitle = new BoardLabel("Login");
        loginTitle.addInsets(50, 0, 0, 0);
        loginTitle.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 40);
        loginTitle.setFGMain();
        builder.addToNextCell(loginTitle);

        // Container for all input fields
        BoardPanel fieldsContainer = new BoardPanel(frame, ByteBoardTheme.MAIN);
        initFields(frame, fieldsContainer);
        builder.addToNextCell(fieldsContainer);

        // Signup Option
        BoardPanel signupContainer = getSignupOptionContainer(frame);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.addToNextCell(signupContainer);
    }

    private void initFormSubmission() {
        String usernameOrEmail = usernameOrEmailField.getText();
        boolean isValidEmail = EncryptionUtils.isValidEmail(usernameOrEmail);

        if (usernameOrEmail.isEmpty()) {
            clearFieldErrors();
            usernameOrEmailField.setErrorLabel("Username Empty");
            return;
        }

        DBDataObject userData = DBUser.accessUser(usernameOrEmail, isValidEmail, false);

        if (userData == null) {
            clearFieldErrors();
            usernameOrEmailField.setErrorLabel(usernameOrEmail.contains("@") ? "Invalid Email" : "Invalid Username");
            return;
        }

        if (!EncryptionUtils.checkPwd(passwordField.getPassword(), userData.getValue(DBUser.K_PASSWORD))) {
            clearFieldErrors();
            passwordField.setErrorLabel("Incorrect Password");
            return;
        }

        clearFieldErrors();
        requestSwitchMainFrame(QnAForumMainFrame.class, userData.getValue(DBUser.K_USER_ID));
    }

    private void initFields(Frame frame, BoardPanel fieldsContainer) {
        fieldsContainer.addInsets(40);
        fieldsContainer.setCornerRadius(90);

        // Username Email Label
        BoardLabel usernameOrEmailLabel = new BoardLabel("Username");
        usernameOrEmailLabel.setFGLight();
        usernameOrEmailLabel.setAlignmentTrailing();
        usernameOrEmailLabel.addInsets(10);

        // Username Email field
        usernameOrEmailField = new BoardTextField(frame, ByteBoardTheme.MAIN_DARK, 20);
        usernameOrEmailField.setHintText("Username or Email");
//        builder.addToNextCell(usernameOrEmailField.getTextFieldContainer());

        // Password Label
        BoardLabel passwordLabel = new BoardLabel("Password");
        passwordLabel.setFGLight();
        passwordLabel.setAlignmentTrailing();
        passwordLabel.addInsets(10);

        // Password field
        passwordField = new BoardPasswordField(frame, ByteBoardTheme.MAIN_DARK, 20);
        passwordField.setHintText("********");

        // Login Button
        BoardButton loginButton = new BoardButton("Login", "login");
        loginButton.addActionListener(e -> initFormSubmission());

        GridBagBuilder builder = new GridBagBuilder(fieldsContainer, 2);

        builder.insets(10).fillBoth()
                .addToNextCell(usernameOrEmailLabel).addToNextCell(usernameOrEmailField.getTextFieldContainer())
                .addToNextCell(passwordLabel).addToNextCell(passwordField.getTextFieldContainer());

        builder.gridSize(2, 1).fillNone().insets(40, 10, 10, 10)
                .addToNextCell(loginButton);
    }

    private BoardPanel getSignupOptionContainer(Frame frame) {
        BoardPanel signupContainer = new BoardPanel(frame);
        signupContainer.addInsets(0, 0, 50, 0);

        GridBagBuilder builder = new GridBagBuilder(signupContainer);

        // Signup Label
        BoardLabel signupLabel = new BoardLabel("Don't have an account?");
        signupLabel.addInsets(0, 0, 10, 0);
        builder.addToNextCell(signupLabel);

        // Signup Button
        BoardButton signUpButton = new BoardButton("Signup", "add");
        signUpButton.setFGDark();
        signUpButton.addActionListener(e -> {
            setPanelVisibility(SignupFormPanel.class, true);
            setVisible(false);
            clearFieldErrors();
        });
        builder.addToNextCell(signUpButton);
        return signupContainer;
    }

    public void clearFieldErrors() {
        usernameOrEmailField.setErrorLabel("");
        passwordField.clearError();
    }

    public void clearFields() {
        clearFieldErrors();
        usernameOrEmailField.setText("");
        passwordField.setText("");
    }

}