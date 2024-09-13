import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Test2 extends JFrame {

    private JTextPane textPane;
    private DefaultStyledDocument doc;

    public Test2() {
        ResourceManager.init();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        doc = new DefaultStyledDocument();
        textPane = new JTextPane(doc);
        textPane.setBackground(new Color(0x1e1f22));
        textPane.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Insert initial text
        insertStyledText("This is a text before the image.\n", Color.white, 20);

        System.out.println(textPane.getCaretPosition());
        // Insert image in the text pane
        insertImage("search");

        // Insert more text after the image
        insertStyledText("\nThis is a text after the image.\n", Color.white, 20);

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to insert styled text
    private void insertStyledText(String text, Color color, int fontSize) {
        StyleContext cont = StyleContext.getDefaultStyleContext();
        AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, color);
        attr = cont.addAttribute(attr, StyleConstants.FontSize, fontSize);

        try {
            doc.insertString(doc.getLength(), text, attr);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Method to insert an image as an icon
    private void insertImage(String imagePath) {
        // Create an attribute set for the icon
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet attr = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.IconAttribute, ResourceManager.getIcon(imagePath, 32));
        textPane.setCaretPosition(doc.getLength());
        textPane.insertIcon(ResourceManager.getIcon(imagePath, 32));
        // Insert the icon at the current caret position (or doc end)
        try {
            doc.insertString(doc.getLength(), " ", attr); // Insert a space for the image
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Test2();
    }
}
