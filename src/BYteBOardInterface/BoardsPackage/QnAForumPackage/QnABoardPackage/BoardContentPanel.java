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

        BoardPanel votePanel = new BoardPanel(main, frame);
        votePanel.addInsets(10);
        GridBagBuilder votePanelBuilder = new GridBagBuilder(votePanel, 1);
        votePanelBuilder.insets(10, 20, 10, 20)
                .addToNextCell(upVoteButton)
                .addToNextCell(downVoteButton);

        BoardPanel userBottomPanel = new BoardPanel(main, frame);
        userBottomPanel.addInsets(10);
        GridBagBuilder userBottomPanelBuilder = new GridBagBuilder(userBottomPanel, 2);
        userBottomPanelBuilder.weightX(1).fillBoth()
                .addToNextCell(contentBytes)
                .addToNextCell(contentUsername);

        BoardPanel contentHeadPanel = new BoardPanel(main, frame, ByteBoardTheme.MAIN_LIGHT);
        contentHeadPanel.setCornerRadius(90);
        contentHeadPanel.addInsets(20);
        contentHeadPanel.setLayout(new BorderLayout());
        contentHeadPanel.add(contentHead);

        GridBagBuilder panelBuilder = new GridBagBuilder(panel, 2);

        panelBuilder.gridHeight(2).weightY(1).fillVertical()
                .addToNextCell(votePanel);

        panelBuilder.gridHeight(1).weightX(1).fillHorizontal()
                .addToNextCell(contentHeadPanel).skipCells(1);

        panelBuilder.weight(1, 0).fillBoth()
                .addToNextCell(userBottomPanel);

        return panel;
    }

    private void initComponents(MainFrame main, Frame frame) {
        contentCommentPanel = new BoardContentResponsePanel(main, frame, ByteBoardTheme.MAIN);
        contentCommentPanel.setTitle("Comments", "No Comments!");

        upVoteButton = new VoteButton("upvote", ResourceManager.SMALL, DBVote.V_VOTE_UP);
        downVoteButton = new VoteButton("downvote", ResourceManager.SMALL, DBVote.V_VOTE_DOWN);

        contentBytes = new BoardLabel("123456", "bytescore_icon");
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

    public void setContentComments(DBDataObject[] commentDataObjects, String userID) {
        contentCommentPanel.clearContentCards();

        for (DBDataObject commentData : commentDataObjects) {
            BoardContentCard card = contentCommentPanel.addContentCard();

            String commentID = commentData.getValue(DBComment.K_COMMENT_ID);
            card.setCardData(
                    commentData.getValue(DBUser.K_USER_NAME),
                    commentData.getValue(DBUser.K_USER_PROFILE),
                    commentData.getValue(DBComment.K_COMMENT), commentID);
            card.setContentAction(
                    DBCFeedback.isFeedbackUseful(commentData.getValue(DBCFeedback.K_FEEDBACK)),
                    feedback -> {
                        DEBUG.printlnRed("Liked: " + feedback);
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

    @FunctionalInterface
    public interface UpdateVoteDatabase {
        void invoke(String contentID, String voteType);
    }
}
