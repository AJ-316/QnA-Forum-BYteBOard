package BoardStructurePackage;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class BoardFrame extends Board implements Frame {

    private final Map<String, Panel> boardPanels;
    private final BoardFrameSwitchDelegate frameSwitchDelegate;

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

    public final void addPanel(String panelName, Panel panel) {
        boardPanels.put(panelName, panel);
    }

    public final void addPanel(String panelName, Panel panel, Object constraints) {
        addPanel(panelName, panel);
        add(panel.getBoardPanel(), constraints);
    }

    public final Panel removePanel(String panelName) {
        Panel panel = boardPanels.get(panelName);
        if(panel == null) return null;

        boardPanels.remove(panelName);
        remove(panel.getBoardPanel());

        return panel;
    }

    public final Panel getPanel(String panelName) {
        return boardPanels.get(panelName);
    }

    public final void setPanelVisibility(String panelName, boolean isVisible) {
        Panel panel = boardPanels.get(panelName);
        if(panel == null) return;

        panel.getBoardPanel().setVisible(isVisible);
    }

    public void init(MainFrame main) {}

    public final void refresh() {
        frameSwitchDelegate.retrieveData(null);
        applyFrameSwitchContext(frameSwitchDelegate);
    }

    public final void setContext(String context) {
        frameSwitchDelegate.retrieveData(context);
        applyFrameSwitchContext(frameSwitchDelegate);
    }

    public abstract void applyFrameSwitchContext(BoardFrameSwitchDelegate frameSwitchDelegate);

    public final BoardFrame getBoardFrame() {
        return this;
    }
}
