package BoardStructurePackage;

public interface Frame {

    void init(MainFrame main);

    void refresh();

    void setContext(String context);

    void addPanel(String panelName, Panel panel, Object constraints);

    void addPanel(String panelName, Panel panel);

    Panel removePanel(String panelName);

    Panel getPanel(String panelName);

    void setPanelVisibility(String panelName, boolean isVisible);

    BoardFrame getBoardFrame();
}
