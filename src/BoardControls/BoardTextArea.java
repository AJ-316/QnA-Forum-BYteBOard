package BoardControls;

import BoardEventListeners.CustomDocumentListener;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class BoardTextArea extends JTextArea implements BoardControl {

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
        setBackground(ByteBoardTheme.MAIN_DARK);

        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                repaint();
            }

            public void removeUpdate(DocumentEvent e) {
                repaint();
            }

            public void changedUpdate(DocumentEvent e) {
                repaint();
            }
        });
    }

    public void setHintText(String text) {
        this.hintText = text;
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (hintText == null) return;

        if (getDocument().getLength() == 0) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();

            g.setColor(BoardTextField.getHintColor(getBackground()));
            g.drawString(hintText, ins.left, fm.getAscent() - 2);
        }
    }

    public void addDocumentListener(CustomDocumentListener documentListener) {
        getDocument().addDocumentListener(documentListener);
        documentListener.setTextComponent(this);
    }

    public void setBackground(String bg) {
        setBackground(ResourceManager.getColor(bg));
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
