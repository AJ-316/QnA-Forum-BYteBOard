package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.BoardComboBox;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;

public class ProfileBoardButtonPanel extends BoardPanel {

    private BoardButton searchButton;
    private BoardButton askButton;
    private BoardButton activityButton;
    private BoardButton editProfileButton;
    private BoardButton logoutButton;

    private BoardComboBox themeComboBox;

    public ProfileBoardButtonPanel(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN);

        addButtonListeners();
    }

    public void init(MainFrame main, Frame frame) {
        themeComboBox = new BoardComboBox(main, frame, ResourceManager.getThemes(), ResourceManager.getCurrentTheme());
        themeComboBox.setLabel("Themes", "theme");
        themeComboBox.setBackground(ByteBoardTheme.MAIN_LIGHT);

        setCornerRadius(90);
        addInsets(30);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.weightY(1).insets(10).fillHorizontal()
                .anchor(GridBagBuilder.NORTH)
                .addToNextCell(themeComboBox.getComponent());

        builder.weightY(0).anchor(GridBagBuilder.SOUTH)
                .addToNextCell(searchButton = createButton("    Search", "search"))
                .addToNextCell(askButton = createButton("Ask Question", "question"))
                .addToNextCell(activityButton = createButton("View Activity", "show"))
                .addToNextCell(editProfileButton = createButton("  Edit Profile", "edit"))
                .addToNextCell(logoutButton = createButton("    Log Out", "logout"));
    }

    private BoardButton createButton(String text, String icon) {
        BoardButton button = new BoardButton(text, icon);
        button.setFGLight();
        button.setAlignmentLeading();
        return button;
    }

    private void addButtonListeners() {

        activityButton.addActionListener(e -> setActivitiesVisible(!getPanelVisibility(ProfileBoardActivityPanel.class)));
        editProfileButton.addActionListener(e -> setEditProfileVisible(true));

        searchButton.addActionListener(e -> searchButton.setEnabled(false));
        askButton.addActionListener(e -> askButton.setEnabled(false));

        logoutButton.addActionListener(e -> {
            requestSwitchMainFrame(0);
            setActivitiesVisible(false);
            setEditProfileVisible(false);
        });

        themeComboBox.addItemListener(e -> {
            ResourceManager.setTheme(e.getItem().toString());
            restartMainFrame();
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
        setPanelVisibility(ProfileBoardActivityPanel.class, isVisible);
        setPanelVisibility(ProfileBoardUserDataPanel.class, !isVisible);
    }
}
