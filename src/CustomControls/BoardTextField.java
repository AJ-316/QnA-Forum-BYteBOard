package CustomControls;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.CustomListenerPackage.CustomDocumentListener;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class BoardTextField extends JTextField implements CustomControl {

    private final BoardPanel container;
    private BoardLabel errorLabel;

    public BoardTextField(MainFrame main, Frame frame, String background, int cols) {
        addInsets(0);
        setColumns(cols);
        setMinimumSize(new Dimension(getPreferredSize().width, getPreferredSize().height + 10));

        container = new BoardPanel(main, frame, background);
        container.setCornerRadius(60);
        container.addInsets(10, 10, 10, 10);
        container.setLayout(new BorderLayout());
        container.add(this);

        setBackground(container.getBackground());

        if(!getBackground().equals(ResourceManager.getColor(ByteBoardTheme.BASE)))
            setFGLight();
    }

    public void setErrorLabel(String errorText) {
        if(errorLabel == null) {
            errorLabel = new BoardLabel();
            errorLabel.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 16);
            errorLabel.setFGError();
            errorLabel.setAlignmentLeading();
            container.add(errorLabel, BorderLayout.SOUTH);

            addActionListener(e-> errorLabel.setText(""));
        }

        errorLabel.setText(errorText.length() > 28 ? errorText.substring(0, 28) : errorText);
    }

    public BoardPanel getTextFieldContainer() {
        return container;
    }

    public void setFontPrimary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(type, size)));
    }

    public void setFontSecondary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_SECONDARY(type, size)));
    }

    public void setFGDark() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
    }

    public void setFGLight() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
    }

    public void setFGMain() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_MAIN));
    }

    public void addInsets(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public void addDocumentListener(CustomDocumentListener documentListener) {
        getDocument().addDocumentListener(documentListener);
        documentListener.setTextField(this);
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        super.setCaretColor(c);
    }

    /*public static void initTextField(JTextField textField, CustomDocumentListener documentListener) {
        textField.getDocument().addDocumentListener(documentListener);
        documentListener.setTextField(textField);

        if(textField.getParent() == null) return;

        textField.setBackground(textField.getParent().getBackground());

        if(!textField.getParent().getBackground().equals(ResourceManager.getColor(ByteBoardTheme.BASE)))
            textField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
    }*/
}
