package CustomControls.CustomListenerPackage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

public class TagDocumentListener extends CustomDocumentListener {
    protected boolean validateTextInput(String text) {
        if (!text.matches("^[a-zA-Z0-9]+$")) {
            SwingUtilities.invokeLater(() -> {
                int lastCaretPosition = textField.getCaretPosition();
                String newText = text.substring(0, lastCaretPosition - 1) + text.substring(lastCaretPosition);
                textField.setText(newText);
                textField.setCaretPosition(lastCaretPosition - 1);
            });
            return true;
        }

        return false;
    }
}
