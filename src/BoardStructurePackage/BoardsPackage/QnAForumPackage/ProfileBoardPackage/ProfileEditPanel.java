package BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.*;
import DatabasePackage.DBDataObject;
import DatabasePackage.DBUser;
import DatabasePackage.EncryptionUtils;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileEditPanel extends BoardPanel {

    private BoardLabel profileLabel;
    private BoardLabel editIconLabel;

    private BoardTextField usernameField;
    private BoardTextField emailField;
    private BoardPasswordField oldPasswordField;
    private BoardPasswordField newPasswordField;
    private BoardPasswordField rePasswordField;

    private BoardButton saveButton;
    private BoardButton cancelButton;

    public ProfileEditPanel(MainFrame main, Frame frame) {
        super(main, frame);

        addButtonListeners();
        setVisible(false);
    }

    public void init(MainFrame main, Frame frame) {
        GridBagBuilder builder = new GridBagBuilder(this, 3);

        BoardPanel buttonsPanel = getButtonsPanel(main, frame);
        builder.fill(GridBagConstraints.VERTICAL);
        builder.gridWeightY(1);
        builder.add(buttonsPanel, 1, 3);

        builder.fill(GridBagConstraints.NONE);
        builder.insets(10, 5, 10, 5);

        profileLabel = new BoardLabel();
        ProfileSelectionPanel profileSelectionPanel = new ProfileSelectionPanel(main, frame, profileLabel);
        builder.skipCells(2);
        builder.gridHeight(3);
        addPanel(ProfileSelectionPanel.class.getSimpleName(), profileSelectionPanel, builder.getConstraints());
        builder.gridHeight(1);

        BoardPanel fieldsPanel = getFieldsPanel(main, frame);
        builder.gridWeightX(1);
        builder.skipCells(1);
        builder.add(fieldsPanel);
    }

    public BoardPanel getProfilePanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame);
        GridBagBuilder builder = new GridBagBuilder(panel);
        builder.fill(GridBagConstraints.BOTH);
        builder.gridWeightX(1);
        builder.gridWeightY(1);

        profileLabel.addInsets(20);
        profileLabel.setProfileIcon("0", ResourceManager.LARGE);

        editIconLabel = new BoardLabel();
        editIconLabel.setName("edit_profile");

        panel.add(editIconLabel, builder.getConstraints());
        panel.add(profileLabel, builder.getConstraints());

        return panel;
    }

    public BoardPanel getFieldsPanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        panel.addInsets(25);
        panel.setCornerRadius(90);

        GridBagBuilder builder = new GridBagBuilder(panel, 3);
        builder.fill(GridBagConstraints.BOTH);
        builder.insets(10, 10, 10, 10);

        BoardPanel profilePanel = getProfilePanel(main, frame);
        builder.gridWeightY(0);
        builder.add(profilePanel, 3, 1);
        builder.skipCells(2);

        // New Username Label
        BoardLabel usernameLabel = new BoardLabel("New Username");
        usernameLabel.setFGLight();
        usernameLabel.setAlignmentTrailing();
        usernameLabel.addInsets(10);
        builder.add(usernameLabel);
        // New Username field
        usernameField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        builder.skipCells(1);
        builder.add(usernameField.getTextFieldContainer());

        // New Email Label
        BoardLabel emailLabel = new BoardLabel("New Email");
        emailLabel.setFGLight();
        emailLabel.setAlignmentTrailing();
        emailLabel.addInsets(10);
        builder.add(emailLabel);
        // New Email field
        emailField = new BoardTextField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        builder.skipCells(1);
        builder.add(emailField.getTextFieldContainer());

        // Password Label
        BoardLabel oldPasswordLabel = new BoardLabel("Old Password");
        oldPasswordLabel.setFGLight();
        oldPasswordLabel.setAlignmentTrailing();
        oldPasswordLabel.addInsets(10);
        builder.add(oldPasswordLabel);
        // Password field
        oldPasswordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        builder.skipCells(1);
        builder.add(oldPasswordField.getTextFieldContainer());

        // Password Label
        BoardLabel newPasswordLabel = new BoardLabel("New Password");
        newPasswordLabel.setFGLight();
        newPasswordLabel.setAlignmentTrailing();
        newPasswordLabel.addInsets(10);
        builder.add(newPasswordLabel);
        // Password field
        newPasswordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        builder.skipCells(1);
        builder.add(newPasswordField.getTextFieldContainer());

        // RePassword Label
        BoardLabel rePasswordLabel = new BoardLabel("Repeat Password");
        rePasswordLabel.setFGLight();
        rePasswordLabel.setAlignmentTrailing();
        rePasswordLabel.addInsets(10);
        builder.add(rePasswordLabel);
        // RePassword field
        rePasswordField = new BoardPasswordField(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        builder.skipCells(1);
        builder.add(rePasswordField.getTextFieldContainer());
        return panel;
    }

    public BoardPanel getButtonsPanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        panel.setCornerRadius(90);
        panel.addInsets(30);

        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.fill(GridBagConstraints.HORIZONTAL);
        builder.insets(10, 10, 10, 10);

        builder.gridWeightY(1);
        saveButton = new BoardButton("Save Changes", "save");
        saveButton.setAlignmentLeading();
        saveButton.setFGLight();
        builder.add(saveButton);

        builder.gridWeightY(0);
        cancelButton = new BoardButton("Cancel", "cancel");
        cancelButton.setAlignmentLeading();
        cancelButton.setFGLight();
        builder.add(cancelButton);

        return panel;
    }

    private void addButtonListeners() {

        // Profile Icon Picker
        editIconLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                editIconLabel.setIcon(editIconLabel.getName(), ResourceManager.ROLLOVER, ResourceManager.REGULAR);
            }

            public void mouseExited(MouseEvent e) {
                editIconLabel.setIcon(editIconLabel.getName(), ResourceManager.PRESSED, ResourceManager.REGULAR);
            }

            public void mousePressed(MouseEvent e) {
                editIconLabel.setIcon(editIconLabel.getName(), ResourceManager.PRESSED, ResourceManager.REGULAR);
            }

            public void mouseReleased(MouseEvent e) {
                setPanelVisibility(ProfileSelectionPanel.class.getSimpleName(), !getPanelVisibility(ProfileSelectionPanel.class.getSimpleName()));
            }
        });
        editIconLabel.getMouseListeners()[0].mouseExited(null);

        // Cancel Profile Edit
        cancelButton.addActionListener(e -> {
            setVisible(false);
            setPanelVisibility(ProfileBoardButtonPanel.class.getSimpleName(), true);
            setPanelVisibility(ProfileBoardUserDataPanel.class.getSimpleName(), true);
            setPanelVisibility(ProfileSelectionPanel.class.getSimpleName(), false);
        });

        // Save Profile Edit
        saveButton.addActionListener(e -> {
            if(saveEditedProfile())
                cancelButton.getActionListeners()[0].actionPerformed(null);
        });
    }

    private boolean saveEditedProfile() {
        String newUsername = usernameField.getText().trim();
        String newEmail = emailField.getText().trim();

        List<String> updatedValues = new ArrayList<>();

        boolean changedUsername = !usernameField.getName().equals(newUsername);
        boolean changedEmail = !emailField.getName().equals(newEmail);
        boolean changedPassword = newPasswordField.getPassword().length > 0;

        updatedValues.add(DBUser.K_USER_PROFILE);
        updatedValues.add(profileLabel.getName());

        if (changedUsername) {
            if(isInvalidOldPassword(usernameField)) return false;

            if(isValidUsername(newUsername)) {
                updatedValues.add(DBUser.K_USER_NAME);
                updatedValues.add(newUsername);
            } else return false;
        }

        if (changedEmail) {
            if(isInvalidOldPassword(emailField)) return false;

            if(isValidEmail(newEmail)) {
                updatedValues.add(DBUser.K_EMAIL);
                updatedValues.add(newEmail);
            } else return false;
        }

        if (changedPassword) {
            if(isInvalidOldPassword(null)) return false;

            if(isValidNewPassword()) {
                updatedValues.add(DBUser.K_PASSWORD);
                updatedValues.add(EncryptionUtils.encryptPwd(newPasswordField.getPassword()));
            } else return false;
        }

        clearFieldErrors();
        oldPasswordField.setText("");
        newPasswordField.setText("");
        rePasswordField.setText("");

        DBUser.ops.updateValueBy(
                DBUser.ops.matchByValue(DBUser.K_USER_NAME, usernameField.getName()),
                updatedValues.toArray(new String[0]));

        updatedValues.clear();
        updatedValues = null;

        System.out.println("Changed");
        refresh();

        return true;
    }

    private boolean isValidEmail(String newEmail) {
        if(!EncryptionUtils.isValidEmail(newEmail)) {
            clearFieldErrors();
            emailField.setErrorLabel("Invalid Email");
            return false;
        }
        return true;
    }

    private boolean isValidUsername(String newUsername) {
        if (usernameField.getText().length() <= 3) {
            clearFieldErrors();
            usernameField.setErrorLabel("Username too small");
            return false;
        }

        if (DBUser.isValueAvailable(DBUser.K_USER_NAME, newUsername)) {
            clearFieldErrors();
            usernameField.setErrorLabel("Username already taken");
            return false;
        }
        return true;
    }

    private boolean isValidNewPassword() {
        if (!EncryptionUtils.isPasswordMatching(newPasswordField.getPassword(), rePasswordField.getPassword())) {
            clearFieldErrors();
            rePasswordField.setErrorLabel("Passwords do not match");
            return false;
        }

        String passwordFeedback = EncryptionUtils.getPasswordFeedback(newPasswordField.getPassword());
        if (passwordFeedback != null) {
            clearFieldErrors();
            newPasswordField.setErrorLabel(passwordFeedback);
            return false;
        }
        return true;
    }

    private boolean isInvalidOldPassword(BoardTextField textField) {
        DBDataObject userData = DBUser.getPK(usernameField.getName(), false);
        Objects.requireNonNull(userData);

        if (!EncryptionUtils.checkPwd(oldPasswordField.getPassword(), userData.getValue(DBUser.K_PASSWORD))) {
            clearFieldErrors();
            oldPasswordField.setErrorLabel("Incorrect Password");
            if(textField == null) {
                newPasswordField.setErrorLabel("Verify Old Password");
            } else textField.setErrorLabel("Verify Old Password");
            return true;
        }

        userData.destroy();
        return false;
    }

    protected void setProfile(String profileIcon) {
        profileLabel.setProfileIcon(profileIcon, ResourceManager.REGULAR);
    }

    protected void setUsername(String username) {
        usernameField.setName(username);
        usernameField.setText(username);
    }

    protected void setUserEmail(String email) {
        emailField.setName(email);
        emailField.setText(email);
    }

    private void clearFieldErrors() {
        usernameField.setErrorLabel("");
        emailField.setErrorLabel("");
        oldPasswordField.clearError();
        newPasswordField.clearError();
        rePasswordField.clearError();
    }
}