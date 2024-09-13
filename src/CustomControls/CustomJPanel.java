package CustomControls;

import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author AJ
 */
public class CustomJPanel extends JPanel {

    public static final int NONE = 0;
    public static final int DROP_SHADOW = 1;
    public static final int OFFSET_SHADOW = 2;

    private Color borderColor;
    private int cornerRadius;
    private int shadowState;
    private boolean isRounded;
    private boolean limitRadius;

    public CustomJPanel() {
        setOpaque(false);
        isRounded = true;
        limitRadius = true;
        shadowState = NONE;
        cornerRadius = 20;
    }

    protected void paintComponent(Graphics g) {
        if(!isRounded) {
            super.paintComponent(g);
            return;
        }

        int actualCornerRadius = cornerRadius;

        if (limitRadius)
            actualCornerRadius = Math.min(cornerRadius, Math.min(getWidth(), getHeight()) / 2);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(new Color(0, 0, 0, 25));

        int w = getWidth() - 1;
        int h = getHeight() - 1;

        int i = 0;
        int offset = 5;
        int size = 5;

        if (shadowState == DROP_SHADOW) offset = 0;
        else if (shadowState == NONE) size = 0;

        for (;i < size; i++) {
            g2d.fillRoundRect(i + offset, i + offset, w - i*2 - offset, h - i*2 - offset, actualCornerRadius, actualCornerRadius);
        }

        g2d.setColor(getBackground());
        g2d.fillRoundRect(i, i, w - i*2, h - i*2, actualCornerRadius, actualCornerRadius);

        if (borderColor != null) {
            g2d.setColor(borderColor);
            g2d.drawRoundRect(i, i, w - i*2, h - i*2, actualCornerRadius, actualCornerRadius);
        }

        super.paintComponent(g2d);
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public int getActualCornerRadius() {
        if (limitRadius)
            return Math.min(cornerRadius, Math.min(getWidth(), getHeight()) / 2);

        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        repaint();
    }

    public boolean isLimitRadius() {
        return limitRadius;
    }

    public void setLimitRadius(boolean limitRadius) {
        this.limitRadius = limitRadius;
    }

    public void setShadowState(int shadowState) {
        this.shadowState = shadowState;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = ResourceManager.getColor(borderColor);
    }

    public void setTransparent(boolean isTransparent) {
        isRounded = !isTransparent;
    }
}
