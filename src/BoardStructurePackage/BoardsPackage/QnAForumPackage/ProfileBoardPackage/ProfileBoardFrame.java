package BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BoardStructurePackage.BoardFrame;
import BoardStructurePackage.BoardFrameSwitchDelegate;
import BoardStructurePackage.MainFrame;
import CustomControls.GridBagBuilder;
import DatabasePackage.*;

import java.awt.*;
import java.util.Arrays;

public class ProfileBoardFrame extends BoardFrame {

    private final ProfileBoardButtonPanel buttonPanel;
    private final ProfileBoardUserDataPanel userDataPanel;
    private final ProfileEditPanel editPanel;
    private final ProfileBoardActivityPanel activityPanel;

    public ProfileBoardFrame(MainFrame main) {
        super(main, (context, delegate) -> {
            String userID = context;

            if (userID == null) {
                DBDataObject loadedUserData = delegate.getContext(DBUser.TABLE);
                if(loadedUserData == null) return false;
                userID = loadedUserData.getValue(DBUser.K_USER_ID);
            }

            DBDataObject userData = DBUser.getUser(userID);

            // get all questions asked by matched user
            DBDataObject[] questionsAskedByUser = DBQuestion.ops.findValuesBy(
                    DBQuestion.ops.matchByValue(DBQuestion.K_USER_ID, userID), "*");

            // get all questionId's answered by matched user - join answers on questions and
            DBDataObject[] questionsAnsweredByUser =   DBAnswer.ops.joinValuesBy(
                            DBAnswer.ops.matchByValue(DBAnswer.K_USER_ID, userID),                                                        // where a.uid = userID
                    new String[] {
                            DBAnswer.ops.matchByKey(DBAnswer.K_QUESTION_ID, DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID)),   // join q on a.qid = q.qid
                            DBQuestion.ops.matchByKey(DBQuestion.K_USER_ID, DBUser.ops.appendKeys(DBUser.K_USER_ID))},              // join u on q.uid = u.uid
                    DBAnswer.ops.appendEmptyKeys(),                                                                                       // from a
                    DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID, DBQuestion.K_QUESTION_HEAD, DBQuestion.K_QUESTION_BYTESCORE),                                      // select q.qid, q.q_head (join q)
                    DBUser.ops.appendKeys(DBUser.K_USER_NAME, DBUser.K_USER_PROFILE));                                                                                        // join u

            DBDataObject questionData = new DBDataObject();

            String questionerProfile = userData.getValue(DBUser.K_USER_PROFILE);
            String questionerUsername = userData.getValue(DBUser.K_USER_NAME);
            String questionID;
            String questionHead;
            String questionBytes;

            for (int i = 0; i < questionsAskedByUser.length; i++) {
                questionID = questionsAskedByUser[i].getValue(DBQuestion.K_QUESTION_ID);
                questionHead = "Questioned: " + questionsAskedByUser[i].getValue(DBQuestion.K_QUESTION_HEAD);
                questionBytes = questionsAskedByUser[i].getValue(DBQuestion.K_QUESTION_BYTESCORE);

                questionData.putKeyValue(String.valueOf(i),
                        questionerProfile + BoardFrameSwitchDelegate.DELIMITER +
                        questionerUsername + BoardFrameSwitchDelegate.DELIMITER +
                        questionID + BoardFrameSwitchDelegate.DELIMITER +
                        questionHead + BoardFrameSwitchDelegate.DELIMITER +
                        questionBytes);
                System.out.println("Added Q: " + questionData.getValue(String.valueOf(i)));
            }

            for (int i = 0; i < questionsAnsweredByUser.length; i++) {

                questionerProfile = questionsAnsweredByUser[i].getValue(DBUser.K_USER_PROFILE);
                questionerUsername = questionsAnsweredByUser[i].getValue(DBUser.K_USER_NAME);
                questionID = questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_ID);
                questionHead = "Answered: " + questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_HEAD);
                questionBytes = questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_BYTESCORE);

                questionData.putKeyValue(String.valueOf(i + questionsAskedByUser.length),
                        questionerProfile + BoardFrameSwitchDelegate.DELIMITER +
                                questionerUsername + BoardFrameSwitchDelegate.DELIMITER +
                                questionID + BoardFrameSwitchDelegate.DELIMITER +
                                questionHead + BoardFrameSwitchDelegate.DELIMITER +
                                questionBytes);
                System.out.println("Added A: " + questionData.getValue(String.valueOf(i + questionsAskedByUser.length)));
            }

            delegate.putContext(DBUser.TABLE, userData);
            delegate.putContext(DBQuestion.TABLE, questionData);
            return true;
        });

        GridBagBuilder builder = new GridBagBuilder(this, 3);
        builder.fill(GridBagConstraints.BOTH);
        builder.gridWeightY(1);

        buttonPanel = new ProfileBoardButtonPanel(main, this);
        addPanel(ProfileBoardButtonPanel.class.getSimpleName(), buttonPanel, builder.getConstraints());

        userDataPanel = new ProfileBoardUserDataPanel(main, this);
        builder.skipCells(2);
        builder.gridWeightX(1);
        addPanel(ProfileBoardUserDataPanel.class.getSimpleName(), userDataPanel, builder.getConstraints());

        editPanel = new ProfileEditPanel(main, this);
        builder.anchor(GridBagConstraints.CENTER);
        addPanel(ProfileEditPanel.class.getSimpleName(), editPanel, builder.getConstraints());

        builder.skipCells(1);
        activityPanel = new ProfileBoardActivityPanel(main, this);
        addPanel(ProfileBoardActivityPanel.class.getSimpleName(), activityPanel, builder.getConstraints());
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate frameSwitchDelegate) {
        DBDataObject userData = frameSwitchDelegate.getContext(DBUser.TABLE);

        String userProfile = userData.getValue(DBUser.K_USER_PROFILE);
        String username = userData.getValue(DBUser.K_USER_NAME);
        String userEmail = userData.getValue(DBUser.K_EMAIL);

        editPanel.setProfile(userProfile);
        editPanel.setUsername(username);
        editPanel.setUserEmail(userEmail);

        userDataPanel.setProfile(userProfile);
        userDataPanel.setUsername(username);
        userDataPanel.setUserEmail(userEmail);
        userDataPanel.setUserBytes(userData.getValue(DBUser.K_USER_BYTESCORE));

        DBDataObject questionData = frameSwitchDelegate.getContext(DBQuestion.TABLE);

        activityPanel.clearActivities();

        for (int i = 0; i < questionData.getKeyValueMap().size(); i++) {
            String[] contentData = questionData.getValue(String.valueOf(i)).split(BoardFrameSwitchDelegate.DELIMITER);

            System.out.println(Arrays.toString(contentData));
            ContentPane contentPane = activityPanel.addActivity();
            contentPane.setUserData(contentData[0], contentData[1]);
            contentPane.setContentData(contentData[3], contentData[2], contentData[4]);
        }
    }
}
