package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import Resources.ResourceManager;

import java.awt.*;

public class BoardScrollPanel extends BoardPanel {

    private final SimpleScrollPane scrollPane;

    public BoardScrollPanel(MainFrame main, Frame frame) {
        super(main, frame);
        scrollPane = new SimpleScrollPane(this);
        setVerticalUnitIncrement(15);
    }

    public Component add(Component comp) {
        Component c = super.add(comp);
        scrollPane.resetScroll();
        return c;
    }

    public Component add(Component comp, int index) {
        Component c = super.add(comp, index);
        scrollPane.resetScroll();
        return c;
    }

    public void add(Component comp, Object constraints) {
        super.add(comp, constraints);
        scrollPane.resetScroll();
    }

    public void setScrollMinSize(int width, int height) {
        scrollPane.getViewport().setMinimumSize(new Dimension(width, height));
    }

    public void setScrollSize(int width, int height) {
        scrollPane.getViewport().setMaximumSize(new Dimension(width, height));
    }

    public void setBackground(String bg) {
        super.setBackground(ResourceManager.getColor(bg));
        scrollPane.getViewport().setBackground(getBackground());
    }

    public void setBackground(Color bg) {
        super.setBackground(bg);
        if(scrollPane != null)
            scrollPane.getViewport().setBackground(bg);
    }

    public void setVerticalUnitIncrement(int i) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(i);
    }

    public SimpleScrollPane getComponent() {
        return scrollPane;
    }
}
