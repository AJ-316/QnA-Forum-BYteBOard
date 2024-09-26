package CustomControls.CustomListenerPackage;

import CustomControls.BoardComboBox;
import CustomControls.BoardTextField;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SearchFieldListener implements KeyListener {

    private final Runnable onKeySelectListener;

    private final ValidatedInsertListener validatedInsertListener;
    private final BoardTextField searchTextField;
    private final BoardComboBox searchComboBox;
    private String typingText;
    private boolean lastTopSelected;

    public SearchFieldListener(BoardComboBox searchComboBox, ValidatedInsertListener validatedInsertListener,
                               Runnable onKeySelectListener) {
        this.searchComboBox = searchComboBox;
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
        EventQueue.invokeLater(() -> {
            validatedInsertListener.invoke(searchTextField.getText());
            typingText = searchTextField.getText();
        });
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
            onKeySelectListener.run();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (lastTopSelected) {
                lastTopSelected = false;
                searchTextField.setText(typingText);
            }
            if (searchComboBox.getSelectedIndex() == 0)
                lastTopSelected = true;
        } else
            lastTopSelected = false;

        EventQueue.invokeLater(() -> {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (searchComboBox.getItemCount() == 1) {
                    searchTextField.setText(searchComboBox.getItemAt(0));
                }
            }
        });
    }
}
