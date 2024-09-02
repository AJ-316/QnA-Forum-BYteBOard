package QnAForumInterface.ProfileBoardPackage;

import DataObjects.AnswerDataObject;
import DataObjects.QuestionDataObject;
import DataObjects.UserDataObject;
import QnAForumDatabase.Database;
import QnAForumInterface.InformationBarPackage.ActivityBar;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class ProfileBoard extends JPanel {

    private static String CURRENT_USER;

    private ProfileButtonHolder buttonContainer;
    private UserProfileEditPanel editPanel;
    private UserProfileDisplayPanel displayPanel;
    private ActivityContainer activityContainer;

    public ProfileBoard() {
        setBackground(ResourceManager.getColor("base"));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 0);
        add(buttonContainer = new ProfileButtonHolder(), gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        add(editPanel = new UserProfileEditPanel(), gridBagConstraints);
        add(displayPanel = new UserProfileDisplayPanel(), gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 0, 5, 5);
        add(activityContainer = new ActivityContainer(), gridBagConstraints);

        InterfaceEventManager.addListener("Edit.ProfileBoard", ec -> {
            boolean editProfile = (boolean) ec[0];
            if (isVisible()) {
                InterfaceEventManager.invokeEvent("Display.ActivityContainer", false);
            }

            displayPanel.setVisible(!editProfile);
            editPanel.setVisible(editProfile);
            buttonContainer.setVisible(!editProfile);
        });
    }

    public static ProfileBoard init(String username) {
        ProfileBoard board = new ProfileBoard();

        String userID = Database.getData(UserDataObject.TABLE, UserDataObject.userIDKey(),
                UserDataObject.usernameKey(), username, true)[0];

        UserDataObject userData = (UserDataObject) Database.getData(UserDataObject.TABLE,
                UserDataObject.usernameKey(), username)[0];
        String userProfileIndex = userData.get(UserDataObject.profileKey());

        CURRENT_USER = username;
        board.displayPanel.setUsername(username);
        board.displayPanel.setUserEmail(userData.get(UserDataObject.emailKey()));
        board.editPanel.getProfileChooser().getProfilePane(Integer.parseInt(userProfileIndex)).setSelected();

        ResourceManager.setProfileIcon(userProfileIndex, board.displayPanel.getUserProfile(), ResourceManager.LARGE);
        ResourceManager.setProfileIcon(userProfileIndex, board.editPanel.getEditUserProfile(), ResourceManager.LARGE);

        String[] questionIDList = Database.getData(QuestionDataObject.TABLE, QuestionDataObject.questionIDKey(),
                QuestionDataObject.userIDKey(), userID, false);

        for (String questionID : questionIDList) {
            QuestionDataObject questionData = (QuestionDataObject) Database.getData(QuestionDataObject.TABLE,
                    QuestionDataObject.questionIDKey(), questionID)[0];
            board.activityContainer.addActivityBar(
                    username,
                    userProfileIndex,
                    questionData.get(QuestionDataObject.questionHeadKey()),
                    questionData.get(QuestionDataObject.questionIDKey()));
        }

        String[] allQuesAnsweredID = Database.getData(AnswerDataObject.TABLE, AnswerDataObject.questionIDKey(),
                AnswerDataObject.userIDKey(), userID, true);

        for (String queAnsweredID : allQuesAnsweredID) {
            QuestionDataObject queAnswered = (QuestionDataObject) Database.getData(QuestionDataObject.TABLE,
                    QuestionDataObject.questionIDKey(), queAnsweredID)[0];

            UserDataObject userAsked = (UserDataObject) Database.getData(UserDataObject.TABLE, UserDataObject.userIDKey(),
                    queAnswered.get(QuestionDataObject.userIDKey()))[0];

            board.activityContainer.addActivityBar(
                    userAsked.get(UserDataObject.usernameKey()),
                    userAsked.get(UserDataObject.profileKey()),
                    queAnswered.get(QuestionDataObject.questionHeadKey()),
                    queAnswered.get(QuestionDataObject.questionIDKey()));
        }

        return board;
    }

    public static String getCurrentUser() {
        return CURRENT_USER;
    }

    protected static String setCurrentUser(String CURRENT_USER) {
        return CURRENT_USER;
    }


}
