package BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;

import java.awt.*;
import java.awt.event.ActionListener;

public class ProfileBoardButtonPanel extends BoardPanel {

    private BoardButton searchButton;
    private BoardButton askButton;
    private BoardButton activityButton;
    private BoardButton editProfileButton;
    private BoardButton logoutButton;

    public ProfileBoardButtonPanel(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN);

        addButtonListeners();
    }

    public void init(MainFrame main, Frame frame) {
        setCornerRadius(90);
        addInsets(30);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.anchor(GridBagConstraints.SOUTH);
        builder.fill(GridBagConstraints.HORIZONTAL);
        builder.insets(10, 10, 10, 10);

        builder.gridWeightY(1);
        builder.add(searchButton = createButton("    Search", "search"));

        builder.gridWeightY(0);
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
            boolean isVisible = e == null || getPanelVisibility(ProfileBoardActivityPanel.class.getSimpleName());

            if(isVisible) {
                activityButton.setText("View Activity");
                activityButton.setIcon("show");
            } else {
                activityButton.setText("Hide Activity");
                activityButton.setIcon("hide");
            }
            setPanelVisibility(ProfileBoardActivityPanel.class.getSimpleName(), !isVisible);
            setPanelVisibility(ProfileBoardUserDataPanel.class.getSimpleName(), isVisible);
        });

        editProfileButton.addActionListener(e -> {
            activityButton.getActionListeners()[0].actionPerformed(null);

            setPanelVisibility(ProfileEditPanel.class.getSimpleName(), true);
            setPanelVisibility(ProfileBoardButtonPanel.class.getSimpleName(), false);
            setPanelVisibility(ProfileBoardUserDataPanel.class.getSimpleName(), false);
        });
    }
}
