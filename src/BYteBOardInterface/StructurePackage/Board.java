package BYteBOardInterface.StructurePackage;

import CustomControls.CustomJPanel;

import javax.swing.*;

public abstract class Board extends CustomJPanel {

    private final MainFrame main;

    public Board(MainFrame main) {
        this.main = main;
    }

    public void requestSwitchFrame(Class<?> frameClass, String... switchContext) {
        main.setBoardFrame(frameClass, switchContext);
    }

    public void addInsets(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public abstract void refresh();

    protected abstract void addPanel(Class<?> panelClass, Panel panel, Object constraints);

    protected abstract void addPanel(Class<?> panelClass, Panel panel);

    protected abstract void setPanelVisibility(Class<?> panelClass, boolean isVisible);

    protected abstract boolean getPanelVisibility(Class<?> panelClass);

    public void addInsets(int inset) {
        addInsets(inset, inset, inset, inset);
    }

    protected void requestSwitchMainFrame(Class<?> mainFrameClass, String... switchContext) {
        MainFrame.switchMainFrame(mainFrameClass, switchContext);
    }

    public void restartMainFrame() {
        main.restartMainFrame();
    }

    public MainFrame getMain() {
        return main;
    }
}
