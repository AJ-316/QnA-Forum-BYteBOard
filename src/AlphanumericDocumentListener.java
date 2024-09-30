import CustomControls.DEBUG;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class AlphanumericDocumentListener implements DocumentListener {
    private final boolean isTagField; // True for tag-only field, false for question/tag field

    // Constructor to set whether it's a tag-only field or question/tag field
    public AlphanumericDocumentListener(boolean isTagField) {
        this.isTagField = isTagField;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filterText(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // No need to filter on removal
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Plain text components do not fire these events
    }

    private void filterText(DocumentEvent e) {
        try {
            String newText = e.getDocument().getText(0, e.getDocument().getLength());
            String filteredText = getFilteredText(newText);

            if (!newText.equals(filteredText)) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        e.getDocument().remove(0, e.getDocument().getLength());
                        e.getDocument().insertString(0, filteredText, null);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private String getFilteredText(String newText) {
        String filteredText;

        if (isTagField) {
            // Tag field: Only allow alphanumeric characters
            filteredText = newText.replaceAll("[^a-zA-Z0-9]", "");
        } else {
            // Question/Tag field: First character can be a #, the rest must be alphanumeric if it starts with #
            /*if (!newText.isEmpty() && newText.charAt(0) == '#') {
                filteredText = "#" + newText.substring(1).replaceAll("[^a-zA-Z0-9]", "");
            } else {
                // No filtering for question input, allow any text
                filteredText = newText;
            }*/
            if (!newText.isEmpty() && newText.charAt(0) == '#') {
                // Convert to camel case if spaces are present after the #
                String content = newText.substring(1).replaceAll("[^a-zA-Z0-9 ]", "");  // Temporarily allow spaces for camel case
                filteredText = "#" + convertToCamelCase(content);
                DEBUG.printlnPurple("\"" + convertToCamelCase(content) + "\", \"" + content + "\"");
            } else {
                // No filtering for question input, allow any text
                filteredText = newText;
            }
        }
        return filteredText;
    }

    private String convertToCamelCase(String input) {
        StringBuilder camelCase = new StringBuilder();
        boolean nextUpperCase = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == ' ') {
                nextUpperCase = true;  // Set flag to capitalize the next letter
            } else {
                if (nextUpperCase) {
                    camelCase.append(Character.toUpperCase(c));  // Capitalize letter after space
                    nextUpperCase = false;
                } else {
                    camelCase.append(i == 0 ? Character.toLowerCase(c) : c);  // Lowercase for the first char, normal for the rest
                }
            }
        }
        return camelCase.toString();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tag and Question Fields");
        
        // Create a text field for Question/Tag input (can start with #)
        JTextField questionTagField = new JTextField(20);
        questionTagField.getDocument().addDocumentListener(new AlphanumericDocumentListener(false));

        // Create a text field for Tag input (only alphanumeric, no #)
        JTextField tagField = new JTextField(20);
        tagField.getDocument().addDocumentListener(new AlphanumericDocumentListener(true));

        // Set up the frame layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new JLabel("Question/Tag Field:"));
        frame.add(questionTagField);
        frame.add(new JLabel("Tag Field:"));
        frame.add(tagField);
        
        frame.pack();
        frame.setVisible(true);
    }
}
