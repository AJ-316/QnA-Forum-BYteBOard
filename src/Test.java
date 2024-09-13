import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.*;

public class Test extends JFrame {

    private JTextPane textPane;
    private DefaultStyledDocument doc;
    private String[] keyWords = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "continue",
            "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for",
            "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
            "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"
    };

    public Test() {
        ResourceManager.init();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        // Setup the JTextPane and its document
        doc = new DefaultStyledDocument();
        textPane = new JTextPane(doc);
        //textPane.setText("import javax.swing.*;\n" + "import javax.swing.text.*;\n" + ">import java.awt.*;\n" + "\n" + "public class Test extends JFrame {\n" + "\n" + "    private JTextPane textPane;\n" + "    private DefaultStyledDocument doc;\n" + "    private String[] keyWords = {\n" + "            \"abstract\", \"assert\", \"boolean\", \"break\", \"byte\", \"case\", \"catch\", \"char\", \"class\", \"continue\",\n" + "            \"default\", \"do\", \"double\", \"else\", \"enum\", \"extends\", \"final\", \"finally\", \"float\", \"for\",\n" + "            \"if\", \"implements\", \"import\", \"instanceof\", \"int\", \"interface\", \"long\", \"native\", \"new\",\n" + "            \"null\", \"package\", \"private\", \"protected\", \"public\", \"return\", \"short\", \"static\", \"strictfp\",\n" + "            \"super\", \"switch\", \"synchronized\", \"this\", \"throw\", \"throws\", \"transient\", \"try\", \"void\", \"volatile\", \"while\"\n" + "    };\n" + "\n" + "    public Test() {\n" + "        ResourceManager.init();\n" + "        \n" + "        setDefaultCloseOperation(EXIT_ON_CLOSE);\n" + "        setSize(400, 400);\n" + "        setLocationRelativeTo(null);\n" + "\n" + "        // Setup the JTextPane and its document\n" + "        doc = new DefaultStyledDocument();\n" + "        textPane = new JTextPane(doc);\n" + "        textPane.setBackground(new Color(0x1e1f22));\n" + "        textPane.setBorder(BorderFactory.createEmptyBorder());\n" + "\n" + "        JScrollPane scrollPane = new JScrollPane(textPane);\n" + "        scrollPane.setBorder(BorderFactory.createEmptyBorder());\n" + "\n" + "        // Add a refresh button\n" + "        JButton refreshButton = new JButton(\"Refresh Preview\");\n" + "        refreshButton.addActionListener(e -> refreshSyntaxHighlighting());\n" + "\n" + "        // Layout\n" + "        setLayout(new BorderLayout());\n" + "        add(scrollPane, BorderLayout.CENTER);\n" + "        add(refreshButton, BorderLayout.SOUTH);\n" + "\n" + "        setVisible(true);\n" + "    }\n" + "\n" + "    // Method to refresh the syntax highlighting manually\n" + "    private void refreshSyntaxHighlighting() {\n" + "        String text = textPane.getText();\n" + "\n" + "        final StyleContext cont = StyleContext.getDefaultStyleContext();\n" + "        final AttributeSet attrKeys = getAttributeSet(cont, new Color(0xe07e00), ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_BOLD, 20)));\n" + "        final AttributeSet attrText = getAttributeSet(cont, Color.white, ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_REGULAR, 20)));\n" + "        final AttributeSet attrString = getAttributeSet(cont, new Color(0x6AAB73), ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_REGULAR, 20)));\n" + "        final AttributeSet attrNumber = getAttributeSet(cont, new Color(0x0077cc), ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_REGULAR, 20)));\n" + "\n" + "        // Clear all styles first\n" + "        doc.setCharacterAttributes(0, text.length(), attrText, true);\n" + "\n" + "        // Apply keyword highlighting\n" + "        for (String keyword : keyWords) {\n" + "            highlightPattern(text, \"\\\\b\" + keyword + \"\\\\b\", attrKeys);\n" + "        }\n" + "\n" + "        // Apply number highlighting\n" + "        highlightPattern(text, \"\\\\b\\\\d+\\\\b\", attrNumber);\n" + "\n" + "        // Apply string highlighting (for strings enclosed in quotes)\n" + "        highlightPattern(text, \"\\\".*?\\\"\", attrString);\n" + "    }\n" + "\n" + "    // Helper method to apply the highlight for a regex pattern\n" + "    private void highlightPattern(String text, String pattern, AttributeSet attr) {\n" + "        try {\n" + "            Pattern p = Pattern.compile(pattern);\n" + "            Matcher m = p.matcher(text);\n" + "\n" + "            while (m.find()) {\n" + "                int start = m.start();\n" + "                int end = m.end();\n" + "                doc.setCharacterAttributes(start, end - start, attr, false);\n" + "            }\n" + "        } catch (Exception e) {\n" + "            e.printStackTrace();\n" + "        }\n" + "    }\n" + "\n" + "    private AttributeSet getAttributeSet(StyleContext cont, Color color, Font font) {\n" + "        AttributeSet aset = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, color);\n" + "        return cont.addAttribute(aset, StyleConstants.FontFamily, font.getFamily());\n" + "    }\n" + "\n" + "    public static void main(String[] args) {\n" + "        new Test();\n" + "    }\n" + "}\n");
        textPane.setText("This is a first text\nThis is second text\n>This is new Block\nThis is new Block on second line\n\nThis is a third text");
        textPane.setBackground(new Color(0x1e1f22));
        textPane.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Add a refresh button
        JButton refreshButton = new JButton("Refresh Preview");
        refreshButton.addActionListener(e -> refreshSyntaxHighlighting());

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void insertImage(String imagePath, int offset) {
        textPane.setCaretPosition(offset++);
        textPane.insertIcon(ResourceManager.getIcon(imagePath, 32));

        try {
            doc.insertString(offset, "\n", SimpleAttributeSet.EMPTY);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Method to refresh the syntax highlighting manually
    private void refreshSyntaxHighlighting() {
        insertImages();

        String text = textPane.getText();

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attrKeys = getAttributeSet(cont, new Color(0xe07e00), null, ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_BOLD, 20)));
        final AttributeSet attrText = getAttributeSet(cont, Color.white, null, ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_REGULAR, 20)));
        final AttributeSet attrString = getAttributeSet(cont, new Color(0x6AAB73), Color.gray, ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_REGULAR, 20)));
        final AttributeSet attrNumber = getAttributeSet(cont, new Color(0x0077cc), Color.yellow, ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_REGULAR, 20)));

        // Clear all styles first
        doc.setCharacterAttributes(0, text.length(), attrText, true);
        addBlockQuote(text);
        System.out.println("\n\nKeys: ");
        // Apply keyword highlighting
        for (String keyword : keyWords) {
            highlightPattern(text, "\\b" + keyword + "\\b", attrKeys);
        }
        System.out.println("\n\nNumbers: ");
        // Apply number highlighting
        highlightPattern(text, "\\b\\d+\\b", attrNumber);

        System.out.println("\n\nStrings: ");
        // Apply string highlighting (for strings enclosed in quotes)
        highlightPattern(text, "\".*?\"", attrString);
    }

    private void addBlockQuote(String text) {
        try {
            Pattern p = Pattern.compile("\n>([^\n]*\n)+\n");//"\n>([^\n]*\n)+\n"

            Matcher m = p.matcher(text);

            while (m.find()) {
                int start = m.start();
                int end = m.end();

                String blockText = text.substring(start+2, end-2);
                JPanel quotedBlock = new JPanel(new BorderLayout());
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = constraints.gridy = 0;
                constraints.weightx = 1;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                JTextArea area = new JTextArea(blockText);
                quotedBlock.add(area);
                doc.remove(start+1, text.substring(start+2, end-1).length());
                textPane.setCaretPosition(start+1);
                textPane.insertComponent(quotedBlock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertImages() {
        try {
            String text = textPane.getText();
            Pattern p = Pattern.compile("<img>\\s*\\t*[a-zA-Z0-9_]+\\s*\\t*</img>");
            Matcher m = p.matcher(text);

            while (m.find()) {
                int start = m.start();
                int end = m.end();

                String tagText = text.substring(start, end);
                String imageFile = tagText.replace("<img>", "")
                                     .replace("</img>", "").trim();

                System.out.println("text removed: " + text.substring(start, end));
                doc.remove(start, tagText.length());
                insertImage(imageFile, start);

                text = textPane.getText();
                m = p.matcher(text);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to apply the highlight for a regex pattern
    private void highlightPattern(String text, String pattern, AttributeSet attr) {
        try {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(text);

            while (m.find()) {
                int start = m.start();
                int end = m.end();
                doc.setCharacterAttributes(start, end - start, attr, false);
                System.out.println(text.substring(start, end));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AttributeSet getAttributeSet(StyleContext cont, Color fgColor, Color bgColor, Font font) {
        AttributeSet aset = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, fgColor);
        if(bgColor != null)
            aset = cont.addAttribute(aset, StyleConstants.Background, bgColor);
        return cont.addAttribute(aset, StyleConstants.FontFamily, font.getFamily());
    }

    public static void main(String[] args) {
        new Test();
    }
}



