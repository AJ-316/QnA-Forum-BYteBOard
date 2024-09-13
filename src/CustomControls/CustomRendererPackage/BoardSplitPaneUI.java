package CustomControls.CustomRendererPackage;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class BoardSplitPaneUI extends BasicSplitPaneUI {

    @Override
    protected void dragDividerTo(int location) {
        super.dragDividerTo(location);
        splitPane.setDividerLocation(location);
        splitPane.revalidate();
        splitPane.repaint();
    }

    @Override
    protected void finishDraggingTo(int location) {
        super.finishDraggingTo(location);
    }

    @Override
    public void finishedPaintingChildren(JSplitPane sp, Graphics g) {
        if (sp == splitPane && getLastDragLocation() != -1 && !isContinuousLayout() && !draggingHW) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Dimension size = splitPane.getSize();
            // Paint the divider background
            g2d.setColor(Color.BLUE); // Color when dragging
            int padding = 10;
            int arc = (getDivider().getDividerSize() - padding) / 2;
            g2d.fillRoundRect(padding, getLastDragLocation() + padding, size.width - 2 * padding, dividerSize - 2 * padding, arc, arc);
            g2d.dispose();

            splitPane.revalidate();
            splitPane.repaint();
        }
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new BoardSplitPaneDivider(this);
    }

    private static class BoardSplitPaneDivider extends BasicSplitPaneDivider {

        public BoardSplitPaneDivider(BasicSplitPaneUI ui) {
            super(ui);
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.GRAY);

            int padding = 10;
            int arc = (getDividerSize() - padding) / 2;
            g2d.fillRoundRect(padding, padding, getWidth() - 2 * padding, getHeight() - 2 * padding, arc, arc);
            g2d.dispose();
        }
    }
}