package QnAForumInterface.ProfileBoardPackage;

import BYteBOardDatabase.DBAnswer;
import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQuestion;
import BYteBOardDatabase.DBUser;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class ProfileBoard extends JPanel {

    private static String CURRENT_USER;

    private final ProfileButtonHolder buttonContainer;
    private final UserProfileEditPanel editPanel;
    private final UserProfileDisplayPanel displayPanel;
    private final ActivityContainer activityContainer;

    public ProfileBoard() {
        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
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

        // need userData by matched username

        DBDataObject userDataObject = DBUser.ops.findValuesBy(DBUser.ops.matchByValue(DBUser.K_USER_NAME, username),
                DBUser.K_USER_ID, DBUser.K_USER_NAME, DBUser.K_EMAIL,
                DBUser.K_USER_PROFILE, DBUser.K_USER_BYTESCORE)[0];

        String userID = userDataObject.getValue(DBUser.K_USER_ID);
        String userProfileIndex = userDataObject.getValue(DBUser.K_USER_PROFILE);

        CURRENT_USER = username;
        board.displayPanel.setUsername(username);
        board.displayPanel.setUserBytescore(userDataObject.getValue(DBUser.K_USER_BYTESCORE));
        board.displayPanel.setUserEmail(userDataObject.getValue(DBUser.K_EMAIL));
        board.editPanel.getProfileChooser().getProfilePane(Integer.parseInt(userProfileIndex)).setSelected();

        ResourceManager.setProfileIndexIcon(userProfileIndex, board.displayPanel.getUserProfile(), ResourceManager.LARGE);
        ResourceManager.setProfileIndexIcon(userProfileIndex, board.editPanel.getEditUserProfile(), ResourceManager.LARGE);

        // get all questions asked by matched user
        DBDataObject[] questionsAskedByUser = DBQuestion.ops.findValuesBy(
                DBQuestion.ops.matchByValue(DBQuestion.K_USER_ID, userID), "*");

        for (DBDataObject questionData : questionsAskedByUser) {
            board.activityContainer.addActivityBar(
                    username,
                    userProfileIndex,
                    questionData.getValue(DBQuestion.K_QUESTION_HEAD),
                    questionData.getValue(DBQuestion.K_QUESTION_ID));
        }

        System.out.println("\u001B[33m");

        // get all questionIDs answered by matched user
        DBDataObject[] answeredQuestionsData = DBAnswer.ops.joinValuesBy(
                DBAnswer.ops.matchByValue(DBAnswer.K_USER_ID, userID),
                new String[]{
                        DBAnswer.ops.matchByKey(DBAnswer.K_QUESTION_ID, DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID)), // join on a.qid = q.qid
                        DBQuestion.ops.matchByKey(DBQuestion.K_USER_ID, DBUser.ops.appendKeys(DBUser.K_USER_ID))}, // join on q.uid = u.uid

                DBAnswer.ops.appendKeys(DBAnswer.K_ANSWER_ID),
                DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID, DBQuestion.K_QUESTION_HEAD),
                DBUser.ops.appendKeys(DBUser.K_USER_NAME, DBUser.K_USER_PROFILE));

        System.out.println("\u001B[0m");

        for (DBDataObject answeredQuestionData : answeredQuestionsData) {
            board.activityContainer.addActivityBar(
                    answeredQuestionData.getValue(DBUser.K_USER_NAME),
                    answeredQuestionData.getValue(DBUser.K_USER_PROFILE),
                    answeredQuestionData.getValue(DBQuestion.K_QUESTION_HEAD),
                    answeredQuestionData.getValue(DBQuestion.K_QUESTION_ID));
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
