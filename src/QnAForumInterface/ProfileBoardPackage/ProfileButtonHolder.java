package QnAForumInterface.ProfileBoardPackage;

import CustomControls.RoundedJPanel;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventListener;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.QnAForum;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileButtonHolder extends RoundedJPanel {

    private JButton hideActivityBtn;
    private JButton viewActivityBtn;
    private JComboBox<String> themeListBox;

    public ProfileButtonHolder() {
        init();
    }

    private void init() {
        setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        setCornerRadius(90);
        setLayout(new GridBagLayout());

        themeListBox = new JComboBox<>();

        ActionListener changeThemeBtnListener = e -> {
            String selectedTheme = (String) themeListBox.getSelectedItem();
            if (themeListBox.isVisible()) {
                if(ResourceManager.getCurrentTheme().equals(selectedTheme)) {
                    themeListBox.setVisible(false);
                    ((JButton) e.getSource()).setText("Change Theme");
                    return;
                }
                ResourceManager.setTheme(selectedTheme);
                QnAForum.restart();
            }
            themeListBox.setSelectedItem(ResourceManager.getCurrentTheme());
            themeListBox.setVisible(!themeListBox.isVisible());
            ((JButton) e.getSource()).setText("Select");
        };

        JButton changeThemeBtn = addButton("Change Theme", changeThemeBtnListener, "theme", 0, 0,
                GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, new Insets(100, 30, 0, 35));

        initChangeThemeOption(changeThemeBtn);

        addButton("Search", e -> InterfaceEventManager.invokeEvent("Init.SearchBoard.ProfileBoard"),
                "search", 2, 0.1, GridBagConstraints.NONE, GridBagConstraints.SOUTHWEST, new Insets(5, 30, 5, 35));

        addButton("Ask Question", e -> InterfaceEventManager.invokeEvent("Init.AskBoard.ProfileBoard"),
                "question", 3, 0, GridBagConstraints.NONE, GridBagConstraints.SOUTHWEST, new Insets(5, 30, 5, 35));

        InterfaceEventManager.addListener("Display.ActivityContainer", ec -> {
            boolean display = (boolean) ec[0];
            hideActivityBtn.setVisible(display);
            viewActivityBtn.setVisible(!display);
        });

        viewActivityBtn = addButton("View Activity", e -> InterfaceEventManager.invokeEvent("Display.ActivityContainer", true),
                "show", 4, 0, GridBagConstraints.VERTICAL, GridBagConstraints.SOUTHWEST, new Insets(5, 30, 5, 35));

        hideActivityBtn = addButton("Hide Activity", e -> InterfaceEventManager.invokeEvent("Display.ActivityContainer", false),
                "hide", 4, 0, GridBagConstraints.VERTICAL, GridBagConstraints.SOUTHWEST, new Insets(5, 30, 5, 35));
        hideActivityBtn.setVisible(false);

        addButton("Edit Profile", e -> InterfaceEventManager.invokeEvent("Edit.ProfileBoard", true),
                "edit", 5,
                0, GridBagConstraints.VERTICAL, GridBagConstraints.SOUTHWEST, new Insets(5, 30, 5, 35));

        addButton("Log Out", e -> QnAForum.logout(true), "logout", GridBagConstraints.RELATIVE,
                0, GridBagConstraints.VERTICAL, GridBagConstraints.SOUTHWEST, new Insets(5, 30, 100, 35));
    }

    private void initChangeThemeOption(JButton changeThemeBtn) {
        themeListBox.setVisible(false);

        for (ByteBoardTheme theme : ResourceManager.getThemes()) {
            themeListBox.addItem(theme.getName());
        }
        themeListBox.setFocusable(false);
        themeListBox.setSelectedItem(ResourceManager.getCurrentTheme());
        themeListBox.setPreferredSize(changeThemeBtn.getPreferredSize());
        themeListBox.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        themeListBox.setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton(){
                    @Override
                    public int getWidth() {
                        return ResourceManager.MICRO + 3;
                    }

                    @Override
                    public Dimension getSize(Dimension rv) {
                        return themeListBox.getPreferredSize();
                    }
                };
                button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                button.setIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.DEFAULT, ResourceManager.MICRO));
                button.setPressedIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.PRESSED, ResourceManager.MICRO));
                button.setRolloverIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.ROLLOVER, ResourceManager.MICRO));

                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                return button;
            }
        });

        themeListBox.setEditable(false);
        themeListBox.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        themeListBox.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        themeListBox.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        themeListBox.setFont(ResourceManager.getFont("inter_regular.18"));

        JTextField themeListBoxField = (JTextField) themeListBox.getEditor().getEditorComponent();
        themeListBoxField.setHorizontalAlignment(JTextField.CENTER);
        themeListBoxField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        themeListBoxField.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        themeListBoxField.setFont(ResourceManager.getFont("inter_regular.18"));


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = new Insets(5, 30, 5, 35);
        add(themeListBox, constraints);
    }

    private JButton addButton(String text, ActionListener listener, String iconLabel, int gridy, double weighty, int fill, int anchor, Insets insets) {
        JButton button = new JButton(text);
        button.setFont(ResourceManager.getFont("inter_regular.22"));
        button.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));

        button.setIcon(ResourceManager.getStateIcon(iconLabel, ResourceManager.DEFAULT, ResourceManager.MINI));
        button.setPressedIcon(ResourceManager.getStateIcon(iconLabel, ResourceManager.PRESSED, ResourceManager.MINI));
        button.setRolloverIcon(ResourceManager.getStateIcon(iconLabel, ResourceManager.ROLLOVER, ResourceManager.MINI));
        button.addActionListener(listener);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = gridy;
        constraints.weightx = 1;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        constraints.fill = fill;
        constraints.insets = insets;
        add(button, constraints);
        return button;
    }
}
