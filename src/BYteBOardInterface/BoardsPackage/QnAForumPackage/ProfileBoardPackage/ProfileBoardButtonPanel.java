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

        //////////////////////
        setCornerRadius(90);
        addInsets(30);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.fill(GridBagConstraints.HORIZONTAL);
        builder.insets(10, 10, 10, 10);

        builder.weightY(1);
        builder.anchor(GridBagConstraints.NORTH);
        builder.add(themeComboBox.getComponent());

        builder.weightY(0);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.add(searchButton = createButton("    Search", "search"));
        builder.add(askButton = createButton("Ask Question", "question"));
        builder.add(activityButton = createButton("View Activity", "show"));
        builder.add(editProfileButton = createButton("  Edit Profile", "edit"));
        builder.add(logoutButton = createButton("    Log Out", "logout"));
    }

    private BoardButton createButton(String text, String icon) {
        BoardButton button = new BoardButton(text, icon);
        button.setFGLight();
        button.setAlignmentLeading();
        return button;
    }

    private void addButtonListeners() {

        activityButton.addActionListener(e -> {
            boolean isVisible = e == null || getPanelVisibility(ProfileBoardActivityPanel.class);

            if(isVisible) {
                activityButton.setText("View Activity");
                activityButton.setIcon("show");
            } else {
                activityButton.setText("Hide Activity");
                activityButton.setIcon("hide");
            }

            setPanelVisibility(ProfileBoardActivityPanel.class, !isVisible);
            setPanelVisibility(ProfileBoardUserDataPanel.class, isVisible);
        });

        editProfileButton.addActionListener(e -> {
            activityButton.getActionListeners()[0].actionPerformed(null);

            setPanelVisibility(ProfileEditPanel.class, true);
            setPanelVisibility(ProfileBoardButtonPanel.class, false);
            setPanelVisibility(ProfileBoardUserDataPanel.class, false);
        });

        searchButton.addActionListener(e -> searchButton.setEnabled(false));
        askButton.addActionListener(e -> askButton.setEnabled(false));
        logoutButton.addActionListener(e -> requestSwitchMainFrame(0, null));

        themeComboBox.addItemListener(e -> {
            ResourceManager.setTheme(e.getItem().toString());
            restartMainFrame();
        });
    }
}
