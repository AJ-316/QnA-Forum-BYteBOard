package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.BoardSplitPanel;
import CustomControls.DEBUG;
import CustomControls.GridBagBuilder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class QnABoardFrame extends BoardFrame {

    protected static final int QUESTION = 1;
    protected static final int ANSWER = 2;
    protected static final int EDIT_ANSWER = 3;

    private String userID;
    private BoardButton viewQuestionButton;
    private BoardButton backButton;
    private BoardQuestionPanel questionPanel;

    private BoardEditContentPanel answerCardEditContentPanel;

    private BoardSplitPanel responseSplitPanel;

    public QnABoardFrame(MainFrame main) {
        super(main, (delegate, context) -> {
            context = delegate.getContextOrDefault(context,
                    DBQuestion.TABLE, DBQuestion.K_QUESTION_ID,
                    DBUser.TABLE, DBUser.K_USER_ID);

            String questionID = context[0];
            String userID = context[1];

            DBDataObject questionDataObject = DBQuestion.ops.getQuestion(questionID);
            questionDataObject.putKeyValue(DBVote.V_VOTE_UP, DBVote.getVoteType(userID, DBVote.K_QUESTION_ID, questionID));

            DBDataObject questionUserData = DBUser.getUser(questionDataObject.getValue(DBQuestion.K_USER_ID));
            questionDataObject.putKeyValue(DBUser.K_USER_NAME, questionUserData.getValue(DBUser.K_USER_NAME));
            questionDataObject.putKeyValue(DBUser.K_USER_PROFILE, questionUserData.getValue(DBUser.K_USER_PROFILE));
            delegate.putContext(DBQuestion.TABLE, questionDataObject);

            delegate.putContext(DBUser.TABLE, DBUser.getUser(userID));

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

            DBDataObject[] tagDataObjectList = DBQueTag.getQueTags(questionID);
            delegate.putContextList(DBQueTag.TABLE, DBQueTag.K_QUE_TAG_ID, tagDataObjectList);
            return null;
        });

        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    refresh();
                }
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    answerCardEditContentPanel.getContentResponseCardPanel().selectCard(null);
                }
            }
        });
    }

    public void init(MainFrame main) {
        questionPanel = new BoardQuestionPanel(main, this);

        answerCardEditContentPanel = new BoardEditContentPanel(main, this, contentID -> {
            if (contentID == null) {
                switchBoardContent(QUESTION);
                return;
            }

            setAnswerData(contentID);
            switchBoardContent(ANSWER);
            answerCardEditContentPanel.getContentResponseCardPanel().resetResponseButtons(true);
        });

        responseSplitPanel = new BoardSplitPanel(BoardSplitPanel.VERTICAL_SPLIT);
        responseSplitPanel.setTopComponent(answerCardEditContentPanel.getContentResponseCardPanel());
        responseSplitPanel.setBottomComponent(questionPanel.getContentResponseCardPanel());

        GridBagBuilder builder = new GridBagBuilder(this, 2);

        builder.weight(0, 1).fillBoth()
                .addToNextCell(responseSplitPanel);

        builder.gridHeight(2).weightX(1).skipCells(1);
        addPanel(BoardQuestionPanel.class, questionPanel, builder);
        addPanel(BoardEditContentPanel.class, answerCardEditContentPanel, builder);

        builder.gridHeight(1).weight(0, 0)
                .addToNextCell(createButtons(main));
    }

    /**
     * @param answerID Pass null to update the already loaded answer
     */
    private void setAnswerData(String answerID) {
        BoardAnswerPanel answerPanel = BoardAnswerPanel.getPanel(getMain(), this, answerID);
        if (answerPanel == null) return;

        answerID = BoardAnswerPanel.getCurrentAnswerID();
        answerPanel.setData(answerID, DBAnswer.getAnswer(answerID), questionPanel.getContentHead(), userID);

        addAnswerPanel(answerPanel);

        revalidate();
        repaint();
    }

    private void addAnswerPanel(BoardAnswerPanel answerPanel) {
        for (Component component : getComponents()) {
            if(component instanceof BoardAnswerPanel) {
                if(answerPanel == null) {
                    removePanel(component); // remove if answerPanel@param is null
                    return;
                } else if(answerPanel.equals(component)) // if same instance then don't remove or add
                    return;

                removePanel(component); // remove the other instance if available
            }
        }

        if(answerPanel == null) return;

        addPanel(BoardAnswerPanel.class, answerPanel, ((GridBagLayout) getLayout()).getConstraints(questionPanel));
        answerPanel.setVisible(false);
    }

    private BoardPanel createButtons(MainFrame main) {
        viewQuestionButton = new BoardButton("View Question", "question");
        viewQuestionButton.setVisible(false);
        viewQuestionButton.setAlignmentLeading();
        viewQuestionButton.setFGLight();
        viewQuestionButton.addActionListener(e -> switchBoardContent(QUESTION));

        Dimension buttonSize = new Dimension(400, viewQuestionButton.getPreferredSize().height);
        viewQuestionButton.setMinimumSize(buttonSize);

        backButton = new BoardButton("Profile", "home");
        backButton.setAlignmentLeading();
        backButton.setFGLight();
        backButton.setMinimumSize(buttonSize);
        backButton.addActionListener(e -> {
            requestSwitchFrame(ProfileBoardFrame.class);
            switchBoardContent(QUESTION);
            BoardAnswerPanel.clearAnswers();
            questionPanel.getContentResponseCardPanel().clearResponseCards();
            questionPanel.getContentResponseCardPanel().resetResponseButtons(true);
            answerCardEditContentPanel.getContentResponseCardPanel().clearResponseCards();
            answerCardEditContentPanel.getContentResponseCardPanel().resetResponseButtons(true);
            addAnswerPanel(null);
        });

        BoardPanel panel = new BoardPanel(main, this);

        GridBagBuilder builder = new GridBagBuilder(panel, 1);
        builder.weight(1, 1).fillBoth()
                .addToNextCell(viewQuestionButton)
                .addToNextCell(backButton);

        panel.setPreferredSize(buttonSize);
        return panel;
    }

    protected void switchBoardContent(int contentClass) {
        int location = responseSplitPanel.getDividerLocation();
        switch (contentClass) {
            case QUESTION:
                answerCardEditContentPanel.getContentResponseCardPanel().selectCard(null);
                responseSplitPanel.setBottomComponent(questionPanel.getContentResponseCardPanel());
                break;
            case ANSWER:
                if(!BoardAnswerPanel.isNoneLoaded())
                    responseSplitPanel.setBottomComponent(BoardAnswerPanel.getCurrentAnswerPanel().getContentResponseCardPanel());
                break;
            case EDIT_ANSWER:
                answerCardEditContentPanel.getContentResponseCardPanel().selectCard(null);
                break;
        }
        responseSplitPanel.setDividerLocation(location);

        setPanelVisibility(BoardQuestionPanel.class, contentClass == QUESTION);
        setPanelVisibility(BoardAnswerPanel.class, contentClass == ANSWER);
        setPanelVisibility(BoardEditContentPanel.class, contentClass == EDIT_ANSWER);

        backButton.setVisible(contentClass == QUESTION || contentClass == EDIT_ANSWER);
        viewQuestionButton.setVisible(contentClass == ANSWER);
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate delegate) {
        answerCardEditContentPanel.getContentResponseCardPanel().storeScroll();
        questionPanel.getContentResponseCardPanel().storeScroll();
        if(!BoardAnswerPanel.isNoneLoaded())
            BoardAnswerPanel.getCurrentAnswerPanel().getContentResponseCardPanel().storeScroll();

        DBDataObject userData = delegate.getContext(DBUser.TABLE);
        userID = userData.getValue(DBUser.K_USER_ID);

        DBDataObject questionDataObject = delegate.getContext(DBQuestion.TABLE);
        List<DBDataObject> questionDataList = new ArrayList<>();

        questionPanel.setData(questionDataObject,
                delegate.getContextList(DBComment.TABLE, questionDataList),
                delegate.getContextList(DBQueTag.TABLE, questionDataList), userID);

        answerCardEditContentPanel.setUserID(userID);
        answerCardEditContentPanel.setContentID(questionPanel.getContentID());
        answerCardEditContentPanel.setContentUsername("", userData.getValue(DBUser.K_USER_NAME), userData.getValue(DBUser.K_USER_PROFILE));
        answerCardEditContentPanel.setContentHead("Answering:\n", questionPanel.getContentHead());
        answerCardEditContentPanel.setContentResponseCards(delegate.getContextList(DBAnswer.TABLE, questionDataList), DBAnswer.K_ANSWER_ID);

        setAnswerData(null);

        answerCardEditContentPanel.getContentResponseCardPanel().restoreScrolls();
        questionPanel.getContentResponseCardPanel().restoreScrolls();
        if(!BoardAnswerPanel.isNoneLoaded())
            BoardAnswerPanel.getCurrentAnswerPanel().getContentResponseCardPanel().restoreScrolls();
    }
}
