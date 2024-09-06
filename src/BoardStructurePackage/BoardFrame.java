package BoardStructurePackage;

import java.util.HashMap;
import java.util.Map;

public class BoardFrame extends Board implements Frame {

    private final Map<String, Panel> boardPanels;
    private final BoardFrameSwitchContext frameSwitchContext;

    public BoardFrame(MainFrame main, BoardFrameSwitchContext.DataRetriever dataRetriever) {
        super(main);

        frameSwitchContext = new BoardFrameSwitchContext(dataRetriever);
        boardPanels = new HashMap<>();

        setTransparent(true);
        init(main);
        main.addBoardFrame(this);
    }

    public void refresh() {
        frameSwitchContext.retrieveData(null);
    }

    public void addPanel(String panelName, Panel panel) {
        boardPanels.put(panelName, panel);
    }

    public void addPanel(String panelName, Panel panel, Object constraints) {
        addPanel(panelName, panel);
        add(panel.getBoardPanel(), constraints);
    }

    public Panel removePanel(String panelName) {
        Panel panel = boardPanels.get(panelName);
        if(panel == null) return null;

        boardPanels.remove(panelName);
        remove(panel.getBoardPanel());

        return panel;
    }

    public Panel getPanel(String panelName) {
        return boardPanels.get(panelName);
    }

    public void setPanelVisibility(String panelName, boolean isVisible) {
        Panel panel = boardPanels.get(panelName);
        if(panel == null) return;

        panel.getBoardPanel().setVisible(isVisible);
    }

    public void init(MainFrame main) {}

    public void setContext(String context) {
        frameSwitchContext.retrieveData(context);
    }

    public BoardFrame getBoardFrame() {
        return this;
    }
}
