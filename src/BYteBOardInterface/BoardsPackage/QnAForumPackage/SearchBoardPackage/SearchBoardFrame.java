package BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQueTag;
import BYteBOardDatabase.DBUser;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardButton;
import CustomControls.GridBagBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchBoardFrame extends BoardFrame {

    private BoardButton backButton;
    private SearchBoardPanel searchBoardPanel;
    private SearchPopularTagsPanel popularTagsPanel;
    private SearchQuestionPanel searchQuestionPanel;

    public SearchBoardFrame(MainFrame main) {
        super(main, (delegate, context) -> {

            context = delegate.getContextOrDefault(context, DBUser.TABLE, DBUser.K_USER_ID);

            String userID = context[0];

            delegate.putContext(DBUser.TABLE, DBUser.getUser(userID));
            delegate.putContextList(DBQueTag.TABLE, DBQueTag.getTopTags(5));

            return null;
        });
    }

    public void init(MainFrame main) {
        GridBagBuilder builder = new GridBagBuilder(this, 1);

        searchQuestionPanel = new SearchQuestionPanel(main, this);
        searchBoardPanel = new SearchBoardPanel(main, this, searchQuestionPanel);
        popularTagsPanel = new SearchPopularTagsPanel(main, this);

        backButton = new BoardButton("Profile", "home");
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

        List<DBDataObject> tagDataList = new ArrayList<>();
        popularTagsPanel.setPopularTags(delegate.getContextList(DBQueTag.TABLE, tagDataList), userID);

        searchBoardPanel.setUserID(userID);
    }
}
