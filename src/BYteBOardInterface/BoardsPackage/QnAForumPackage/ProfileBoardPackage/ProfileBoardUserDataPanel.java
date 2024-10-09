package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.BoardLabel;
import BoardControls.UIPackage.GridBagBuilder;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

public class ProfileBoardUserDataPanel extends BoardPanel {

    private BoardLabel userProfileLabel;
    private BoardLabel usernameLabel;
    private BoardLabel userBytesLabel;
    private BoardLabel userEmailLabel;

    public ProfileBoardUserDataPanel(Frame frame) {
        super(frame);
    }

    public void init(Frame frame) {
        GridBagBuilder builder = new GridBagBuilder(this, 2);

        userProfileLabel = new BoardLabel();
        userProfileLabel.setProfileIcon("0", ResourceManager.LARGE);
        userProfileLabel.addInsets(0, 0, 0, 40);

        BoardPanel userDataPanel = new BoardPanel(frame, ByteBoardTheme.MAIN);
        userDataPanel.setCornerRadius(90);
        userDataPanel.setShadowState(BoardPanel.DROP_SHADOW);
        createUserData(userDataPanel);

        builder.addToNextCell(userProfileLabel);
        builder.addToNextCell(userDataPanel);
    }

    private void createUserData(BoardPanel panel) {
        panel.addInsets(40);

        usernameLabel = new BoardLabel("Username");
        usernameLabel.setFGLight();
        usernameLabel.setAlignmentLeading();
        usernameLabel.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 32);
        usernameLabel.addInsets(10, 10, 30, 10);

        userBytesLabel = new BoardLabel("bytescore_icon", ResourceManager.SMALL);
        userBytesLabel.setText("24536");
        userBytesLabel.setFGLight();
        userBytesLabel.setAlignmentLeading();
        userBytesLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
        userBytesLabel.addInsets(10);

        userEmailLabel = new BoardLabel("user123@gmail.com");
        userEmailLabel.setColoredIcon("email", ByteBoardTheme.MAIN_DARK, null, ResourceManager.SMALL);
        userEmailLabel.setFGLight();
        userEmailLabel.setAlignmentLeading();
        userEmailLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
        userEmailLabel.addInsets(10);

        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.weightX(1).fillBoth()
                .addToNextCell(usernameLabel)
                .addToNextCell(userBytesLabel)
                .addToNextCell(userEmailLabel);
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
