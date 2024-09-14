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

            DBDataObject questionDataObject = DBQuestion.ops.getQuestion(questionID);
            questionDataObject.putKeyValue(DBVote.V_VOTE_UP, DBVote.getVoteType(userID, DBVote.K_QUESTION_ID, questionID));

            DBDataObject questionUserData = DBUser.getUser(questionDataObject.getValue(DBQuestion.K_USER_ID));
            questionDataObject.putKeyValue(DBUser.K_USER_NAME, questionUserData.getValue(DBUser.K_USER_NAME));
            questionDataObject.putKeyValue(DBUser.K_USER_PROFILE, questionUserData.getValue(DBUser.K_USER_PROFILE));
            delegate.putContext(DBQuestion.TABLE, questionDataObject);

            DBDataObject userDataObject = new DBDataObject();
            userDataObject.putKeyValue(DBUser.K_USER_ID, userID);
            delegate.putContext(DBUser.TABLE, userDataObject);

            DBDataObject[] answerDataObjectList = DBAnswer.ops.findValuesBy(DBAnswer.ops.matchByValue(DBAnswer.K_QUESTION_ID, questionID), "*");
            for (DBDataObject answer : answerDataObjectList) {
                DBDataObject answerUserData = DBUser.getUser(answer.getValue(DBAnswer.K_USER_ID));
                answer.putKeyValue(DBUser.K_USER_NAME, answerUserData.getValue(DBUser.K_USER_NAME));
                answer.putKeyValue(DBUser.K_USER_PROFILE, answerUserData.getValue(DBUser.K_USER_PROFILE));
            }
            delegate.putContextList(DBAnswer.TABLE, DBAnswer.K_ANSWER_ID, answerDataObjectList);

            DBDataObject[] commentQueDataObjectList = DBComment.getComments(DBComment.K_QUESTION_ID, questionID);
            for (DBDataObject comment : commentQueDataObjectList) {
                DBDataObject commentUserDataObject = DBUser.getUser(comment.getValue(DBComment.K_USER_ID));
                comment.putKeyValue(DBUser.K_USER_NAME, commentUserDataObject.getValue(DBUser.K_USER_NAME));
                comment.putKeyValue(DBUser.K_USER_PROFILE, commentUserDataObject.getValue(DBUser.K_USER_PROFILE));
                comment.putKeyValue(DBCFeedback.K_FEEDBACK, DBCFeedback.getFeedback(userID, comment.getValue(DBComment.K_COMMENT_ID)));
            }
            delegate.putContextList(DBComment.TABLE, DBComment.K_COMMENT_ID, commentQueDataObjectList);

            DBDataObject[] tagDataObjectList = DBTag.getTags(questionID);
            delegate.putContextList(DBTag.TABLE, DBTag.K_TAG_ID, tagDataObjectList);

            return null;
        });

        answerContentPanels = new HashMap<>();
        displayQuestionContent(true);
    }

    public void init(MainFrame main) {
        // Create Instances
        updateAnswerVoteDatabase = (id, type) -> DBVote.ops.voteAnswer(id, getUserID(), type);

        answerCardsPanel = new BoardContentResponsePanel(main, this);
        answerCardsPanel.setTitle("Answers", "answer", "No Answers!");

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
        answerContentPanel.setSelfViewer(answerData.getValue(DBAnswer.K_USER_ID).equals(userID));

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
            requestSwitchFrame(ProfileBoardFrame.class);
            displayQuestionContent(true);
            answerContentPanels.clear();
            if(currentAnswerContentPanel != null)
                remove(currentAnswerContentPanel);
            currentAnswerContentPanel = null;
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

        DBDataObject questionDataObject = delegate.getContext(DBQuestion.TABLE);

        questionContentPanel.setVoteType(questionDataObject.getValue(DBVote.V_VOTE_UP));
        questionContentPanel.setContentID(questionDataObject.getValue(DBQuestion.K_QUESTION_ID));
        questionContentPanel.setContentHead("Question:\n", questionDataObject.getValue(DBQuestion.K_QUESTION_HEAD));
        questionContentPanel.setContentBody(questionDataObject.getValue(DBQuestion.K_QUESTION_BODY));
        questionContentPanel.setContentBytes(questionDataObject.getValue(DBQuestion.K_QUESTION_BYTESCORE));
        questionContentPanel.setContentUserID(questionDataObject.getValue(DBQuestion.K_USER_ID));
        questionContentPanel.setContentUsername("Questioned By - ", questionDataObject.getValue(DBUser.K_USER_NAME),
                questionDataObject.getValue(DBUser.K_USER_PROFILE));
        questionContentPanel.setSelfViewer(questionDataObject.getValue(DBQuestion.K_USER_ID).equals(userID));

        List<DBDataObject> questionDataList = new ArrayList<>();
        questionContentPanel.setContentComments(delegate.getContextList(DBComment.TABLE, questionDataList), userID);
        questionContentPanel.setContentTags(delegate.getContextList(DBTag.TABLE, questionDataList));
        createAnswerCards(delegate.getContextList(DBAnswer.TABLE, questionDataList));

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
                    answerData.getValue(DBUser.K_USER_PROFILE),
                    answer, answerData.getValue(DBAnswer.K_ANSWER_ID));
            card.restoreCardSelection();
        }

        answerCardsPanel.restoreScrolls();
    }
}
