package CustomControls.CustomListenerPackage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

/**
 * Only supports max input limit, default min input limit is 0
 */

public class NumberDocumentListener extends CustomDocumentListener {

    private final int maxInput;

    public NumberDocumentListener(int maxInput) {
        this.maxInput = maxInput;
    }

    protected boolean validateTextInput(DocumentEvent e) throws BadLocationException {
        String text = e.getDocument().getText(0, e.getDocument().getLength());
        boolean isDeleted = false;
        if (!text.matches("[0-9]+") || Integer.parseInt(text) > maxInput) {
            SwingUtilities.invokeLater(() -> {
                int lastCaretPosition = textComponent.getCaretPosition();

                System.out.print("Removed: " + text);
                String newText = text.substring(0, lastCaretPosition - 1) + text.substring(lastCaretPosition);

                if (newText.isEmpty()) {
                    newText = "0";
                    lastCaretPosition++;
                }
                System.out.println(", New: " + newText);

                textComponent.setText(newText);
                textComponent.setCaretPosition(lastCaretPosition - 1);
            });
            isDeleted = true;
        }

        SwingUtilities.invokeLater(() -> {
            if (text.length() > 1 && text.startsWith("0")) {
                System.out.println("Starts with 0: " + text + ", New: " + text.replaceFirst("0", ""));

                try {
                    if (!validateTextInput(e)) {
                        textComponent.setText(text.substring(1));
                    }
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return isDeleted;
    }

    public void removeUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            if (textComponent.getText().trim().isEmpty())
                textComponent.setText("0");
        });
        super.removeUpdate(e);
    }
}
