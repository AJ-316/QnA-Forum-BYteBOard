package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQueTag;
import BYteBOardDatabase.DBTag;
import BYteBOardDatabase.DBUser;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.GridBagBuilder;

import java.util.ArrayList;

public class SearchBoardFrame extends BoardFrame {

    private SearchBoardPanel searchBoardPanel;
    private SearchPopularTagsPanel popularTagsPanel;

    public SearchBoardFrame(MainFrame main) {
        super(main, (delegate, context) -> {

            context = delegate.getContextOrDefault(context, DBUser.TABLE, DBUser.K_USER_ID);

            String userID = context[0];

            if(context.length > 1) {
                DBDataObject tagData = DBTag.ops.findValuesBy(DBTag.ops.matchByValue(DBTag.K_TAG_ID, context[1]), "*")[0];
                delegate.putContext(DBTag.TABLE, tagData);
            } else
                delegate.putContext(DBTag.TABLE, null);

            delegate.putContext(DBUser.TABLE, DBUser.getUser(userID));
            delegate.putContextList(DBQueTag.TABLE, DBQueTag.getTopTags(5));

            return null;
        });
    }

    public void init(MainFrame main) {
        GridBagBuilder builder = new GridBagBuilder(this, 1);

        SearchQuestionPanel searchQuestionPanel = new SearchQuestionPanel(main, this);
        searchBoardPanel = new SearchBoardPanel(main, this, searchQuestionPanel);
        popularTagsPanel = new SearchPopularTagsPanel(main, this);

        BoardButton backButton = new BoardButton("Profile", "home");
        backButton.addActionListener(e -> requestSwitchFrame(ProfileBoardFrame.class));
        backButton.setFGLight();

        BoardPanel searchPanel = new BoardPanel(main, this);
        GridBagBuilder searchPanelBuilder = new GridBagBuilder(searchPanel, 1);
        searchPanelBuilder.weightX(1).fillHorizontal()
                .addToNextCell(searchBoardPanel)
                .addToNextCell(popularTagsPanel)
                .weightY(1).fillBoth()
                .addToNextCell(searchQuestionPanel);

        builder.weight(1, 1).fillBoth()
                .addToNextCell(searchPanel)
                .weightY(0)
                .addToNextCell(backButton);

//        searchQuestionPanel.setVisible(false);
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate delegate) {
        DBDataObject userData = delegate.getContext(DBUser.TABLE);

        String userID = userData.getValue(DBUser.K_USER_ID);

        searchBoardPanel.setUserID(userID);
        searchBoardPanel.searchByTag(delegate.getContext(DBTag.TABLE));

        popularTagsPanel.setPopularTags(delegate.getContextList(DBQueTag.TABLE, new ArrayList<>()), userID);
    }
}
