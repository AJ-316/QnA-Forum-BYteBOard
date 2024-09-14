package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.*;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import CustomControls.CustomRendererPackage.RoundedBorder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class BoardContentPanel extends BoardPanel {

    private String contentID;

    private VoteButton upVoteButton;
    private VoteButton downVoteButton;
    private BoardPanel voteButtonsPanel;

    private BoardTagsDisplayPanel tagsDisplayPanel;
    private BoardLabel contentUsername;
    private BoardLabel contentBytes;
    private BoardTextArea contentHead;
    private BoardTextArea contentBody;
    private SimpleScrollPane bodyScrollPane;

    private final UpdateVoteDatabase voteUpdate;

    private BoardContentResponsePanel contentCommentPanel;

    public BoardContentPanel(MainFrame main, Frame frame, UpdateVoteDatabase voteUpdate) {
        super(main, frame);
        this.voteUpdate = voteUpdate;

        addButtonListeners();
        setPreferredSize(new Dimension(900, getPreferredSize().height));
        setMaximumSize(new Dimension(900, getPreferredSize().height));
        setMinimumSize(new Dimension(900, getPreferredSize().height));
    }

    public void init(MainFrame main, Frame frame) {
        initComponents(main, frame);

        BoardPanel headerPanel = getContentHeadPanel(main, frame);
        BoardPanel bodyPanel = getContentBodyPanel(main, frame);

        GridBagBuilder builder = new GridBagBuilder(this, 1);
        builder.weightX(1).fillBoth()
                .addToNextCell(headerPanel)
                .weightY(1)
                .addToNextCell(bodyPanel);
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
        contentHeadPanel.setBorderColor(ByteBoardTheme.BASE);
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

    private void initComponents(MainFrame main, Frame frame) {
        tagsDisplayPanel = new BoardTagsDisplayPanel(main, frame);
        tagsDisplayPanel.setHorizontalDisplay();


        contentCommentPanel = new BoardContentResponsePanel(main, frame);
        contentCommentPanel.setTitle("Comments", "comment", "No Comments!");

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
                ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT), null));

        bodyScrollPane.getViewport().setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));

        panel.add(bodyScrollPane);
        return panel;
    }

    private void addButtonListeners() {
        upVoteButton.addActionListener(e -> voteContent(upVoteButton.getName(), true));
        downVoteButton.addActionListener(e -> voteContent(downVoteButton.getName(), true));
    }

    private void voteContent(String voteType, boolean updateDatabase) {
        upVoteButton.select(voteType.equals(DBVote.V_VOTE_UP));
        downVoteButton.select(voteType.equals(DBVote.V_VOTE_DOWN));

        if(updateDatabase) {
            voteUpdate.invoke(getContentID(), voteType);
            refresh();
        }
    }

    public void setContentTags(DBDataObject[] tagDataObjectList) {
        tagsDisplayPanel.clearTags();

        for (DBDataObject tagDataObject : tagDataObjectList) {
            tagsDisplayPanel.addTag(tagDataObject.getValue(DBTag.K_TAG));
        }
    }

    public void setContentComments(DBDataObject[] commentDataObjectList, String userID) {
        contentCommentPanel.clearContentCards();

        for (DBDataObject commentData : commentDataObjectList) {
            BoardContentCard card = contentCommentPanel.addContentCard();

            String commentID = commentData.getValue(DBComment.K_COMMENT_ID);
            card.setCardData(
                    commentData.getValue(DBUser.K_USER_NAME),
                    commentData.getValue(DBUser.K_USER_PROFILE),
                    commentData.getValue(DBComment.K_COMMENT), commentID);

            if(commentData.getValue(DBComment.K_USER_ID).equals(userID)) {
                card.setContentText(commentData.getValue(DBComment.K_COMMENT_SCORE));
                continue;
            }

            card.setContentAction(
                    DBCFeedback.isFeedbackUseful(commentData.getValue(DBCFeedback.K_FEEDBACK)),
                    feedback -> {
                        DBCFeedback.giveFeedback(userID, commentID, feedback);
                        refresh();
                    }, commentData.getValue(DBComment.K_COMMENT_SCORE), "useful");
        }

        contentCommentPanel.resetScrolls();
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public void setContentHead(String title, String contentBody) {
        this.contentHead.setText(title + contentBody);
        this.contentHead.setName(contentBody);
    }

    public void setContentBody(String contentBody) {
        this.contentBody.setText(contentBody);
        bodyScrollPane.resetScroll();
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

    public void setVoteType(String voteType) {
        voteContent(voteType, false);
    }

    public BoardContentResponsePanel getContentCommentPanel() {
        return contentCommentPanel;
    }

    public String getContentHead() {
        return this.contentHead.getName();
    }

    public void setSelfViewer(boolean isSelfViewer) {
        voteButtonsPanel.setVisible(!isSelfViewer);
    }

    /*public void setSelfViewer(boolean isSelfViewer) {
        upVoteButton.setEnabled(!isSelfViewer);
        downVoteButton.setEnabled(!isSelfViewer);
    }*/

    @FunctionalInterface
    public interface UpdateVoteDatabase {
        void invoke(String contentID, String voteType);
    }
}
