package BoardControls;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBTag;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage.SearchBoardFrame;
import BYteBOardInterface.StructurePackage.Frame;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import java.awt.*;
import java.awt.event.ActionListener;

public class BoardTagButton extends BoardButton {

    private final Frame frame;
    private ActionListener searchListener;
    private ActionListener removeSelfListener;

    public BoardTagButton(Frame frame, String icon, int defaultIconState) {
        super("", icon, defaultIconState, ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.MICRO);
        this.frame = frame;
        setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        setAlignmentCenter();
        addInsets(5);
        setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);
        setRolloverFGColor(ByteBoardTheme.TEXT_FG_LIGHT);
    }

    public void setTag(DBDataObject tagDataObject, String userID) {
        setText(tagDataObject.getValue(DBTag.K_TAG));
        setName(tagDataObject.getValue(DBTag.K_TAG_ID));

        if(searchListener == null) {
            searchListener = e -> frame.getBoardFrame().requestSwitchFrame(SearchBoardFrame.class, userID, getTagID());
            addActionListener(searchListener);
        }
    }

    public void setTag(DBDataObject tagDataObject, Container container) {
        setText(tagDataObject.getValue(DBTag.K_TAG));
        setName(tagDataObject.getValue(DBTag.K_TAG_ID));

        if(removeSelfListener == null) {
            removeSelfListener = e -> container.remove(this);
            addActionListener(removeSelfListener);
        }
    }

    public String getTagID() {
        return getName();
    }

    public String getTag() {
        return getText();
    }
}
