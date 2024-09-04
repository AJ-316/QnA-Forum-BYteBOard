/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;

import CustomControls.SimpleScrollPane;
import DatabasePackage.*;
import QnAForumInterface.InformationBarPackage.AnswerBar;
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
    private CustomControls.RoundedJPanel answersContainerHolder;
    private SimpleScrollPane answersContainerScrollPane;
    private JLabel noRespondersLabel;
    private OptionsPanel optionsPanel;
    private MainPanel questionPanel;
    private String questionID;

    public QnABoard() {
        initComponents();
        clearAnswerBoards();
        displayQuestionPanel();
        CurrentInstance = this;
    }

    public static QnABoard init(String username, String questioner, String questionerProfileIndex, String questionID) {
        // get question with matching questionID
        DBDataObject questionData = DBQuestion.ops.findValuesBy(DBQuestion.ops.matchByValue(DBQuestion.K_QUESTION_ID, questionID), "*")[0];

        // get logged user
        DBDataObject loggedUserData = DBUser.accessUser(username, false, true);

        QnABoard board = new QnABoard();
        board.questionID = questionID;
        board.optionsPanel.setUserInfo(username, loggedUserData.getValue(DBUser.K_USER_PROFILE), !questioner.equals(username));

        // get tags associated with the question
        DBDataObject[] tagsData = DBTag.ops.findValuesBy(DBTag.ops.matchByValue(DBTag.K_QUESTION_ID, questionID), "*");

        String[] tags = new String[tagsData.length];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = tagsData[i].getValue(DBTag.K_TAG);
        }

        board.questionPanel.setContent(questioner, questionerProfileIndex, tags,
                questionData.getValue(DBQuestion.K_QUESTION_HEAD),
                questionData.getValue(DBQuestion.K_QUESTION_BODY));

        // get all the answers of matching questionID
        DBDataObject[] answersOfQuestion = DBAnswer.ops.joinValuesBy(
                DBAnswer.ops.matchByValue(DBAnswer.K_QUESTION_ID, questionID),
                new String[] {DBAnswer.ops.matchByKey(DBAnswer.K_USER_ID, DBUser.ops.appendKeys(DBUser.K_USER_ID))},
                DBAnswer.ops.appendKeys(DBAnswer.K_ANSWER), DBUser.ops.appendKeys(DBUser.K_USER_NAME, DBUser.K_USER_PROFILE));

        if (answersOfQuestion != null) {
            for (DBDataObject answerData : answersOfQuestion) {
                board.addAnswerBoard(
                        answerData.getValue(DBUser.K_USER_NAME),
                        answerData.getValue(DBUser.K_USER_PROFILE),
                        answerData.getValue(DBAnswer.K_ANSWER));
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

    public void setAnswerPanelContent(String responderName, String responderProfileIndex, String answerBody) {
        answerPanel.setVisible(true);
        questionPanel.setVisible(false);

        answerPanel.setContent(responderName, responderProfileIndex, null, null, answerBody);
    }

    public void displayQuestionPanel() {
        answerPanel.setVisible(false);
        questionPanel.setVisible(true);
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

    public void addAnswerBoard(String responderName, String responderProfileIndex, String answerBody) {
        this.noRespondersLabel.setVisible(false);

        AnswerBar answerBar = new AnswerBar();
        answerBar.setContent(responderName, responderProfileIndex, answerBody);
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

        questionPanel = new MainPanel();
        questionPanel.setContentType(1);
        answerPanel = new MainPanel();
        answerPanel.setContentType(0);
        optionsPanel = new OptionsPanel();
        answersContainerHolder = new CustomControls.RoundedJPanel();
        noRespondersLabel = new JLabel();
        answersContainerScrollPane = new SimpleScrollPane();
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
