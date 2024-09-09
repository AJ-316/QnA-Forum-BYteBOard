package BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.BoardLabel;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;

public class ProfileDisplayPanel extends BoardPanel {

    private BoardButton searchButton;
    private BoardButton askButton;
    private BoardButton activityButton;
    private BoardButton editProfileButton;
    private BoardButton logoutButton;

    private BoardLabel userProfileLabel;
    private BoardLabel userBytesLabel;
    private BoardLabel usernameLabel;
    private BoardLabel userEmailLabel;

    public ProfileDisplayPanel(MainFrame main, Frame frame) {
        super(main, frame);
    }

    public void init(MainFrame main, Frame frame) {
        GridBagBuilder builder = new GridBagBuilder(this, 3);
        builder.gridWeightY(1);

        BoardPanel buttonsPanel = getButtonsPanel(main, frame);
        builder.fill(GridBagConstraints.BOTH);
        builder.add(buttonsPanel);

        BoardPanel userMainPanel = getUserMainPanel(main, frame);
        builder.fill(GridBagConstraints.NONE);
        builder.gridWeightX(1);
        builder.add(userMainPanel);

        addButtonListeners();
    }

    private BoardPanel getButtonsPanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        panel.setCornerRadius(90);
        panel.addInsets(30);

        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.fill(GridBagConstraints.HORIZONTAL);
        builder.insets(10, 10, 10, 10);

        builder.gridWeightY(1);
        builder.add(searchButton = createLightButton("    Search", "search"));

        builder.gridWeightY(0);
        builder.add(askButton = createLightButton("Ask Question", "question"));
        builder.add(activityButton = createLightButton("View Activity", "show"));
        builder.add(editProfileButton = createLightButton("  Edit Profile", "edit"));
        builder.add(logoutButton = createLightButton("    Log Out", "logout"));
        return panel;
    }

    private BoardButton createLightButton(String text, String icon) {
        BoardButton button = new BoardButton(text, icon);
        button.setFGLight();
        button.setAlignmentLeading();
        return button;
    }

    private BoardPanel getUserMainPanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame);

        userProfileLabel = new BoardLabel();
        userProfileLabel.setProfileIcon("0", ResourceManager.LARGE);
        userProfileLabel.addInsets(0, 0, 0, 40);

        BoardPanel userDataPanel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        userDataPanel.setShadowState(BoardPanel.DROP_SHADOW);
        createUserData(userDataPanel);
        userDataPanel.setCornerRadius(90);

        panel.add(userProfileLabel);
        panel.addPanel("UserDataPanel", userDataPanel, null);
        return panel;
    }

    private void createUserData(BoardPanel panel) {
        panel.addInsets(40);

        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.fill(GridBagConstraints.BOTH);
        builder.gridWeightX(1);

        usernameLabel = new BoardLabel("Username");
        usernameLabel.setFGLight();
        usernameLabel.setAlignmentLeading();
        usernameLabel.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 32);
        usernameLabel.addInsets(10, 10, 30, 10);
        builder.add(usernameLabel);

        userBytesLabel = new BoardLabel("bytescore_icon", ResourceManager.SMALL);
        userBytesLabel.setText("24536");
        userBytesLabel.setFGLight();
        userBytesLabel.setAlignmentLeading();
        userBytesLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
        userBytesLabel.addInsets(10);
        builder.add(userBytesLabel);

        userEmailLabel = new BoardLabel("user123@gmail.com");
        userEmailLabel.setColoredIcon("email", ByteBoardTheme.MAIN_DARK, null, ResourceManager.SMALL);
        userEmailLabel.setFGLight();
        userEmailLabel.setAlignmentLeading();
        userEmailLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
        userEmailLabel.addInsets(10);
        builder.add(userEmailLabel);
    }

    private void addButtonListeners() {
        editProfileButton.addActionListener(e -> {
            setVisible(false);
            setPanelVisibility(ProfileEditPanel.class.getSimpleName(), true);
        });
    }

    protected void setProfile(String profileIcon) {
        userProfileLabel.setProfileIcon(profileIcon, ResourceManager.LARGE);
    }

    protected void setUsername(String username) {
        usernameLabel.setText(username);
    }

    protected void setUserEmail(String email) {
        userEmailLabel.setText(email);
    }

    protected void setUserBytes(String bytes) {
        userBytesLabel.setText(bytes);
    }
}
