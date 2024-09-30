package BYteBOardInterface.BoardsPackage.QnAForumPackage.AskBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.BoardTagsDisplayPanel;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import CustomControls.CustomListenerPackage.LimitCharacterDocumentListener;
import CustomControls.CustomListenerPackage.SearchFieldListener;
import CustomControls.CustomListenerPackage.TagListener;
import CustomControls.CustomRendererPackage.RoundedBorder;
import QnAForumInterface.TagExtractor;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class AskInputPanel extends BoardPanel {

    private BoardTextArea headInput;
    private BoardTextArea bodyInput;
    private BoardComboBox tagInput;
    private BoardTagsDisplayPanel tagsDisplayPanel;
    private TagListener tagListener;
    private boolean isTagsSuggestionVisible;

    public AskInputPanel(MainFrame main, Frame frame) {
        super(main, frame);
    }

    public void init(MainFrame main, Frame frame) {
        GridBagBuilder builder = new GridBagBuilder(this, 1);

        headInput = new BoardTextArea("");
        headInput.setHintText("Type Head...");
        headInput.setEditable(true);
        headInput.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 24);
        headInput.addDocumentListener(new LimitCharacterDocumentListener(255, null));
        BoardPanel headPanel = getInputPanel(main, frame, "Question Title (brief and clear)", headInput);

        bodyInput = new BoardTextArea("");
        bodyInput.setHintText("Type Body...");
        bodyInput.setEditable(true);
        bodyInput.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        bodyInput.addDocumentListener(new LimitCharacterDocumentListener(LimitCharacterDocumentListener.MAX_TEXT_LENGTH, null));
        BoardPanel bodyPanel = getInputPanel(main, frame, "Question Explanation (elaborate on your query)", bodyInput);

        BoardPanel tagPanel = new BoardPanel(main, frame, ByteBoardTheme.MAIN_DARK);
        tagPanel.setCornerRadius(90);
        tagPanel.addInsets(20);
        tagPanel.setLayout(new BorderLayout());
        tagsDisplayPanel = new BoardTagsDisplayPanel(main, frame);
        tagsDisplayPanel.setBackground(ByteBoardTheme.MAIN_DARK);

        tagInput = new BoardComboBox(main, frame, ByteBoardTheme.MAIN_DARK, 20);
        tagInput.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        tagInput.removeMouseListener(tagInput.getMouseListeners()[0]);
        tagInput.removeDropButtonActions();
        tagInput.getDropDownButton().addActionListener(e -> tagInput.setPopupVisible(!tagInput.isPopupVisible()));
        tagInput.setMaximumRowCount(3);
        tagInput.setDropLocation(SwingConstants.TOP);
        tagInput.setEditable(true);
        tagInput.getTextField().addInsets(5, 0, 10, 0);
        tagInput.getTextField().setHintText("Add Tags...");
        tagInput.getTextField().setErrorLabel("");
        tagInput.getTextField().getErrorLabel().addInsets(0, 10, 0, 0);
        tagPanel.add(tagsDisplayPanel, BorderLayout.NORTH);
        tagPanel.add(tagInput);
        tagPanel.add(tagInput.getTextField().getErrorLabel(), BorderLayout.SOUTH);

        // TODO:
        //  1) Get Tags for DB
        //  2) Add tags to Display Panel
        //  3) Submit Button Listener

        builder.weightX(1).fillBoth().insets(5)
                .addToNextCell(headPanel)
                .weightY(1)
                .addToNextCell(bodyPanel)
                .weightY(0)
                .addToNextCell(tagPanel);

        //SearchFieldListener.create(tagInput, t -> EventQueue.invokeLater(() -> setTagSuggestions(tagInput.getTextField().getText())),
        //        () -> EventQueue.invokeLater(() -> addTag(tagInput.getTextField().getText())));
        tagInput.getTextField().addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(() -> setTagSuggestions(tagInput.getTextField().getText()));
            }

            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    tagInput.setPopupVisible(isTagsSuggestionVisible = false);
                else if(e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown())
                    tagInput.setPopupVisible(isTagsSuggestionVisible = true);
            }
        });

        tagInput.getTextField().addActionListener(e ->
                EventQueue.invokeLater(() -> addTag(tagInput.getTextField().getText())));

        tagListener = new TagListener(true);
        enableTagListener();
    }

    private BoardPanel getInputPanel(MainFrame main, Frame frame, String title, Component input) {
        BoardPanel panel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        panel.setLayout(new BorderLayout());
        panel.setCornerRadius(90);
        panel.addInsets(20);

        SimpleScrollPane scrollPane = new SimpleScrollPane(input);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(new RoundedBorder(60, 60, 30,
                ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT),
                ResourceManager.getColor(ByteBoardTheme.MAIN_DARK)));
        scrollPane.getViewport().setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));

        BoardLabel titleLabel = new BoardLabel(title, "edit", ResourceManager.DEFAULT, ResourceManager.MINI);
        titleLabel.setFGLight();
        titleLabel.setAlignmentLeading();
        titleLabel.addInsets(10);

        input.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                titleLabel.setIcon("edit", ResourceManager.ROLLOVER, ResourceManager.MINI);
            }
            public void focusLost(FocusEvent e) {
                titleLabel.setIcon("edit", ResourceManager.DEFAULT, ResourceManager.MINI);
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
        if(tagsDisplayPanel.getTagButtons().length < 3) {
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
        if(tag.isEmpty()) return;

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
        return headInput.getText();
    }

    public String getBody() {
        return bodyInput.getText();
    }

    public BoardTagButton[] getTagButtons() {
        return tagsDisplayPanel.getTagButtons();
    }
}