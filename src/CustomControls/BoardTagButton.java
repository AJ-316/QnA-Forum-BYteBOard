package CustomControls;

import BYteBOardInterface.StructurePackage.Frame;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;

public class BoardTagButton extends BoardButton {

    private final Frame frame;

    public BoardTagButton(Frame frame, String icon, int defaultIconState) {
        super("", icon, defaultIconState, ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.MICRO);
        this.frame = frame;
        setFGLight();
        setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        setAlignmentCenter();
        addInsets(5);
        setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);
        setRolloverFGColor(ByteBoardTheme.TEXT_FG_LIGHT);
    }

    public void setTag(String tag, String userID) {
        setText(tag);
        // TODO addActionListener(e -> frame.getBoardFrame().requestSwitchFrame(SearchBoardFrame.class, userID, tag));
    }

    public void setTag(String tag, Container container) {
        setText(tag);
        addActionListener(e -> container.remove(this));
    }

    public String getTagID() {
        return getName();
    }

    public void setTagID(String id) {
        setName(id);
    }

    public String getTag() {
        return getText();
    }
}
