package CustomControls;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;

public class BoardLabel extends JLabel implements CustomControl {

    public BoardLabel() {
        this("");
    }

    // just the text
    public BoardLabel(String text) {
        super(text);

        init();
    }

    /**
     * regular icon
     */
    public BoardLabel(String text, String icon) {
        this(text, icon, ResourceManager.NONE, ResourceManager.MINI);
    }

    /**
     * just the regular icon with size
     */
    public BoardLabel(String icon, int size) {
        this("", icon, ResourceManager.NONE, size);
    }

    /**
     * state icon (recolored)
     */
    public BoardLabel(String text, String icon, int state) {
        this(text, icon, state, ResourceManager.MINI);
    }

    /**
     * tate/regular icon with size
     */
    public BoardLabel(String text, String icon, int state, int size) {
        super(text);

        if (state == ResourceManager.NONE)
            setIcon(ResourceManager.getIcon(icon, size));
        else
            setIcon(ResourceManager.getStateIcon(icon, state, size));

        init();
    }

    private void init() {
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        setAlignmentCenter();
    }

    public void setProfileIcon(String userProfileIndex, int size) {
        setIcon(ResourceManager.getProfileIcon(userProfileIndex, size));
        setName(userProfileIndex);
    }

    public void setProfileIcon(String userProfileIndex, String recolor, int size) {
        setIcon(ResourceManager.getProfileIcon(userProfileIndex, ResourceManager.getColor(recolor), size));
        setName(userProfileIndex);
    }

    public void setColoredIcon(String icon, String blackRecolor, String whiteRecolor, int size) {
        setIcon(ResourceManager.getColoredIcon(icon,
                blackRecolor == null ? null : ResourceManager.getColor(blackRecolor),
                whiteRecolor == null ? null : ResourceManager.getColor(whiteRecolor), size));
    }

    public void setIcon(String icon, int state, int size) {
        setIcon(ResourceManager.getStateIcon(icon, state, size));
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

    public void setFGError() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.ERROR));
    }

    public void setBackground(String bg) {
        setBackground(ResourceManager.getColor(bg));
    }

    public void addInsets(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}
