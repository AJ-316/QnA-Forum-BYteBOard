package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.BoardButton;
import BoardControls.BoardLabel;
import BoardControls.BoardScrollPanel;
import BoardControls.SimpleScrollPane;
import BoardControls.UIPackage.GridBagBuilder;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BoardResponseCardPanel extends BoardPanel {

    private final Map<String, BoardResponseCard> cardsMap = new HashMap<>();
    protected BoardButton cancelAddResponseButton;
    private SimpleScrollPane scrollPane;
    private int lastScroll;
    private GridBagBuilder layoutBuilder;
    private BoardLabel statusLabel;
    private BoardLabel titleLabel;
    private BoardButton addResponseButton;
    private CardSelectListener cardSelectListener;

    public BoardResponseCardPanel(Frame frame) {
        super(frame, ByteBoardTheme.MAIN);
        clearResponseCards();
    }

    public void setTitle(String title, String icon, String statusLabel) {
        titleLabel.setText(title);
        titleLabel.setColoredIcon(icon, ByteBoardTheme.MAIN_DARK, ByteBoardTheme.MAIN, ResourceManager.MINI);

        this.statusLabel.setText(statusLabel);
        this.statusLabel.setName(statusLabel);
    }

    public void setCardSelectListener(CardSelectListener selectListener) {
        cardSelectListener = selectListener;
    }

    public void setAddResponseAction(String text, ActionListener listener) {
        addResponseButton.setText(text);
        addResponseButton.addActionListener(listener);
        addResponseButton.addActionListener(e -> resetResponseButtons(false));
        cancelAddResponseButton.addActionListener(e -> {
            refresh();
            resetResponseButtons(true);
        });
    }

    public void setCancelAddResponseAction(ActionListener listener) {
        cancelAddResponseButton.addActionListener(listener);
    }

    public void init(Frame frame) {
        setShadowState(BoardPanel.OFFSET_SHADOW);
        setLayout(new BorderLayout());
        setCornerRadius(30);
        addInsets(10);

        BoardScrollPanel scrollPanePanel = new BoardScrollPanel(frame);
        scrollPanePanel.setAutoscrolls(false);
        scrollPanePanel.setBackground(getBackground());
        scrollPanePanel.getComponent().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane = scrollPanePanel.getComponent();
        statusLabel = createResponseStatusLabel();
        layoutBuilder = new GridBagBuilder(scrollPanePanel, 1).weightX(1).insets(10).fillHorizontal();

        addResponseButton = new BoardButton("Add Response", "edit", ResourceManager.DEFAULT_LIGHT,
                ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.MICRO);
        addResponseButton.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);
        addResponseButton.addInsets(0, 10, 0, 10);

        cancelAddResponseButton = new BoardButton("Cancel", "cancel", ResourceManager.DEFAULT_LIGHT,
                ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.MICRO);
        cancelAddResponseButton.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);
        cancelAddResponseButton.addInsets(0, 10, 0, 10);
        cancelAddResponseButton.setVisible(false);

        titleLabel = new BoardLabel("Response Title");
        titleLabel.addInsets(8);
        titleLabel.setAlignmentLeading();
        titleLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 22);
        titleLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));

        BoardPanel titleBar = new BoardPanel(frame);
        titleBar.setLayout(new BorderLayout());
        titleBar.add(titleLabel, BorderLayout.WEST);

        BoardPanel responseButtonPanel = new BoardPanel(frame);
        GridBagBuilder buttonBuilder = new GridBagBuilder(responseButtonPanel);
        buttonBuilder.weight(1, 1).fillBoth()
                .addToCurrentCell(addResponseButton)
                .addToCurrentCell(cancelAddResponseButton);

        titleBar.add(responseButtonPanel, BorderLayout.EAST);
        titleBar.addInsets(10, 0, 10, 10);

        add(titleBar, BorderLayout.NORTH);
        add(scrollPanePanel.getComponent(), BorderLayout.CENTER);
    }

    private BoardLabel createResponseStatusLabel() {
        BoardLabel statusLabel = new BoardLabel();
        statusLabel.setText("");
        statusLabel.setName(statusLabel.getText());
        statusLabel.addInsets(10);
        statusLabel.setFGLight();
        statusLabel.setFontPrimary(ByteBoardTheme.FONT_T_THIN, 20);

        return statusLabel;
    }

    public void selectCard(BoardResponseCard cardToSelect) {
        if (cardSelectListener == null) return;

        if (cardToSelect == null) {
            for (BoardResponseCard card : cardsMap.values()) {
                if (card.isSelected()) {
                    card.setSelected(false);
                    cardSelectListener.invoke(null);
                    return;
                }
            }
            return;
        }

        if (cardToSelect.isSelected()) {
            cardToSelect.setSelected(false);
            cardSelectListener.invoke(null);
            return;
        }

        for (BoardResponseCard card : cardsMap.values()) {
            card.setSelected(false);
        }

        cardToSelect.setSelected(true);
        cardSelectListener.invoke(cardToSelect.getContentID());
    }

    public Map<String, BoardResponseCard> getCards() {
        return cardsMap;
    }

    public void clearResponseCards() {
        cardsMap.clear();
        layoutBuilder.getContainer().removeAll();
        layoutBuilder.gridCell(0, 0);
        addStatusLabel(statusLabel.getName());
    }

    public void addResponseCard(BoardResponseCard card) {
        removeStatusLabel();

        cardsMap.put(card.getContentID(), card);
        layoutBuilder.addToNextCell(card);
        addStatusLabel("");
    }

    public void addEditResponseCard(BoardEditResponseCard card) {
        removeStatusLabel();

        cardsMap.put("addEditCard", card);
        layoutBuilder.addToNextCell(card);
        addStatusLabel("");
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

    public void storeScroll() {
        lastScroll = scrollPane.getVerticalScrollBar().getValue();
    }

    public void restoreScrolls() {
        scrollPane.setVerticalScroll(lastScroll);
    }

    public void resetResponseButtons(boolean isReset) {
        addResponseButton.setVisible(isReset);
        cancelAddResponseButton.setVisible(!isReset);
    }

    public SimpleScrollPane getScrollPane() {
        return scrollPane;
    }

    public interface CardSelectListener {
        void invoke(String contentID);
    }

}
