package CustomControls;

import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardButton extends JButton implements CustomControl {

    private Color defaultFGColor;
    private Color rolloverFGColor;
    private boolean isRounded;
    private int iconSize;

    public BoardButton() {
        this("", null, 0, 0, 0, 0);
    }

    // default state icons with size
    public BoardButton(String icon, int iconSize) {
        this("", icon, ResourceManager.DEFAULT, ResourceManager.PRESSED, ResourceManager.ROLLOVER, iconSize);
    }

    // default state icons
    public BoardButton(String text, String icon) {
        this(text, icon, ResourceManager.DEFAULT, ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.MINI);
    }

    public BoardButton(String text, String icon, int defaultState, int pressedState, int rolloverState, int iconSize) {
        super(text);
        this.iconSize = iconSize;
        if(icon != null)
            ResourceManager.setButtonIcons(this, icon, defaultState, pressedState, rolloverState, iconSize);

        setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setFGDark();
        addInsets(15);

        addFontColorUpdates();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                            getActionCommand(),
                            EventQueue.getMostRecentEventTime(),
                            ((KeyEvent) EventQueue.getCurrentEvent()).getModifiers()));
            }
        });
    }

    public void setIcon(String icon) {
        ResourceManager.setButtonIcons(this, icon, iconSize);
    }

    public void setIcon(String icon, int size) {
        ResourceManager.setButtonIcons(this, icon, (iconSize = size));
    }

    public void setIcon(String icon, int defaultState, int pressedState, int rolloverState) {
        ResourceManager.setButtonIcons(this, icon, defaultState, pressedState, rolloverState, iconSize);
    }

    public void setRounded(boolean isRounded) {
        this.isRounded = isRounded;
        if(isRounded)
            setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int actualCornerRadius = getActualRadius();

        if(isRounded) {
//            g2d.setColor(hasFocus() ? ResourceManager.getColor(ByteBoardTheme.ACCENT_DARK) : getBackground());
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, actualCornerRadius, actualCornerRadius);
        }

        if(hasFocus()) {
            g2d.setColor(ResourceManager.getColor(ByteBoardTheme.ACCENT));
            g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, actualCornerRadius, actualCornerRadius);
        }

        super.paintComponent(g2d);
    }

    protected int getActualRadius() {
        return Math.min(60, Math.min(getWidth(), getHeight()) / 2);
    }

    private void addFontColorUpdates() {
        MouseAdapter adapter = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(!isEnabled()) return;
                setFGColor(rolloverFGColor);
            }

            public void mouseExited(MouseEvent e) {
                if(!isEnabled()) return;
                setFGColor(defaultFGColor);
            }

            public void mouseReleased(MouseEvent e) {
                if(isEnabled()) {
                    setFGColor(defaultFGColor);
                    return;
                }
                setFGColor(ByteBoardTheme.DISABLED);
                repaint();
            }
        };

        addMouseListener(adapter);
    }

    public void addInterfaceEventInvokeOnClick(String event, Object... eventConstraints) {
        addActionListener(e -> InterfaceEventManager.invokeEvent(event, eventConstraints));
    }

    public void setFontPrimary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(type, size)));
    }

    public void setFontSecondary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_SECONDARY(type, size)));
    }

    public void setFGDark() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
    }

    public void setFGLight() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        setRounded(true);
    }

    public void setFGMain() {
        setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_MAIN));
    }

    private void setFGColor(String fgColor) {
        super.setForeground(ResourceManager.getColor(fgColor));
    }

    private void setFGColor(Color fgColor) {
        super.setForeground(fgColor);
    }

    public void setForeground(Color fg) {
        super.setForeground(fg);
        defaultFGColor = fg;

        rolloverFGColor = ResourceManager.getColor(
                fg.equals(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK)) ?
                        ByteBoardTheme.ACCENT_DARK : ByteBoardTheme.ACCENT);
    }

    public void setAlignmentLeading() {
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    public void setAlignmentTrailing() {
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    public void setAlignmentCenter() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void addInsets(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}
