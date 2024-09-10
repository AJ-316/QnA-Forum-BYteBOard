package BYteBOardInterface.StructurePackage;

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
        frame.getBoardFrame().refresh();
    }

    public void addPanel(Class<?> panelClass, Panel panel, Object constraints) {
        frame.getBoardFrame().addPanel(panelClass, panel);
        add(panel.getBoardPanel(), constraints);
    }

    public void addPanel(Class<?> panelClass, Panel panel) {
        addPanel(panelClass, panel, null);
    }

    public void setPanelVisibility(Class<?> panelClass, boolean isVisible) {
        frame.getBoardFrame().setPanelVisibility(panelClass, isVisible);
    }

    public boolean getPanelVisibility(Class<?> panelClass) {
        return frame.getBoardFrame().getPanelVisibility(panelClass);
    }

    public BoardPanel getBoardPanel() {
        return this;
    }

    public void setBackground(String bg) {
        super.setBackground(ResourceManager.getColor(bg));
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
