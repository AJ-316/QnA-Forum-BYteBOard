package CustomControls;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;

public class BoardLabel extends JLabel implements CustomControl {

    public BoardLabel() {}

    // just the text
    public BoardLabel(String text) {
        super(text);

        init();
    }

    /** regular icon */
    public BoardLabel(String text, String icon) {
        this(text, icon, ResourceManager.NONE, ResourceManager.MINI);
    }

    /** regular icon with size */
    public BoardLabel(String icon, int size) {
        this("", icon, ResourceManager.NONE, size);
    }

    /** state icon (recolored) */
    public BoardLabel(String text, String icon, int state) {
        this(text, icon, state, ResourceManager.MINI);
    }

    /** tate/regular icon with size */
    public BoardLabel(String text, String icon, int state, int size) {
        super(text);

        if(state == ResourceManager.NONE)
            setIcon(ResourceManager.getIcon(icon, size));
        else
            setIcon(ResourceManager.getStateIcon(icon, state, size));

        init();
    }

    private void init() {
        setAlignmentCenter();
    }

    public void setProfileIcon(String userProfileIndex, int size) {
        setIcon(ResourceManager.getProfileIcon(userProfileIndex, size));
        setName(userProfileIndex);
    }

    public void setFontPrimary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(type, size)));
    }

    public void setFontSecondary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(type, size)));
    }

    public void setAlignmentLeading() {
        setHorizontalAlignment(LEADING);
    }

    public void setAlignmentTrailing() {
        setHorizontalAlignment(TRAILING);
    }

    public void setAlignmentCenter() {
        setHorizontalAlignment(CENTER);
    }

    public void setFGDark() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
    }

    public void setFGLight() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
    }

    public void setFGMain() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_MAIN));
    }

    public void addInsets(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}
