package BoardEventListeners;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

public class TagListener extends CustomDocumentListener {

    private final boolean isTagField;

    public TagListener(boolean isTagField) {
        this.isTagField = isTagField;
    }

    protected boolean validateTextInput(DocumentEvent e) throws BadLocationException {
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
        return false;
    }

    private String getFilteredText(String newText) {
        String filteredText;

        if (isTagField) {
            filteredText = convertToCamelCase(newText.replaceAll("[^a-zA-Z0-9]", ""));
        } else {

            if (!newText.isEmpty() && newText.charAt(0) == '#') {
                // convert to camel case if spaces present (allow spaces for camel case)
                String content = newText.substring(1).replaceAll("[^a-zA-Z0-9 ]", "");
                filteredText = "#" + convertToCamelCase(content);
            } else
                filteredText = newText;
        }
        return filteredText;
    }

    private String convertToCamelCase(String input) {

        StringBuilder camelCase = new StringBuilder();
        boolean nextUpperCase = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == ' ') {
                nextUpperCase = true; // capitalize the next letter
            } else {
                if (nextUpperCase) {
                    camelCase.append(Character.toUpperCase(c)); // capitalize letter after space
                    nextUpperCase = false;
                } else {
                    camelCase.append(i == 0 ? Character.toLowerCase(c) : c); // lowercase for the first char; normal for the rest
                }
            }
        }
        return camelCase.toString();
    }
}
