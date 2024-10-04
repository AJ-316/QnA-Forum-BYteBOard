package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardTagButton;
import CustomControls.DEBUG;
import CustomControls.GridBagBuilder;
import Resources.ResourceManager;

import java.awt.*;

public class SearchPopularTagsPanel extends BoardPanel {

    private BoardPanel popularTagsPanel;

    public SearchPopularTagsPanel(Frame frame) {
        super(frame);
    }

    public void init(Frame frame) {
        addInsets(0, 40, 0, 0);

        GridBagBuilder builder = new GridBagBuilder(this, 2);

        BoardLabel label = new BoardLabel("Popular Tags");
        label.addInsets(5);

        popularTagsPanel = new BoardPanel(frame);
        popularTagsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        builder.weightY(1).fillBoth()
                .addToNextCell(label)
                .weightX(1)
                .addToNextCell(popularTagsPanel);

        createTagButtons();
    }

    protected void createTagButtons() {
        for (int i = 0; i < 5; i++) {
            BoardTagButton tagButton = new BoardTagButton(getFrame(), "add", ResourceManager.DEFAULT);
            tagButton.addInsets(10);
            tagButton.setFGLight();
            popularTagsPanel.add(tagButton);
        }
    }

    protected void setPopularTags(DBDataObject[] tagDataObjects, String userID) {
        for (int i = 0; i < popularTagsPanel.getComponentCount(); i++) {
            Component component = popularTagsPanel.getComponent(i);
            if(!(component instanceof BoardTagButton))
                return;

            if(i >= tagDataObjects.length) {
                component.setVisible(false);
                continue;
            }

            component.setVisible(true);
            BoardTagButton tagButton = (BoardTagButton) component;
            tagButton.setTag(tagDataObjects[i], userID);
            //DE BUG.printlnYellow("Popular Tag: " + tagDataObjects[i].getValue(DBTag.K_TAG) + "[" + tagDataObjects[i].getValue(DBTag.K_TAG_ID) + "]");
        }
    }
}
