package QnAForumInterface.InformationBarPackage;

import CustomControls.RoundedJPanel;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public abstract class InformationBar extends JPanel {

    protected RoundedJPanel container;

    public InformationBar() {
        setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        setPreferredSize(new Dimension(338, 90));
        setOpaque(false);
        setLayout(new BorderLayout());

        container = new RoundedJPanel();
        container.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        container.setBorderColor(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        container.setCornerRadius(75);
        container.setLimitRadius(false);
        container.setLayout(new GridBagLayout());

        init();
        addMouseListeners();
        add(container, BorderLayout.CENTER);
    }

    protected abstract void init();
    protected abstract void addMouseListeners();

    protected JLabel getLabel(String text, int align) {
        JLabel label = new JLabel(text);
        label.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        label.setFont(ResourceManager.getFont("inter_regular.22"));
        label.setHorizontalAlignment(align);
        return label;
    }

}
