package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardTextArea;
import CustomControls.CustomRendererPackage.RoundedBorder;
import CustomControls.GridBagBuilder;
import CustomControls.SimpleScrollPane;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BoardContentPanel extends BoardPanel {

    private final UpdateVoteDatabase voteUpdate;
    protected BoardPanel contentBodyPanel;
    protected BoardLabel contentBytes;
    protected BoardTextArea contentBody;
    private String userID;
    private String contentID;
    private VoteButton upVoteButton;
    private VoteButton downVoteButton;
    private BoardPanel voteButtonsPanel;
    private BoardTagsDisplayPanel tagsDisplayPanel;
    private BoardLabel contentUsername;
    private BoardTextArea contentHead;
    private SimpleScrollPane bodyScrollPane;
    private BoardResponseCardPanel contentResponseCardPanel;

    public BoardContentPanel(MainFrame main, Frame frame, UpdateVoteDatabase voteUpdate) {
        super(main, frame);
        this.voteUpdate = voteUpdate;

        setPreferredSize(new Dimension(900, getPreferredSize().height));
        setMaximumSize(new Dimension(900, getPreferredSize().height));
        setMinimumSize(new Dimension(900, getPreferredSize().height));

        addResponseButtonListeners();
    }

    public void init(MainFrame main, Frame frame) {
        initComponents(main, frame);

        BoardPanel headerPanel = getContentHeadPanel(main, frame);
        contentBodyPanel = getContentBodyPanel(main, frame);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.weightX(1).fillBoth()
                .addToNextCell(headerPanel)
                .weightY(1)
                .addToNextCell(contentBodyPanel);
    }

    private BoardPanel getContentHeadPanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        panel.setCornerRadius(90);
        panel.addInsets(20);

        voteButtonsPanel = new BoardPanel(main, frame);
        GridBagBuilder votePanelBuilder = new GridBagBuilder(voteButtonsPanel, 1);
        votePanelBuilder.insets(10, 5, 10, 5)
                .addToNextCell(upVoteButton)
                .addToNextCell(downVoteButton);

        BoardPanel headerPane = new BoardPanel(main, frame);
        GridBagBuilder headerPaneLayout = new GridBagBuilder(headerPane, 3);
        headerPaneLayout.fillBoth().insets(10)
                .addToNextCell(contentBytes).weight(1, 1)
                .addToNextCell(tagsDisplayPanel.getComponent()).weight(0, 0)
                .addToNextCell(contentUsername);

        BoardPanel contentHeadPanel = new BoardPanel(main, frame, ByteBoardTheme.MAIN_LIGHT);
        contentHeadPanel.setLayout(new BorderLayout());
        contentHeadPanel.addInsets(20);
        contentHeadPanel.setCornerRadius(90);
        contentHeadPanel.setBorderColor(ByteBoardTheme.MAIN_DARK);
        contentHeadPanel.add(contentHead);

        BoardPanel footerPane = new BoardPanel(main, frame);
        GridBagBuilder footerPaneLayout = new GridBagBuilder(footerPane, 2);
        footerPaneLayout.insets(10)
                .addToNextCell(voteButtonsPanel);
        footerPaneLayout.weight(1, 1).fillBoth()
                .addToNextCell(contentHeadPanel);

        panel.setLayout(new BorderLayout());
        panel.add(headerPane, BorderLayout.NORTH);
        panel.add(footerPane, BorderLayout.CENTER);
        return panel;
    }

    protected void initComponents(MainFrame main, Frame frame) {
        tagsDisplayPanel = new BoardTagsDisplayPanel(main, frame);
        tagsDisplayPanel.setHorizontalDisplay();

        contentResponseCardPanel = new BoardResponseCardPanel(main, frame);
        contentResponseCardPanel.setTitle("Comments", "comment", "No Comments!");

        upVoteButton = new VoteButton("upvote", ResourceManager.SMALL, DBVote.V_VOTE_UP);
        upVoteButton.addInsets(0);
        downVoteButton = new VoteButton("downvote", ResourceManager.SMALL, DBVote.V_VOTE_DOWN);
        downVoteButton.addInsets(0);

        contentBytes = new BoardLabel("123456", "bytescore_icon");
        contentBytes.addInsets(10);
        contentBytes.setAlignmentLeading();
        contentBytes.setFGLight();
        contentBytes.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);

        contentUsername = new BoardLabel("Username");
        contentUsername.addInsets(0, 0, 0, 20);
        contentUsername.setHorizontalTextPosition(BoardLabel.LEFT);
        contentUsername.setAlignmentTrailing();
        contentUsername.setFGLight();
        contentUsername.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 20);

        contentHead = new BoardTextArea("Content Head goes here...");
        contentHead.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 26);

        contentBody = new BoardTextArea("Content Body goes here...");
        contentBody.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 22);
        contentBody.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
    }

    private BoardPanel getContentBodyPanel(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardPanel(main, frame, ByteBoardTheme.MAIN);
        panel.setLayout(new BorderLayout());
        panel.setCornerRadius(90);
        panel.addInsets(40);

        bodyScrollPane = new SimpleScrollPane(contentBody);
        bodyScrollPane.setOpaque(false);
        bodyScrollPane.setBorder(new RoundedBorder(60, 60, 30,
                ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT),
                ResourceManager.getColor(ByteBoardTheme.MAIN_DARK)));

        bodyScrollPane.getViewport().setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));

        panel.add(bodyScrollPane);
        return panel;
    }

    protected void addResponseButtonListeners() {
        upVoteButton.addActionListener(e -> voteContent(upVoteButton.getName(), true));
        downVoteButton.addActionListener(e -> voteContent(downVoteButton.getName(), true));
        contentResponseCardPanel.setCancelAddResponseAction(e -> {
            contentResponseCardPanel.clearResponseCards();
            refresh();
        });
        contentResponseCardPanel.setAddResponseAction("Add Comment", e -> {
            contentResponseCardPanel.clearResponseCards();
            createAddCommentCard(contentResponseCardPanel);
        });
    }

    private void voteContent(String voteType, boolean updateDatabase) {
        upVoteButton.select(voteType.equals(DBVote.V_VOTE_UP));
        downVoteButton.select(voteType.equals(DBVote.V_VOTE_DOWN));

        if (updateDatabase) {
            voteUpdate.invoke(getContentID(), getUserID(), voteType);
            refresh();
        }
    }

    public void setContentTags(DBDataObject[] tagDataObjectList, String userID) {
        tagsDisplayPanel.clearTags();

        for (DBDataObject tagDataObject : tagDataObjectList) {
            tagsDisplayPanel.addTag(tagDataObject.getValue(DBTag.K_TAG), userID);
        }
    }

    protected void createAddCommentCard(BoardResponseCardPanel contentCommentPanel) {
        refresh();
    }

    public void setContentResponseCards(DBDataObject[] commentDataObjectList, String contentIDKey) {
        Map<String, BoardResponseCard> cards = contentResponseCardPanel.getCards();
        contentResponseCardPanel.storeScroll();
        for (DBDataObject commentData : commentDataObjectList) {
            String contentID = commentData.getValue(contentIDKey);
            BoardResponseCard card = cards.get(contentID);

            if (card == null) {
                addNewResponseCard(commentData);
                continue;
            }

            updateResponseCard(card, commentData);
        }

        //contentResponseCardPanel.restoreScrolls();
    }

    protected BoardResponseCard addNewResponseCard(DBDataObject commentData) {
        BoardResponseCard card = new BoardResponseCard(getMain(), getFrame());
        updateResponseCard(card, commentData);
        contentResponseCardPanel.addResponseCard(card);
        return card;
    }

    protected void updateResponseCard(BoardResponseCard card, DBDataObject commentData) {
        String commentID = commentData.getValue(DBComment.K_COMMENT_ID);
        card.setCardData(
                commentData.getValue(DBUser.K_USER_NAME),
                commentData.getValue(DBUser.K_USER_PROFILE),
                commentData.getValue(DBComment.K_COMMENT), commentID);

        // if self viewing comment then don't set contentAction
        if (commentData.getValue(DBComment.K_USER_ID).equals(getUserID())) {
            card.setContentText(commentData.getValue(DBComment.K_COMMENT_SCORE));
            return;
        }

        card.setContentAction(
                DBCFeedback.isFeedbackUseful(commentData.getValue(DBCFeedback.K_FEEDBACK)),
                feedback -> {
                    DBCFeedback.giveFeedback(getUserID(), commentID, feedback);
                    refresh();
                }, commentData.getValue(DBComment.K_COMMENT_SCORE));
    }

    public void setContentHead(String title, String contentBody) {
        this.contentHead.setText(title + contentBody);
        this.contentHead.setName(contentBody);
    }

    public void setContentUsername(String title, String contentUsername, String contentUserprofile) {
        this.contentUsername.setText(title + contentUsername);
        this.contentUsername.setProfileIcon(contentUserprofile, ResourceManager.MINI);
    }

    public void setContentBytes(String contentBytes) {
        this.contentBytes.setText(contentBytes);
    }

    public void setContentUserID(String contentUserID) {
        contentUsername.setName(contentUserID);
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public void setVoteType(String voteType) {
        voteContent(voteType, false);
    }

    public BoardResponseCardPanel getContentResponseCardPanel() {
        return contentResponseCardPanel;
    }

    public String getContentHead() {
        return this.contentHead.getName();
    }

    public String getContentBody() {
        return this.contentBody.getText();
    }

    public void setContentBody(String contentBody) {
        this.contentBody.setText(contentBody);
        bodyScrollPane.resetScroll();
    }

    public void setSelfViewer(boolean isSelfViewer) {
        voteButtonsPanel.setVisible(!isSelfViewer);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @FunctionalInterface
    public interface UpdateVoteDatabase {
        void invoke(String contentID, String userID, String voteType);
    }
}
