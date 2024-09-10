package QnAForumInterface.ProfileBoardPackage;

import CustomControls.CustomJPanel;
import CustomControls.SimpleScrollPane;
import BYteBOardDatabase.DBUser;
import BYteBOardDatabase.EncryptionUtils;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.WrapLayout;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserProfileEditPanel extends JPanel {

    private JButton backBtn;
    private JButton saveEditBtn;

    private final EditControlsContainer editControlsContainer;
    private final UserProfileChooser profileChooser;

    private JLabel editProfileIcon;
    private JLabel editUserProfile;
    private JLabel saveErrorLabel;

    public UserProfileEditPanel() {
        profileChooser = new UserProfileChooser();
        editProfileIcon = new JLabel();
        editUserProfile = new JLabel();
        editControlsContainer = new EditControlsContainer();
        saveErrorLabel = new JLabel();

        init();
        setVisible(false);
    }

    private void init() {
        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setMinimumSize(new Dimension(640, 360));
        setPreferredSize(new Dimension(640, 360));
        setLayout(new GridBagLayout());

        profileChooser.init(editUserProfile);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(profileChooser, gridBagConstraints);

        editProfileIcon.setIcon(ResourceManager.getStateIcon("edit_profile", ResourceManager.DEFAULT, ResourceManager.REGULAR));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(editProfileIcon, gridBagConstraints);

        editUserProfile.setIcon(ResourceManager.getProfileIcon("0", ResourceManager.LARGE));
        editUserProfile.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                editProfileIcon.setIcon(ResourceManager.getStateIcon("edit_profile", ResourceManager.ROLLOVER, ResourceManager.REGULAR));
            }

            public void mouseExited(MouseEvent evt) {
                editProfileIcon.setIcon(ResourceManager.getStateIcon("edit_profile", ResourceManager.DEFAULT, ResourceManager.REGULAR));
            }

            public void mouseReleased(MouseEvent evt) {
                profileChooser.setVisible(!profileChooser.isVisible());
                revalidate();
                repaint();
            }
        });

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.1;
        add(editUserProfile, gridBagConstraints);

        editControlsContainer.init();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(editControlsContainer, gridBagConstraints);

        saveErrorLabel.setText("errorLabel");
        saveErrorLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.ERROR));
        saveErrorLabel.setFont(ResourceManager.getFont("inter_semibold.20"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(50, 0, 0, 0);
        add(saveErrorLabel, gridBagConstraints);

        /////////////////////////////////////////////////////////////////////////////////////////
        initExitButtons();
    }

    private void initExitButtons() {
        JPanel exitButtonsHolder = new JPanel(new GridBagLayout());
        exitButtonsHolder.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weighty = 0.1;
        saveEditBtn = getButton("Save Changes", e -> saveEditedProfile(), "save");
        exitButtonsHolder.add(saveEditBtn, constraints);

        constraints.gridy = 1;
        backBtn = getButton("Back", e -> InterfaceEventManager.invokeEvent("Edit.ProfileBoard", false), "home");
        exitButtonsHolder.add(backBtn, constraints);

        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(25, 0, 0, 0);
        add(exitButtonsHolder, constraints);

        /*JPanel exitButtonsHolder = new JPanel(new GridBagLayout());
        exitButtonsHolder.setOpaque(false);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.1;
        saveEditBtn = getButton("Save Changes", e -> {}, "save");

        // f ixme: saveEditBtn.addActionListener(evt -> {
        //   if (saveEditedProfile()) {
        //       setEditProfile(false);
        //       userName.setText(editUserName.getText());
        //       userEmail.setText(editUserEmail.getText());
        //   }
        //   });
        exitButtonsHolder.add(saveEditBtn, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.1;
        backBtn = getButton("Back", e -> {}, "home");
        exitButtonsHolder.add(backBtn, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(25, 0, 0, 0);
        add(exitButtonsHolder, gridBagConstraints);*/
    }

    private void saveEditedProfile() {
        String newUsername = editControlsContainer.getEditUsername();
        String newEmail = editControlsContainer.getEditUserEmail();

        if (newUsername.length() <= 3) {
            saveErrorLabel.setText("Username too small");
            return;
        }

        Object[] userProfileData = InterfaceEventManager.invokeRequest("UserData.ProfileBoard");
        String currentUsername = (String) userProfileData[1];

        if (!currentUsername.equals(newUsername) &&
                DBUser.isValueAvailable(DBUser.K_USER_NAME, newUsername)) {
            saveErrorLabel.setText("Username already taken");
            return;
        }

        if (!EncryptionUtils.isValidEmail(newEmail)) {
            saveErrorLabel.setText("Invaild Email");
            return;
        }

        String newPassword = null;

        if (editControlsContainer.getPassword().length != 0) {
            if (!isPasswordConfirmed()) {
                saveErrorLabel.setText("Passwords do not match");
                return;
            }

            if (EncryptionUtils.isInvalidPassword(editControlsContainer.getPassword())) {
                if (editControlsContainer.getPassword().length < 8 || editControlsContainer.getPassword().length > 20) {
                    saveErrorLabel.setText("Password length must be 8-20");
                    return;
                }
                saveErrorLabel.setText("Include Uppercase, Lowercase, Digit, and Special Character");
                return;
            }
            newPassword = EncryptionUtils.encryptPwd(editControlsContainer.getPassword());
        }

        ProfileBoard.setCurrentUser(newUsername);

        String newProfileIndex = Integer.toString(profileChooser.getSelectedProfile());

        if (newPassword == null) {
            String currentProfileIndex = (String) userProfileData[0];
            String currentEmail = (String) userProfileData[2];
            if(newEmail.equals(currentEmail) && newUsername.equals(currentUsername) && newProfileIndex.equals(currentProfileIndex)) {
                return;
            }
        }

        DBUser.updateUser(currentUsername, newUsername, newPassword, newEmail, newProfileIndex);
        InterfaceEventManager.invokeEvent("Update.ProfileBoard", newProfileIndex, newUsername, newEmail);
        InterfaceEventManager.invokeEvent("Edit.ProfileBoard", false);
    }

    private boolean isPasswordConfirmed() {
        if (editControlsContainer.getPassword().length != editControlsContainer.getRePassword().length)
            return false;

        for (int i = 0; i < editControlsContainer.getPassword().length; i++) {
            if (editControlsContainer.getPassword()[i] != editControlsContainer.getRePassword()[i])
                return false;
        }

        return true;
    }

    private JButton getButton(String text, ActionListener listener, String iconLabel) {
        JButton button = new JButton(text);
        button.setFont(ResourceManager.getFont("inter_regular.22"));
        button.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        button.addActionListener(listener);

        ResourceManager.setButtonIcons(button, iconLabel, ResourceManager.MINI);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    public static class UserProfileChooser extends CustomJPanel {

        private final JPanel profilesContainer = new JPanel();

        public UserProfileChooser() {
            setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setMinimumSize(new Dimension(310, 41));
            setLayout(new BorderLayout());

            setVisible(false);
        }

        public void init(JLabel editUserProfile) {

            // pane.setName("");
            profilesContainer.setOpaque(false);
            profilesContainer.setLayout(new WrapLayout(WrapLayout.LEADING, 10, 10));

            SimpleScrollPane scrollPane = new SimpleScrollPane();
            scrollPane.setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            scrollPane.setMaximumSize(new Dimension(300, 32767));
            scrollPane.setPreferredSize(new Dimension(300, 100));
            scrollPane.setViewportView(profilesContainer);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(15);

            int iconPtr = 0;
            Icon icon;

            while ((icon = ResourceManager.getProfileIcon("" + iconPtr++, ResourceManager.REGULAR)) != null) {
                ProfilePane profilePane = ProfilePane.create(icon);
                profilesContainer.add(profilePane);
                profilePane.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        Component[] components = profilesContainer.getComponents();
                        for (int i = 0; i < components.length; i++) {
                            if (components[i].equals(e.getComponent())) {
                                ResourceManager.setProfileIndexIcon(Integer.toString(i), editUserProfile, ResourceManager.LARGE);
                                break;
                            }
                        }
                    }
                });
            }

            scrollPane.revalidate();
            scrollPane.repaint();

            add(scrollPane, BorderLayout.CENTER);
        }

        public ProfilePane getProfilePane(int index) {
            return (ProfilePane) profilesContainer.getComponents()[index];
        }

        public int getSelectedProfile() {
            for (int i = 0; i < profilesContainer.getComponents().length; i++) {
                if(((ProfilePane) profilesContainer.getComponent(i)).isSelected()) return i;
            }
            return 0;
        }
    }

    public static class EditControlsContainer extends JPanel {

        private JPasswordField editUserPassword;
        private JPasswordField editUserRePassword;
        private JTextField editUserEmail;
        private JTextField editUsername;

        private JLabel userEmailLabel;
        private JLabel userNameLabel;
        private JLabel userPasswordLabel;
        private JLabel userRePasswordLabel;

        public EditControlsContainer() {
            setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
            setLayout(new GridBagLayout());
        }

        public void init() {
            addContent(
                    userNameLabel = new JLabel("Username: "),
                    editUsername = new JTextField("Username"), 24, 1);

            addContent(
                    userEmailLabel = new JLabel("Email-ID:"),
                    editUserEmail = new JTextField("example@gmail.com"), 24, 2);

            addContent(
                    userPasswordLabel = new JLabel("New Password:"),
                    editUserPassword = new JPasswordField(), 14, 3);

            addContent(
                    userRePasswordLabel = new JLabel("Re-Password:"),
                    editUserRePassword = new JPasswordField(), 14,4);

            editUserPassword.setAutoscrolls(false);
            editUserRePassword.setAutoscrolls(false);
        }

        private void addContent(JLabel label, JTextField textField, int textFieldFontSize, int index) {
            label.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
            label.setFont(ResourceManager.getFont("inter_regular.24"));
            label.setHorizontalAlignment(SwingConstants.TRAILING);

            textField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
            textField.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
            textField.setCaretColor(textField.getForeground());
            textField.setFont(ResourceManager.getFont("inter_regular." + textFieldFontSize));
            textField.setMinimumSize(new Dimension(220, 40));
            textField.setPreferredSize(new Dimension(220, 40));

            GridBagConstraints constraintLabel = new GridBagConstraints();
            constraintLabel.gridx = 0;
            constraintLabel.gridy = index;
            constraintLabel.anchor = GridBagConstraints.EAST;

            GridBagConstraints constraintTextField = new GridBagConstraints();
            constraintTextField.gridx = 1;
            constraintTextField.gridy = index;
            constraintTextField.anchor = GridBagConstraints.WEST;
            constraintTextField.insets = new Insets(10, 10, 10, 20);

            add(label, constraintLabel);
            add(textField, constraintTextField);
        }

        public void setEditUserEmail(String editUserEmail) {
            this.editUserEmail.setText(editUserEmail);
        }

        public void setEditUsername(String editUsername) {
            this.editUsername.setText(editUsername);
        }

        public char[] getPassword() {
            return this.editUserPassword.getPassword();
        }

        public char[] getRePassword() {
            return this.editUserRePassword.getPassword();
        }

        public String getEditUsername() {
            return editUsername.getText();
        }

        public String getEditUserEmail() {
            return editUserEmail.getText();
        }

        public void resetPasswordFields() {
            editUserPassword.setText("");
            editUserRePassword.setText("");
        }
    }

    public JLabel getEditUserProfile() {
        return editUserProfile;
    }

    public UserProfileChooser getProfileChooser() {
        return profileChooser;
    }

    @Override
    public void setVisible(boolean flagVisible) {
        super.setVisible(flagVisible);
        saveErrorLabel.setText("");

        if (flagVisible) {
            Object[] userData = InterfaceEventManager.invokeRequest("UserData.ProfileBoard");
            editControlsContainer.setEditUsername((String) userData[1]);
            editControlsContainer.setEditUserEmail((String) userData[2]);
            saveEditBtn.requestFocus();
        }
        editControlsContainer.resetPasswordFields();
    }
}
