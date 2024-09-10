package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardScrollPanel;
import QnAForumInterface.WrapLayout;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;

public class ProfileSelectionPanel extends BoardPanel {

    private final BoardLabel userProfileLabel;

    public ProfileSelectionPanel(MainFrame main, Frame frame, BoardLabel userProfileLabel) {
        super(main, frame, ByteBoardTheme.MAIN);
        this.userProfileLabel = userProfileLabel;
        setVisible(false);
    }

    public void init(MainFrame main, Frame frame) {
        addInsets(20);
        setCornerRadius(90);
        setLayout(new BorderLayout());

        BoardScrollPanel profilesScrollPane = new BoardScrollPanel(main, frame);
        profilesScrollPane.getComponent().getVerticalScrollBar().setUnitIncrement(8);

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