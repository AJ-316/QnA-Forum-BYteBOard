package BoardControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardEventListeners.CustomDocumentListener;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class BoardPasswordField extends JPasswordField implements BoardControl {

    private final BoardPanel container;
    private BoardLabel errorLabel;
    private String hintText;

    public BoardPasswordField(Frame frame, String background, int cols) {
        container = createContainer(frame, background);

        addInsets(0);
        setEchoChar('•');
        setHintText("••••••••");
        setColumns(cols);
        setMinimumSize(new Dimension(getPreferredSize().width, getPreferredSize().height + 10));
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        setBackground(container.getBackground());
        if (!getBackground().equals(ResourceManager.getColor(ByteBoardTheme.BASE)))
            setFGLight();
    }

    private BoardPanel createContainer(Frame frame, String background) {
        BoardPanel container = new BoardPanel(frame, background) {
            public void paint(Graphics g) {
                super.paint(g);
                if (BoardPasswordField.this.hasFocus()) {
                    int radius = getActualCornerRadius();
                    g.setColor(ResourceManager.getColor(ByteBoardTheme.ACCENT));
                    g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (hintText == null) return;

        if (getDocument().getLength() == 0) {
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            g.setColor(BoardTextField.getHintColor(getBackground()));
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
        documentListener.setTextComponent(this);
    }

    public BoardLabel getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(String errorText) {
        if (errorLabel == null) {
            errorLabel = new BoardLabel();
            errorLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 16);
            errorLabel.setFGError();
            errorLabel.setAlignmentLeading();
            container.add(errorLabel, BorderLayout.SOUTH);
            addActionListener(e -> errorLabel.setText(""));
        }

        errorLabel.setText(errorText.length() > 28 ? errorText.substring(0, 28) : errorText);
    }

    public void setBackground(String bg) {
        setBackground(ResourceManager.getColor(bg));
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        super.setCaretColor(c);
    }
}
