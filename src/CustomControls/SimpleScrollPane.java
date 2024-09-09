package CustomControls;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class SimpleScrollPane extends JScrollPane {

    private static final int SCROLL_BAR_ALPHA_ROLLOVER = 200;
    private static final int SCROLL_BAR_ALPHA = 100;
    private static final int THUMB_SIZE = 8;
    private static final int SB_SIZE = 10;
    private static final Color THUMB_COLOR = Color.white;

    public SimpleScrollPane() {
        this(null);
    }

    public SimpleScrollPane(Component view) {

        setBorder(null);

        // Set ScrollBar UI
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new ModernScrollBarUI(this));

        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setOpaque(false);
        horizontalScrollBar.setUI(new ModernScrollBarUI(this));

        setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                Rectangle availR = parent.getBounds();
                availR.x = availR.y = 0;

                // viewport
                Insets insets = parent.getInsets();
                availR.x = insets.left;
                availR.y = insets.top;
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;
                if (viewport != null) {
                    viewport.setBounds(availR);
                }

                boolean vsbNeeded = isVerticalScrollBarfNecessary();
                boolean hsbNeeded = isHorizontalScrollBarNecessary();

                // vertical scroll bar
                Rectangle vsbR = new Rectangle();
                vsbR.width = SB_SIZE;
                vsbR.height = availR.height - (hsbNeeded ? vsbR.width : 0);
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;
                if (vsb != null) {
                    vsb.setBounds(vsbR);
                }

                // horizontal scroll bar
                Rectangle hsbR = new Rectangle();
                hsbR.height = SB_SIZE;
                hsbR.width = availR.width - (vsbNeeded ? hsbR.height : 0);
                hsbR.x = availR.x;
                hsbR.y = availR.y + availR.height - hsbR.height;
                if (hsb != null) {
                    hsb.setBounds(hsbR);
                }
            }
        });

        // Layering
        setComponentZOrder(getVerticalScrollBar(), 0);
        setComponentZOrder(getHorizontalScrollBar(), 1);
        setComponentZOrder(getViewport(), 2);

        viewport.setView(view);
    }
    private boolean isVerticalScrollBarfNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getHeight() > viewRect.getHeight();
    }

    private boolean isHorizontalScrollBarNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getWidth() > viewRect.getWidth();
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {

        private final JScrollPane sp;

        public ModernScrollBarUI(SimpleScrollPane sp) {
            this.sp = sp;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            boolean isVertical = scrollbar.getOrientation() == JScrollBar.VERTICAL;

            BoundedRangeModel model = scrollbar.getModel();
            boolean scrollbarNecessary = model.getMaximum() - model.getMinimum() > model.getExtent();

            if (scrollbarNecessary) {
                int alpha = isDragging ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
                Graphics2D graphics2D = (Graphics2D) g;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));

                if (isVertical) {
                    graphics2D.fillRect(
                            trackBounds.x + (trackBounds.width / 2) - 1 + 1,
                            trackBounds.y,
                            2,  // Thin width
                            trackBounds.height
                    );
                } else {
                    graphics2D.fillRect(
                            trackBounds.x,
                            trackBounds.y + (trackBounds.height / 2) - 1,
                            trackBounds.width,
                            2
                    );
                }
            }
        }


        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            int alpha = isDragging ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
            int orientation = scrollbar.getOrientation();
            int x = thumbBounds.x + 2;
            int y = thumbBounds.y;

            int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
            width = Math.max(width, THUMB_SIZE) + 4;

            int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
            height = Math.max(height, THUMB_SIZE);

            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));

            graphics2D.fillRect(x, y, width/2 - 3, height);
            graphics2D.fillRect(x + width/2 - 1, y, width/2, height);
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            sp.repaint();
        }

        private static class InvisibleScrollBarButton extends JButton {

            private static final long serialVersionUID = 1552427919226628689L;

            private InvisibleScrollBarButton() {
                setOpaque(false);
                setFocusable(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }

}