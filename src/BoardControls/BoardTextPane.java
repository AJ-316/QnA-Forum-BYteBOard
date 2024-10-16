package BoardControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardEventListeners.CustomDocumentListener;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardTextPane extends JTextPane implements BoardControl {

    private static final String BULLET_POINT_1 = "•";
    private static final String SUB_BULLET_POINT = "⁃";
    private static final String BULLET_POINT_2 = "‣";

    private static final String[] TOKENS = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "continue",
            "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for",
            "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
            "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"
    };
    private final Frame frame;
    private String hintText;
    private int lineIndex;
    private int linesCount;

    public BoardTextPane(String bg) {
        this.frame = null;
        setOpaque(false);
        setEditable(false);

        setBackground(ResourceManager.getColor(bg));
        setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));

        setFGLight();
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
    }

    public BoardTextPane(Frame frame, String bg) {
        this.frame = frame;

        setOpaque(false);
        setEditable(false);

        setBackground(ResourceManager.getColor(bg));
        setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));

        setFGLight();
        setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);

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

    public static void trimNewLines(Document doc) throws BadLocationException {
        while (doc.getLength() > 0 && doc.getText(0, 1).equals("\n")) {
            doc.remove(0, 1);
        }
        while (doc.getLength() > 0 && doc.getText(doc.getLength() - 1, 1).equals("\n")) {
            doc.remove(doc.getLength() - 1, 1);
        }
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

    private void highlightPattern(StyledDocument doc, String pattern, AttributeSet attr) {
        try {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(doc.getText(0, doc.getLength()));

            while (m.find()) {
                int start = m.start();
                int end = m.end();
                int length = end - start;

                doc.setCharacterAttributes(start, length, attr, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTextWithStyles(String text) {
        /*while (text.startsWith("\n")) {
            text = text.substring(1);
        }

        while (text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }*/
        text = text.replaceAll("^\\n+|\\n+$", "");

        setText("");
        StyledDocument doc = getStyledDocument();
        doc.setCharacterAttributes(0, doc.getLength(), getStyle("default"), true);

        String[] lines = text.split("\n");
        linesCount = lines.length;
        boolean inCodeBlock = false;
        boolean inTextBlock = false;
        boolean isListItem = false;
        BoardPanel codeBlockPanel = null;
        BoardPanel textBlockPanel = null;

        try {
            for (lineIndex = 0; lineIndex < linesCount; lineIndex++) {
                String line = lines[lineIndex];

                if (line.equals("'''")) {
                    inCodeBlock = !inCodeBlock;
                    if (inCodeBlock) {
                        codeBlockPanel = addToCodeBlock(null, null);
                    } else
                        codeBlockPanel = addToCodeBlock(null, codeBlockPanel);
                    continue;
                }

                if (inCodeBlock) {
                    addToCodeBlock(line, codeBlockPanel);
                    continue;
                }

                line = line.trim();

                if (line.endsWith(" \\"))
                    line = line.substring(0, line.length() - 1).trim() + "\n";
                else if (!line.isEmpty() && !line.equals("\\"))
                    line += " ";

                if (line.startsWith("> ")) {
                    if (inTextBlock) {
                        addToTextBlock(null, textBlockPanel);
                    }

                    inTextBlock = true;
                    if (textBlockPanel == null) {
                        textBlockPanel = addToTextBlock(null, null);
                    }

                    addToTextBlock(line.substring(2), textBlockPanel);
                    continue;
                }

                if (inTextBlock) {
                    if (line.equals("\\"))
                        line = "\n";

                    if (line.isEmpty()) {
                        textBlockPanel = addToTextBlock(null, textBlockPanel);
                        inTextBlock = false;
                        continue;
                    }

                    addToTextBlock(line, textBlockPanel);
                    continue;
                }

                isListItem = insertListItem(line, isListItem);
                if (isListItem) continue;

                if (line.isEmpty())
                    line = "\n";
                else if (line.equals("\\"))
                    line = "\n\n";

                applyStyledText(doc, line);
            }

            if (inCodeBlock) {
                addToCodeBlock(null, codeBlockPanel);
            } else if (inTextBlock) {
                addToTextBlock(null, textBlockPanel);
            }

            trimNewLines(doc);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean insertListItem(String line, boolean isListItem) throws BadLocationException {
        if (line.isEmpty()) return false;

        String bulletPoint = line.startsWith("* ") ? BULLET_POINT_1 : line.startsWith("- ") ? BULLET_POINT_2 : null;

        if (bulletPoint != null) {
            line = (isListItem ? "\n " : " ") + bulletPoint + " " + line.substring(2);
            isListItem = true;
        }

        if (line.startsWith("+ ")) {
            line = (isListItem ? "\n    " : " ") + SUB_BULLET_POINT + " " + line.substring(2);
            isListItem = true;
        }

        if (isListItem)
            insertText(line);

        return isListItem;
    }

    private BoardPanel addToCodeBlock(String line, BoardPanel codeBlock) throws BadLocationException {
        return addToBlock(line, codeBlock, true);
    }

    private BoardPanel addToTextBlock(String line, BoardPanel textBlock) throws BadLocationException {
        return addToBlock(line, textBlock, false);
    }

    /**
     * start    - line and block = null <br/>
     * addLine  - none = null <br/>
     * end      - block = null <br/>
     */
    private BoardPanel addToBlock(String line, BoardPanel blockPanel, boolean isCodeBlock) throws BadLocationException {
        if (blockPanel == null) {

            blockPanel = getBlockPanel(isCodeBlock);
            if (line == null)
                return blockPanel; // start of code blockPanel

        } else if (line == null) {

            JTextComponent textComponent = ((JTextComponent) getBlockArea(blockPanel));
            Document doc = textComponent.getDocument();
            if (doc.getLength() > 0 && textComponent.getText(doc.getLength() - 1, 1).equals("\n")) {
                doc.remove(doc.getLength() - 1, 1);
            }

            if (isCodeBlock) {
                applyCodeSyntaxHighlighting((BoardTextPane) getBlockArea(blockPanel));
                getBlockScroll(blockPanel).resetScroll();
            }

            insertText("\n");
            insertComponent(blockPanel);
            insertText("\n");
            return null; // end of code blockPanel
        }

        if (isCodeBlock)
            line = line + "\n";

        addLineToBlock(line, blockPanel); // add line to code blockPanel

        return blockPanel;
    }

    private void addLineToBlock(String line, BoardPanel panel) throws BadLocationException {
        Component blockArea = getBlockArea(panel);

        if (blockArea instanceof BoardTextArea) {
            BoardTextArea textArea = (BoardTextArea) blockArea;
            textArea.append(line);
            return;
        }

        if (blockArea instanceof BoardTextPane) {
            BoardTextPane textPane = (BoardTextPane) blockArea;
            textPane.insertText(line);
        }
    }

    private void insertText(String text) throws BadLocationException {
        StyledDocument doc = getStyledDocument();
        doc.insertString(doc.getLength(), text, null);
    }

    private Component getBlockArea(JPanel panel) {
        Component component = getBlockScroll(panel).getViewport().getView();

        if (component instanceof JTextComponent) {
            return component;
        }

        return ((BoardPanel) component).getComponent(0);
    }

    private SimpleScrollPane getBlockScroll(JPanel panel) {
        return (SimpleScrollPane) ((BoardPanel) panel.getComponent(0)).getComponent(0);
    }

    private void applyCodeSyntaxHighlighting(BoardTextPane codeArea) {
        StyledDocument doc = codeArea.getStyledDocument();

        doc.setCharacterAttributes(0, codeArea.getText().length(), ResourceManager.getAttributeSet(ByteBoardTheme.AS_CODE_TEXT), true);

        for (String keyword : TOKENS) {
            highlightPattern(doc, "\\b" + keyword + "\\b", ResourceManager.getAttributeSet(ByteBoardTheme.AS_CODE_TOKEN));
        }
        highlightPattern(doc, "\\b\\d+\\b", ResourceManager.getAttributeSet(ByteBoardTheme.AS_CODE_NUMBER));
        highlightPattern(doc, "\".*?\"", ResourceManager.getAttributeSet(ByteBoardTheme.AS_CODE_STRING));
    }

    private BoardPanel getBlockPanel(boolean isCodeBlock) {
        String bgColor = ByteBoardTheme.MAIN_DARK;
        BoardPanel panel = createBlockPanel(bgColor);

        SimpleScrollPane scrollPane = isCodeBlock ?
                createCodeTextArea(bgColor) : createBlockTextArea(bgColor);

        panel.add(scrollPane, BorderLayout.CENTER);

        BoardPanel paddedPanel = new BoardPanel(frame);
        paddedPanel.addInsets(5, 5, 5, 10);
        paddedPanel.setLayout(new BorderLayout());
        paddedPanel.add(panel);

        addMouseWheelForwarding(scrollPane);

        return paddedPanel;
    }

    private BoardPanel createBlockPanel(String bgColor) {
        BoardPanel panel = new BoardPanel(frame, bgColor) {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paint(g2d);

                int pad = 12;

                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(pad, pad, 5, getHeight() - pad * 2, 5, 5);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setCornerRadius(40);
        panel.addInsets(10, 30, 10, 10);
        return panel;
    }

    private SimpleScrollPane createBlockTextArea(String bgColor) {
        BoardTextArea textArea = new BoardTextArea("");
        textArea.setBackground(bgColor);
        textArea.setFGLight();
        textArea.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 18);

        SimpleScrollPane scrollPane = new SimpleScrollPane(textArea);
        scrollPane.setBackground(bgColor);
        return scrollPane;
    }

    private SimpleScrollPane createCodeTextArea(String bgColor) {
        BoardTextPane textPane = new BoardTextPane(frame, bgColor);
        textPane.setFGLight();
        textPane.setFontSecondary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);
        textPane.addInsets(10, 0, 15, 0);

        BoardPanel scrollPanel = new BoardPanel(frame);
        scrollPanel.setLayout(new BorderLayout());
        scrollPanel.add(textPane);

        SimpleScrollPane scrollPane = new SimpleScrollPane(scrollPanel);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.setBackground(bgColor);
        return scrollPane;
    }

    private void applyStyledText(StyledDocument doc, String text) {
        try {
            doc.insertString(doc.getLength(), text, getStyle("default"));
        } catch (BadLocationException e) {
            e.printStackTrace();
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

    private void addMouseWheelForwarding(JComponent component) {
        component.addMouseWheelListener(e -> {
            JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, BoardTextPane.this);
            if (scrollPane != null) {
                scrollPane.dispatchEvent(SwingUtilities.convertMouseEvent(component, e, scrollPane));
            }
        });
    }
}
