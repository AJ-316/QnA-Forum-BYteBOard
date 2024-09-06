package BoardStructurePackage;

import CustomControls.CustomJPanel;

import javax.swing.*;

public abstract class Board extends CustomJPanel {

    private final MainFrame main;

    public Board(MainFrame main) {
        this.main = main;
    }

    public void requestSwitchFrame(String frameName, String switchContext) {
        main.setBoardFrame(frameName, switchContext);
    }

    public void addInsets(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public void addInsets(int inset) {
        addInsets(inset, inset, inset, inset);
    }

}
