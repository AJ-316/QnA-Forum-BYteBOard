package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.DBAnswer;
import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBUser;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import CustomControls.BoardButton;
import CustomControls.CustomListenerPackage.LimitCharacterDocumentListener;
import CustomControls.GridBagBuilder;

import java.awt.*;
import java.awt.event.ActionListener;

public class BoardEditContentPanel extends BoardContentPanel {

    private BoardButton previewStyleButton;
    private BoardButton previewEditButton;
    private BoardButton submitPreviewButton;
    private BoardButton submitEditButton;

    public BoardEditContentPanel(Frame frame, BoardResponseCardPanel.CardSelectListener cardSelectListener) {
        super(frame, null);

        getContentResponseCardPanel().setCardSelectListener(cardSelectListener);
        getContentResponseCardPanel().setTitle("Answers", "answer", "No Answers!");
        setVisible(false);
    }

    public void init(Frame frame) {
        super.init(frame);
        setSelfViewer(true);

        BoardPanel previewBtnPanel = new BoardPanel(frame);

        GridBagBuilder btnBuilder = new GridBagBuilder(previewBtnPanel, 2);
        btnBuilder.weight(1, 1)
                .insets(10, 10, 0, 10)
                .fillVertical().anchor(GridBagBuilder.EAST)
                .addToNextCell(previewStyleButton)
                .weightX(0)
                .addToNextCell(submitEditButton);

        contentBodyPanel.add(previewBtnPanel, BorderLayout.SOUTH);

        BoardPanel editBtnPanel = new BoardPanel(frame);
        editBtnPanel.setLayout(new GridBagLayout());
        editBtnPanel.add(previewEditButton, btnBuilder.getConstraints(previewStyleButton));
        editBtnPanel.add(submitPreviewButton, btnBuilder.getConstraints(submitEditButton));
        contentStyledBodyPanel.add(editBtnPanel, BorderLayout.SOUTH);

        setEditableInput(true);
    }

    protected void initComponents(Frame frame) {
        super.initComponents(frame);

        ActionListener submitListener = e -> {
            String text = getContentBody();

            if (!validateAnswer(text)) return;
            System.out.println(text);

            DBAnswer.addAnswer(text, getUserID(), getContentID());
            setEditAnswerPanel(false);
            getContentResponseCardPanel().resetResponseButtons(true);
            refresh();
        };

        submitEditButton = getButton("Submit", "submit", submitListener);
        submitPreviewButton = getButton("Submit", "submit", submitListener);
        previewEditButton = getButton("Edit", "edit", e -> setEditableInput(true));
        previewStyleButton = getButton("Preview", "show", e -> {
            setEditableInput(false);
            setContentStyledBody(contentBody.getText());
        });

        contentBytes.setVisible(false);

        LimitCharacterDocumentListener listener = new LimitCharacterDocumentListener(LimitCharacterDocumentListener.MAX_TEXT_LENGTH, null);
        contentBody.addDocumentListener(listener);
        contentBody.setHintText("Type your answer here...");
        resetAddAnswer();
    }

    private BoardButton getButton(String text, String icon, ActionListener listener) {
        BoardButton btn = new BoardButton(text, icon);
        btn.addInsets(10);
        btn.setFGLight();
        btn.addActionListener(listener);
        return btn;
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
