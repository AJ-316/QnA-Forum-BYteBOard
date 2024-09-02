/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;


import QnAForumInterface.ProfileBoardPackage.ProfileBoard;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * @author AJ
 */
public class OptionsPanel extends JPanel {

    private JButton answerButton;
    private JButton askBtn;
    private JPanel buttonHolder;
    private JButton displayAnswerBtn;
    private JButton profileBtn;
    private JButton searchBtn;
    private JLabel userName;
    private JLabel userProfile;
    /**
     * Creates new form OptionsPanel
     */
    public OptionsPanel() {
        initComponents();
    }

    public void setUserInfo(String username, String profileIndex, boolean isAnswerOptionVisible) {
        this.userName.setText(username);
        ResourceManager.setProfileIcon(profileIndex, userProfile, ResourceManager.REGULAR);
        this.answerButton.setVisible(isAnswerOptionVisible);
    }

    public String getUsername() {
        return userName.getText();
    }

    public String getUserProfileIndex() {
        return userProfile.getName();
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        userProfile = new JLabel();
        userName = new JLabel();
        buttonHolder = new JPanel();
        profileBtn = new JButton();
        searchBtn = new JButton();
        askBtn = new JButton();
        answerButton = new JButton();
        displayAnswerBtn = new JButton();

        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setLayout(new GridBagLayout());

        userProfile.setIcon(ResourceManager.getProfileIcon("0", ResourceManager.SMALL));
        userProfile.setVerticalAlignment(SwingConstants.BOTTOM);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(60, 25, 0, 25);
        add(userProfile, gridBagConstraints);

        userName.setText("Username");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 25, 25, 25);
        add(userName, gridBagConstraints);
        userName.setFont(ResourceManager.getFont("inter_bold.28"));
        userName.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        buttonHolder.setOpaque(false);
        buttonHolder.setLayout(new GridBagLayout());

