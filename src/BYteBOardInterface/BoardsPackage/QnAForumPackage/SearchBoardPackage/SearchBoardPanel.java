package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQuestion;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.QnABoardFrame;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardComboBox;
import CustomControls.CustomListenerPackage.SearchFieldListener;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchBoardPanel extends BoardPanel {

    protected static final int NULL_INPUT = 0;
    protected static final int TAG_INPUT = 1;
    protected static final int QUE_INPUT = 2;
    private final List<DBDataObject> searchDataObjects;
    private final SearchQuestionPanel questionPanel;
    private String userID;
    private BoardComboBox searchField;
    private int inputState;

    public SearchBoardPanel(MainFrame main, Frame frame, SearchQuestionPanel questionPanel) {
        super(main, frame);
        this.searchDataObjects = new ArrayList<>();
        this.questionPanel = questionPanel;
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

        searchFieldBuilder.weight(1, 1).fillBoth()
                .addToNextCell(searchField);

        builder.weightX(1).fillHorizontal().insets(10)
                .addToNextCell(searchFieldPanel);
    }

    private void addListeners() {
        SearchFieldListener.create(searchField,
                this::updateSearchField,
                this::searchSelected);
    }

    /*private boolean searchQuestion() {
        String question = searchField.getTextField().getText();

        String mostRelevantQueHead = "\n";
        String mostRelevantQueID = "\n";

        if(!searchDataObjects.isEmpty()) {
            mostRelevantQueHead = searchDataObjects.get(0).getValue(DBQuestion.K_QUESTION_HEAD);
            mostRelevantQueID = searchDataObjects.get(0).getValue(DBQuestion.K_QUESTION_ID);
        }

        // if the question selected is not same as most relevant question
        if(question.equalsIgnoreCase(mostRelevantQueHead)) {
            requestSwitchFrame(QnABoardFrame.class, mostRelevantQueID, getUserID());

            clearSearchObjects();
            searchField.getEditor().setItem("");
            return true;
        }

        questionPanel.searchQuestions(searchDataObjects, QUE_INPUT);

        return false;
    }*/

    private void searchSelected() {
        searchField.setPopupVisible(false);
        searchByField();
        setInputState(NULL_INPUT);
    }

    private void searchByTag(String tag, String tagID) {
        if (!questionPanel.addTag(tag, tagID)) return;
        clearSearchObjects();
        questionPanel.searchQuestions(searchDataObjects, TAG_INPUT);
        searchField.getEditor().setItem("#");
    }

    private void searchByField() {
        if (inputState == NULL_INPUT) return;

        String search = searchField.getTextField().getText();

        String mostRelevantSearch = "\n";
        String mostRelevantSearchID = "\n";

        if (!searchDataObjects.isEmpty()) {
            mostRelevantSearch = searchDataObjects.get(0).getValue(
                    inputState == QUE_INPUT ? DBQuestion.K_QUESTION_HEAD : DBTag.K_TAG);
            mostRelevantSearchID = searchDataObjects.get(0).getValue(
                    inputState == QUE_INPUT ? DBQuestion.K_QUESTION_ID : DBTag.K_TAG_ID);
        }

        if (inputState == TAG_INPUT) {
            if (!search.substring(1).equalsIgnoreCase(mostRelevantSearch))
                return;
            searchByTag(mostRelevantSearch, mostRelevantSearchID);
            return;
        }

        // if type == QUE_INPUT
        if (search.equalsIgnoreCase(mostRelevantSearch)) {
            requestSwitchFrame(QnABoardFrame.class, mostRelevantSearchID, getUserID());

            clearSearchObjects();
            searchField.getEditor().setItem("");
            return;
        }

        questionPanel.searchQuestions(searchDataObjects, QUE_INPUT);
    }
/*

    private boolean issValidSearchText(String searchText, String textKey, String idKey) {
        String mostRelevantTag = "\n";
        String mostRelevantTagID = "\n";

        if(!searchDataObjects.isEmpty()) {
            mostRelevantTag = searchDataObjects.get(0).getValue(textKey);
            mostRelevantTagID = searchDataObjects.get(0).getValue(idKey);
        }

        // if the question selected is not same as most relevant question
        if(searchText.equalsIgnoreCase(mostRelevantTag)) {
            questionPanel.addTag(mostRelevantTag, mostRelevantTagID);
            questionPanel.loadQuestionsByTags();
            searchField.getEditor().setItem("#");
            return true;
        }

        //return false;
        // TODO //////////////////////////////////////////

        if(searchDataObjects.isEmpty()) return false;

        String nextValidTag = searchField.getItemAt(0);
        String validTagID = itemIDList.get(0);
        System.out.println("Valid Tag: " + validTagID + " -> " + nextValidTag);

        searchField.removeAllItems();
        itemIDList.clear();

        questionPanel.addTag(nextValidTag.substring(1), validTagID);

        questionPanel.loadQuestionsByTags();
        searchField.getEditor().setItem("#");
    }*/

    private void clearSearchObjects() {
        searchField.removeAllItems();
        searchDataObjects.clear();
    }

    private void setSearchFieldItems(String searchText, int type) {
        int selectionStart = searchField.getTextField().getSelectionStart();
        int selectionEnd = searchField.getTextField().getSelectionEnd();

        clearSearchObjects();

        if (type == QUE_INPUT)
            type = loadQuestions(searchText);
        else if (type == TAG_INPUT)
            type = loadTags(searchText);

        searchField.getEditor().setItem(searchText);
        searchField.getTextField().setSelectionStart(selectionStart);
        searchField.getTextField().setSelectionEnd(selectionEnd);

        setInputState(type);
    }

    private int loadQuestions(String searchText) {
        DBDataObject[] suggestedQuestions = DBQuestion.searchByQuestion(searchText.trim(), "*");

        for (DBDataObject question : suggestedQuestions) {
            searchField.addItem(question.getValue(DBQuestion.K_QUESTION_HEAD));
            searchDataObjects.add(question);
        }
        return QUE_INPUT;
    }

    private int loadTags(String searchText) {
        String tag = searchText.substring(1);
        if (searchText.length() > 1) {
            for (DBDataObject tagDataObject : DBTag.getRelevantTags(tag, false)) {
                searchField.addItem("#" + tagDataObject.getValue(DBTag.K_TAG));
                searchDataObjects.add(tagDataObject);
            }
            return TAG_INPUT;
        }

        for (DBDataObject tagDataObject : DBTag.getRelevantTags(tag, true)) {
            searchField.addItem("#" + tagDataObject.getValue(DBTag.K_TAG));
            searchDataObjects.add(tagDataObject);
        }
        return NULL_INPUT;
    }

    private void updateSearchField(String text) {
        if (text.isEmpty()) {
            setInputState(NULL_INPUT);
            return;
        }

        if (!text.startsWith("#")) {
            setSearchFieldItems(text, QUE_INPUT);
            /*//System.out.println("question: " + text);
            int selectionStart = searchField.getTextField().getSelectionStart();
            int selectionEnd = searchField.getTextField().getSelectionEnd();

            searchField.removeAllItems();
            searchDataObjects.clear();

            DBDataObject[] suggestedQuestions = DBQuestion.searchByQuestion(text.trim(), DBQuestion.K_QUESTION_HEAD, DBQuestion.K_QUESTION_ID);

            for (DBDataObject question : suggestedQuestions) {
                searchField.addItem(question.getValue(DBQuestion.K_QUESTION_HEAD));
                searchDataObjects.add(question);
            }

            searchField.getEditor().setItem(text);
            searchField.getTextField().setSelectionStart(selectionStart);
            searchField.getTextField().setSelectionEnd(selectionEnd);
            setInputState(QUE_INPUT);*/
            return;
        }

        EventQueue.invokeLater(() -> {
            /*String tag = text.substring(1);
            //System.out.println("tag: " + tag);
            int selectionStart = searchField.getTextField().getSelectionStart();
            int selectionEnd = searchField.getTextField().getSelectionEnd();

            searchField.removeAllItems();
            searchDataObjects.clear();

            if (text.length() > 1) {
                for (DBDataObject tagDataObject : DBTag.getRelevantTags(tag, false)) {
                    searchField.addItem("#" + tagDataObject.getValue(DBTag.K_TAG));
                    searchDataObjects.add(tagDataObject);
                }

                searchField.getEditor().setItem(text);
                searchField.getTextField().setSelectionStart(selectionStart);
                searchField.getTextField().setSelectionEnd(selectionEnd);
                setInputState(TAG_INPUT);
                return;
            }

            for (DBDataObject tagDataObject : DBTag.getRelevantTags(tag, true)) {
                searchField.addItem("#" + tagDataObject.getValue(DBTag.K_TAG));
                searchDataObjects.add(tagDataObject);
            }

            searchField.getEditor().setItem(text);
            searchField.getTextField().setSelectionStart(selectionStart);
            searchField.getTextField().setSelectionEnd(selectionEnd);
            setInputState(NULL_INPUT);*/
            setSearchFieldItems(text, TAG_INPUT);
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
