package CustomControls;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

public class BoardTagButton extends BoardButton {

    public BoardTagButton() {
        super("", "tag", ResourceManager.DEFAULT_DARK, ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.MICRO);
        setFGLight();
        setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        setAlignmentCenter();
        addInsets(5);
        setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);
        setRolloverFGColor(ByteBoardTheme.TEXT_FG_LIGHT);
    }

    public void setTag(String tag) {
        setText(tag);
    }
}
