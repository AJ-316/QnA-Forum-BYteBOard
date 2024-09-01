package CustomControls;

import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author AJ
 */
public class RoundedJPanel extends JPanel {

    private int cornerRadius;
    private boolean limitRadius;
    private Color borderColor;

    public RoundedJPanel() {
        initComponents();
        limitRadius = true;
        cornerRadius = 20;
        borderColor = ResourceManager.getColor("base");
        setForeground(borderColor);
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        int actualCornerRadius = cornerRadius;

        if (limitRadius)
            actualCornerRadius = Math.min(cornerRadius, Math.min(getWidth(), getHeight()) / 2);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), actualCornerRadius, actualCornerRadius);
        g2d.setColor(borderColor);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, actualCornerRadius, actualCornerRadius);

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
