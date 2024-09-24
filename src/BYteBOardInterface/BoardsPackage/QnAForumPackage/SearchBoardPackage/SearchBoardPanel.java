package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQuestion;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.QnABoardFrame;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import CustomControls.CustomListenerPackage.SearchFieldListener;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchBoardPanel extends BoardPanel {

    private static final int NULL_INPUT = 0;
    private static final int TAG_INPUT = 1;
    private static final int QUE_INPUT = 2;

    private String userID;
    private static String lastSearchInput;
    private BoardComboBox searchField;
    private final List<String> itemIDList;

    private final SearchQuestionPanel questionPanel;

    private int inputState;

    public SearchBoardPanel(MainFrame main, Frame frame, SearchQuestionPanel questionPanel) {
        super(main, frame);
        this.questionPanel = questionPanel;
        itemIDList = new ArrayList<>();
        addListeners();
    }

    public void init(MainFrame main, Frame frame) {
        GridBagBuilder builder = new GridBagBuilder(this, 1);

        BoardPanel searchFieldPanel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        searchFieldPanel.setCornerRadius(90);
        searchFieldPanel.addInsets(20);
        GridBagBuilder searchFieldBuilder = new GridBagBuilder(searchFieldPanel, 1);

        searchField = new BoardComboBox(main, frame, ByteBoardTheme.MAIN, 10);
        searchField.removeMouseListener(searchField.getMouseListeners()[0]);
        searchField.removeDropButtonActions();
        searchField.setMaximumRowCount(3);
        searchField.setIcon("search", ResourceManager.SMALL);
        searchField.setEditable(true);
        searchField.getTextField().addInsets(10);
        searchField.getTextField().setHintText("Search...");
        searchField.getDropDownButton().addActionListener(e -> searchSelected());
        searchField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.emptySet());
        searchField.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.emptySet());

        // TODO remove the searchTagsDisplay from searchFieldPanel and add it to searched questions panel
        /*searchTagsPanel = new BoardTagsDisplayPanel(main, frame);
        searchTagsPanel.addInsets(0);
        searchTagsPanel.setScrollSize(0, 60);
        searchTagsPanel.setHorizontalDisplay();*/

        searchFieldBuilder.weight(1, 1).fillBoth()
                .addToNextCell(searchField)
                ;//.addToNextCell(searchTagsPanel.getComponent());

        builder.weightX(1).fillHorizontal().insets(10)
                .addToNextCell(searchFieldPanel);

        //searchTagsPanel.addTag("tag", this);
        //((BoardTagButton) searchTagsPanel.getComponent(0)).getActionListeners()[0].actionPerformed(null);
    }

    private void addListeners() {
        SearchFieldListener.create(searchField,
                this::loadSearchSuggestions,
                this::searchSelected);
    }

    private void searchSelected() {
        searchField.setPopupVisible(false);

        if(inputState == QUE_INPUT)
            searchQuestion();
        else if(inputState == TAG_INPUT)
            searchTag();

        setInputState(NULL_INPUT);
    }

    /*
        Search() {
            ClearIDs()

            fetch searchText        = search.text
            fetch validSearchText   = search.item(0)
            fetch validSearchID     = ids[0]

            If searchText is validSearchText
            Then
                Switch Frame QnABoard (validSearchID)
            Else
                q.AddIDs(ids)

            search.clear
        }
    */

    private void searchQuestion() {
        String question = searchField.getTextField().getText();
        String nextValidQuestion = searchField.getItemAt(0);

        String validQuestionID = null;
        if(nextValidQuestion != null)
            validQuestionID = itemIDList.get(0);

        if(!question.equalsIgnoreCase(nextValidQuestion)) {
            questionPanel.loadQuestionsBySimilarity(itemIDList.toArray(new String[0]));
            return;
        }

        searchField.removeAllItems();
        itemIDList.clear();

        requestSwitchFrame(QnABoardFrame.class, validQuestionID, getUserID());
        searchField.getEditor().setItem("");
    }

    /*
        Search() {
            q.ClearIDs()

            fetch searchText        = search.text | search.item(0)
            fetch validSearchText   = search.item(0)
            fetch validSearchID     = ids[0]

            If searchText is validSearchText
            Then
                Switch Frame QnABoard (validSearchID)
            Else
                q.AddIDs(ids)

            search.clear
        }
    */

    private void searchTag() {
        if(itemIDList.isEmpty()) return;

        String nextValidTag = searchField.getItemAt(0);
        String validTagID = itemIDList.get(0);
        System.out.println("Valid Tag: " + validTagID + " -> " + nextValidTag);

        searchField.removeAllItems();
        itemIDList.clear();

        questionPanel.addTag(nextValidTag.substring(1), validTagID);

        questionPanel.loadQuestionsByTags();
        searchField.getEditor().setItem("#");
    }

    private void loadSearchSuggestions(String text) {
        text = text.trim();

        if(text.isEmpty()) {
            setInputState(NULL_INPUT);
            return;
        }

        if(text.equals(lastSearchInput)) return;

        lastSearchInput = text;

        if(!text.startsWith("#")) {
            //System.out.println("question: " + text);
            searchField.removeAllItems();
            itemIDList.clear();

            DBDataObject[] suggestedQuestions = DBQuestion.searchByQuestion(text, DBQuestion.K_QUESTION_HEAD, DBQuestion.K_QUESTION_ID);

            for (DBDataObject question : suggestedQuestions) {
                searchField.addItem(question.getValue(DBQuestion.K_QUESTION_HEAD));
                itemIDList.add(question.getValue(DBQuestion.K_QUESTION_ID));
            }

            searchField.getEditor().setItem(text);
            setInputState(QUE_INPUT);
            return;
        }

        String finalText = text;
        EventQueue.invokeLater(() -> {
            String tag = finalText.substring(1);
            //System.out.println("tag: " + tag);
            searchField.removeAllItems();
            itemIDList.clear();

            if (finalText.length() > 1) {
                for (DBDataObject tagDataObject : DBTag.getRelevantTags(tag, false)) {
                    searchField.addItem("#" + tagDataObject.getValue(DBTag.K_TAG));
                    itemIDList.add(tagDataObject.getValue(DBTag.K_TAG_ID));
                }

                searchField.getEditor().setItem(finalText);
                setInputState(TAG_INPUT);
                return;
            }

            for (DBDataObject tagDataObject : DBTag.getRelevantTags(tag, true)) {
                searchField.addItem("#" + tagDataObject.getValue(DBTag.K_TAG));
                itemIDList.add(tagDataObject.getValue(DBTag.K_TAG_ID));
            }

            searchField.getEditor().setItem(finalText);

            setInputState(NULL_INPUT);
            searchField.setPopupVisible(true);
        });
    }

    private void setInputState(int state) {
        this.inputState = state;

        searchField.setPopupVisible(false);
        searchField.setPopupVisible(state != NULL_INPUT && searchField.getItemCount() != 0);
        revalidate();
        repaint();
    }

    /*private void addTag(String tag, String tagID) {
        BoardTagButton tagButton = searchTagsPanel.addTag(tag, this);
        tagButton.setTagID(tagID);
        tagButton.addActionListener(e -> EventQueue.invokeLater(this::loadQuestionsByTags));
    }*/

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
