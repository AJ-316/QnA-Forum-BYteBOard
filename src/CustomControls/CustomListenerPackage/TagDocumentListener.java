package CustomControls.CustomListenerPackage;

import javax.swing.*;

public class TagDocumentListener extends CustomDocumentListener {
    protected boolean validateTextInput(String text) {
        if (!text.matches("^[a-zA-Z0-9]+$")) {
            SwingUtilities.invokeLater(() -> {
                int lastCaretPosition = textComponent.getCaretPosition();
                String newText = text.substring(0, lastCaretPosition - 1) + text.substring(lastCaretPosition);
                textComponent.setText(newText);
                textComponent.setCaretPosition(lastCaretPosition - 1);
            });
            return true;
        }

        return false;
    }
}
