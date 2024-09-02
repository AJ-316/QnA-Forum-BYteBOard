package QnAForumInterface.ProfileBoardPackage;

import CustomControls.RoundedJPanel;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventListener;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.QnAForum;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProfileButtonHolder extends RoundedJPanel {

    private JButton askQuestionBtn;
    private JButton searchBtn;
    private JButton editProfileBtn;
    private JButton hideActivityBtn;
    private JButton viewActivityBtn;
    private JButton logoutBtn;

    public ProfileButtonHolder() {
        searchBtn = new JButton();
        askQuestionBtn = new JButton();
        viewActivityBtn = new JButton();
        hideActivityBtn = new JButton();
        editProfileBtn = new JButton();
        logoutBtn = new JButton();

        init();
    }

    private void init() {
        setBackground(ResourceManager.getColor("main"));
        setCornerRadius(90);
        setLayout(new GridBagLayout());

        searchBtn = addButton("Search", e -> InterfaceEventManager.invokeEvent("Init.SearchBoard.ProfileBoard"),
                "search", GridBagConstraints.RELATIVE, 0.1, GridBagConstraints.NONE, new Insets(5, 30, 5, 35));

        askQuestionBtn = addButton("Ask Question", e -> InterfaceEventManager.invokeEvent("Init.AskBoard.ProfileBoard"),
                "question", GridBagConstraints.RELATIVE, 0, GridBagConstraints.NONE, new Insets(5, 30, 5, 35));

        InterfaceEventManager.addListener("Display.ActivityContainer", ec -> {
            boolean display = (boolean) ec[0];
            hideActivityBtn.setVisible(display);
            viewActivityBtn.setVisible(!display);
        });
        viewActivityBtn = addButton("View Activity", e -> InterfaceEventManager.invokeEvent("Display.ActivityContainer", true),
                "show", GridBagConstraints.RELATIVE, 0, GridBagConstraints.VERTICAL, new Insets(5, 30, 5, 35));

        hideActivityBtn = addButton("Hide Activity", e -> InterfaceEventManager.invokeEvent("Display.ActivityContainer", false),
                "hide", 2, 0, GridBagConstraints.VERTICAL, new Insets(5, 30, 5, 35));
        hideActivityBtn.setVisible(false);

        editProfileBtn = addButton("Edit Profile", e -> InterfaceEventManager.invokeEvent("Edit.ProfileBoard", true),
                "edit", GridBagConstraints.RELATIVE,
                0, GridBagConstraints.VERTICAL, new Insets(5, 30, 5, 35));

        logoutBtn = addButton("Log Out", e -> QnAForum.logout(), "logout", GridBagConstraints.RELATIVE,
                0, GridBagConstraints.VERTICAL, new Insets(5, 30, 100, 35));
    }


    private JButton addButton(String text, ActionListener listener, String iconLabel, int gridy, double weighty, int fill, Insets insets) {
        JButton button = new JButton(text);
        button.setFont(ResourceManager.getFont("inter_regular.22"));
        button.setForeground(ResourceManager.getColor("text_fg_light"));

        button.setIcon(ResourceManager.getIcon(iconLabel + "_default", ResourceManager.MINI));
        button.setPressedIcon(ResourceManager.getIcon(iconLabel + "_pressed", ResourceManager.MINI));
        button.setRolloverIcon(ResourceManager.getIcon(iconLabel + "_rollover", ResourceManager.MINI));
        button.addActionListener(listener);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = gridy;
        constraints.weightx = 1;
        constraints.weighty = weighty;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        constraints.fill = fill;
        constraints.insets = insets;
        add(button, constraints);
        return button;
    }
}
