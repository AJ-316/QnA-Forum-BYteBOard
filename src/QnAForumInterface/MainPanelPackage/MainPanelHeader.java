package QnAForumInterface.MainPanelPackage;

import CustomControls.CustomJPanel;

import CustomControls.DEBUG;
import DatabasePackage.DBVote;
import QnAForumInterface.InformationBarPackage.AnswerBar;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.QnABoard;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPanelHeader extends JPanel {

    private final JPanel contentUserProfileContainer;
    private final JPanel votesBtnContainers;
    private final CustomJPanel contentBytescoreContainer;
    private final JLabel contentUserProfile;
    private final JLabel contentUserName;
    private final JButton viewQuestionBtn;
    private final JLabel tagText;
    private JButton upvoteBtn;
    private JButton downvoteBtn;

    private final JLabel contentBytescore;
    private final CustomJPanel tagsPanel;
    private final String contentType;

    public MainPanelHeader(String contentType) {
        this.contentType = contentType;
        votesBtnContainers = new JPanel();
        contentUserProfileContainer = new JPanel();
        contentBytescoreContainer = new CustomJPanel();
        contentUserProfile = new JLabel();
        contentUserName = new JLabel();
        tagsPanel = new CustomJPanel();
        tagText = new JLabel();
        viewQuestionBtn = new JButton();
        contentBytescore = new JLabel();

        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        contentUserProfileContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        contentUserProfileContainer.setOpaque(false);
        contentUserProfileContainer.setLayout(new GridBagLayout());
        contentUserProfileContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        contentBytescoreContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        contentBytescoreContainer.setLayout(new GridBagLayout());
        contentBytescoreContainer.setShadowState(CustomJPanel.DROP_SHADOW);
        contentBytescoreContainer.setLimitRadius(false);
        contentBytescoreContainer.setCornerRadius(60);
        contentBytescoreContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setOpaque(false);
        setBackground(Color.red);

        initUserProfile(contentUserProfileContainer);
        initUserName(contentUserProfileContainer);
        initTagsPanel(contentUserProfileContainer);
        initViewQuestionButton(contentUserProfileContainer);
        initBytescore(contentBytescoreContainer);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        add(contentUserProfileContainer, constraints);
        constraints.gridx = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(contentBytescoreContainer, constraints);

        //contentBytescoreContainer.setPreferredSize(new Dimension(
        //        contentBytescoreContainer.getPreferredSize().width + 50,
        //        contentBytescoreContainer.getPreferredSize().height));
    }

    private void upvote(boolean invokeDBEvent) {
        upvoteBtn.setName(DBVote.V_VOTE_UP);
        downvoteBtn.setName(DBVote.V_VOTE_NONE);

        ResourceManager.setButtonIcons(upvoteBtn, "upvote",
                ResourceManager.PRESSED, ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.SMALL);
        ResourceManager.setButtonIcons(downvoteBtn, "downvote", ResourceManager.MINI);

        if(invokeDBEvent)
            InterfaceEventManager.invokeEvent("Update." + contentType + "Vote", DBVote.V_VOTE_UP);
        DEBUG.printlnYellow("Voting UP");
    }

    private void downvote(boolean invokeDBEvent) {
        downvoteBtn.setName(DBVote.V_VOTE_DOWN);
        upvoteBtn.setName(DBVote.V_VOTE_NONE);
        ResourceManager.setButtonIcons(downvoteBtn, "downvote",
                ResourceManager.PRESSED, ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.SMALL);

        ResourceManager.setButtonIcons(upvoteBtn, "upvote", ResourceManager.MINI);

        if(invokeDBEvent)
            InterfaceEventManager.invokeEvent("Update." + contentType + "Vote", DBVote.V_VOTE_DOWN);
        DEBUG.printlnYellow("Voting DOWN");
    }

    private void voteNone(boolean invokeDBEvent) {
        upvoteBtn.setName(DBVote.V_VOTE_NONE);
        downvoteBtn.setName(DBVote.V_VOTE_NONE);
        ResourceManager.setButtonIcons(upvoteBtn, "upvote", ResourceManager.MINI);
        ResourceManager.setButtonIcons(downvoteBtn, "downvote", ResourceManager.MINI);

        if(invokeDBEvent)
            InterfaceEventManager.invokeEvent("Update." + contentType + "Vote", DBVote.V_VOTE_NONE);
        DEBUG.printlnYellow("Voting to NONE");
    }

    private void initBytescore(JPanel container) {
        votesBtnContainers.setLayout(new GridLayout(2, 1));
        votesBtnContainers.setOpaque(false);

        upvoteBtn = getBytescoreButton("upvote");
        downvoteBtn = getBytescoreButton("downvote");
        upvoteBtn.addActionListener(e -> {
            if(upvoteBtn.getName().equals(DBVote.V_VOTE_UP)) voteNone(true);
            else upvote(true);
            System.out.println(upvoteBtn);
        });

        downvoteBtn.addActionListener(e -> {
            if(downvoteBtn.getName().equals(DBVote.V_VOTE_DOWN)) voteNone(true);
            else downvote(true);
        });

        votesBtnContainers.add(upvoteBtn);
        votesBtnContainers.add(downvoteBtn);
        Dimension size = votesBtnContainers.getPreferredSize();
        votesBtnContainers.setPreferredSize(new Dimension(size.width, size.height + ResourceManager.SMALL));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.anchor = GridBagConstraints.EAST;
        container.add(votesBtnContainers, constraints);

        JLabel bytesIcon = new JLabel();
        bytesIcon.setIcon(ResourceManager.getIcon("bytescore_icon", ResourceManager.SMALL));
        bytesIcon.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.anchor = GridBagConstraints.WEST;
        container.add(bytesIcon, constraints);

        contentBytescore.setText("12687");
        contentBytescore.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentBytescore.setHorizontalAlignment(JLabel.CENTER);
        contentBytescore.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        contentBytescore.setFont(ResourceManager.getFont("inter_bold.24"));
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        container.add(contentBytescore, constraints);
    }

    private JButton getBytescoreButton(String iconLabel) {
        JButton button = new JButton();
        button.setName(DBVote.V_VOTE_NONE);
        ResourceManager.setButtonIcons(button, iconLabel, ResourceManager.MINI);
        return button;
    }

    private void initUserProfile(JPanel container) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;

        contentUserProfile.setIcon(ResourceManager.getProfileIcon("0", ResourceManager.REGULAR));

        container.add(contentUserProfile, gridBagConstraints);
    }

    private void initUserName(JPanel container) {
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

        container.add(contentUserName, gridBagConstraints);
    }

    private void initTagsPanel(JPanel container) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 20, 20, 0);

        tagsPanel.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
        tagsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        tagsPanel.setBorderColor(ResourceManager.getColor(ByteBoardTheme.MAIN));
        tagsPanel.setShadowState(CustomJPanel.NONE);
        tagsPanel.setCornerRadius(50);
        tagsPanel.setLimitRadius(false);
        tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.X_AXIS));

        tagText.setText("tag1 | tag2 | tag3");
        tagText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagText.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        tagText.setFont(ResourceManager.getFont("inter_regular.22"));
        tagsPanel.add(tagText);

        container.add(tagsPanel, gridBagConstraints);
    }

    private void initViewQuestionButton(JPanel container) {
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

        ResourceManager.setButtonIcons(viewQuestionBtn, "arrowL", ResourceManager.MINI);
        viewQuestionBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        viewQuestionBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                viewQuestionBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.ACCENT));
            }

            public void mouseExited(MouseEvent evt) {
                viewQuestionBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
            }
        });

        viewQuestionBtn.addActionListener(evt -> {
            AnswerBar.setSelected(null);
            QnABoard.CurrentInstance.displayQuestionPanel();
        });

        container.add(viewQuestionBtn, gridBagConstraints);
    }

    public void setContent(String contentUserName, String contentUserProfileIndex, String[] tags, String contentBytescore, String lastVoteType) {
        this.contentUserName.setText(contentUserName);
        this.contentBytescore.setText(contentBytescore);

        DEBUG.printlnRed(lastVoteType + ": " + contentType);
        if(lastVoteType.equals(DBVote.V_VOTE_UP))
            upvote(false);
        else if(lastVoteType.equals(DBVote.V_VOTE_DOWN))
            downvote(false);
        else voteNone(false);

        ResourceManager.setProfileIndexIcon(contentUserProfileIndex, contentUserProfile, ResourceManager.REGULAR);

        if(tags == null) return;

        tagText.setText("");
        tagsPanel.setVisible(tags.length != 0);

        for (String tag : tags)
            createTag(tag);
    }

    private void createTag(String tagText) {
        if (!this.tagText.getText().isEmpty())
            tagText = this.tagText.getText() + " | " + tagText;

        this.tagText.setToolTipText(String.join(", ", tagText.split(" \\| ")));

        if(tagText.length() > 50) {
            tagText = tagText.substring(0, 50).trim();

            int lastDelimiter = tagText.indexOf("\\|", tagText.length() - 2);
            if (lastDelimiter != -1)
                tagText = tagText.substring(0, lastDelimiter).trim();
            tagText = tagText.substring(0, tagText.length() - 3) + "...";
        }
        this.tagText.setText(tagText);
    }

    public void setContentType(String type) {
        viewQuestionBtn.setVisible(type.equals(MainPanel.CONTENT_ANSWER));
        tagsPanel.setVisible(type.equals(MainPanel.CONTENT_QUESTION));
    }

    public String getContentUserName() {
        return contentUserName.getText();
    }

    public void setContentBytescore(String bytescore) {
        contentBytescore.setText(bytescore);
    }

    public void disableVotes() {
        votesBtnContainers.setVisible(false);
    }
}
