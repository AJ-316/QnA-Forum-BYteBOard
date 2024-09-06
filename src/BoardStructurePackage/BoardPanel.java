package BoardStructurePackage;

import Resources.ResourceManager;

import javax.swing.*;

public class BoardPanel extends Board implements Panel {

    private final Frame frame;

    public BoardPanel(MainFrame main, Frame frame) {
        this(main, frame, null);
        setTransparent(true);
    }

    public BoardPanel(MainFrame main, Frame frame, String bgColor) {
        super(main);
        this.frame = frame;
        if(bgColor != null)
            setBackground(ResourceManager.getColor(bgColor));
        init(main);
    }

    public void init(MainFrame main) {}

    public void refresh() {
        frame.refresh();
    }

    public void addPanel(String panelName, Panel panel, Object constraints) {
        frame.addPanel(panelName, panel);
        add(panel.getBoardPanel(), constraints);
    }

    public void removePanel(String panelName) {
        Panel panel = frame.removePanel(panelName);
        if(panel == null) return;

        remove(panel.getBoardPanel());
    }

    public void setPanelVisibility(String panelName, boolean isVisible) {
        frame.setPanelVisibility(panelName, isVisible);
    }

    public Panel getPanel(String panelName) {
        return frame.getPanel(panelName);
    }

    public BoardPanel getBoardPanel() {
        return this;
    }
}
