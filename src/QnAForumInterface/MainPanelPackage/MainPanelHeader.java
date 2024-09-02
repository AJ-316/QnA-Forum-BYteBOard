package QnAForumInterface.MainPanelPackage;

import CustomControls.RoundedJPanel;

import QnAForumInterface.InformationBarPackage.AnswerBar;
import QnAForumInterface.QnABoard;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPanelHeader extends JPanel {

    private final JLabel contentUserProfile;
    private final JLabel contentUserName;
    private final JButton viewQuestionBtn;
    private final JLabel tagText;

    private RoundedJPanel tagsPanel;

    public MainPanelHeader() {
        contentUserProfile = new JLabel();
        contentUserName = new JLabel();
        tagsPanel = new RoundedJPanel();
        tagText = new JLabel();
        viewQuestionBtn = new JButton();

        init();
    }

    private void init() {
        setBackground(new Color(0, 102, 51));
        setOpaque(false);
        setPreferredSize(new Dimension(315, 130));
        setLayout(new GridBagLayout());

        initUserProfile();
        initUserName();
        initTagsPanel();
        initViewQuestionButton();
    }

    private void initUserProfile() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;

        contentUserProfile.setIcon(ResourceManager.getProfileIcon("0", ResourceManager.REGULAR));

        add(contentUserProfile, gridBagConstraints);
    }

    private void initUserName() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(20, 20, 0, 0);

        contentUserName.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        contentUserName.setText("Content User Name");
        contentUserName.setFont(ResourceManager.getFont("inter_bold.36"));

        add(contentUserName, gridBagConstraints);
    }

    private void initTagsPanel() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 20, 20, 0);

        tagsPanel.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
        tagsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        tagsPanel.setBorderColor(ResourceManager.getColor(ByteBoardTheme.MAIN));
        tagsPanel.setCornerRadius(50);
        tagsPanel.setLimitRadius(false);
        tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.X_AXIS));

        tagText.setText("tag1 | tag2 | tag3");
        tagText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagText.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        tagText.setFont(ResourceManager.getFont("inter_regular.22"));
        tagsPanel.add(tagText);

        add(tagsPanel, gridBagConstraints);
    }

    private void initViewQuestionButton() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 20, 20, 0);

        viewQuestionBtn.setText("View Question");
        viewQuestionBtn.setHorizontalAlignment(SwingConstants.LEFT);
        viewQuestionBtn.setBackground(new Color(204, 204, 204));
        viewQuestionBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        viewQuestionBtn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT), 2, true), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        viewQuestionBtn.setBorderPainted(false);
        viewQuestionBtn.setContentAreaFilled(false);
        viewQuestionBtn.setFocusPainted(false);
        viewQuestionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        viewQuestionBtn.setIcon(ResourceManager.getStateIcon("arrowL", ResourceManager.DEFAULT, ResourceManager.MINI));
        viewQuestionBtn.setPressedIcon(ResourceManager.getStateIcon("arrowL", ResourceManager.PRESSED, ResourceManager.MINI));
        viewQuestionBtn.setRolloverIcon(ResourceManager.getStateIcon("arrowL",ResourceManager.ROLLOVER, ResourceManager.MINI));
        viewQuestionBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        viewQuestionBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN));
            }

            public void mouseExited(MouseEvent evt) {
                setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
            }
        });

        viewQuestionBtn.addActionListener(evt -> {
            AnswerBar.setSelected(null);
            QnABoard.CurrentInstance.displayQuestionPanel();
        });

        add(viewQuestionBtn, gridBagConstraints);
    }

    public void setContent(String contentUserName, String contentUserProfileIndex, String[] tags) {
        this.contentUserName.setText(contentUserName);
        ResourceManager.setProfileIcon(contentUserProfileIndex, contentUserProfile, ResourceManager.REGULAR);

        if(tags == null) return;

        tagText.setText("");
        tagsPanel.setVisible(tags.length != 0);

        for (String tag : tags)
            createTag(tag);
    }

    private void createTag(String tagText) {
        if (!this.tagText.getText().isEmpty())
            tagText = this.tagText.getText() + " | " + tagText;

        this.tagText.setText(tagText);
    }

    public void setContentType(int type) {
        viewQuestionBtn.setVisible(type == 0);
        tagsPanel.setVisible(type != 0);
    }

    public String getContentUserName() {
        return contentUserName.getText();
    }
}
