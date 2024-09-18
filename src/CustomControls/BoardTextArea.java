package CustomControls;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class BoardTextArea extends JTextArea implements CustomControl {

    private String hintText;

    public BoardTextArea(String text) {
        super(text);

        setOpaque(false);
        setEditable(false);
        setWrapStyleWord(true);
        setLineWrap(true);

        addInsets(0);
        setFGLight();
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));

        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {repaint();}
            public void removeUpdate(DocumentEvent e) {repaint();}
            public void changedUpdate(DocumentEvent e) {repaint();}
        });
    }

    public void setHintText(String text) {
        this.hintText = text;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(hintText == null) return;

        if (getText().isEmpty()) {
            int h = getHeight();
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int c = 0xfefefefe;
            g.setColor(new Color(((c0 & c) >>> 1) + ((c1 & c) >>> 1), true));
            g.drawString(hintText, ins.left, fm.getAscent() - 2);
        }
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
