package BoardStructurePackage;

public interface Panel {

    void init(MainFrame main, Frame frame);

    void refresh();

    void addPanel(String panelName, Panel panel, Object constraints);

    void removePanel(String panelName);

    void setPanelVisibility(String panelName, boolean isVisible);

    boolean getPanelVisibility(String panelName);

    Panel getPanel(String panelName);

    BoardPanel getBoardPanel();

    // add components and their getters

}
