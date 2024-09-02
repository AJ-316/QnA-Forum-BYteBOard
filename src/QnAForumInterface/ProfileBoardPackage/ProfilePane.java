package QnAForumInterface.ProfileBoardPackage;

import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author AJ
 */
public class ProfilePane extends JPanel {

    private static ProfilePane SELECTED_PANE;
    private JLabel profileIcon;
    private CustomControls.RoundedJPanel roundedPane;

    public ProfilePane() {
        initComponents();
    }

    public static ProfilePane create(Icon icon) {
        ProfilePane pane = new ProfilePane();
        pane.profileIcon.setIcon(icon);
        return pane;
    }

    private void initComponents() {

        roundedPane = new CustomControls.RoundedJPanel();
        profileIcon = new JLabel();

        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (isSelected()) return;
                setColor(ResourceManager.getColor("accent"));
            }

            public void mouseExited(MouseEvent evt) {
                if (isSelected()) return;
                setColor(ResourceManager.getColor("main_light"));
            }

            public void mouseReleased(MouseEvent evt) {
                setSelected();
            }
        });
        setLayout(new BorderLayout());

        roundedPane.setBackground(ResourceManager.getColor("main_light"));
        roundedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        roundedPane.setBorderColor(ResourceManager.getColor("main_dark"));
        roundedPane.setCornerRadius(180);
        roundedPane.setLimitRadius(false);
        roundedPane.setLayout(new BorderLayout());

        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        roundedPane.add(profileIcon, BorderLayout.CENTER);

        add(roundedPane, BorderLayout.CENTER);
    }

    private void setColor(Color c) {
        roundedPane.setBackground(c);
    }

    public boolean isSelected() {
        if (SELECTED_PANE == null) return false;
        return SELECTED_PANE.equals(this);
    }

    public void setSelected() {
        if (isSelected()) return;

        if (SELECTED_PANE != null)
            SELECTED_PANE.setColor(ResourceManager.getColor("main_light"));
        SELECTED_PANE = this;
        setColor(ResourceManager.getColor("accent_dark"));
    }
}
