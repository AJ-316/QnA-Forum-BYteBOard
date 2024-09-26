package QnAForumInterface.ProfileBoardPackage;

import CustomControls.CustomJPanel;
import QnAForumInterface.AskBoard;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.QnAForum;
import QnAForumInterface.SearchBoard;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class UserProfileDisplayPanel extends JPanel {

    private JLabel userProfile;
    private JLabel username;
    private JLabel userEmail;
    private JLabel userBytescore;

    public UserProfileDisplayPanel() {
        init();
    }

    private void init() {
        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setMinimumSize(new Dimension(640, 360));
        setPreferredSize(new Dimension(640, 360));
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        add(userProfile = new JLabel(), constraints);

        CustomJPanel displayHolder = new CustomJPanel();
        displayHolder.setLayout(new BoxLayout(displayHolder, BoxLayout.Y_AXIS));
        displayHolder.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        displayHolder.setCornerRadius(90);

        username = new JLabel("Username");
        username.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        username.setFont(ResourceManager.getFont("inter_bold.32"));
        username.setBorder(BorderFactory.createEmptyBorder(40, 40, 10, 60));
        displayHolder.add(username, constraints);

        int bytes = 0;

        userBytescore = new JLabel(String.valueOf(bytes));
        userBytescore.setIcon(ResourceManager.getIcon("bytescore_icon", ResourceManager.SMALL));
        userBytescore.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        userBytescore.setFont(ResourceManager.getFont("inter_semibold.20"));
        userBytescore.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 60));
        displayHolder.add(userBytescore, constraints);

        userEmail = new JLabel("EmailID");
        userEmail.setIcon(ResourceManager.getColoredIcon("email",
                ResourceManager.getColor(ByteBoardTheme.MAIN_DARK), null, ResourceManager.SMALL));
        userEmail.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        userEmail.setFont(ResourceManager.getFont("inter_semibold.20"));
        userEmail.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 60));
        displayHolder.add(userEmail);

        constraints.gridx = 1;
        constraints.insets = new Insets(25, 40, 25, 50);
        add(displayHolder, constraints);

        InterfaceEventManager.addListener("Display.ActivityContainer", ec -> {
            boolean display = (boolean) ec[0];
            displayHolder.setVisible(!display);
        });
        InterfaceEventManager.addListener("Init.AskBoard.ProfileBoard", ec -> QnAForum.setContent(AskBoard.init(username.getText(), userProfile.getName())));
        InterfaceEventManager.addListener("Init.SearchBoard.ProfileBoard", ec -> QnAForum.setContent(SearchBoard.init(username.getText(), userProfile.getName())));
        InterfaceEventManager.addListener("UserData.ProfileBoard", () -> new Object[]{userProfile.getName(), username.getText(), userEmail.getText()});
        InterfaceEventManager.addListener("Update.ProfileBoard", ec -> {
            ResourceManager.setProfileIndexIcon((String) ec[0], userProfile, ResourceManager.LARGE);
            username.setText((String) ec[1]);
            userEmail.setText((String) ec[2]);
        });
    }

    public JLabel getUserProfile() {
        return userProfile;
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setUserBytescore(String bytescore) {
        this.userBytescore.setText(bytescore);
    }

    public void setUserEmail(String userEmail) {
        this.userEmail.setText(userEmail);
    }
}
