package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;

import java.util.HashMap;
import java.util.Map;

public class BoardAnswerPanel extends BoardContentPanel {

    private static final Map<String, BoardAnswerPanel> ANSWER_PANELS = new HashMap<>();
    private static String currentAnswerID;

    public BoardAnswerPanel(MainFrame main, Frame frame) {
        super(main, frame, DBVote::voteAnswer);
    }

    protected static BoardAnswerPanel getPanel(MainFrame main, Frame frame, String answerID) {
        if (answerID == null) {
            if (currentAnswerID == null) return null;
            answerID = currentAnswerID;
        }

        currentAnswerID = answerID;

        BoardAnswerPanel answerPanel = ANSWER_PANELS.get(answerID);

        return answerPanel != null ? answerPanel : addPanel(main, frame, answerID);
    }

    protected static BoardAnswerPanel addPanel(MainFrame main, Frame frame, String answerID) {
        BoardAnswerPanel answerPanel = new BoardAnswerPanel(main, frame);
        ANSWER_PANELS.put(answerID, answerPanel);
        return answerPanel;
    }

    protected static boolean isNoneLoaded() {
        return currentAnswerID == null && ANSWER_PANELS.isEmpty();
    }

    protected static BoardAnswerPanel getCurrentAnswerPanel() throws NullPointerException {
        return ANSWER_PANELS.get(currentAnswerID);
    }

    protected static String getCurrentAnswerID() {
        return currentAnswerID;
    }

    protected static void clearAnswers() {
        currentAnswerID = null;
        for (BoardAnswerPanel answerPanel : ANSWER_PANELS.values()) {
            answerPanel.getContentResponseCardPanel().clearResponseCards();
        }

        ANSWER_PANELS.clear();
    }

    public void setData(String answerID, DBDataObject answerDataObject, String questionHead, String userID) {
        // Load and set the answer panel data
        setUserID(userID);
        setContentID(answerDataObject.getValue(DBAnswer.K_ANSWER_ID));
        setContentHead("Answering:\n", questionHead);
        setContentBody(answerDataObject.getValue(DBAnswer.K_ANSWER));
        setContentBytes(answerDataObject.getValue(DBAnswer.K_ANSWER_BYTESCORE));
        setContentUserID(answerDataObject.getValue(DBAnswer.K_USER_ID));
        setSelfViewer(answerDataObject.getValue(DBAnswer.K_USER_ID).equals(userID));

        setVoteType(DBVote.getVoteType(userID, DBVote.K_ANSWER_ID, answerID));

        DBDataObject userData = DBUser.getUser(answerDataObject.getValue(DBAnswer.K_USER_ID));
        setContentUsername("Answered By - ", userData.getValue(DBUser.K_USER_NAME),
                userData.getValue(DBUser.K_USER_PROFILE));

        DBDataObject[] commentDataObjects = DBComment.getComments(DBComment.K_ANSWER_ID, answerID);
        for (DBDataObject comment : commentDataObjects) {
            DBDataObject commentUserdata = DBUser.getUser(comment.getValue(DBComment.K_USER_ID));
            comment.putKeyValue(DBUser.K_USER_NAME, commentUserdata.getValue(DBUser.K_USER_NAME));
            comment.putKeyValue(DBUser.K_USER_PROFILE, commentUserdata.getValue(DBUser.K_USER_PROFILE));
            comment.putKeyValue(DBCFeedback.K_FEEDBACK, DBCFeedback.getFeedback(userID, comment.getValue(DBComment.K_COMMENT_ID)));
        }

        setContentResponseCards(commentDataObjects, DBComment.K_COMMENT_ID);
    }

    protected void createAddCommentCard(BoardResponseCardPanel commentsPanel) {
        BoardEditResponseCard card = new BoardEditResponseCard(getMain(), getFrame());
        card.setSubmitAction(getUserID(), getContentResponseCardPanel());
        card.setCommentContentType(DBComment.K_ANSWER_ID, getContentID());

        commentsPanel.addEditResponseCard(card);
    }
}
