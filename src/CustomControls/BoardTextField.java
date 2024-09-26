package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.CustomListenerPackage.CustomDocumentListener;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

public class BoardTextField extends JTextField implements CustomControl {

    private BoardPanel container;
    private BoardLabel errorLabel;
    private String hintText;

    public BoardTextField(MainFrame main, Frame frame, Color background) {
        init(main, frame, background, 9);
    }

    public BoardTextField(MainFrame main, Frame frame, String background, int cols) {
        init(main, frame, ResourceManager.getColor(background), cols);
    }

    private void init(MainFrame main, Frame frame, Color background, int cols) {
        container = createContainer(main, frame, ByteBoardTheme.MAIN_LIGHT);

        addInsets(0);
        setColumns(cols);
        setMinimumSize(new Dimension(getPreferredSize().width, getPreferredSize().height + 10));
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        setBackground(background);
        if (!getBackground().equals(ResourceManager.getColor(ByteBoardTheme.BASE)))
            setFGLight();

        addUndoableListener();
    }

    private void addUndoableListener() {
        UndoManager undoManager = new UndoManager();

        getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        // Ctrl+Z to undo
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Undo");
        getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        // Ctrl+Y to redo
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "Redo");
        getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
    }

    private BoardPanel createContainer(MainFrame main, Frame frame, String background) {
        BoardPanel container = new BoardPanel(main, frame, background) {
            public void paint(Graphics g) {
                super.paint(g);
                if (BoardTextField.this.hasFocus()) {
                    int radius = getActualCornerRadius();
                    g.setColor(ResourceManager.getColor(ByteBoardTheme.ACCENT));
                    g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                }
            }
        };
        container.setCornerRadius(60);
        container.addInsets(10, 10, 10, 10);
        container.setLayout(new BorderLayout());
        container.add(this);

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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (hintText == null) return;

        if (getText().isEmpty()) {
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int c = 0xfefefefe;
            g.setColor(new Color(((c0 & c) >>> 1) + ((c1 & c) >>> 1), true));
            g.drawString(hintText, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (container != null)
            container.setBackground(bg);
    }

    public void setBackground(String bg) {
        this.setBackground(ResourceManager.getColor(bg));
    }

    public void setHintText(String text) {
        this.hintText = text;
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

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        super.setCaretColor(c);
    }

    /*public static void initTextField(JTextField textField, CustomDocumentListener documentListener) {
        textField.getDocument().addDocumentListener(documentListener);
        documentListener.setTextField(textField);

        if(textField.getParent() == null) return;

        textField.setBackground(textField.getParent().getBackground());

        if(!textField.getParent().getBackground().equals(ResourceManager.getColor(ByteBoardTheme.BASE)))
            textField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
    }*/
}
