package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.DBCFeedback;
import BYteBOardDatabase.DBComment;
import BYteBOardDatabase.DBDataObject;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import javax.swing.*;
import java.awt.*;

public class BoardContentResponsePanel extends BoardPanel {

    private GridBagBuilder commentsBuilder;
    private GridBagBuilder answersBuilder;

    private BoardLabel answersStatusLabel;
    private BoardLabel commentsStatusLabel;

    public BoardContentResponsePanel(MainFrame main, Frame frame) {
        super(main, frame);
    }

    public void init(MainFrame main, Frame frame) {
        setLayout(new BorderLayout());

        BoardPanel commentsPanel = getCommentsPanel(main, frame);
        BoardPanel answerPanel = getAnswersPanel(main, frame);

        BoardSplitPanel splitPanel = new BoardSplitPanel(BoardSplitPanel.VERTICAL_SPLIT, answerPanel, commentsPanel);
        add(splitPanel);

        clearContentCards(commentsBuilder, commentsStatusLabel);
        clearContentCards(answersBuilder, answersStatusLabel);

        /*for (int i = 0; i < 2; i++)
            addContentCard(commentsBuilder, commentsStatusLabel).setContentAction(e -> System.out.println("Selected: " + e), "search");

        for (int i = 0; i < 10; i++)
            addContentCard(answersBuilder, answersStatusLabel);*/
    }

    private BoardPanel getCommentsPanel(MainFrame main, Frame frame) {
        BoardScrollPanel commentsScrollPanel = new BoardScrollPanel(main, frame);
        BoardPanel commentsPanel = createContentResponsePanel(main, frame, "Comments",
                commentsScrollPanel, ByteBoardTheme.MAIN);

        commentsBuilder = createResponseLayoutBuilder(commentsScrollPanel);
        commentsStatusLabel = createResponseStatusLabel("<html><div style='text-align: center;'>No Comments<br>Yet</div></html>");
        return commentsPanel;
    }

    private BoardPanel getAnswersPanel(MainFrame main, Frame frame) {
        BoardScrollPanel answersScrollPanel = new BoardScrollPanel(main, frame);
        BoardPanel answersPanel = createContentResponsePanel(main, frame, "Answers",
                answersScrollPanel, ByteBoardTheme.MAIN_DARK);

        answersBuilder = createResponseLayoutBuilder(answersScrollPanel);
        answersStatusLabel = createResponseStatusLabel("<html><div style='text-align: center;'>No Answers<br>Yet</div></html>");
        return answersPanel;
    }

    private BoardPanel createContentResponsePanel(MainFrame main, Frame frame, String title, BoardScrollPanel scrollPanel, String bgColor) {
        BoardPanel panel = new BoardPanel(main, frame, bgColor);
        panel.setShadowState(BoardPanel.OFFSET_SHADOW);
        panel.setLayout(new BorderLayout());
        panel.setCornerRadius(30);
        panel.addInsets(10);

        BoardLabel titleLabel = new BoardLabel(title);
        titleLabel.addInsets(8);
        titleLabel.setAlignmentLeading();
        titleLabel.setFGLight();
        titleLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 22);

        scrollPanel.setBackground(panel.getBackground());
        scrollPanel.getComponent().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPanel.getComponent(), BorderLayout.CENTER);

        return panel;
    }

    private GridBagBuilder createResponseLayoutBuilder(Container container) {
        GridBagBuilder builder = new GridBagBuilder(container, 1);
        builder.insets(10, 10, 10, 20);
        builder.weightX(1);
        builder.fill(GridBagConstraints.HORIZONTAL);

        return builder;
    }

    private BoardLabel createResponseStatusLabel(String text) {
        BoardLabel statusLabel = new BoardLabel();
        statusLabel.setText(text);
        statusLabel.setName(statusLabel.getText());
        statusLabel.addInsets(10);
        statusLabel.setFGLight();
        statusLabel.setFontPrimary(ByteBoardTheme.FONT_T_THIN, 20);

        return statusLabel;
    }

    public void clearAnswers() {
        clearContentCards(answersBuilder, answersStatusLabel);
    }

    public void clearComments() {
        clearContentCards(commentsBuilder, commentsStatusLabel);
    }

    public void addAnswer(String username, String answer, String answerID) {
        BoardContentCard card = addContentCard(answersBuilder, answersStatusLabel);
        card.setCardData(username, answer, answerID);
    }

    public void addComment(String username, String comment, String commentID, boolean isFeedbackUseful, String userID) {
        BoardContentCard card = addContentCard(commentsBuilder, commentsStatusLabel);
        card.setCardData(username, comment, commentID);
        card.setContentAction(isFeedbackUseful, isActionSelect -> DBCFeedback.giveFeedback(userID, commentID, isActionSelect), "upvote");
    }

    // todo: on select answer -> load all its data and comments in DBDataObjects of ContentPanel (question/answer).
    //  and make a list of answerPanels to save loading and unloading.

    private void clearContentCards(GridBagBuilder layoutBuilder, BoardLabel statusLabel) {
        layoutBuilder.getContainer().removeAll();
        layoutBuilder.gridCell(0, 0);
        addStatusLabel(layoutBuilder, statusLabel, statusLabel.getName());
    }

    private BoardContentCard addContentCard(GridBagBuilder layoutBuilder, BoardLabel statusLabel) {
        removeStatusLabel(layoutBuilder, statusLabel);

        BoardContentCard pane = new BoardContentCard(getMain(), getFrame());
        layoutBuilder.add(pane);

        addStatusLabel(layoutBuilder, statusLabel, "");

        return pane;
    }

    private void addStatusLabel(GridBagBuilder layoutBuilder, BoardLabel statusLabel, String text) {
        layoutBuilder.anchor(GridBagConstraints.CENTER);
        layoutBuilder.weightY(1);
        layoutBuilder.add(statusLabel);
        statusLabel.setText(text);
    }

    private void removeStatusLabel(GridBagBuilder layoutBuilder, BoardLabel statusLabel) {
        layoutBuilder.skipCells(-1);
        layoutBuilder.getContainer().remove(statusLabel);
        statusLabel.setText("");

        layoutBuilder.anchor(GridBagConstraints.CENTER);
        layoutBuilder.weightY(0);
    }

}
