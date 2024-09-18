package CustomControls.CustomListenerPackage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public abstract class CustomDocumentListener implements DocumentListener {

    protected JTextComponent textComponent;

    public CustomDocumentListener setTextComponent(JTextComponent textComponent) {
        this.textComponent = textComponent;
        return this;
    }

    protected abstract boolean validateTextInput(String text) throws StringIndexOutOfBoundsException;

    public void insertUpdate(DocumentEvent e) {
        try {
            String inputText = e.getDocument().getText(0, e.getDocument().getLength());
            validateTextInput(inputText);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void removeUpdate(DocumentEvent e) {}
    public void changedUpdate(DocumentEvent e) {}
}
