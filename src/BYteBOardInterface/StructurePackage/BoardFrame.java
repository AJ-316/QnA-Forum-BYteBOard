package BYteBOardInterface.StructurePackage;

import CustomControls.DEBUG;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class BoardFrame extends Board implements Frame {

    private final Map<String, Panel> boardPanels;
    private final BoardFrameSwitchDelegate frameSwitchDelegate;
    private String recoverContext;

    public BoardFrame(MainFrame main, BoardFrameSwitchDelegate.DataRetriever dataRetriever) {
        super(main);

        frameSwitchDelegate = new BoardFrameSwitchDelegate(dataRetriever);
        boardPanels = new HashMap<>();

        addInsets(10);
        setTransparent(true);
        setLayout(new BorderLayout());

        init(main);

        main.addBoardFrame(this);
    }

    public final void addPanel(Class<?> panelClass, Panel panel) {
        boardPanels.put(panelClass.getSimpleName(), panel);
    }

    public final void addPanel(Class<?> panelClass, Panel panel, Object constraints) {
        addPanel(panelClass, panel);
        add(panel.getBoardPanel(), constraints);
    }

    public final Panel getPanel(Class<?> panelClass) {
        return boardPanels.get(panelClass.getSimpleName());
    }

    public final void setPanelVisibility(Class<?> panelClass, boolean isVisible) {
        Panel panel = getPanel(panelClass);
        if(panel == null) return;

        panel.getBoardPanel().setVisible(isVisible);
    }

    public boolean getPanelVisibility(Class<?> panelClass) {
        Panel panel = getPanel(panelClass);
        if(panel == null) return false;

        return panel.getBoardPanel().isVisible();
    }

    public final void removePanel(String panelName) {
        Panel panel = boardPanels.get(panelName);
        if(panel == null) return;

        boardPanels.remove(panelName);
        remove(panel.getBoardPanel());
    }

    public final void removePanel(Component panel) {
        remove(panel);
        for (String panelName : boardPanels.keySet()) {
            if(boardPanels.get(panelName).equals(panel)) {
                boardPanels.remove(panelName);
                return;
            }
        }
    }

    public void init(MainFrame main) {}

    public final void refresh() {
        recoverContext = frameSwitchDelegate.retrieveData();
        applyFrameSwitchContext(frameSwitchDelegate);
    }

    public final void setContext(String... context) {
        recoverContext = frameSwitchDelegate.retrieveData(context);
        applyFrameSwitchContext(frameSwitchDelegate);
    }

    public String recoverContext() {
        return recoverContext;
    }

    protected BoardFrameSwitchDelegate requestFrameSwitchDelegate() {
        return frameSwitchDelegate;
    }

    public final BoardFrame getBoardFrame() {
        return this;
    }
}
