package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.BoardLabel;
import BoardControls.BoardScrollPanel;
import BoardControls.UIPackage.WrapLayout;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import java.awt.*;

public class ProfileSelectionPanel extends BoardPanel {

    private final BoardLabel userProfileLabel;

    public ProfileSelectionPanel(Frame frame, BoardLabel userProfileLabel) {
        super(frame, ByteBoardTheme.MAIN);
        this.userProfileLabel = userProfileLabel;
        setVisible(false);
    }

    public void init(Frame frame) {
        addInsets(20);
        setCornerRadius(90);
        setLayout(new BorderLayout());

        BoardScrollPanel profilesScrollPane = new BoardScrollPanel(frame);
        profilesScrollPane.setVerticalUnitIncrement(8);

        int layoutGap = 5;
        int size = ResourceManager.REGULAR * 2 + layoutGap * 8;
        profilesScrollPane.setLayout(new WrapLayout(WrapLayout.CENTER, layoutGap, layoutGap));
        profilesScrollPane.setScrollMinSize(size, 1000);
        profilesScrollPane.setBackground(getBackground());

        int index = 0;
        while ((ResourceManager.getProfileIcon("" + index, ResourceManager.REGULAR)) != null) {
            ProfileIconPane profilePane = new ProfileIconPane(String.valueOf(index),
                    profileIconPane -> userProfileLabel.setProfileIcon(profileIconPane.getName(), ResourceManager.REGULAR));
            profilesScrollPane.add(profilePane);
            index++;
        }

        add(profilesScrollPane.getComponent());
    }
}