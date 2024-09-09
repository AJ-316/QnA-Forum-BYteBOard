package BoardStructurePackage.BoardsPackage.AuthenticationPackage;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.BoardsPackage.QnAForumPackage.QnAForumMainFrame;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.*;
import DatabasePackage.DBDataObject;
import DatabasePackage.DBUser;
import DatabasePackage.EncryptionUtils;
import QnAForumInterface.AuthenticationForm;
import Resources.ByteBoardTheme;

import java.awt.*;

public class LoginFormPanel extends BoardPanel {

    private BoardTextField usernameOrEmailField;
    private BoardPasswordField passwordField;

    public LoginFormPanel(MainFrame main, Frame frame) {
        super(main, frame);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.gridWeightY(1);

        // Title
        BoardLabel loginTitle = new BoardLabel("Login");
        loginTitle.addInsets(50, 0, 0, 0);
        loginTitle.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 40);
        loginTitle.setFGMain();
        builder.add(loginTitle);

        // Container for all input fields
        BoardPanel fieldsContainer = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        initFields(main, frame, fieldsContainer);
        builder.add(fieldsContainer);

        // Signup Option
        BoardPanel signupContainer = getSignupOptionContainer(main, frame);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.add(signupContainer);
    }

    private void initFormSubmission() {
        String usernameOrEmail = usernameOrEmailField.getText();
        boolean isValidEmail = EncryptionUtils.isValidEmail(usernameOrEmail);

        if(usernameOrEmail.isEmpty()) {
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
        requestSwitchMainFrame(QnAForumMainFrame.ID, userData.getValue(DBUser.K_USER_ID));
    }

    private void initFields(MainFrame main, Frame frame, BoardPanel fieldsContainer) {
        fieldsContainer.addInsets(40);
        fieldsContainer.setCornerRadius(90);

        GridBagBuilder builder = new GridBagBuilder(fieldsContainer, 2);

        builder.fill(GridBagConstraints.BOTH);
        builder.insets(10, 10, 10, 10);

        // Username Email Label
        BoardLabel usernameOrEmailLabel = new BoardLabel("Username");
        usernameOrEmailLabel.setFGLight();
        usernameOrEmailLabel.setAlignmentTrailing();
        usernameOrEmailLabel.addInsets(10);
        builder.add(usernameOrEmailLabel);

        // Username Email field
        usernameOrEmailField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        usernameOrEmailField.setHintText("Username or Email");
        builder.add(usernameOrEmailField.getTextFieldContainer());

        // Password Label
        BoardLabel passwordLabel = new BoardLabel("Password");
        passwordLabel.setFGLight();
        passwordLabel.setAlignmentTrailing();
        passwordLabel.addInsets(10);
        builder.add(passwordLabel);

        // Password field
        passwordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        passwordField.setHintText("********");
        builder.add(passwordField.getTextFieldContainer());

        // Login Button
        BoardButton loginButton = new BoardButton("Login", "login");
        loginButton.setFGLight();
        loginButton.addActionListener(e -> initFormSubmission());
        builder.fill(GridBagConstraints.NONE);
        builder.insets(40, 10, 10, 10);
        builder.add(loginButton, 2, 1);
    }

    private BoardPanel getSignupOptionContainer(MainFrame main, Frame frame) {
        BoardPanel signupContainer = new BoardPanel(main, frame);
        signupContainer.addInsets(0, 0, 50, 0);

        GridBagBuilder builder = new GridBagBuilder(signupContainer);

        // Signup Label
        BoardLabel signupLabel = new BoardLabel("Don't have an account?");
        signupLabel.addInsets(0, 0, 10, 0);
        builder.add(signupLabel);

        // Signup Button
        BoardButton signUpButton = new BoardButton("Signup", "signup");
        signUpButton.addActionListener(e -> {
            setPanelVisibility("signupForm", true);
            setVisible(false);
            clearFieldErrors();
        });
        builder.add(signUpButton);
        return signupContainer;
    }

    private void clearFieldErrors() {
        usernameOrEmailField.setErrorLabel("");
        passwordField.clearError();
    }

}