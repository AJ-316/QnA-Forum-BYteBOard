package BoardStructurePackage;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;

public class BoardPanel extends Board implements Panel {

    private final Frame frame;

    public BoardPanel(MainFrame main, Frame frame) {
        this(main, frame, null);
        setTransparent(true);
    }

    public BoardPanel(MainFrame main, Frame frame, String bgColor) {
        super(main);
        this.frame = frame;
        if(bgColor != null) {
            setBackground(ResourceManager.getColor(bgColor));
            if(bgColor.equals(ByteBoardTheme.MAIN))
                setShadowState(OFFSET_SHADOW);
        }
        init(main, frame);
    }

    public void init(MainFrame main, Frame frame) {}

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

    public boolean getPanelVisibility(String panelName) {
        return ((BoardPanel) frame.getPanel(panelName)).isVisible();
    }

    public Panel getPanel(String panelName) {
        return frame.getPanel(panelName);
    }

    public BoardPanel getBoardPanel() {
        return this;
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setTransparent(false);
    }

    public Frame getFrame() {
        return frame;
    }
}
