package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardInterface.BoardsPackage.AuthenticationPackage.AuthenticateBoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.QnABoardFrame;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ActivityPane extends BoardPanel {

    private BoardLabel userProfileLabel;
    private BoardLabel usernameLabel;
    private BoardLabel questionHeadLabel;
    private BoardLabel bytesLabel;

    public ActivityPane(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN_LIGHT);

        addListeners();
    }

    public void init(MainFrame main, Frame frame) {
        setCornerRadius(80);
        setLimitRadius(false);

        addInsets(20);
        GridBagBuilder builder = new GridBagBuilder(this, 2);
        builder.fill(GridBagConstraints.BOTH);
        builder.gridWeightY(1);

        userProfileLabel = new BoardLabel();
        userProfileLabel.setProfileIcon("0", ResourceManager.SMALL);
        userProfileLabel.addInsets(5);
        builder.add(userProfileLabel);

        BoardPanel contentPanel = getContentPanel(main, frame);
        builder.gridWeightX(1);
        builder.add(contentPanel);
    }

    private BoardPanel getContentPanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame);
        GridBagBuilder builder = new GridBagBuilder(panel, 2);
        builder.fill(GridBagConstraints.BOTH);
        builder.insets(4, 4, 4, 4);

        usernameLabel = new BoardLabel("Username");
        usernameLabel.setFGLight();
        usernameLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 24);
        usernameLabel.setAlignmentLeading();
        builder.gridWeightX(0.5);
        builder.add(usernameLabel);

        bytesLabel = new BoardLabel("517465", "bytescore_icon");
        bytesLabel.setFGLight();
        bytesLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 24);
        bytesLabel.setAlignmentTrailing();
        bytesLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        builder.add(bytesLabel);

        questionHeadLabel = new BoardLabel("Content goes here, and it could be very long one so test it before moving on.");
        questionHeadLabel.setFGLight();
        questionHeadLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        questionHeadLabel.setAlignmentLeading();
        builder.gridWeightX(0);
        builder.add(questionHeadLabel);

        return panel;
    }

    public void setUserData(String userProfile, String username) {
        userProfileLabel.setProfileIcon(userProfile, ResourceManager.REGULAR);
        usernameLabel.setText(username);
    }

    public void setContentData(String contentText, String contentID, String bytes) {
        questionHeadLabel.setText(contentText);
        questionHeadLabel.setName(contentID);
        bytesLabel.setText(bytes);
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                setBackground(ByteBoardTheme.ACCENT_DARK);
                requestSwitchFrame(QnABoardFrame.class, getQuestionID());
            }

            public void mouseEntered(MouseEvent e) {
                setBackground(ByteBoardTheme.ACCENT);
            }

            public void mouseExited(MouseEvent e) {
                setBackground(ByteBoardTheme.MAIN_LIGHT);
            }
        });
    }

    public String getQuestionID() {
        return questionHeadLabel.getName();
    }
}
