import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TextPaneExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JTextPane Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            // Create a JTextPane
            JTextPane textPane = new JTextPane();
            textPane.setEditable(false); // Optional: Make text pane read-only

            // Get the styled document
            StyledDocument doc = textPane.getStyledDocument();

            // Add some styled text
            addStyledText(doc, "Bold and Blue Text\n", true, false, 16, Color.BLUE);
            addStyledText(doc, "Italic and Red Text\n", false, true, 14, Color.RED);
            addStyledText(doc, "Normal Black Text\n", false, false, 12, Color.BLACK);

            // Add the JTextPane to a JScrollPane
            JScrollPane scrollPane = new JScrollPane(textPane);

            // Add the scroll pane to the frame
            frame.add(scrollPane);
            frame.setVisible(true);
        });
    }

    // Utility method to add styled text to the document
    private static void addStyledText(StyledDocument doc, String text, boolean isBold, boolean isItalic, int fontSize, Color color) {
        // Create a new style
        Style style = doc.addStyle("CustomStyle", null);

        // Set the font size, bold, italic, and color attributes
        StyleConstants.setBold(style, isBold);
        StyleConstants.setItalic(style, isItalic);
        StyleConstants.setFontSize(style, fontSize);
        StyleConstants.setForeground(style, color);

        try {
            // Insert the text with the specified style into the document
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
