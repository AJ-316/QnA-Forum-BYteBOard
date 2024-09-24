package CustomControls.CustomListenerPackage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public abstract class CustomDocumentListener implements DocumentListener {

    protected JTextComponent textComponent;
    protected ValidatedInsertListener validatedInsertListener;

    public void setTextComponent(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    public void setValidatedInsertListener(ValidatedInsertListener validatedInsertListener) {
        this.validatedInsertListener = validatedInsertListener;
    }

    protected abstract boolean validateTextInput(String text) throws StringIndexOutOfBoundsException;

    public void insertUpdate(DocumentEvent e) {
        try {
            String inputText = e.getDocument().getText(0, e.getDocument().getLength());
            validateTextInput(inputText);
            updateValidatedInsertListener();
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void removeUpdate(DocumentEvent e) { updateValidatedInsertListener(); }
    public void changedUpdate(DocumentEvent e) {}

    private void updateValidatedInsertListener() {
        if(validatedInsertListener == null) return;

        SwingUtilities.invokeLater(() -> validatedInsertListener.invoke(textComponent.getText()));
    }
}
