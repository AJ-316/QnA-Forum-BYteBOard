package BoardStructurePackage;

public interface Panel {

    void init(MainFrame main);

    void refresh();

    void addPanel(String panelName, Panel panel, Object constraints);

    void removePanel(String panelName);

    void setPanelVisibility(String panelName, boolean isVisible);

    Panel getPanel(String panelName);

    BoardPanel getBoardPanel();

    // add components and their getters

}
