package CustomControls.CustomListenerPackage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

/**
 * Only supports max input limit, default min input limit is 0
 * */

public class NumberDocumentListener extends CustomDocumentListener {

    private final int maxInput;

    public NumberDocumentListener(int maxInput) {
        this.maxInput = maxInput;
    }

    protected boolean validateTextInput(String text) {
        boolean isDeleted = false;
        if (!text.matches("[0-9]+") || Integer.parseInt(text) > maxInput) {
            SwingUtilities.invokeLater(() -> {
                int lastCaretPosition = textField.getCaretPosition();

                System.out.print("Removed: "+ text);
                String newText = text.substring(0, lastCaretPosition - 1) + text.substring(lastCaretPosition);

                if(newText.isEmpty()) {
                    newText = "0";
                    lastCaretPosition++;
                }
                System.out.println(", New: " + newText);

                textField.setText(newText);
                textField.setCaretPosition(lastCaretPosition - 1);
            });
            isDeleted = true;
        }

        SwingUtilities.invokeLater(() -> {
            if (text.length() > 1 && text.startsWith("0")) {
                System.out.println("Starts with 0: " + text + ", New: " + text.replaceFirst("0", ""));

                String newText = text.substring(1);
                if(!validateTextInput(newText)) {
                    textField.setText(newText);
                }
            }
        });

        return isDeleted;
    }

    public void removeUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            if (textField.getText().trim().isEmpty())
                textField.setText("0");
        });
    }
}
