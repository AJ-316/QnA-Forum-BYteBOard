package CustomControls.CustomRendererPackage;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {

    private final Insets insets = new Insets(12, 12, 12, 12);
    private final int arcWidth;
    private final int arcHeight;
    private Color bgColor;
    private Color borderColor;

    public RoundedBorder(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(bgColor);
        g2.fillRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);

        g2.setPaint(borderColor);
        g2.drawRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);
        g2.dispose();
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(insets.top, insets.left, insets.bottom, insets.right);
        return insets;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
}
