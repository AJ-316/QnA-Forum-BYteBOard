package CustomControls;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;

public class BoardTextArea extends JTextArea implements CustomControl {

    public BoardTextArea(String text) {
        super(text);

        setOpaque(false);
        setEditable(false);
        setWrapStyleWord(true);
        setLineWrap(true);

        addInsets(0);
        setFGLight();
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
    }

    public void setFontPrimary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(type, size)));
    }

    public void setFontSecondary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_SECONDARY(type, size)));
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
