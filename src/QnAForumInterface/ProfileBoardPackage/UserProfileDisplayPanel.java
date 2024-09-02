package QnAForumInterface.ProfileBoardPackage;

import QnAForumInterface.AskBoard;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.QnABoard;
import QnAForumInterface.QnAForum;
import QnAForumInterface.SearchBoard;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class UserProfileDisplayPanel extends JPanel {

    private JLabel userProfile;
    private JLabel username;
    private JLabel userEmail;

    public UserProfileDisplayPanel() {
        init();
    }

    private void init() {
        setBackground(ResourceManager.getColor("base"));
        setMinimumSize(new Dimension(640, 360));
        setPreferredSize(new Dimension(640, 360));
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(userProfile = new JLabel(), constraints);

        username = new JLabel("Username");
        constraints.gridy = GridBagConstraints.RELATIVE;
        username.setForeground(ResourceManager.getColor("text_fg_dark"));
        username.setFont(ResourceManager.getFont("inter_bold.32"));
        add(username, constraints);

        userEmail = new JLabel("EmailID");
        userEmail.setForeground(ResourceManager.getColor("text_fg_dark"));
        userEmail.setFont(ResourceManager.getFont("inter_semibold.22"));
        add(userEmail, constraints);

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
