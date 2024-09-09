package BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BoardStructurePackage.BoardFrame;
import BoardStructurePackage.BoardFrameSwitchDelegate;
import BoardStructurePackage.MainFrame;
import CustomControls.GridBagBuilder;
import DatabasePackage.*;

import java.awt.*;

public class ProfileBoardFrame extends BoardFrame {

    private final ProfileDisplayPanel displayPanel;
    private final ProfileEditPanel editPanel;

    public ProfileBoardFrame(MainFrame main) {
        super(main, (context, delegate) -> {
            String userID = context;

            if (userID == null) {
                DBDataObject loadedUserData = delegate.getContext(DBUser.TABLE);
                if(loadedUserData == null) return false;
                userID = loadedUserData.getValue(DBUser.K_USER_ID);
            }

            // get all questions asked by matched user
            DBDataObject[] questionsAskedByUser = DBQuestion.ops.findValuesBy(
                    DBQuestion.ops.matchByValue(DBQuestion.K_USER_ID, userID), "*");

            // get all questionId's answered by matched user - join answers on questions and
            DBDataObject[] questionsAnsweredByUser = DBAnswer.ops.joinValuesBy(
                    DBAnswer.ops.matchByValue(DBAnswer.K_USER_ID, userID),                                                          // where a.uid = userID
                    new String[] {
                            DBAnswer.ops.matchByKey(DBAnswer.K_QUESTION_ID, DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID)),   // join q on a.qid = q.qid
                            DBQuestion.ops.matchByKey(DBQuestion.K_USER_ID, DBUser.ops.appendKeys(DBUser.K_USER_ID))},              // join u on q.uid = u.uid
                    DBAnswer.ops.appendEmptyKeys(),                                                                                       // from a
                    DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID, DBQuestion.K_QUESTION_HEAD),                                      // select q.qid, q.q_head (join q)
                    DBUser.ops.appendEmptyKeys());                                                                                        // join u

            DBDataObject questionData = new DBDataObject();

            for (int i = 0; i < questionsAskedByUser.length; i++) {

                String questionID = questionsAskedByUser[i].getValue(DBQuestion.K_QUESTION_ID);
                String questionHead = questionsAskedByUser[i].getValue(DBQuestion.K_QUESTION_HEAD);

                questionData.putKeyValue(String.valueOf(i),
                        questionID + BoardFrameSwitchDelegate.DELIMITER + questionHead);
            }

            for (int i = 0; i < questionsAnsweredByUser.length; i++) {

                String questionID = questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_ID);
                String questionHead = questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_HEAD);

                questionData.putKeyValue(String.valueOf(i + questionsAskedByUser.length),
                        questionID + BoardFrameSwitchDelegate.DELIMITER + questionHead);
            }

            delegate.putContext(DBUser.TABLE, DBUser.getUser(userID));
            delegate.putContext(DBQuestion.TABLE, questionData);
            return true;
        });

        GridBagBuilder builder = new GridBagBuilder(this);
        builder.fill(GridBagConstraints.BOTH);
        builder.gridWeightX(1);
        builder.gridWeightY(1);

        displayPanel = new ProfileDisplayPanel(main, this);
        addPanel(ProfileDisplayPanel.class.getSimpleName(), displayPanel, builder.getConstraints());

        editPanel = new ProfileEditPanel(main, this);
        addPanel(ProfileEditPanel.class.getSimpleName(), editPanel, builder.getConstraints());
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate frameSwitchDelegate) {
        DBDataObject userData = frameSwitchDelegate.getContext(DBUser.TABLE);

        String userProfile = userData.getValue(DBUser.K_USER_PROFILE);
        String username = userData.getValue(DBUser.K_USER_NAME);
        String userEmail = userData.getValue(DBUser.K_EMAIL);

        editPanel.setProfile(userProfile);
        editPanel.setUsername(username);
        editPanel.setUserEmail(userEmail);

        displayPanel.setProfile(userProfile);
        displayPanel.setUsername(username);
        displayPanel.setUserEmail(userEmail);
        displayPanel.setUserBytes(userData.getValue(DBUser.K_USER_BYTESCORE));
    }
}
