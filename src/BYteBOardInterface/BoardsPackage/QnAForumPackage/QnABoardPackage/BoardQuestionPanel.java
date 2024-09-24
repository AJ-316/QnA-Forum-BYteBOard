package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;

public class BoardQuestionPanel extends BoardContentPanel {

    public BoardQuestionPanel(MainFrame main, Frame frame) {
        super(main, frame, DBVote::voteQuestion);
    }

    public void setData(DBDataObject questionDataObject, DBDataObject[] commentDataObjects, DBDataObject[] tagDataObjects, String userID) {
        setUserID(userID);
        setVoteType(questionDataObject.getValue(DBVote.V_VOTE_UP));

        setContentID(questionDataObject.getValue(DBQuestion.K_QUESTION_ID));
        setContentHead("Question:\n", questionDataObject.getValue(DBQuestion.K_QUESTION_HEAD));
        setContentBody(questionDataObject.getValue(DBQuestion.K_QUESTION_BODY));
        setContentBytes(questionDataObject.getValue(DBQuestion.K_QUESTION_BYTESCORE));

        setContentUserID(questionDataObject.getValue(DBQuestion.K_USER_ID));
        setContentUsername("Questioned By - ", questionDataObject.getValue(DBUser.K_USER_NAME),
                questionDataObject.getValue(DBUser.K_USER_PROFILE));

        setSelfViewer(questionDataObject.getValue(DBQuestion.K_USER_ID).equals(userID));

        setContentResponseCards(commentDataObjects, DBComment.K_COMMENT_ID);
        setContentTags(tagDataObjects, userID);
    }

    protected void createAddCommentCard(BoardResponseCardPanel commentsPanel) {
        BoardEditResponseCard card = new BoardEditResponseCard(getMain(), getFrame());
        card.setSubmitAction(getUserID(), getContentResponseCardPanel());
        card.setCommentContentType(DBComment.K_QUESTION_ID, getContentID());

        commentsPanel.addEditResponseCard(card);
    }
}
