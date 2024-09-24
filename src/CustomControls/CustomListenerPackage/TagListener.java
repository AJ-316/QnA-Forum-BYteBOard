package CustomControls.CustomListenerPackage;

import javax.swing.*;

public class TagListener extends CustomDocumentListener{

    protected boolean validateTextInput(String text) {
        if(!text.startsWith("#") || text.length() < 2) return false;

        String tagText = text.substring(1);

        if (!tagText.matches("^[a-zA-Z0-9]+$")) {
            SwingUtilities.invokeLater(() -> {
                int lastCaretPosition = textComponent.getCaretPosition() - 1;

                if (lastCaretPosition - 1 == -1) {
                    textComponent.setText("#");
                    return;
                }

                String newText = "#" + tagText.substring(0, lastCaretPosition - 1) + tagText.substring(lastCaretPosition);
                System.out.println("Current: " + textComponent.getText() + ", Old: " + tagText + ", New: " + newText);
                textComponent.setText(newText);
                textComponent.setCaretPosition(lastCaretPosition);
                textComponent.repaint();
            });
        }
        return false;
    }
}
