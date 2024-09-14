package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import javax.swing.*;
import java.awt.*;

public class BoardContentResponsePanel extends BoardPanel {

    private SimpleScrollPane scrollPane;
    private int lastScroll;
    private GridBagBuilder layoutBuilder;
    private BoardLabel statusLabel;
    private BoardLabel titleLabel;

    public BoardContentResponsePanel(MainFrame main, Frame frame, String bgColor) {
        super(main, frame, bgColor);
    }

    public void setTitle(String title, String statusLabel) {
        titleLabel.setText(title);
        this.statusLabel.setText(statusLabel);
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

        BoardScrollPanel scrollPanePanel = new BoardScrollPanel(main, frame);
        scrollPanePanel.setAutoscrolls(false);
        scrollPanePanel.setBackground(getBackground());
        scrollPanePanel.getComponent().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollPane = scrollPanePanel.getComponent();

        layoutBuilder = new GridBagBuilder(scrollPanePanel, 1).weightX(1).insets(10).fillHorizontal();
        statusLabel = createResponseStatusLabel();

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPanePanel.getComponent(), BorderLayout.CENTER);

        clearContentCards();
    }

    private BoardLabel createResponseStatusLabel() {
        BoardLabel statusLabel = new BoardLabel();
        statusLabel.setText("<html><div style='text-align: center;'>No Comments<br>Yet</div></html>");
        statusLabel.setName(statusLabel.getText());
        statusLabel.addInsets(10);
        statusLabel.setFGLight();
        statusLabel.setFontPrimary(ByteBoardTheme.FONT_T_THIN, 20);

        return statusLabel;
    }

    public void clearContentCards() {
        lastScroll = scrollPane.getVerticalScrollBar().getValue();
        layoutBuilder.getContainer().removeAll();
        layoutBuilder.gridCell(0, 0);
        addStatusLabel(statusLabel.getName());
    }

    public BoardContentCard addContentCard() {
        removeStatusLabel();

        BoardContentCard card = new BoardContentCard(getMain(), getFrame());
        layoutBuilder.addToNextCell(card);
        addStatusLabel("");

        return card;
    }

    private void addStatusLabel(String text) {
        layoutBuilder.anchor(GridBagConstraints.CENTER);
        layoutBuilder.weightY(1);
        layoutBuilder.addToNextCell(statusLabel);
        statusLabel.setText(text);
    }

    private void removeStatusLabel() {
        layoutBuilder.skipCells(-1);
        layoutBuilder.getContainer().remove(statusLabel);
        statusLabel.setText("");

        layoutBuilder.anchor(GridBagConstraints.CENTER);
        layoutBuilder.weightY(0);
    }

    public void resetScrolls() {
        scrollPane.resetScroll();
    }

    public void restoreScrolls() {
        scrollPane.setVerticalScroll(lastScroll);
    }

}
