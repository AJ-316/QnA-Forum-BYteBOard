package QnAForumInterface.InformationBarPackage;

import CustomControls.RoundedJPanel;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public abstract class InformationBar extends JPanel {

    protected RoundedJPanel container;

    public InformationBar() {
        setBackground(ResourceManager.getColor("main_light"));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        setPreferredSize(new Dimension(338, 90));
        setOpaque(false);
        setLayout(new BorderLayout());

        container = new RoundedJPanel();
        container.setBackground(ResourceManager.getColor("main"));
        container.setBorderColor(ResourceManager.getColor("main_dark"));
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
        label.setForeground(ResourceManager.getColor("text_fg_light"));
        label.setFont(ResourceManager.getFont("inter_regular.22"));
        label.setHorizontalAlignment(align);
        return label;
    }

}
