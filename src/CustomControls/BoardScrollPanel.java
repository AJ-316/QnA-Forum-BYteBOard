package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import Resources.ResourceManager;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BoardScrollPanel extends BoardPanel {

    protected final SimpleScrollPane scrollPane;

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
        scrollPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension viewSize = scrollPane.getViewport().getPreferredSize();
                viewSize.width = width != 0 ? width : viewSize.width;
                viewSize.height = height != 0 ? height : viewSize.height;
                scrollPane.getViewport().setPreferredSize(viewSize);
                scrollPane.setPreferredSize(viewSize);
                revalidate();
            }
        });
    }

    public void setBackground(String bg) {
        super.setBackground(ResourceManager.getColor(bg));
        scrollPane.getViewport().setBackground(getBackground());
    }

    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (scrollPane != null)
            scrollPane.getViewport().setBackground(bg);
    }

    public void setVerticalUnitIncrement(int i) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(i);
    }

    public void setHorizontalUnitIncrement(int i) {
        scrollPane.getHorizontalScrollBar().setUnitIncrement(i);
    }

    public SimpleScrollPane getComponent() {
        return scrollPane;
    }
}
