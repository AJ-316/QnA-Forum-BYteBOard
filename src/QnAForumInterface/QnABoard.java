/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;

import CustomControls.CustomJPanel;
import CustomControls.DEBUG;
import CustomControls.SimpleScrollPane;
import DatabasePackage.*;
import QnAForumInterface.InformationBarPackage.AnswerBar;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.MainPanelPackage.MainPanel;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author AJ
 */
public class QnABoard extends JPanel {

    public static QnABoard CurrentInstance;
    private MainPanel answerPanel;
    private JPanel answersContainer;
    private CustomJPanel answersContainerHolder;
    private JLabel noRespondersLabel;
    private OptionsPanel optionsPanel;
    private MainPanel questionPanel;
    private String questionID;
    private String answerID;
    private String viewerUserID;
    private String viewerUserName;

    public QnABoard() {
        initComponents();
        clearAnswerBoards();
        displayQuestionPanel();
        CurrentInstance = this;

        InterfaceEventManager.addListener("Update." + MainPanel.CONTENT_QUESTION + "Vote", ec -> {
            String bytescore = DBVote.ops.voteQuestion(questionID, viewerUserID, (String) ec[0]);
            questionPanel.updateByteScore(bytescore);
        });

        InterfaceEventManager.addListener("Update." + MainPanel.CONTENT_ANSWER + "Vote", ec -> {
            String bytescore = DBVote.ops.voteAnswer(answerID, viewerUserID, (String) ec[0]);
            answerPanel.updateByteScore(bytescore);
        });
    }

    public static QnABoard init(String username, String questioner, String questionerProfileIndex, String questionID) {
        // get question with matching questionID
        DBDataObject questionData = DBQuestion.ops.findValuesBy(DBQuestion.ops.matchByValue(DBQuestion.K_QUESTION_ID, questionID), "*")[0];

        // get logged user
        DBDataObject loggedUserData = DBUser.accessUser(username, false, true);

        QnABoard board = new QnABoard();
        if(username.equals(questioner))
            board.questionPanel.disableVotes();

        board.viewerUserName = username;
        board.viewerUserID = loggedUserData.getValue(DBUser.K_USER_ID);
        board.questionID = questionID;
        board.optionsPanel.setUserInfo(username, loggedUserData.getValue(DBUser.K_USER_PROFILE), !questioner.equals(username));

        // get tags associated with the question
        DBDataObject[] tagsData = DBTag.ops.findValuesBy(DBTag.ops.matchByValue(DBTag.K_QUESTION_ID, questionID), "*");

        String[] tags = new String[tagsData.length];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = tagsData[i].getValue(DBTag.K_TAG);
        }

        DBDataObject[] voteData = DBVote.ops.findVotesBy(DBVote.K_QUESTION_ID,
                board.viewerUserID, questionID, DBVote.K_VOTE_TYPE);
        String lastVoteType = DBVote.V_VOTE_NONE;

        if(voteData.length != 0)
            lastVoteType = voteData[0].getValue(DBVote.K_VOTE_TYPE);

        board.questionPanel.setContent(questioner, questionerProfileIndex, tags,
                questionData.getValue(DBQuestion.K_QUESTION_HEAD),
                questionData.getValue(DBQuestion.K_QUESTION_BODY),
                questionData.getValue(DBQuestion.K_QUESTION_BYTESCORE), lastVoteType);

        // get all the answers of matching questionID
        DBDataObject[] answersOfQuestion = DBAnswer.ops.joinValuesBy(
                DBAnswer.ops.matchByValue(DBAnswer.K_QUESTION_ID, questionID),
                new String[] {DBAnswer.ops.matchByKey(DBAnswer.K_USER_ID, DBUser.ops.appendKeys(DBUser.K_USER_ID))},
                DBAnswer.ops.appendKeys(DBAnswer.K_ANSWER, DBAnswer.K_ANSWER_ID),
                DBUser.ops.appendKeys(DBUser.K_USER_NAME, DBUser.K_USER_PROFILE));

