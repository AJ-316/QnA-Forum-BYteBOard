package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardDatabase.DBAnswer;
import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQuestion;
import BYteBOardDatabase.DBUser;
import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.GridBagBuilder;

public class ProfileBoardFrame extends BoardFrame {

    private ProfileBoardButtonPanel buttonPanel;
    private ProfileBoardUserDataPanel userDataPanel;
    private ProfileEditPanel editPanel;
    private BoardContentDisplayPanel activityPanel;

    public ProfileBoardFrame(MainFrame main) {
        super(main, (delegate, context) -> {
            String userID = delegate.getContextOrDefault(context, DBUser.TABLE, DBUser.K_USER_ID)[0];

            DBDataObject userData = DBUser.getUser(userID);

            // get all questions asked by matched user
            DBDataObject[] questionsAskedByUser = DBQuestion.ops.findValuesBy(
                    DBQuestion.ops.matchByValue(DBQuestion.K_USER_ID, userID), "*");

            // get all questionId's answered by matched user - join answers on questions and
            DBDataObject[] questionsAnsweredByUser = DBAnswer.ops.joinValuesBy(
                    DBAnswer.ops.matchByValue(DBAnswer.K_USER_ID, userID),                                                        // where a.uid = userID
                    new String[]{
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
                questionHead = questionsAskedByUser[i].getValue(DBQuestion.K_QUESTION_HEAD);
                questionBytes = questionsAskedByUser[i].getValue(DBQuestion.K_QUESTION_BYTESCORE);

                questionData.putKeyValue(String.valueOf(i),
                        questionerProfile + BoardFrameSwitchDelegate.DELIMITER +
                                "Questioned by - " + questionerUsername + BoardFrameSwitchDelegate.DELIMITER +
                                questionID + BoardFrameSwitchDelegate.DELIMITER +
                                questionHead + BoardFrameSwitchDelegate.DELIMITER +
                                questionBytes);
            }

            for (int i = 0; i < questionsAnsweredByUser.length; i++) {

                questionerProfile = questionsAnsweredByUser[i].getValue(DBUser.K_USER_PROFILE);
                questionerUsername = questionsAnsweredByUser[i].getValue(DBUser.K_USER_NAME);
                questionID = questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_ID);
                questionHead = questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_HEAD);
                questionBytes = questionsAnsweredByUser[i].getValue(DBQuestion.K_QUESTION_BYTESCORE);

                questionData.putKeyValue(String.valueOf(i + questionsAskedByUser.length),
                        questionerProfile + BoardFrameSwitchDelegate.DELIMITER +
                                "Answered to - " + questionerUsername + BoardFrameSwitchDelegate.DELIMITER +
                                questionID + BoardFrameSwitchDelegate.DELIMITER +
                                questionHead + BoardFrameSwitchDelegate.DELIMITER +
                                questionBytes);
            }

            delegate.putContext(DBUser.TABLE, userData);
            delegate.putContext(DBQuestion.TABLE, questionData);
            return userID;
        });
    }

    public void init(MainFrame main) {
        buttonPanel = new ProfileBoardButtonPanel(main, this);
        userDataPanel = new ProfileBoardUserDataPanel(main, this);
        editPanel = new ProfileEditPanel(main, this);
        activityPanel = new BoardContentDisplayPanel(main, this);
        activityPanel.setStatusLabel("No Activity\nLet's Get Started!");
        activityPanel.setShadowState(OFFSET_SHADOW);

        GridBagBuilder builder = new GridBagBuilder(this, 3);

        builder.weightY(1).fillBoth();
        addPanel(ProfileBoardButtonPanel.class, buttonPanel, builder);

        builder.weightX(1).skipCells(2);
        addPanel(ProfileBoardUserDataPanel.class, userDataPanel, builder);
        addPanel(ProfileEditPanel.class, editPanel, builder);

        builder.skipCells(1);
        addPanel(BoardContentDisplayPanel.class, activityPanel, builder);
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

        buttonPanel.setUserID(userData.getValue(DBUser.K_USER_ID));

        DBDataObject questionData = frameSwitchDelegate.getContext(DBQuestion.TABLE);

        activityPanel.clearQuestions();

        for (int i = 0; i < questionData.getKeyValueMap().size(); i++) {
            String[] contentData = questionData.getValue(String.valueOf(i)).split(BoardFrameSwitchDelegate.DELIMITER);

            ContentDisplayPane activityPane = activityPanel.addActivity();
            activityPane.setUserData(contentData[0], contentData[1], userData.getValue(DBUser.K_USER_ID));
            activityPane.setContentData(contentData[3], contentData[2], contentData[4]);
        }
        activityPanel.resetScrolls();
    }
}
