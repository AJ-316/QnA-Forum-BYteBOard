package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import javax.swing.*;
import java.awt.*;

public class BoardContentResponsePanel extends BoardPanel {

    private GridBagBuilder layoutBuilder;
    private BoardLabel statusLabel;
    private BoardLabel titleLabel;

    public BoardContentResponsePanel(MainFrame main, Frame frame, String bgColor) {
        super(main, frame, bgColor);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void init(MainFrame main, Frame frame) {
        setShadowState(BoardPanel.OFFSET_SHADOW);
        setLayout(new BorderLayout());
        setCornerRadius(30);
        addInsets(10);

        titleLabel = new BoardLabel("Response Title");
        titleLabel.addInsets(8);
        titleLabel.setAlignmentLeading();
        titleLabel.setFGLight();
        titleLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 22);

        BoardScrollPanel scrollPanel = new BoardScrollPanel(main, frame);
        scrollPanel.setAutoscrolls(false);
        scrollPanel.setBackground(getBackground());
        scrollPanel.getComponent().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        layoutBuilder = createResponseLayoutBuilder(scrollPanel);
        statusLabel = createResponseStatusLabel("<html><div style='text-align: center;'>No Comments<br>Yet</div></html>");

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPanel.getComponent(), BorderLayout.CENTER);

        clearContentCards();
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

    public void clearContentCards() {
        layoutBuilder.getContainer().removeAll();
        layoutBuilder.gridCell(0, 0);
        addStatusLabel(statusLabel.getName());
    }

    public BoardContentCard addContentCard() {
        removeStatusLabel();

        BoardContentCard card = new BoardContentCard(getMain(), getFrame());
        layoutBuilder.add(card);

        addStatusLabel("");

        return card;
    }

    private void addStatusLabel(String text) {
        layoutBuilder.anchor(GridBagConstraints.CENTER);
        layoutBuilder.weightY(1);
        layoutBuilder.add(statusLabel);
        statusLabel.setText(text);
    }

    private void removeStatusLabel() {
        layoutBuilder.skipCells(-1);
        layoutBuilder.getContainer().remove(statusLabel);
        statusLabel.setText("");

        layoutBuilder.anchor(GridBagConstraints.CENTER);
        layoutBuilder.weightY(0);
    }

}
