package QnAForumInterface.ProfileBoardPackage;

import CustomControls.RoundedJPanel;
import QnAForumInterface.AskBoard;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.QnABoard;
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
        constraints.gridheight = 2;
        add(userProfile = new JLabel(), constraints);

        RoundedJPanel displayHolder = new RoundedJPanel();
        displayHolder.setLayout(new BoxLayout(displayHolder, BoxLayout.Y_AXIS));
        displayHolder.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        displayHolder.setCornerRadius(90);

        username = new JLabel("Username");
        username.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        username.setFont(ResourceManager.getFont("inter_bold.32"));
        username.setBorder(BorderFactory.createEmptyBorder(40, 40, 10, 60));
        displayHolder.add(username, constraints);

        userEmail = new JLabel("EmailID");
        userEmail.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_MAIN_DARK));
        userEmail.setFont(ResourceManager.getFont("inter_semibold.22"));
        userEmail.setBorder(BorderFactory.createEmptyBorder(10, 40, 5, 60));
        displayHolder.add(userEmail);

        int bytes = 0;

        userBytescore = new JLabel(bytes + " BYtes");
        userBytescore.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_MAIN_DARK));
        userBytescore.setFont(ResourceManager.getFont("inter_semibold.22"));
        userBytescore.setBorder(BorderFactory.createEmptyBorder(5, 40, 40, 60));
        displayHolder.add(userBytescore, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
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
            ResourceManager.setProfileIcon((String) ec[0], userProfile, ResourceManager.LARGE);
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

    public void setUserEmail(String userEmail) {
        this.userEmail.setText(userEmail);
    }
}
