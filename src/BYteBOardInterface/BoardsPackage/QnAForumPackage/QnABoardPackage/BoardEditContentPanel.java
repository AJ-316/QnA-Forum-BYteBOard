package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.CustomListenerPackage.LimitCharacterDocumentListener;
import CustomControls.GridBagBuilder;

import java.awt.*;

public class BoardEditContentPanel extends BoardContentPanel {

    private BoardButton submitButton;

    public BoardEditContentPanel(MainFrame main, Frame frame, BoardResponseCardPanel.CardSelectListener cardSelectListener) {
        super(main, frame, null);

        getContentResponseCardPanel().setCardSelectListener(cardSelectListener);
        setVisible(false);
    }

    public void init(MainFrame main, Frame frame) {
        super.init(main, frame);
        setSelfViewer(true);

        BoardPanel submitPanel = new BoardPanel(main, frame);
        GridBagBuilder submitBuilder = new GridBagBuilder(submitPanel);
        submitBuilder.weight(1, 1).insets(10, 10, 0, 10)
                .fillVertical().anchor(GridBagBuilder.EAST)
                .addToCurrentCell(submitButton);

        contentBodyPanel.add(submitPanel, BorderLayout.SOUTH);
    }

    protected void initComponents(MainFrame main, Frame frame) {
        super.initComponents(main, frame);

        getContentResponseCardPanel().setTitle("Answers", "answer", "No Answers!");

        submitButton = new BoardButton("Submit", "submit");
        submitButton.addInsets(10);
        submitButton.setFGLight();
        submitButton.addActionListener(e-> {
            if(!validateAnswer(getContentBody())) return;

            DBAnswer.addAnswer(getContentBody(), getUserID(), getContentID());
            setEditAnswerPanel(false);
            getContentResponseCardPanel().resetResponseButtons(true);
        });

        contentBytes.setVisible(false);

        LimitCharacterDocumentListener listener = new LimitCharacterDocumentListener(LimitCharacterDocumentListener.MAX_TEXT_LENGTH, null);
        contentBody.addDocumentListener(listener);
        contentBody.setHintText("Type your answer here...");
        contentBody.setEditable(true);
        resetAddAnswer();
    }

    // temp
    private boolean validateAnswer(String answer) {
        return !answer.isEmpty();
    }

    protected void addResponseButtonListeners() {
        getContentResponseCardPanel().setAddResponseAction("Add Answer", e -> setEditAnswerPanel(true));
        getContentResponseCardPanel().setCancelAddResponseAction(e -> setEditAnswerPanel(false));
    }

    public void resetAddAnswer() {
        contentBody.setText("");
    }

    protected BoardResponseCard addNewResponseCard(DBDataObject answerData) {
        BoardResponseCard card = super.addNewResponseCard(answerData);
        card.addMouseListeners(getContentResponseCardPanel());
        return card;
    }

    protected void updateResponseCard(BoardResponseCard card, DBDataObject answerData) {
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
    }

    private void setEditAnswerPanel(boolean isVisible) {
        ((QnABoardFrame) getFrame().getBoardFrame()).switchBoardContent(isVisible ? QnABoardFrame.EDIT_ANSWER : QnABoardFrame.QUESTION);
    }
}
