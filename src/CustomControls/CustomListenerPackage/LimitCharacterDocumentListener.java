package CustomControls.CustomListenerPackage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

public class LimitCharacterDocumentListener extends CustomDocumentListener {

    public static final int MAX_TEXT_LENGTH = 65535;
    private final CharacterLengthListener listener;
    private final int maxLength;

    public LimitCharacterDocumentListener(int maxLength, CharacterLengthListener listener) {
        this.maxLength = maxLength;
        this.listener = listener;
    }

    protected boolean validateTextInput(String text) {
        boolean isDeleted = false;
        if (text.length() > maxLength) {
            SwingUtilities.invokeLater(() -> {
                int lastCaretPosition = textComponent.getCaretPosition();

                String newText = text.substring(0, lastCaretPosition - 1) + text.substring(lastCaretPosition);

                textComponent.setText(newText);
                textComponent.setCaretPosition(lastCaretPosition - 1);
            });
            isDeleted = true;
        }

        updateListener();

        return isDeleted;
    }

    private void updateListener() {
        SwingUtilities.invokeLater(() -> {
            if(listener != null)
                listener.invoke(textComponent.getText().length());
        });
    }

    public void removeUpdate(DocumentEvent e) {
        updateListener();
    }

    public interface CharacterLengthListener {
        void invoke(int length);
    }
}
