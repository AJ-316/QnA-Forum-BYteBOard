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

    private final Map<String, BoardContentPanel> answerContentPanels;
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

            DBDataObject questionUserData = DBUser.getUser(questionData.getValue(DBQuestion.K_USER_ID));
            questionData.putKeyValue(DBUser.K_USER_NAME, questionUserData.getValue(DBUser.K_USER_NAME));
            questionData.putKeyValue(DBUser.K_USER_PROFILE, questionUserData.getValue(DBUser.K_USER_PROFILE));
            delegate.putContext(DBQuestion.TABLE, questionData);

            DBDataObject userData = new DBDataObject();
            userData.putKeyValue(DBUser.K_USER_ID, userID);
            delegate.putContext(DBUser.TABLE, userData);

            DBDataObject[] answers = DBAnswer.ops.findValuesBy(DBAnswer.ops.matchByValue(DBAnswer.K_QUESTION_ID, questionID), "*");
            for (DBDataObject answer : answers) {
                DBDataObject answerUserData = DBUser.getUser(answer.getValue(DBAnswer.K_USER_ID));
                answer.putKeyValue(DBUser.K_USER_NAME, answerUserData.getValue(DBUser.K_USER_NAME));
                answer.putKeyValue(DBUser.K_USER_PROFILE, answerUserData.getValue(DBUser.K_USER_PROFILE));
            }
            delegate.putContextList(DBAnswer.TABLE, DBAnswer.K_ANSWER_ID, answers);

            DBDataObject[] questionComments = DBComment.ops.getComments(DBComment.K_QUESTION_ID, questionID);
            for (DBDataObject comment : questionComments) {
                DBDataObject commentUserData = DBUser.getUser(comment.getValue(DBComment.K_USER_ID));
                comment.putKeyValue(DBUser.K_USER_NAME, commentUserData.getValue(DBUser.K_USER_NAME));
                comment.putKeyValue(DBUser.K_USER_PROFILE, commentUserData.getValue(DBUser.K_USER_PROFILE));
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
        updateAnswerVoteDatabase = (id, type) -> DBVote.ops.voteAnswer(id, getUserID(), type);

        answerCardsPanel = new BoardContentResponsePanel(main, this, ByteBoardTheme.MAIN_DARK);
        answerCardsPanel.setTitle("Answers", "No Answers!");

        responseSplitPanel = new BoardSplitPanel(BoardSplitPanel.VERTICAL_SPLIT);
        responseSplitPanel.setTopComponent(answerCardsPanel);

        questionContentPanel = new BoardContentPanel(main, this,
                (id, type) -> DBVote.ops.voteQuestion(id, getUserID(), type));

        // Add Components
        GridBagBuilder builder = new GridBagBuilder(this, 2);

        builder.weight(0, 1).fillBoth()
                .addToNextCell(responseSplitPanel);

        builder.gridHeight(2).weightX(1)
                .addToNextCell(questionContentPanel);

        builder.gridHeight(1).weight(0, 0)
                .addToNextCell(createButtons(main));
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
        }

        // Load and set the answer panel data
        DBDataObject answerData = DBAnswer.getAnswer(answerID);

        answerContentPanel.setContentID(answerData.getValue(DBAnswer.K_ANSWER_ID));
        answerContentPanel.setContentHead("Answering:\n", questionContentPanel.getContentHead());
        answerContentPanel.setContentBody(answerData.getValue(DBAnswer.K_ANSWER));
        answerContentPanel.setContentBytes(answerData.getValue(DBAnswer.K_ANSWER_BYTESCORE));
        answerContentPanel.setContentUserID(answerData.getValue(DBAnswer.K_USER_ID));

        answerContentPanel.setVoteType(DBVote.getVoteType(userID, DBVote.K_ANSWER_ID, answerID));

        DBDataObject userData = DBUser.getUser(answerData.getValue(DBAnswer.K_USER_ID));
        answerContentPanel.setContentUsername("Answered By - ", userData.getValue(DBUser.K_USER_NAME),
                userData.getValue(DBUser.K_USER_PROFILE));

        DBDataObject[] comments = DBComment.ops.getComments(DBComment.K_ANSWER_ID, answerID);
        for (DBDataObject comment : comments) {
            DBDataObject commentUserdata = DBUser.getUser(comment.getValue(DBComment.K_USER_ID));
            comment.putKeyValue(DBUser.K_USER_NAME, commentUserdata.getValue(DBUser.K_USER_NAME));
            comment.putKeyValue(DBUser.K_USER_PROFILE, commentUserdata.getValue(DBUser.K_USER_PROFILE));
            comment.putKeyValue(DBCFeedback.K_FEEDBACK, DBCFeedback.getFeedback(userID, comment.getValue(DBComment.K_COMMENT_ID)));
        }
        answerContentPanel.setContentComments(comments, userID);

        // Add the answer panel at the position of question panel and hide question panel
        GridBagConstraints constraints = ((GridBagLayout) getLayout()).getConstraints(questionContentPanel);

        if (currentAnswerContentPanel != null)
            remove(currentAnswerContentPanel);

        currentAnswerContentPanel = answerContentPanel;

        add(currentAnswerContentPanel, constraints);

        if(setVisibility) displayQuestionContent(false);

        revalidate();
        repaint();
    }

    private BoardPanel createButtons(MainFrame main) {

        viewQuestionButton = new BoardButton("View Question", "question");
        viewQuestionButton.setVisible(false);
        viewQuestionButton.setAlignmentLeading();
        viewQuestionButton.setFGLight();
        viewQuestionButton.addActionListener(e -> displayQuestionContent(true));

        Dimension buttonSize = new Dimension(400, viewQuestionButton.getPreferredSize().height);
        viewQuestionButton.setMinimumSize(buttonSize);

        backButton = new BoardButton("Profile", "home");
        backButton.setAlignmentLeading();
        backButton.setFGLight();
        backButton.setMinimumSize(buttonSize);
        backButton.addActionListener(e -> {
            answerContentPanels.clear();
            requestSwitchFrame(ProfileBoardFrame.class);
            displayQuestionContent(true);
            questionContentPanel.setContentID("-1");
        });

        BoardPanel panel = new BoardPanel(main, this);

        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.weight(1, 1).fillBoth()
                .addToNextCell(viewQuestionButton)
                .addToNextCell(backButton);

        panel.setPreferredSize(buttonSize);
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

        BoardContentResponsePanel commentCardsPanel = isVisible ?
                questionContentPanel.getContentCommentPanel() :
                currentAnswerContentPanel.getContentCommentPanel();

        responseSplitPanel.setBottomComponent(commentCardsPanel);
    }

    protected String getUserID() {
        return userID;
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate delegate) {
        DBDataObject userData = delegate.getContext(DBUser.TABLE);
        userID = userData.getValue(DBUser.K_USER_ID);

        DBDataObject questionData = delegate.getContext(DBQuestion.TABLE);
        String lastQuestionID = questionContentPanel.getContentID();

        questionContentPanel.setVoteType(questionData.getValue(DBVote.V_VOTE_UP));
        questionContentPanel.setContentID(questionData.getValue(DBQuestion.K_QUESTION_ID));
        questionContentPanel.setContentHead("Question:\n", questionData.getValue(DBQuestion.K_QUESTION_HEAD));
        questionContentPanel.setContentBody(questionData.getValue(DBQuestion.K_QUESTION_BODY));
        questionContentPanel.setContentBytes(questionData.getValue(DBQuestion.K_QUESTION_BYTESCORE));
        questionContentPanel.setContentUserID(questionData.getValue(DBQuestion.K_USER_ID));
        questionContentPanel.setContentUsername("Questioned By - ", questionData.getValue(DBUser.K_USER_NAME),
                questionData.getValue(DBUser.K_USER_PROFILE));

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
                System.out.println("contentID null");
                return;
            }

            System.out.println("Setting answer: " + contentID);
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
                    answerData.getValue(DBUser.K_USER_PROFILE),
                    answer, answerData.getValue(DBAnswer.K_ANSWER_ID));
            card.restoreCardSelection();
        }

        answerCardsPanel.restoreScrolls();
    }
}
