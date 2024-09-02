package QnAForumInterface.InformationBarPackage;

import QnAForumInterface.ProfileBoardPackage.ProfileBoard;
import QnAForumInterface.QnABoard;
import QnAForumInterface.QnAForum;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author AJ
 */
public class ActivityBar extends InformationBar {

    private JLabel questionHead;
    private JLabel userName;
    private JLabel userProfile;
    private String questionID;
    private Color defaultBackground;

    protected void init() {
        userProfile = new JLabel();

        initUserProfile();
        initUserName();
        initQuestionHead();
    }

    protected void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setColor(ResourceManager.getColor("accent"));
            }

            public void mouseExited(MouseEvent evt) {
                setColor(defaultBackground);
            }

            public void mouseReleased(MouseEvent evt) {
                ActivityBar activityBar = (ActivityBar) evt.getComponent();
                System.out.printf("%n%s, %s, %s%n",
                        activityBar.userName.getName(),
                        activityBar.userProfile.getName(),
                        questionID);
                QnAForum.setContent(QnABoard.init(ProfileBoard.getCurrentUser(),
                        activityBar.userName.getName(),
                        activityBar.userProfile.getName(),
                        questionID));
            }
        });
    }

    private void initUserProfile() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(15, 15, 15, 5);
        container.add(userProfile, gridBagConstraints);
    }

    private void initUserName() {
        userName = getLabel("Questioner", SwingConstants.CENTER);
        System.out.println("Instantiated Username: " + this);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(15, 5, 15, 5);

        container.add(userName, gridBagConstraints);
    }

    private void initQuestionHead() {
        questionHead = getLabel("Question Head...", SwingConstants.LEADING);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(15, 5, 15, 15);

        container.add(questionHead, gridBagConstraints);
    }

    private void setColor(Color c) {
        container.setBackground(c);
    }

    public boolean isRespondent(String questioner) {
        return this.userName.getName().equals(questioner);
    }

    public void setContent(String username, String userProfileIndex, String questionHead, String questionID) {
        this.userName.setText(username + ":");
        this.userName.setName(username);
        this.questionHead.setText(questionHead);
        this.questionID = questionID;
        
        ResourceManager.setProfileIcon(userProfileIndex, userProfile, ResourceManager.SMALL);
    }

    public void setDefaultBackground(Color color) {
        setColor(color);
        defaultBackground = color;
    }

    public String getQuestionID() {
        return questionID;
    }
}
