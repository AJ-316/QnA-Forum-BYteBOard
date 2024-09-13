package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QnABoardFrame extends BoardFrame {

    private String userID;
    private BoardButton viewQuestionButton;
    private BoardButton backButton;

    private Map<String, BoardContentPanel> answerContentPanels;
    private BoardContentPanel questionContentPanel;
    private BoardContentResponsePanel answerCardsPanel;

    private BoardSplitPanel responseSplitPanel;

    private BoardContentPanel currentAnswerContentPanel;
    private BoardContentPanel.UpdateVoteDatabase updateAnswerVoteDatabase;


    public QnABoardFrame(MainFrame main) {
        super(main, (delegate, context) -> {
            String questionID;
            String userID;

            if (context != null && context.length != 0) {
                questionID = context[0];
                userID = context[1];
            } else {
                DBDataObject loadedQuestionData = delegate.getContext(DBQuestion.TABLE);
                if (loadedQuestionData == null) return null;

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
            for (DBDataObject answer : answers)
                answer.putKeyValue(DBUser.K_USER_NAME, DBUser.getUser(answer.getValue(DBAnswer.K_USER_ID)).getValue(DBUser.K_USER_NAME));
            delegate.putContextList(DBAnswer.TABLE, DBAnswer.K_ANSWER_ID, answers);

            DBDataObject[] questionComments = DBComment.ops.getComments(DBComment.K_QUESTION_ID, questionID);
            for (DBDataObject comment : questionComments) {
                comment.putKeyValue(DBUser.K_USER_NAME, DBUser.getUser(comment.getValue(DBComment.K_USER_ID)).getValue(DBUser.K_USER_NAME));
                comment.putKeyValue(DBCFeedback.K_FEEDBACK, DBCFeedback.getFeedback(userID, comment.getValue(DBComment.K_COMMENT_ID)));
            }
            delegate.putContextList(DBComment.TABLE, DBComment.K_COMMENT_ID, questionComments);

            return null;
        });

        answerContentPanels = new HashMap<>();
        displayQuestionContent(true);
    }

    public void init(MainFrame main) {

        // Create Instances
        questionContentPanel = new BoardContentPanel(main, this,
                (id, type) -> DBVote.ops.voteQuestion(id, getUserID(), type));

        updateAnswerVoteDatabase = (id, type) -> DBVote.ops.voteAnswer(id, getUserID(), type);

        answerCardsPanel = new BoardContentResponsePanel(main, this, ByteBoardTheme.MAIN_DARK);
        answerCardsPanel.setTitle("Answers");

        BoardPanel buttonsPanel = createButtons(main);

        // Add Components
        GridBagBuilder builder = new GridBagBuilder(this, 2);

        // cell(0, 0)
        builder.gridWidth(1);
        builder.gridHeight(2);
        builder.weightX(0.8);
        builder.weightY(1);
        builder.fill(GridBagConstraints.BOTH);
        add(questionContentPanel, builder.getConstraints());

        // cell(1, 0)
        builder.skipCells(1);
        builder.add(buttonsPanel, 1, 1, 1, 0, GridBagConstraints.BOTH);

        // cell(1, 2)
        builder.skipCells(2);
        builder.gridWidth(1);
        builder.gridHeight(1);
        builder.weightX(0.2);
        builder.weightY(1);
        builder.fill(GridBagConstraints.BOTH);

        responseSplitPanel = new BoardSplitPanel(BoardSplitPanel.VERTICAL_SPLIT);
        responseSplitPanel.setTopComponent(answerCardsPanel);
        add(responseSplitPanel, builder.getConstraints());
    }

    /**
     * @param answerID Pass null to update the already loaded answer
     * */
    private void setAnswerData(String answerID, boolean setVisibility) {
        if (answerID == null) {
            if (currentAnswerContentPanel == null) return;
            answerID = currentAnswerContentPanel.getContentID();
        }

        BoardContentPanel answerContentPanel = answerContentPanels.get(answerID);

        if (answerContentPanel == null) {
            // If the answer panel not created then create
            answerContentPanel = new BoardContentPanel(getMain(), this, updateAnswerVoteDatabase);
            answerContentPanels.put(answerID, answerContentPanel);
            System.out.println("Loaded: " + answerID);
        }

        // Load and set the answer panel data
        DBDataObject answerData = DBAnswer.getAnswer(answerID);

        answerContentPanel.setContentID(answerData.getValue(DBAnswer.K_ANSWER_ID));
        answerContentPanel.setContentHead("Answering:\n", questionContentPanel.getContentHead());
        answerContentPanel.setContentBody(answerData.getValue(DBAnswer.K_ANSWER));
        answerContentPanel.setContentBytes(answerData.getValue(DBAnswer.K_ANSWER_BYTESCORE));
        answerContentPanel.setContentUserID(answerData.getValue(DBAnswer.K_USER_ID));

        answerContentPanel.setVoteType(DBVote.getVoteType(userID, DBVote.K_ANSWER_ID, answerID));
        answerContentPanel.setContentUsername(DBUser.getUser(answerData.getValue(DBAnswer.K_USER_ID)).getValue(DBUser.K_USER_NAME));

        DBDataObject[] comments = DBComment.ops.getComments(DBComment.K_ANSWER_ID, answerID);
        for (DBDataObject comment : comments) {
            comment.putKeyValue(DBUser.K_USER_NAME, DBUser.getUser(comment.getValue(DBComment.K_USER_ID)).getValue(DBUser.K_USER_NAME));
            comment.putKeyValue(DBCFeedback.K_FEEDBACK, DBCFeedback.getFeedback(userID, comment.getValue(DBComment.K_COMMENT_ID)));
        }
        answerContentPanel.setContentComments(comments, userID);

        // Add the answer panel at the position of question panel and hide question panel
        GridBagConstraints constraints = ((GridBagLayout) getLayout()).getConstraints(questionContentPanel);

        if (currentAnswerContentPanel != null) {
            System.out.println("Removed Old: " + currentAnswerContentPanel.getContentID());
            remove(currentAnswerContentPanel);
        }

        currentAnswerContentPanel = answerContentPanel;

        System.out.println("Added New: " + answerID);
        add(currentAnswerContentPanel, constraints);
        revalidate();
        repaint();

        if(setVisibility) {
            displayQuestionContent(false);
        }
    }

    private BoardPanel createButtons(MainFrame main) {
        viewQuestionButton = new BoardButton("View Question", "question");
        viewQuestionButton.setVisible(false);
        viewQuestionButton.setAlignmentLeading();
        viewQuestionButton.addActionListener(e -> displayQuestionContent(true));

        backButton = new BoardButton("Profile", "home");
        backButton.setAlignmentLeading();
        backButton.addActionListener(e -> {
            requestSwitchFrame(ProfileBoardFrame.class);
            displayQuestionContent(true);
        });

        BoardPanel panel = new BoardPanel(main, this);
        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.fill(GridBagConstraints.BOTH);
        builder.weightX(1);
        builder.weightY(1);
        builder.anchor(GridBagConstraints.WEST);
        builder.add(viewQuestionButton);
        builder.add(backButton);
        return panel;
    }

    private void displayQuestionContent(boolean isVisible) {
        questionContentPanel.setVisible(isVisible);
        backButton.setVisible(isVisible);
        viewQuestionButton.setVisible(!isVisible);

        if(isVisible) BoardContentCard.selectCard(false, null);

        if (currentAnswerContentPanel == null) {
            responseSplitPanel.setBottomComponent(questionContentPanel.getContentCommentPanel());
            return;
        }

        currentAnswerContentPanel.setVisible(!isVisible);

        BoardContentResponsePanel commentsPanel = isVisible ?
                questionContentPanel.getContentCommentPanel() :
                currentAnswerContentPanel.getContentCommentPanel();

        responseSplitPanel.setBottomComponent(commentsPanel);
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
        questionContentPanel.setContentComments(delegate.getContextList(DBComment.TABLE, questionResponseData), userID);

        createAnswerCards(delegate.getContextList(DBAnswer.TABLE, questionResponseData));

        setAnswerData(null, false);
    }

    private void createAnswerCards(DBDataObject[] answersData) {
        answerCardsPanel.clearContentCards();
        final BoardContentCard.CardSelectListener listener = contentID -> {
            if(contentID == null) {
                displayQuestionContent(true);
                return;
            }

            setAnswerData(contentID, true);
        };

        for (DBDataObject answerData : answersData) {
            BoardContentCard card = answerCardsPanel.addContentCard();
            card.addMouseListeners(listener);

            String answer = answerData.getValue(DBAnswer.K_ANSWER);
            if (answer.length() > 100) {
                String trimmedAnswer = answer.substring(0, 100).trim();

                int lastWordIndex = trimmedAnswer.lastIndexOf(' ');

                answer = lastWordIndex != -1 ? answer.substring(0, lastWordIndex) : trimmedAnswer;
                answer += "...";
            }

            card.setCardData(answerData.getValue(DBUser.K_USER_NAME),
                    answer, answerData.getValue(DBAnswer.K_ANSWER_ID));
        }
    }
}
