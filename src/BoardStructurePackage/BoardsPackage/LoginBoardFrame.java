package BoardStructurePackage.BoardsPackage;

import BoardStructurePackage.BoardFrame;
import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class LoginBoardFrame extends BoardFrame {

    private BoardPanel titlePanel;
    private LoginFormPanel loginFormPanel;

    public LoginBoardFrame(MainFrame main) {
        super(main, (context) -> true);
    }

    public void init(MainFrame main) {
        setLayout(new GridLayout(1, 2));

        addInsets(20);

        titlePanel = new BoardPanel(main, this, ByteBoardTheme.MAIN);
        titlePanel.setCornerRadius(90);
        titlePanel.setLayout(new BorderLayout());

        BoardLabel titleIcon = new BoardLabel("byteboard/byteboard-logo-transparent", -512);
        titlePanel.add(titleIcon, BorderLayout.CENTER);

        addPanel("byteboard", titlePanel, null);

        loginFormPanel = new LoginFormPanel(main, this);
        addPanel("loginForm", loginFormPanel, null);
    }

    private static class LoginFormPanel extends BoardPanel {

        private BoardTextField usernameOrEmailField;
        private BoardPasswordField passwordField;

        public LoginFormPanel(MainFrame main, Frame frame) {
            super(main, frame);

            GridBagBuilder builder = new GridBagBuilder(this, 1);
            builder.insets(new Insets(20, 20, 20, 20));

            // Title
            BoardLabel loginTitle = new BoardLabel("Login");
            loginTitle.addInsets(25, 0, 0, 0);
            loginTitle.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 38);
            loginTitle.setFGMain();
            builder.add(loginTitle);

            // Container for all input fields
            BoardPanel fieldsContainer = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
            initFields(main, frame, fieldsContainer);
            builder.add(fieldsContainer);

            // Signup Option
            BoardPanel signupContainer = getSignupOptionContainer(main, frame);
            signupContainer.addInsets(60, 0, 0, 0);
            builder.add(signupContainer);
        }

        private static BoardPanel getSignupOptionContainer(MainFrame main, Frame frame) {
            BoardPanel signupContainer = new BoardPanel(main, frame);
            signupContainer.setLayout(new GridLayout(2, 1));

            // Signup Label
            BoardLabel signupLabel = new BoardLabel("Don't have an account?");
            signupLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
            signupContainer.add(signupLabel);

            // Signup Button
            BoardButton signUpButton = new BoardButton("Signup", "signup");
            signUpButton.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
            signupContainer.add(signUpButton);
            return signupContainer;
        }

        private void initFields(MainFrame main, Frame frame, BoardPanel fieldsContainer) {
            fieldsContainer.addInsets(40);
            fieldsContainer.setCornerRadius(90);

            GridBagBuilder builder = new GridBagBuilder(fieldsContainer, 2);

            builder.fill(GridBagConstraints.BOTH);
            builder.insets(new Insets(10, 10, 10, 10));

            // Username Email Label
            BoardLabel usernameOrEmailLabel = new BoardLabel("Username or Email");
            usernameOrEmailLabel.setFGLight();
            usernameOrEmailLabel.setAlignmentTrailing();
            usernameOrEmailLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
            usernameOrEmailLabel.addInsets(10);
            builder.add(usernameOrEmailLabel);

            // Username Email field
            usernameOrEmailField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
            usernameOrEmailField.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
            builder.add(usernameOrEmailField.getTextFieldContainer());

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

            // Login Button
            BoardButton signUpButton = new BoardButton("Login", "login");
            signUpButton.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
            signUpButton.setFGLight();
            builder.fill(GridBagConstraints.NONE);
            builder.insets(new Insets(40, 10, 10, 10));
            builder.add(signUpButton, 2, 1);
        }

    }
}