        profileBtn.setBackground(getBackground());
        profileBtn.setIcon(ResourceManager.getStateIcon("home", ResourceManager.DEFAULT, ResourceManager.MINI));
        profileBtn.setText("Profile");
        profileBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        profileBtn.setBorderPainted(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setFocusPainted(false);
        profileBtn.setPressedIcon(ResourceManager.getStateIcon("home", ResourceManager.PRESSED, ResourceManager.MINI));
        profileBtn.setRolloverIcon(ResourceManager.getStateIcon("home", ResourceManager.ROLLOVER, ResourceManager.MINI));
        profileBtn.addActionListener(evt -> profileBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        buttonHolder.add(profileBtn, gridBagConstraints);
        profileBtn.setFont(ResourceManager.getFont("inter_regular.22"));
        profileBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        searchBtn.setBackground(getBackground());
        searchBtn.setIcon(ResourceManager.getStateIcon("search", ResourceManager.DEFAULT, ResourceManager.MINI));
        searchBtn.setText("Search");
        searchBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchBtn.setBorderPainted(false);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setPressedIcon(ResourceManager.getStateIcon("search", ResourceManager.PRESSED, ResourceManager.MINI));
        searchBtn.setRolloverIcon(ResourceManager.getStateIcon("search", ResourceManager.ROLLOVER, ResourceManager.MINI));
        searchBtn.addActionListener(evt -> searchBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        buttonHolder.add(searchBtn, gridBagConstraints);
        searchBtn.setFont(ResourceManager.getFont("inter_regular.22"));
        searchBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        askBtn.setBackground(getBackground());
        askBtn.setIcon(ResourceManager.getStateIcon("question", ResourceManager.DEFAULT, ResourceManager.MINI));
        askBtn.setText("Ask");
        askBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        askBtn.setBorderPainted(false);
        askBtn.setContentAreaFilled(false);
        askBtn.setFocusPainted(false);
        askBtn.setPressedIcon(ResourceManager.getStateIcon("question", ResourceManager.PRESSED, ResourceManager.MINI));
        askBtn.setRolloverIcon(ResourceManager.getStateIcon("question", ResourceManager.ROLLOVER, ResourceManager.MINI));
        askBtn.addActionListener(evt -> askBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        buttonHolder.add(askBtn, gridBagConstraints);
        askBtn.setFont(ResourceManager.getFont("inter_regular.22"));
        askBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        answerButton.setBackground(getBackground());
        answerButton.setIcon(ResourceManager.getStateIcon("answer", ResourceManager.DEFAULT, ResourceManager.MINI));
        answerButton.setText("Answer");
        answerButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        answerButton.setBorderPainted(false);
        answerButton.setContentAreaFilled(false);
        answerButton.setFocusPainted(false);
        answerButton.setPressedIcon(ResourceManager.getStateIcon("answer", ResourceManager.PRESSED, ResourceManager.MINI));
        answerButton.setRolloverIcon(ResourceManager.getStateIcon("answer", ResourceManager.ROLLOVER, ResourceManager.MINI));
        answerButton.addActionListener(evt -> answerButtonActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        buttonHolder.add(answerButton, gridBagConstraints);
        answerButton.setFont(ResourceManager.getFont("inter_regular.22"));
        answerButton.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(25, 25, 10, 25);
        add(buttonHolder, gridBagConstraints);

        displayAnswerBtn.setBackground(getBackground());
        displayAnswerBtn.setIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.DEFAULT, ResourceManager.MINI));
        displayAnswerBtn.setText("Hide Answers");
        displayAnswerBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        displayAnswerBtn.setBorderPainted(false);
        displayAnswerBtn.setContentAreaFilled(false);
        displayAnswerBtn.setFocusPainted(false);
        displayAnswerBtn.setPressedIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.PRESSED, ResourceManager.MINI));
        displayAnswerBtn.setRolloverIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.ROLLOVER, ResourceManager.MINI));
        displayAnswerBtn.addActionListener(evt -> displayAnswerBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new Insets(10, 0, 25, 0);
        add(displayAnswerBtn, gridBagConstraints);
        displayAnswerBtn.setFont(ResourceManager.getFont("inter_regular.22"));
        displayAnswerBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
    }

    private void profileBtnActionPerformed(ActionEvent evt) {
        QnAForum.setContent(ProfileBoard.init(userName.getText()));
    }

    private void askBtnActionPerformed(ActionEvent evt) {
        QnAForum.setContent(AskBoard.init(userName.getText(), userProfile.getName()));
    }

    private void answerButtonActionPerformed(ActionEvent evt) {
        QnABoard.answerCurrentQuestion();
    }

    private void displayAnswerBtnActionPerformed(ActionEvent evt) {

        boolean isVisible = QnABoard.CurrentInstance.toggleAnswerDisplay();

        if (isVisible) {
            displayAnswerBtn.setText("Hide Answers");
            displayAnswerBtn.setIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.DEFAULT, ResourceManager.MINI));
            displayAnswerBtn.setPressedIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.PRESSED, ResourceManager.MINI));
            displayAnswerBtn.setRolloverIcon(ResourceManager.getStateIcon("arrowD", ResourceManager.ROLLOVER, ResourceManager.MINI));

            return;
        }

        displayAnswerBtn.setText("Show Answers");
        displayAnswerBtn.setIcon(ResourceManager.getStateIcon("arrowU", ResourceManager.DEFAULT, ResourceManager.MINI));
        displayAnswerBtn.setPressedIcon(ResourceManager.getStateIcon("arrowU", ResourceManager.PRESSED, ResourceManager.MINI));
        displayAnswerBtn.setRolloverIcon(ResourceManager.getStateIcon("arrowU", ResourceManager.ROLLOVER, ResourceManager.MINI));
    }

    private void searchBtnActionPerformed(ActionEvent evt) {
        QnAForum.setContent(SearchBoard.init(getUsername(), getUserProfileIndex()));
    }
}

