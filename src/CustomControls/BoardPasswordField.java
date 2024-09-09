package CustomControls;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.CustomListenerPackage.CustomDocumentListener;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class BoardPasswordField extends JPasswordField implements CustomControl {

    private final BoardPanel container;
    private BoardLabel errorLabel;
    private String hintText;

    public BoardPasswordField(MainFrame main, Frame frame, String background, int cols) {
        container = createContainer(main, frame, background);

        addInsets(0);
        setEchoChar('•');
        setHintText("••••••••");
        setColumns(cols);
        setMinimumSize(new Dimension(getPreferredSize().width, getPreferredSize().height + 10));
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        setBackground(container.getBackground());
        if(!getBackground().equals(ResourceManager.getColor(ByteBoardTheme.BASE)))
            setFGLight();
    }

    private BoardPanel createContainer(MainFrame main, Frame frame, String background) {
        BoardPanel container = new BoardPanel(main, frame, background) {
            public void paint(Graphics g) {
                super.paint(g);
                if(BoardPasswordField.this.hasFocus()) {
                    int radius = getActualCornerRadius();
                    g.setColor(ResourceManager.getColor(ByteBoardTheme.ACCENT));
                    g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
                }
            }
        };
        container.setCornerRadius(60);
        container.addInsets(10, 10, 10, 10);
        container.setLayout(new BorderLayout());
        container.add(this, BorderLayout.CENTER);

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                container.repaint();
            }
            public void focusLost(FocusEvent e) {
                container.repaint();
            }
        });

        return container;
    }

    public void setErrorLabel(String errorText) {
        if(errorLabel == null) {
            errorLabel = new BoardLabel();
            errorLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 16);
            errorLabel.setFGError();
            errorLabel.setAlignmentLeading();
            container.add(errorLabel, BorderLayout.SOUTH);
            addActionListener(e-> errorLabel.setText(""));
        }

        errorLabel.setText(errorText.length() > 28 ? errorText.substring(0, 28) : errorText);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(hintText == null) return;

        if (getPassword().length == 0) {
            int h = getHeight();
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int c = 0xfefefefe;
            g.setColor(new Color(((c0 & c) >>> 1) + ((c1 & c) >>> 1), true));
            g.drawString(hintText, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    public void setHintText(String text) {
        this.hintText = text;
    }

    public void clearError() {
        setErrorLabel("");
    }

    public BoardPanel getTextFieldContainer() {
        return container;
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

    public void addDocumentListener(CustomDocumentListener documentListener) {
        getDocument().addDocumentListener(documentListener);
        documentListener.setTextField(this);
    }

    public BoardLabel getErrorLabel() {
        return errorLabel;
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        super.setCaretColor(c);
    }
}
