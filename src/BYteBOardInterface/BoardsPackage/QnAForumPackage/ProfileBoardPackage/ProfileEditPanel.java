package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBUser;
import BYteBOardDatabase.EncryptionUtils;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.BoardButton;
import BoardControls.BoardLabel;
import BoardControls.BoardPasswordField;
import BoardControls.BoardTextField;
import BoardControls.UIPackage.GridBagBuilder;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

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

    public ProfileEditPanel(Frame frame) {
        super(frame);

        addButtonListeners();
        setVisible(false);
    }

    public void init(Frame frame) {
        BoardPanel buttonsPanel = getButtonsPanel(frame);
        profileLabel = new BoardLabel();
        ProfileSelectionPanel profileSelectionPanel = new ProfileSelectionPanel(frame, profileLabel);
        BoardPanel fieldsPanel = getFieldsPanel(frame);
//        builder.fill(GridBagConstraints.VERTICAL);
//        builder.weightY(1);
//        builder.addToNextCell(buttonsPanel, 1, 3);
//
//        builder.fill(GridBagConstraints.NONE);
//        builder.insets(10, 5, 10, 5);
//        builder.skipCells(2);
//        builder.gridHeight(3);
//        addPanel(ProfileSelectionPanel.class, profileSelectionPanel, builder.getConstraints());
//        builder.gridHeight(1);
//
//        builder.weightX(1);
//        builder.skipCells(1);
//        builder.addToNextCell(fieldsPanel);
        GridBagBuilder builder = new GridBagBuilder(this, 3);

        builder.gridHeight(3).weightY(1).fillVertical()
                .addToNextCell(buttonsPanel).skipCells(2);

        builder.insets(10, 5, 10, 5).fillNone();
        addPanel(ProfileSelectionPanel.class, profileSelectionPanel, builder);

        builder.gridHeight(1).weightX(1).skipCells(1)
                .addToNextCell(fieldsPanel);
    }

    public BoardPanel getProfilePanel(Frame frame) {
        BoardPanel panel = new BoardPanel(frame);
        profileLabel.addInsets(20);
        profileLabel.setProfileIcon("0", ResourceManager.LARGE);

        editIconLabel = new BoardLabel();
        editIconLabel.setName("edit_transparent");

        GridBagBuilder builder = new GridBagBuilder(panel);
        builder.weight(1, 1).fillBoth();

        builder.addToCurrentCell(editIconLabel);
        builder.addToCurrentCell(profileLabel);

        return panel;
    }

    private BoardLabel getLabel(String text) {
        BoardLabel label = new BoardLabel(text);
        label.setFGLight();
        label.setAlignmentTrailing();
        label.addInsets(10);
        return label;
    }

    public BoardPanel getFieldsPanel(Frame frame) {
        BoardPanel panel = new BoardPanel(frame, ByteBoardTheme.MAIN);
        panel.addInsets(25);
        panel.setCornerRadius(90);

        BoardPanel profilePanel = getProfilePanel(frame);

        BoardLabel usernameLabel = getLabel("New Username");
        usernameField = new BoardTextField(frame, ByteBoardTheme.MAIN_DARK, 20);

        BoardLabel emailLabel = getLabel("New Email");
        emailField = new BoardTextField(frame, ByteBoardTheme.MAIN_DARK, 20);

        BoardLabel oldPasswordLabel = getLabel("Old Password");
        oldPasswordField = new BoardPasswordField(frame, ByteBoardTheme.MAIN_DARK, 20);

        BoardLabel newPasswordLabel = getLabel("New Password");
        newPasswordField = new BoardPasswordField(frame, ByteBoardTheme.MAIN_DARK, 20);

        BoardLabel rePasswordLabel = getLabel("Repeat Password");
        rePasswordField = new BoardPasswordField(frame, ByteBoardTheme.MAIN_DARK, 20);

        GridBagBuilder builder = new GridBagBuilder(panel, 3);

        builder.gridWidth(3).weightY(0).insets(10).fillBoth()
                .addToNextCell(profilePanel).skipCells(2);

        builder.gridWidth(1)
                .addToNextCell(usernameLabel).skipCells(1)
                .addToNextCell(usernameField.getTextFieldContainer())
                .addToNextCell(emailLabel).skipCells(1)
                .addToNextCell(emailField.getTextFieldContainer())
                .addToNextCell(oldPasswordLabel).skipCells(1)
                .addToNextCell(oldPasswordField.getTextFieldContainer())
                .addToNextCell(newPasswordLabel).skipCells(1)
                .addToNextCell(newPasswordField.getTextFieldContainer())
                .addToNextCell(rePasswordLabel).skipCells(1)
                .addToNextCell(rePasswordField.getTextFieldContainer());

        return panel;
    }

    public BoardPanel getButtonsPanel(Frame frame) {
        BoardPanel panel = new BoardPanel(frame, ByteBoardTheme.MAIN);
        panel.setCornerRadius(90);
        panel.addInsets(30);

        saveButton = new BoardButton("Save Changes", "save");
        saveButton.setAlignmentLeading();

        cancelButton = new BoardButton("    Cancel", "cancel");
        cancelButton.setAlignmentLeading();

        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.weightY(1).insets(10).fillHorizontal()
                .anchor(GridBagBuilder.SOUTH)
                .addToNextCell(saveButton);

        builder.weightY(0).addToNextCell(cancelButton);

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
                setPanelVisibility(ProfileSelectionPanel.class, !getPanelVisibility(ProfileSelectionPanel.class));
            }
        });
        editIconLabel.getMouseListeners()[0].mouseExited(null);

        // Cancel Profile Edit
        cancelButton.addActionListener(e -> {
            setVisible(false);
            setPanelVisibility(ProfileBoardButtonPanel.class, true);
            setPanelVisibility(ProfileBoardUserDataPanel.class, true);
            setPanelVisibility(ProfileSelectionPanel.class, false);
        });

        // Save Profile Edit
        saveButton.addActionListener(e -> {
            if (saveEditedProfile())
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
            if (isInvalidOldPassword(usernameField)) return false;

            if (isValidUsername(newUsername)) {
                updatedValues.add(DBUser.K_USER_NAME);
                updatedValues.add(newUsername);
            } else return false;
        }

        if (changedEmail) {
            if (isInvalidOldPassword(emailField)) return false;

            if (isValidEmail(newEmail)) {
                updatedValues.add(DBUser.K_EMAIL);
                updatedValues.add(newEmail);
            } else return false;
        }

        if (changedPassword) {
            if (isInvalidOldPassword(null)) return false;

            if (isValidNewPassword()) {
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

        refresh();

        return true;
    }

    private boolean isValidEmail(String newEmail) {
        if (!EncryptionUtils.isValidEmail(newEmail)) {
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
            if (textField == null) {
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