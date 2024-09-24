package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQueTag;
import BYteBOardDatabase.DBQuestion;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.BoardTagsDisplayPanel;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import java.awt.*;

public class SearchQuestionPanel extends BoardPanel {

    private BoardLabel titleLabel;
    private BoardTagsDisplayPanel searchTagsPanel;
    private GridBagBuilder questionsBuilder;
    private BoardScrollPanel questionsPanel;

    public SearchQuestionPanel(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN);
    }

    public void init(MainFrame main, Frame frame) {
        setCornerRadius(90);
        addInsets(20);

        titleLabel = new BoardLabel("Related Questions");
        titleLabel.setAlignmentLeading();
        titleLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 22);
        titleLabel.setFGLight();
        titleLabel.addInsets(10);

        BoardPanel searchTagsScrollPanel = new BoardPanel(main, frame);
        searchTagsScrollPanel.setLayout(new BorderLayout());
        searchTagsPanel = new BoardTagsDisplayPanel(main, frame);
        searchTagsPanel.addInsets(0);
        searchTagsPanel.setHorizontalDisplay();
        searchTagsScrollPanel.setMinimumSize(new Dimension(0, 60));
        searchTagsScrollPanel.add(searchTagsPanel.getComponent());

        // todo tags panel hiding again!!!!!
        //  also create contentClickablePane and its panel
        BoardPanel questionPanelHolder = new BoardPanel(main, frame, ByteBoardTheme.MAIN_DARK);
        questionPanelHolder.setLayout(new BorderLayout());
        questionPanelHolder.setCornerRadius(90);
        questionPanelHolder.addInsets(20);
        questionsPanel = new BoardScrollPanel(main, frame);
        questionsPanel.setBackground(ByteBoardTheme.MAIN_DARK);
        questionsBuilder = new GridBagBuilder(questionsPanel, 1).weightX(1).insets(10).fillHorizontal();

        questionPanelHolder.add(questionsPanel.getComponent());

        GridBagBuilder builder = new GridBagBuilder(this, 1);

        builder.weightX(1).fillBoth().insets(0, 20, 0, 20)
                .addToNextCell(searchTagsScrollPanel)
                .addToNextCell(titleLabel)
                .weight(0, 1).insets(0)
                .addToNextCell(questionPanelHolder);

        // requires one tag to be added first for the panel to be visible
        searchTagsPanel.remove(searchTagsPanel.addTag("tag", this));
    }

    public void loadQuestionsBySimilarity(String[] similarQuestionIDs) {
        if(similarQuestionIDs.length == 0) {
            setVisible(false);
            return;
        }
        questionsPanel.removeAll();

        setTitle("Similar Questions");

        for (String questionID : similarQuestionIDs) {
            DBDataObject questionDataObject = DBQuestion.getQuestion(questionID);
            addQuestions(questionDataObject);
        }
        setVisible(true);
    }

    public void loadQuestionsByTags() {
        if(searchTagsPanel.getComponents().length == 0) {
            setVisible(false);
            return;
        }
        questionsPanel.removeAll();

        setTitle("Related Questions");

        System.out.println(DEBUG.PURPLE + "Searching related Questions of tags: ");
        for (Component component : searchTagsPanel.getComponents()) {
            if (!(component instanceof BoardTagButton)) continue;
            BoardTagButton tagButton = (BoardTagButton) component;

            DBDataObject[] questionDataObjects = DBQueTag.getQuestionsOrdered(tagButton.getTagID(), "*");

            addQuestions(questionDataObjects);
        }
        System.out.println(DEBUG.NONE);
        setVisible(true);
    }

    private void addQuestions(DBDataObject... questionDataObjects) {
        for (DBDataObject questionDataObject : questionDataObjects) {
            BoardLabel label = new BoardLabel(questionDataObject.getValue(DBQuestion.K_QUESTION_HEAD));
            label.setFGLight();
            label.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 30);
            questionsBuilder.addToNextCell(label);
            System.out.println("Loaded: " + questionDataObject);
        }
    }

    private void setTitle(String text) {
        titleLabel.setText(text);
    }

    public void addTag(String tag, String tagID) {
        searchTagsPanel.addTag(tag, tagID, e -> EventQueue.invokeLater(this::loadQuestionsByTags), this);
    }
}
