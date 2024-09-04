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
    private int cornerRadius;
    private boolean limitRadius;
    private int shadowState;
    private Color borderColor;

    public CustomJPanel() {
        initComponents();
        limitRadius = true;
        shadowState = DROP_SHADOW;
        cornerRadius = 20;
        borderColor = ResourceManager.getColor(ByteBoardTheme.BASE);
        setForeground(borderColor);
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
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

        g2d.setColor(new Color(0, 0, 0, 15));

        int w = getWidth() - 1;
        int h = getHeight() - 1;

        int i = 0;
        int offset = 5;
        int size = 5;

        if (shadowState == DROP_SHADOW) offset = 0;
        else size = 0;

        for (;i < size; i++) {
            g2d.fillRoundRect(i + offset, i + offset, w - i*2 - offset, h - i*2 - offset, actualCornerRadius, actualCornerRadius);
        }

        g2d.setColor(getBackground());
        g2d.fillRoundRect(i, i, w - i*2, h - i*2, actualCornerRadius, actualCornerRadius);

        //g2d.setColor(borderColor);
        //g2d.drawRoundRect(i, i, w - i*2, h - i*2, actualCornerRadius, actualCornerRadius);

        super.paintComponent(g2d);
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
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

    private void initComponents() {

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
    }
}
