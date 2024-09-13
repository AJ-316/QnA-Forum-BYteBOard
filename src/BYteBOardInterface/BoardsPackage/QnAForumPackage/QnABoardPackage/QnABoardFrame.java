package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QnABoardFrame extends BoardFrame {

    private String userID;
    private BoardButton viewQuestionButton;
    private BoardButton backButton;

    private BoardContentPanel questionContentPanel;
    private Map<String, BoardContentPanel> answersContentPanel;

    //private BoardPanel responsePanel;
    private BoardSplitPanel responseSplitPanel;

    private BoardContentPanel currentAnswerPanel;
    private BoardContentPanel.UpdateVoteDatabase updateVoteDatabase;
    //private BoardContentResponsePanel[] contentResponsePanel;

    public QnABoardFrame(MainFrame main) {
        super(main, (delegate, context) -> {
            String questionID;
            String userID;

            if (context != null && context.length != 0) {
                questionID = context[0];
                userID = context[1];
            } else {
                DBDataObject loadedQuestionData = delegate.getContext(DBQuestion.TABLE);
                if(loadedQuestionData == null) return null;

                questionID = loadedQuestionData.getValue(DBQuestion.K_QUESTION_ID);
                userID = delegate.getContext(DBUser.TABLE).getValue(DBUser.K_USER_ID);
            }

            DBDataObject questionData = DBQuestion.ops.getQuestion(questionID);
            questionData.putKeyValue(DBVote.V_VOTE_UP, DBVote.getVoteType(userID, DBVote.K_QUESTION_ID, questionID));

            String questionUsername = DBUser.getUser(questionData.getValue(DBQuestion.K_USER_ID)).getValue(DBUser.K_USER_NAME);
            questionData.putKeyValue(DBUser.K_USER_NAME, questionUsername);
            delegate.putContext(DBQuestion.TABLE, questionData);

            DBDataObject userData = new DBDataObject();
            userData.putKeyValue(DBUser.K_USER_ID, userID);
            delegate.putContext(DBUser.TABLE, userData);

            DBDataObject[] answers = DBAnswer.ops.findValuesBy(DBAnswer.ops.matchByValue(DBAnswer.K_QUESTION_ID, questionID), "*");
            for(DBDataObject answer : answers)
                answer.putKeyValue(DBUser.K_USER_NAME, DBUser.getUser(answer.getValue(DBAnswer.K_USER_ID)).getValue(DBUser.K_USER_NAME));
            delegate.putContextList(DBAnswer.TABLE, DBAnswer.K_ANSWER_ID, answers);

            DBDataObject[] questionComments = DBComment.ops.getComments(DBComment.K_QUESTION_ID, questionID);
            for(DBDataObject comment : questionComments) {
                comment.putKeyValue(DBUser.K_USER_NAME, DBUser.getUser(comment.getValue(DBComment.K_USER_ID)).getValue(DBUser.K_USER_NAME));
                comment.putKeyValue(DBCFeedback.K_FEEDBACK, DBCFeedback.getFeedback(userID, comment.getValue(DBComment.K_COMMENT_ID)));
            }
            delegate.putContextList(DBComment.TABLE, DBComment.K_COMMENT_ID, questionComments);

            return null;
        });

        answersContentPanel = new HashMap<>();
    }

    public void init(MainFrame main) {
        questionContentPanel = new BoardContentPanel(main, this, true,
                (id, type) -> DBVote.ops.voteQuestion(id, getUserID(), type));

        updateVoteDatabase = (id, type) -> DBVote.ops.voteAnswer(id, getUserID(), type);
        /*answersContentPanel = new BoardContentPanel(main, this, false,
                (id, type) -> DBVote.ops.voteAnswer(id, getUserID(), type));*/

        BoardPanel buttonsPanel = createButtons(main);

        //contentResponsePanel = new BoardContentResponsePanel(main, this);
        GridBagBuilder builder = new GridBagBuilder(this, 2);

        builder.gridWidth(1);
        builder.gridHeight(2);
        builder.weightX(0.8);
        builder.weightY(1);
        builder.fill(GridBagConstraints.BOTH);
        add(questionContentPanel, builder.getConstraints());
        // add(answersContentPanel, builder.getConstraints()); // added on cell(0, 0)

        builder.skipCells(1);
        builder.add(buttonsPanel, 1, 1, 1, 0, GridBagConstraints.BOTH);

        builder.skipCells(2);
        builder.gridWidth(1);
        builder.gridHeight(1);
        builder.weightX(0.2);
        builder.weightY(1);
        builder.fill(GridBagConstraints.BOTH);

        responseSplitPanel = new BoardSplitPanel(BoardSplitPanel.VERTICAL_SPLIT, answerPanel, commentsPanel);
        responseSplitPanel.setLayout(new BorderLayout());
        add(responseSplitPanel, builder.getConstraints());
        //add(contentResponsePanel, builder.getConstraints());
        //builder.add(contentResponsePanel, 1, 1, 0.2, 1, GridBagConstraints.BOTH);
    }

    private void setAnswer(String answerID) {
        BoardContentPanel answerPanel = answersContentPanel.get(answerID);
        if(answerPanel != null && answerPanel.equals(currentAnswerPanel))
            return;

        if(answerPanel == null) {
            answerPanel = new BoardContentPanel(getMain(), this, true, updateVoteDatabase);
            answersContentPanel.put(answerID, answerPanel);
        } else {
            remove(currentAnswerPanel);
        }

        DBDataObject answerData = DBAnswer.getAnswer(answerID);

        answerPanel.setContentID(answerData.getValue(DBAnswer.K_ANSWER_ID));
        answerPanel.setContentHead("Answering:\n", questionContentPanel.getContentHead());
        answerPanel.setContentBody(answerData.getValue(DBAnswer.K_ANSWER));
        answerPanel.setContentBytes(answerData.getValue(DBAnswer.K_ANSWER_BYTESCORE));
        answerPanel.setContentUserID(answerData.getValue(DBAnswer.K_USER_ID));

        answerPanel.setVoteType(DBVote.getVoteType(userID, DBVote.K_ANSWER_ID, answerID));
        answerPanel.setContentUsername(DBUser.getUser(answerData.getValue(DBAnswer.K_USER_ID)).getValue(DBUser.K_USER_NAME));

        DBDataObject[] comments = DBComment.ops.getComments(DBComment.K_ANSWER_ID, answerID);
        for (DBDataObject comment : comments) {
            comment.putKeyValue(DBUser.K_USER_NAME, DBUser.getUser(comment.getValue(DBComment.K_USER_ID)).getValue(DBUser.K_USER_NAME));
            comment.putKeyValue(DBCFeedback.K_FEEDBACK, DBCFeedback.getFeedback(userID, comment.getValue(DBComment.K_COMMENT_ID)));
        }
        answerPanel.setContentComments(comments, userID);

        GridBagConstraints constraints = ((GridBagLayout) getLayout()).getConstraints(questionContentPanel);
        add(currentAnswerPanel = answerPanel, constraints);

        displayQuestionContent(false);
    }

    private BoardPanel createButtons(MainFrame main) {
        viewQuestionButton = new BoardButton("View Question", "question");
        backButton = new BoardButton("Profile", "home");

        backButton.addActionListener(e -> requestSwitchFrame(ProfileBoardFrame.class));
        viewQuestionButton.addActionListener(e -> {
            displayQuestionContent(true);
            viewQuestionButton.setVisible(false);
        });

        BoardPanel panel = new BoardPanel(main, this);
        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.add(viewQuestionButton);
        builder.add(backButton);
        return panel;
    }

    private void displayQuestionContent(boolean isVisible) {
        if (isVisible) {
            questionContentPanel.setVisible(true);
            if(currentAnswerPanel != null)
                currentAnswerPanel.setVisible(false);

            responseSplitPanel.removeAll();
            responseSplitPanel.add(questionContentPanel.getContentResponsePanel());
            return;
        }

        questionContentPanel.setVisible(false);
        if(currentAnswerPanel != null) {
            currentAnswerPanel.setVisible(true);
            responseSplitPanel.removeAll();
            responseSplitPanel.add(currentAnswerPanel.getContentResponsePanel());
        }
    }

    protected String getUserID() {
        return userID;
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate delegate) {
        DBDataObject userData = delegate.getContext(DBUser.TABLE);
        userID = userData.getValue(DBUser.K_USER_ID);

        DBDataObject questionData = delegate.getContext(DBQuestion.TABLE);
        questionContentPanel.setVoteType(questionData.getValue(DBVote.V_VOTE_UP));
        questionContentPanel.setContentID(questionData.getValue(DBQuestion.K_QUESTION_ID));
        questionContentPanel.setContentHead("Question:\n", questionData.getValue(DBQuestion.K_QUESTION_HEAD));
        questionContentPanel.setContentBody(questionData.getValue(DBQuestion.K_QUESTION_BODY));
        questionContentPanel.setContentBytes(questionData.getValue(DBQuestion.K_QUESTION_BYTESCORE));
        questionContentPanel.setContentUserID(questionData.getValue(DBQuestion.K_USER_ID));
        questionContentPanel.setContentUsername(questionData.getValue(DBUser.K_USER_NAME));

        List<DBDataObject> questionResponseData = new ArrayList<>();
        questionContentPanel.setContentResponse(delegate.getContextList(DBAnswer.TABLE, questionResponseData));
        questionContentPanel.setContentComments(delegate.getContextList(DBComment.TABLE, questionResponseData), userID);

        //displayQuestionContent(true);

        setAnswer("1");
    }
}
