package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardTagButton;
import CustomControls.GridBagBuilder;
import Resources.ResourceManager;

import java.awt.*;

public class SearchPopularTagsPanel extends BoardPanel {

    private BoardPanel popularTagsPanel;

    public SearchPopularTagsPanel(MainFrame main, Frame frame) {
        super(main, frame);
    }

    public void init(MainFrame main, Frame frame) {
        addInsets(0, 40, 0, 0);

        GridBagBuilder builder = new GridBagBuilder(this, 2);

        BoardLabel label = new BoardLabel("Popular Tags");
        label.addInsets(5);

        popularTagsPanel = new BoardPanel(main, frame);
        popularTagsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        builder.weightY(1).fillBoth()
                .addToNextCell(label)
                .weightX(1)
                .addToNextCell(popularTagsPanel);
    }

    protected void clearTags() {
        popularTagsPanel.removeAll();
    }

    protected void setPopularTags(DBDataObject[] tagDataObjects, String userID) {
        clearTags();
        for (DBDataObject tag : tagDataObjects) {
            BoardTagButton tagButton = new BoardTagButton(getFrame(), "add", ResourceManager.DEFAULT);
            tagButton.addInsets(10);
            tagButton.setFGLight();
            tagButton.setTag(tag.getValue(DBTag.K_TAG), userID);
            popularTagsPanel.add(tagButton);
        }
    }
}