        if (answersOfQuestion != null) {
            for (DBDataObject answerData : answersOfQuestion) {
                board.addAnswerBoard(
                        answerData.getValue(DBUser.K_USER_NAME),
                        answerData.getValue(DBUser.K_USER_PROFILE),
                        answerData.getValue(DBAnswer.K_ANSWER),
                        answerData.getValue(DBAnswer.K_ANSWER_ID));
            }
        }
        System.out.println("\u001b[0m");
        return board;
    }

    public static void answerCurrentQuestion() {
        QnAForum.setContent(AskBoard.init(
                CurrentInstance.optionsPanel.getUsername(),
                CurrentInstance.optionsPanel.getUserProfileIndex(),
                CurrentInstance.questionPanel.getContentUser(),
                CurrentInstance.questionPanel.getContentHead(),
                CurrentInstance.questionPanel.getContentBody(),
                CurrentInstance.questionID));
    }

    public void setAnswerPanelContent(String responderName, String responderProfileIndex, String answerBody, String answerID) {
        if(responderName.equals(viewerUserName)) {
            answerPanel.disableVotes();
        }

        DBDataObject answerData = DBAnswer.ops.findValuesBy(
                DBAnswer.ops.matchByValue(DBAnswer.K_ANSWER_ID, answerID), DBAnswer.K_ANSWER_BYTESCORE)[0];

        DBDataObject[] voteData = DBVote.ops.findVotesBy(DBVote.K_ANSWER_ID, viewerUserID, answerID, DBVote.K_VOTE_TYPE);
        String lastVotedType = DBVote.V_VOTE_NONE;
        if (voteData.length != 0)
            lastVotedType = voteData[0].getValue(DBVote.K_VOTE_TYPE);

        answerPanel.setVisible(true);
        questionPanel.setVisible(false);
        this.answerID = answerID;
        answerPanel.setContent(responderName, responderProfileIndex,
                null, null, answerBody, answerData.getValue(DBAnswer.K_ANSWER_BYTESCORE), lastVotedType);
    }

    public void displayQuestionPanel() {
        answerPanel.setVisible(false);
        questionPanel.setVisible(true);
        revalidate();
        repaint();
    }

    public void clearAnswerBoards() {
        this.answersContainer.removeAll();
        this.noRespondersLabel.setVisible(true);
        revalidate();
        repaint();
    }

    public void removeAnswerBoard(String responderName) {
        int indexToRemove = -1;
        for (int i = 0; i < answersContainer.getComponents().length; i++) {
            Component component = answersContainer.getComponents()[i];
            if (component instanceof AnswerBar) {
                if (((AnswerBar) component).isRespondent(responderName)) {
                    indexToRemove = i;
                    break;
                }
            }
        }
        if (indexToRemove == -1) return;

        answersContainer.remove(indexToRemove);
        this.noRespondersLabel.setVisible(answersContainer.getComponentCount() == 0);

        updateAnswerBoardColors();

        revalidate();
        repaint();
    }

    public void addAnswerBoard(String responderName, String responderProfileIndex, String answerBody, String answerID) {
        this.noRespondersLabel.setVisible(false);

        AnswerBar answerBar = new AnswerBar();
        answerBar.setContent(responderName, responderProfileIndex, answerBody, answerID);
        this.answersContainer.add(answerBar);

        updateAnswerBoardColors();
        revalidate();
        repaint();
    }

    private void updateAnswerBoardColors() {
        for (int i = 0; i < answersContainer.getComponentCount(); i++) {
            AnswerBar answerBar = (AnswerBar) answersContainer.getComponent(i);

            answerBar.setDefaultBackground(i % 2 == 0 ?
                    ResourceManager.getColor(ByteBoardTheme.MAIN) :
                    ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
        }
    }

    public boolean toggleAnswerDisplay() {
        answersContainerHolder.setVisible(!answersContainerHolder.isVisible());
        return answersContainerHolder.isVisible();
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        questionPanel = new MainPanel(MainPanel.CONTENT_QUESTION);
        questionPanel.setContentType(MainPanel.CONTENT_QUESTION);
        answerPanel = new MainPanel(MainPanel.CONTENT_ANSWER);
        answerPanel.setContentType(MainPanel.CONTENT_ANSWER);
        optionsPanel = new OptionsPanel();
        answersContainerHolder = new CustomJPanel();
        noRespondersLabel = new JLabel();
        SimpleScrollPane answersContainerScrollPane = new SimpleScrollPane();
        answersContainerScrollPane.getVerticalScrollBar().setUnitIncrement(8);
        answersContainer = new JPanel();

        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new GridBagLayout());

        questionPanel.setMinimumSize(new Dimension(499, 400));
        questionPanel.setPreferredSize(new Dimension(540, 500));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(questionPanel, gridBagConstraints);

        answerPanel.setMinimumSize(new Dimension(499, 400));
        answerPanel.setPreferredSize(new Dimension(540, 500));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(answerPanel, gridBagConstraints);

        optionsPanel.setMaximumSize(new Dimension(196, 304));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.4;
        add(optionsPanel, gridBagConstraints);

        answersContainerHolder.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        answersContainerHolder.setCornerRadius(90);
        answersContainerHolder.setMinimumSize(new Dimension(398, 120));
        answersContainerHolder.setLayout(new GridBagLayout());

        noRespondersLabel.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        noRespondersLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.DISABLED));
        noRespondersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noRespondersLabel.setText("No Responds Yet!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        answersContainerHolder.add(noRespondersLabel, gridBagConstraints);
        noRespondersLabel.setFont(ResourceManager.getFont("inter_thin.48"));

        answersContainerScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        answersContainerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        answersContainerScrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        answersContainerScrollPane.setMinimumSize(new Dimension(0, 0));
        answersContainerScrollPane.setOpaque(false);
        answersContainerScrollPane.setPreferredSize(new Dimension(0, 150));
        answersContainerScrollPane.setViewportView(null);

        answersContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        answersContainer.setMinimumSize(new Dimension(0, 150));
        answersContainer.setOpaque(false);
        answersContainer.setLayout(new BoxLayout(answersContainer, BoxLayout.Y_AXIS));
        answersContainerScrollPane.setViewportView(answersContainer);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        answersContainerHolder.add(answersContainerScrollPane, gridBagConstraints);
        answersContainerScrollPane.getViewport().setOpaque(false);
        answersContainerScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        add(answersContainerHolder, gridBagConstraints);
    }

}
