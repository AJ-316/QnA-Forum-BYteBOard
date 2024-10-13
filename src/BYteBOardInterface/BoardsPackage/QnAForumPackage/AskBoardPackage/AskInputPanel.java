package BYteBOardInterface.BoardsPackage.QnAForumPackage.AskBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.BoardTagsDisplayPanel;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.*;
import BoardControls.UIPackage.GridBagBuilder;
import BoardControls.UIPackage.RoundedBorder;
import BoardEventListeners.LimitCharacterDocumentListener;
import BoardEventListeners.TagListener;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AskInputPanel extends BoardPanel {

    private BoardTextArea headInput;
    private BoardTextArea bodyInput;
    private BoardTextPane bodyPreview;
    private BoardComboBox tagInput;

    private BoardPanel bodyPanel;
    private BoardPanel bodyPreviewPanel;

    private BoardTagsDisplayPanel tagsDisplayPanel;
    private TagListener tagListener;
    private boolean isTagsSuggestionVisible;

    public AskInputPanel(Frame frame) {
        super(frame);
    }

    public void init(Frame frame) {
        GridBagBuilder builder = new GridBagBuilder(this, 1);

        headInput = new BoardTextArea("");
        headInput.setHintText("Type Head...");
        headInput.setEditable(true);
        headInput.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 24);
        headInput.addDocumentListener(new LimitCharacterDocumentListener(255, null));

        bodyInput = new BoardTextArea("");
        bodyInput.setHintText("Type Body...");
        bodyInput.setEditable(true);
        bodyInput.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        bodyInput.addDocumentListener(new LimitCharacterDocumentListener(LimitCharacterDocumentListener.MAX_TEXT_LENGTH, null));

        bodyPreview = new BoardTextPane(frame, ByteBoardTheme.MAIN_LIGHT);

        BoardPanel headPanel = getInputPanel(frame, "edit:Question Title (brief and clear)", headInput, ByteBoardTheme.MAIN_DARK, ByteBoardTheme.ACCENT);
        headPanel.setMinimumSize(new Dimension(0, 200));

        bodyPanel = getInputPanel(frame, "edit:Question Explanation (elaborate on your query)", bodyInput, ByteBoardTheme.MAIN_DARK, ByteBoardTheme.ACCENT);

        bodyPreviewPanel = getInputPanel(frame, "show:Preview", bodyPreview, ByteBoardTheme.MAIN_LIGHT, ByteBoardTheme.MAIN_DARK);
        bodyPreviewPanel.setVisible(false);

        BoardPanel tagPanel = getTagPanel(frame);

        builder.weightX(1).fillBoth().insets(5)
                .addToNextCell(headPanel)
                .weightY(1)
                .addToNextCell(bodyPanel)
                .addToCurrentCell(bodyPreviewPanel)
                .weightY(0)
                .addToNextCell(tagPanel);
    }

    public boolean togglePreview() {
        bodyPreviewPanel.setVisible(!bodyPreviewPanel.isVisible());
        boolean isPreviewVisible = bodyPreviewPanel.isVisible();

        bodyPanel.setVisible(!isPreviewVisible);

        if (isPreviewVisible) {
            bodyPreview.setTextWithStyles(bodyInput.getText());
        }

        return isPreviewVisible;
    }

    private BoardComboBox getTagInput(Frame frame) {
        BoardComboBox comboBox = new BoardComboBox(frame, ByteBoardTheme.MAIN_DARK, 20);
        comboBox.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);

        comboBox.setMaximumRowCount(3);
        comboBox.setDropLocation(SwingConstants.TOP);

        comboBox.setEditable(true);
        comboBox.getTextField().addInsets(5, 0, 10, 0);
        comboBox.getTextField().setHintText("Add Tags...");
        comboBox.getTextField().setErrorLabel("");
        comboBox.getTextField().getErrorLabel().addInsets(0, 10, 0, 0);

        comboBox.removeMouseListener(comboBox.getMouseListeners()[0]);
        comboBox.removeDropButtonActions();
        comboBox.getDropDownButton().addActionListener(e -> comboBox.setPopupVisible(!comboBox.isPopupVisible()));

        comboBox.getTextField().addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(() -> setTagSuggestions(comboBox.getTextField().getText()));
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    comboBox.setPopupVisible(isTagsSuggestionVisible = false);
                else if (e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown())
                    comboBox.setPopupVisible(isTagsSuggestionVisible = true);
            }
        });

        comboBox.getTextField().addActionListener(e ->
                EventQueue.invokeLater(() -> addTag(comboBox.getTextField().getText())));

        tagListener = new TagListener(true);
        return comboBox;
    }

    private BoardPanel getTagPanel(Frame frame) {
        BoardPanel panel = new BoardPanel(frame, ByteBoardTheme.MAIN_DARK);
        panel.setCornerRadius(90);
        panel.addInsets(20);
        panel.setLayout(new BorderLayout());

        tagsDisplayPanel = new BoardTagsDisplayPanel(frame);
        tagsDisplayPanel.setBackground(ByteBoardTheme.MAIN_DARK);

        tagInput = getTagInput(frame);
        enableTagListener();

        panel.add(tagsDisplayPanel, BorderLayout.NORTH);
        panel.add(tagInput);
        panel.add(tagInput.getTextField().getErrorLabel(), BorderLayout.SOUTH);

        return panel;
    }

    private BoardPanel getInputPanel(Frame frame, String title, Component input, String bgColor, String borderColor) {
        BoardPanel panel = new BoardPanel(frame, ByteBoardTheme.MAIN);
        panel.setLayout(new BorderLayout());
        panel.setCornerRadius(90);
        panel.addInsets(20);

        ((BoardControl) input).setBackground(bgColor);

        SimpleScrollPane scrollPane = new SimpleScrollPane(input);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(new RoundedBorder(60, 60, 30,
                ResourceManager.getColor(bgColor),
                ResourceManager.getColor(borderColor)));
        scrollPane.getViewport().setBackground(ResourceManager.getColor(bgColor));

        String[] titleLabelIcon = title.split(":");
        BoardLabel titleLabel = new BoardLabel(titleLabelIcon[1], titleLabelIcon[0], ResourceManager.DEFAULT, ResourceManager.MINI);
        titleLabel.setFGLight();
        titleLabel.setAlignmentLeading();
        titleLabel.addInsets(10);

        input.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                titleLabel.setIcon(titleLabelIcon[0], ResourceManager.ROLLOVER, ResourceManager.MINI);
            }

            public void focusLost(FocusEvent e) {
                titleLabel.setIcon(titleLabelIcon[0], ResourceManager.DEFAULT, ResourceManager.MINI);
            }
        });

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane);
        return panel;
    }

    private void addTag(String tag) {
        clearTagInputError();

        tagInput.removeAllItems();
        tagInput.setPopupVisible(false);

        DBDataObject tagDataObject = new DBDataObject();
        tagDataObject.putKeyValue(DBTag.K_TAG, tag);
        tagsDisplayPanel.addTag(tagDataObject, this);
    }

    public boolean validateTags() {
        if (tagsDisplayPanel.getTagButtons().length < 3) {
            tagInput.getTextField().setErrorLabel("Enter at least 3 tags");
            return false;
        }
        clearTagInputError();
        return true;
    }

    public boolean validateQuestion() {
        return !headInput.getText().isEmpty() && !bodyInput.getText().isEmpty();
    }

    protected void clearTagInputError() {
        tagInput.getTextField().setErrorLabel("");
    }

    public void clearQuestion() {
        headInput.setText("");
        bodyInput.setText("");
        tagInput.removeAllItems();
        tagsDisplayPanel.removeAll();
        clearTagInputError();
    }

    private void setTagSuggestions(String tag) {
        if (tag.isEmpty()) return;

        disableTagListener();
        DBDataObject[] suggestedTags = DBTag.getRelevantTags(tag, true);

        int selectionStart = tagInput.getTextField().getSelectionStart();
        int selectionEnd = tagInput.getTextField().getSelectionEnd();

        tagInput.removeAllItems();

        for (DBDataObject dbDataObject : suggestedTags) {
            tagInput.addItem(dbDataObject.getValue(DBTag.K_TAG));
        }

        tagInput.getEditor().setItem(tag);
        tagInput.getTextField().setSelectionStart(selectionStart);
        tagInput.getTextField().setSelectionEnd(selectionEnd);
        tagInput.setPopupVisible(isTagsSuggestionVisible);
        enableTagListener();
    }

    private void enableTagListener() {
        tagInput.getTextField().addDocumentListener(tagListener);
    }

    private void disableTagListener() {
        tagInput.getTextField().getDocument().removeDocumentListener(tagListener);
    }

    public String getHead() {
        return headInput.getText().replaceAll("\n", " ");
    }

    public String getBody() {
        return bodyInput.getText();
    }

    public BoardTagButton[] getTagButtons() {
        return tagsDisplayPanel.getTagButtons();
    }
}