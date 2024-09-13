package CustomControls.CustomRendererPackage;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        if (sp == splitPane && getLastDragLocation() != -1) {

            BoardSplitPaneUI.drawDivider(g,
                    getDivider().getDividerSize(), getDivider().getWidth(), getDivider().getHeight(),
                    getLastDragLocation(), 6, ByteBoardTheme.ACCENT_DARK);

            splitPane.revalidate();
            splitPane.repaint();
        }
    }

    private static void drawDivider(Graphics g, int divideSize, int w, int h, int y, int padding, String bgColor) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = (divideSize - padding) / 2;
        int width = w - 2 * padding;
        int height = h - 2 * padding;

        g2d.setColor(ResourceManager.getColor(bgColor));
        g2d.fillRoundRect(padding, y + padding, width, height, arc, arc);

        arc = 8;
        width = w/2 - arc*(4 /2);
        height = h/2 - arc/2;
        g2d.setColor(ResourceManager.getColor(ByteBoardTheme.BASE));
        g2d.fillRoundRect(width, y + height, arc* 4, arc, arc, arc);
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new BoardSplitPaneDivider(this);
    }

    public static class BoardSplitPaneDivider extends BasicSplitPaneDivider {

        private String bgColor;

        public BoardSplitPaneDivider(BasicSplitPaneUI ui) {
            super(ui);
            setBorder(BorderFactory.createEmptyBorder());
            bgColor = ByteBoardTheme.MAIN_LIGHT;

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBgColor(ByteBoardTheme.ACCENT);
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    setBgColor(ByteBoardTheme.MAIN_LIGHT);
                    repaint();
                }
            });

        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        @Override
        public void paint(Graphics g) {
            BoardSplitPaneUI.drawDivider(g, getDividerSize(), getWidth(), getHeight(),
                    0, 8, bgColor);
        }
    }
}