package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import CustomControls.*;
import CustomControls.CustomRendererPackage.RoundedBorder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.Map;

public class BoardContentPanel extends BoardPanel {

    private final UpdateVoteDatabase voteUpdate;
    protected BoardPanel contentStyledBodyPanel;
    protected BoardPanel contentBodyPanel;
    protected BoardLabel contentBytes;
    protected BoardTextPane contentStyledBody;
    protected BoardTextArea contentBody;
    private String userID;
    private String contentID;
    private VoteButton upVoteButton;
    private VoteButton downVoteButton;
    private BoardPanel voteButtonsPanel;
    private BoardTagsDisplayPanel tagsDisplayPanel;
    private BoardLabel contentUsername;
    private BoardTextArea contentHead;
    private SimpleScrollPane bodyStyledScrollPane;
    private BoardResponseCardPanel contentResponseCardPanel;

    public BoardContentPanel(Frame frame, UpdateVoteDatabase voteUpdate) {
        super(frame);
        this.voteUpdate = voteUpdate;

        setPreferredSize(new Dimension(900, getPreferredSize().height));
        setMaximumSize(new Dimension(900, getPreferredSize().height));
        setMinimumSize(new Dimension(900, getPreferredSize().height));

        addResponseButtonListeners();
    }

    public void init(Frame frame) {
        initComponents(frame);

        BoardPanel headerPanel = getContentHeadPanel(frame);
        contentStyledBodyPanel = getBodyPanel(frame, contentStyledBody,
                (bodyStyledScrollPane = new SimpleScrollPane(contentStyledBody)), ByteBoardTheme.MAIN_LIGHT, ByteBoardTheme.MAIN_DARK);

        contentBodyPanel = getBodyPanel(frame, contentBody,
                new SimpleScrollPane(contentBody), ByteBoardTheme.MAIN_DARK, ByteBoardTheme.ACCENT);


        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.weightX(1).fillBoth()
                .addToNextCell(headerPanel)
                .weightY(1)
                .addToNextCell(contentStyledBodyPanel)
                .addToCurrentCell(contentBodyPanel);

        setEditableInput(false);
    }

    protected void setEditableInput(boolean isEditable) {
        contentStyledBodyPanel.setVisible(!isEditable);
        contentBodyPanel.setVisible(isEditable);
    }

    private BoardPanel getContentHeadPanel(Frame frame) {
        BoardPanel panel = new BoardPanel(frame, ByteBoardTheme.MAIN);
        panel.setCornerRadius(90);
        panel.addInsets(20);

        voteButtonsPanel = new BoardPanel(frame);
        GridBagBuilder votePanelBuilder = new GridBagBuilder(voteButtonsPanel, 1);
        votePanelBuilder.insets(10, 5, 10, 5)
                .addToNextCell(upVoteButton)
                .addToNextCell(downVoteButton);

        BoardPanel headHeaderPane = new BoardPanel(frame);
        GridBagBuilder HeadHeaderPaneLayout = new GridBagBuilder(headHeaderPane, 3);
        HeadHeaderPaneLayout.fillBoth().insets(10)
                .addToNextCell(contentBytes).weight(1, 1)
                .addToNextCell(tagsDisplayPanel.getComponent()).weight(0, 0)
                .addToNextCell(contentUsername);

        BoardPanel contentHeadPanel = new BoardPanel(frame, ByteBoardTheme.MAIN_LIGHT);
        contentHeadPanel.setLayout(new BorderLayout());
        contentHeadPanel.addInsets(20);
        contentHeadPanel.setCornerRadius(90);
        contentHeadPanel.setBorderColor(ByteBoardTheme.MAIN_DARK);
        contentHeadPanel.add(contentHead);

        BoardPanel headFooterPane = new BoardPanel(frame);
        GridBagBuilder headFooterPaneLayout = new GridBagBuilder(headFooterPane, 2);
        headFooterPaneLayout.insets(10)
                .addToNextCell(voteButtonsPanel);
        headFooterPaneLayout.weight(1, 1).fillBoth()
                .addToNextCell(contentHeadPanel);

        panel.setLayout(new BorderLayout());
        panel.add(headHeaderPane, BorderLayout.NORTH);
        panel.add(headFooterPane, BorderLayout.CENTER);
        return panel;
    }

    protected void initComponents(Frame frame) {
        tagsDisplayPanel = new BoardTagsDisplayPanel(frame);
        tagsDisplayPanel.setHorizontalDisplay();

        contentResponseCardPanel = new BoardResponseCardPanel(frame);
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
        contentHead.addInsets(10);
        contentHead.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 26);

        contentStyledBody = new BoardTextPane(frame, ByteBoardTheme.MAIN);
        contentBody = new BoardTextArea("");
        contentBody.setEditable(true);
    }

    private BoardPanel getBodyPanel(Frame frame, Component component, SimpleScrollPane scrollPane, String bgColor, String borderColor) {
        BoardPanel panel = new BoardPanel(frame, ByteBoardTheme.MAIN);
        panel.setLayout(new BorderLayout());
        panel.setCornerRadius(90);
        panel.addInsets(20);

        ((CustomControl)component).setBackground(bgColor);

        scrollPane.setOpaque(false);
        scrollPane.setBorder(new RoundedBorder(60, 60, 30,
                ResourceManager.getColor(bgColor),
                ResourceManager.getColor(borderColor)));
        scrollPane.getViewport().setBackground(ResourceManager.getColor(bgColor));

        panel.add(scrollPane);
        return panel;
    }

    private void getBodyScrollPane(Component component, String bgColor, String borderColor) {
        SimpleScrollPane bodyScrollPane = new SimpleScrollPane(component);
        bodyScrollPane.setOpaque(false);
        bodyScrollPane.setBorder(new RoundedBorder(60, 60, 30,
                ResourceManager.getColor(bgColor),
                ResourceManager.getColor(borderColor)));
        bodyScrollPane.getViewport().setBackground(ResourceManager.getColor(bgColor));
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
            tagsDisplayPanel.addTag(tagDataObject, userID);
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
        BoardResponseCard card = new BoardResponseCard(getFrame());
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

    public String getContentStyledBody() {
        return this.contentStyledBody.getText();
    }

    public String getContentBody() {
        return contentBody.getText();
    }

    public void setContentStyledBody(String contentStyledBody) {
        this.contentStyledBody.setTextWithStyles(contentStyledBody);
        bodyStyledScrollPane.resetScroll();
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
