package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.DBComment;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.BoardLabel;
import CustomControls.CustomListenerPackage.LimitCharacterDocumentListener;
import CustomControls.GridBagBuilder;
import CustomControls.SimpleScrollPane;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;

public class BoardEditResponseCard extends BoardResponseCard {

    private BoardButton submitButton;

    public BoardEditResponseCard(Frame frame) {
        super(frame);
    }

    public void init(Frame frame) {
        super.init(frame);

        GridBagLayout layout = (GridBagLayout) getLayout();
        GridBagBuilder builder = (GridBagBuilder) layout.getConstraints(contentText);

        remove(contentText);
        remove(contentUsername);
        remove(contentAction);

        contentUsername = contentAction = null;

        BoardLabel maxLengthLabel = new BoardLabel("0/255");
        maxLengthLabel.setFGLight();
        maxLengthLabel.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 16);

        LimitCharacterDocumentListener listener = new LimitCharacterDocumentListener(
                255, len -> maxLengthLabel.setText(len + "/255"));
        contentText.addDocumentListener(listener);
        contentText.setHintText("Add Comment...");
        contentText.setFocusable(true);
        contentText.setEditable(true);

        SimpleScrollPane scrollPane = new SimpleScrollPane(contentText);
        scrollPane.getViewport().setPreferredSize(new Dimension(getPreferredSize().width, 100));
        scrollPane.getViewport().setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));

        submitButton = new BoardButton("submit", ResourceManager.MICRO);
        submitButton.setText("Submit");
        submitButton.setFGLight();
        submitButton.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);

        builder.gridWidth(2).weight(1, 1).insets(10).fillBoth()
                .gridCell(0, 0)
                .addToCurrentCell(scrollPane).skipCells(1);

        builder.gridWidth(1).weightY(0)
                .addToNextCell(maxLengthLabel).skipCells(1)
                .addToNextCell(submitButton);
    }

    public void setSubmitAction(String userID, BoardResponseCardPanel cardPanel) {
        submitButton.addActionListener(e -> {
            String comment = contentText.getText().replace("\n", " ");
            if (comment.isEmpty()) return;

            DBComment.addComment(userID, comment, getContentKey(), getContentID());
            cardPanel.clearResponseCards();
            cardPanel.resetResponseButtons(true);
            refresh();
        });
    }

    private String getContentKey() {
        return submitButton.getName();
    }


    public void setCommentContentType(String contentKey, String contentID) {
        submitButton.setName(contentKey);
        contentText.setName(contentID);
    }


}
