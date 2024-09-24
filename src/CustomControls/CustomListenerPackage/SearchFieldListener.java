package CustomControls.CustomListenerPackage;

import CustomControls.BoardComboBox;
import CustomControls.BoardTextField;

import java.awt.*;
import java.awt.event.*;

public class SearchFieldListener implements KeyListener {

    private final Runnable onKeySelectListener;

    private final ValidatedInsertListener validatedInsertListener;
    private final BoardTextField searchTextField;

    public SearchFieldListener(BoardComboBox searchComboBox, ValidatedInsertListener validatedInsertListener,
                               Runnable onKeySelectListener) {
        this.searchTextField = searchComboBox.getTextField();
        this.searchTextField.addKeyListener(this);
        this.validatedInsertListener = validatedInsertListener;
        this.onKeySelectListener = onKeySelectListener;
        searchTextField.addDocumentListener(new TagListener());
    }

    public static void create(BoardComboBox searchComboBox, ValidatedInsertListener validatedInsertListener,
                              Runnable onEnterListener) {
        new SearchFieldListener(searchComboBox, validatedInsertListener, onEnterListener);
    }

    public void keyTyped(KeyEvent e) {
        EventQueue.invokeLater(() -> validatedInsertListener.invoke(searchTextField.getText()));
    }

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
            onKeySelectListener.run();
        }
    }
}
