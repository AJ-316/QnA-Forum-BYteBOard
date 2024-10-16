package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardInterface.BoardsPackage.AuthenticationPackage.AuthenticationMainFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.AskBoardPackage.AskBoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage.SearchBoardFrame;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.BoardButton;
import BoardControls.BoardComboBox;
import BoardControls.UIPackage.GridBagBuilder;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import java.awt.event.ItemEvent;

public class ProfileBoardButtonPanel extends BoardPanel {

    private String userID;
    private BoardButton searchButton;
    private BoardButton askButton;
    private BoardButton activityButton;
    private BoardButton editProfileButton;
    private BoardButton logoutButton;

    private BoardComboBox themeComboBox;

    public ProfileBoardButtonPanel(Frame frame) {
        super(frame, ByteBoardTheme.MAIN);

        addButtonListeners();
    }

    public void init(Frame frame) {
        themeComboBox = new BoardComboBox(frame, ResourceManager.getThemes(), ResourceManager.getCurrentTheme());
        themeComboBox.setLabel("Themes", "theme");
        themeComboBox.setBackground(ByteBoardTheme.MAIN_LIGHT);

        setCornerRadius(90);
        addInsets(30);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.weightY(1).insets(10).fillHorizontal()
                .anchor(GridBagBuilder.NORTH)
                .addToNextCell(themeComboBox.getComponent());

        builder.weight(1, 0).anchor(GridBagBuilder.SOUTH)
                .addToNextCell(searchButton = createButton("    Search", "search"))
                .addToNextCell(askButton = createButton("Ask Question", "question"))
                .addToNextCell(activityButton = createButton("View Activity", "show"))
                .addToNextCell(editProfileButton = createButton("  Edit Profile", "edit"))
                .addToNextCell(logoutButton = createButton("    Log Out", "logout"));
    }

    private BoardButton createButton(String text, String icon) {
        BoardButton button = new BoardButton(text, icon);
        button.setAlignmentLeading();
        return button;
    }

    private void addButtonListeners() {
        activityButton.addActionListener(e -> setActivitiesVisible(!getPanelVisibility(BoardContentDisplayPanel.class)));
        editProfileButton.addActionListener(e -> setEditProfileVisible(true));

        searchButton.addActionListener(e -> requestSwitchFrame(SearchBoardFrame.class, getUserID()));
        askButton.addActionListener(e -> requestSwitchFrame(AskBoardFrame.class, getUserID()));

        logoutButton.addActionListener(e -> {
            requestSwitchMainFrame(AuthenticationMainFrame.class);
            setActivitiesVisible(false);
            setEditProfileVisible(false);
        });

        themeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ResourceManager.setTheme(e.getItem().toString());
                restartMainFrame();
            }
        });
    }

    private void setEditProfileVisible(boolean isVisible) {
        setActivitiesVisible(false);

        setPanelVisibility(ProfileEditPanel.class, isVisible);
        setPanelVisibility(ProfileBoardButtonPanel.class, !isVisible);
        setPanelVisibility(ProfileBoardUserDataPanel.class, !isVisible);
    }

    private void setActivitiesVisible(boolean isVisible) {
        activityButton.setText((isVisible ? "Hide" : "View") + " Activity");
        activityButton.setIcon(isVisible ? "hide" : "show");
        setPanelVisibility(BoardContentDisplayPanel.class, isVisible);
        setPanelVisibility(ProfileBoardUserDataPanel.class, !isVisible);
    }

    protected String getUserID() {
        return userID;
    }

    protected void setUserID(String userID) {
        this.userID = userID;
    }
}
