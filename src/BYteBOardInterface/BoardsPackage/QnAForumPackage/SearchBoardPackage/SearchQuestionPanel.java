package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.BoardContentDisplayPanel;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.BoardContentDisplayPane;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.BoardTagsDisplayPanel;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardTagButton;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchQuestionPanel extends BoardPanel {

    private BoardLabel titleLabel;
    private BoardTagsDisplayPanel searchTagsPanel;
    private BoardPanel searchTagsScrollPanel;
    private BoardContentDisplayPanel questionsPanel;

    public SearchQuestionPanel(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN);
        setVisible(false);
    }

    public void init(MainFrame main, Frame frame) {
        setCornerRadius(90);
        addInsets(20);

        titleLabel = new BoardLabel("Related Questions");
        titleLabel.setAlignmentLeading();
        titleLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 22);
        titleLabel.setFGLight();
        titleLabel.addInsets(10);

        searchTagsScrollPanel = new BoardPanel(main, frame);
        searchTagsScrollPanel.setLayout(new BorderLayout());
        searchTagsPanel = new BoardTagsDisplayPanel(main, frame);
        searchTagsPanel.addInsets(0);
        searchTagsPanel.setHorizontalDisplay();
        searchTagsScrollPanel.setMinimumSize(new Dimension(0, 60));
        searchTagsScrollPanel.add(searchTagsPanel.getComponent());

        BoardPanel questionPanelHolder = new BoardPanel(main, frame, ByteBoardTheme.MAIN_DARK);
        questionPanelHolder.setLayout(new BorderLayout());
        questionPanelHolder.setCornerRadius(90);
        questionPanelHolder.addInsets(20);
        questionsPanel = new BoardContentDisplayPanel(main, frame);
        questionsPanel.setVisible(true);

        questionPanelHolder.add(questionsPanel);

        GridBagBuilder builder = new GridBagBuilder(this, 1);

        builder.weightX(1).fillBoth().insets(0, 20, 0, 20)
                .addToNextCell(searchTagsScrollPanel)
                .addToNextCell(titleLabel)
                .weight(0, 1).insets(0)
                .addToNextCell(questionsPanel);

        // requires one tag to be added first for the panel to be visible
        searchTagsPanel.remove(searchTagsPanel.addTag(new DBDataObject(), this));
    }

    public void searchQuestions(List<DBDataObject> searchObjects, int type) {
        if (searchObjects == null)
            searchObjects = new ArrayList<>();

        if (type == SearchBoardPanel.QUE_INPUT && !searchObjects.isEmpty()) {
            setTagsVisible(false);
            setTitle("Similar Question" + (searchObjects.size() > 1 ? "s - " : " - ") + searchObjects.size());
        }

        if (type == SearchBoardPanel.TAG_INPUT) {
            searchObjects.clear();

            for (Component component : searchTagsPanel.getComponents()) {
                if (!(component instanceof BoardTagButton)) continue;
                BoardTagButton tagButton = (BoardTagButton) component;

                DBDataObject[] questionDataObjects = DBQueTag.getQuestionsOrdered(tagButton.getTagID(), "*");

                Collections.addAll(searchObjects, questionDataObjects);
            }

            setTagsVisible(true);
            setTitle("Related Question" + (searchObjects.size() > 1 ? "s - " : " - ") + searchObjects.size());
        }

        questionsPanel.clearQuestions();

        if (searchObjects.isEmpty()) {
            setVisible(false);
            return;
        }

        addQuestions(searchObjects);
        setVisible(true);
    }

    private void setTagsVisible(boolean flag) {
        searchTagsScrollPanel.setVisible(flag);
        if (!flag) searchTagsPanel.clearTags();
    }

    private void addQuestions(List<DBDataObject> questionDataObjects) {
        for (DBDataObject questionDataObject : questionDataObjects) {
            BoardLabel label = new BoardLabel(questionDataObject.getValue(DBQuestion.K_QUESTION_HEAD));
            label.setFGLight();
            label.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 30);
            BoardContentDisplayPane activityPane = questionsPanel.addContentDisplayPanel();

            String answerCount = "Answers: " + DBQuestion.getAnswerCount(questionDataObject.getValue(DBQuestion.K_QUESTION_ID));
            activityPane.setContentData(
                    questionDataObject.getValue(DBQuestion.K_QUESTION_HEAD),
                    questionDataObject.getValue(DBQuestion.K_QUESTION_ID),
                    answerCount,
                    questionDataObject.getValue(DBQuestion.K_QUESTION_BYTESCORE));

            DBDataObject userData = DBUser.getUser(questionDataObject.getValue(DBQuestion.K_USER_ID));
            activityPane.setUserData(
                    userData.getValue(DBUser.K_USER_PROFILE),
                    userData.getValue(DBUser.K_USER_NAME),
                    userData.getValue(DBUser.K_USER_ID));
        }
        questionsPanel.resetScrolls();
    }

    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        questionsPanel.setVisible(aFlag);
    }

    private void setTitle(String text) {
        titleLabel.setText(text);
    }

    public boolean addTag(DBDataObject tagDataObject) {
        if (searchTagsPanel.contains(tagDataObject.getValue(DBTag.K_TAG))) return false;

        searchTagsScrollPanel.setVisible(true);
        searchTagsPanel.addTag(tagDataObject, e -> EventQueue.invokeLater(() ->
                searchQuestions(null, SearchBoardPanel.TAG_INPUT)), this);
        return true;
    }
}
