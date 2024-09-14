package CustomControls.CustomRendererPackage;

import Resources.ResourceManager;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {

    private final Insets insets;
    private final int arcWidth;
    private final int arcHeight;
    private Color bgColor;
    private Color borderColor;

    public RoundedBorder(int arcWidth, int arcHeight, int insets, Color bgColor, Color borderColor) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.bgColor = bgColor;
        this.borderColor = borderColor;
        this.insets = new Insets(insets, insets, insets, insets);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(bgColor);
        g2.fillRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);

        if(borderColor != null) {
            g2.setPaint(borderColor);
            g2.drawRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);
        }
        g2.dispose();
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(insets.top, insets.left, insets.bottom, insets.right);
        return insets;
    }
}
