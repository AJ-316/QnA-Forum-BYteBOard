package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardDatabase.DBOperation;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.QnABoardFrame;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.BoardLabel;
import BoardControls.BoardTextArea;
import BoardControls.UIPackage.GridBagBuilder;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardContentDisplayPane extends BoardPanel {

    private BoardTextArea contentText;
    private BoardLabel contentResponseText;
    private BoardLabel contentBytes;
    private BoardLabel contentUsername;
    private String userID;

    public BoardContentDisplayPane(Frame frame) {
        super(frame, ByteBoardTheme.MAIN_LIGHT);
        addListeners();
    }

    public void init(Frame frame) {
        setCornerRadius(80);
        setLimitRadius(false);
        addInsets(20);

        initComponents();

        BoardPanel footerPanel = new BoardPanel(frame);
        footerPanel.setCornerRadius(60);
        GridBagBuilder footerBuilder = new GridBagBuilder(footerPanel, 3);

        footerBuilder.weight(0, 1).fillBoth()
                .addToNextCell(contentBytes)
                .weightX(1)
                .addToNextCell(contentResponseText)
                .addToNextCell(contentUsername);

        GridBagBuilder builder = new GridBagBuilder(this, 1);

        builder.weight(1, 1).fillHorizontal()
                .addToNextCell(contentText)
                .fillBoth().weightY(0)
                .addToNextCell(footerPanel);

        EventQueue.invokeLater(() -> {
            setPreferredSize(new Dimension(600, 150));
            if (getParent() != null)
                getParent().revalidate();
        });
    }

    private void initComponents() {
        contentUsername = new BoardLabel("Username");
        contentUsername.setFGLight();
        contentUsername.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        contentUsername.setHorizontalTextPosition(SwingConstants.LEFT);
        contentUsername.setAlignmentTrailing();

        contentBytes = new BoardLabel("517465", "bytescore_icon");
        contentBytes.setFGLight();
        contentBytes.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        contentBytes.setAlignmentLeading();

        contentText = new BoardTextArea("Content goes here, and it could be very long one so test it before moving on.");
        contentText.setFGLight();
        contentText.setFontPrimary(ByteBoardTheme.FONT_T_BOLD, 26);

        contentResponseText = new BoardLabel("Responses: 40");
        contentResponseText.setFGLight();
        contentResponseText.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);
        contentResponseText.addInsets(0, 20, 0, 0);
        contentResponseText.setAlignmentLeading();
    }

    public void setUserData(String userProfile, String username, String userID) {
        contentUsername.setProfileIcon(userProfile, ResourceManager.MINI);
        contentUsername.setText(username);

        this.userID = userID;
    }

    public void setContentData(String contentText, String contentID, String contentResponseText, String bytes) {
        this.contentText.setText(contentText);
        this.contentText.setName(contentID);
        this.contentResponseText.setText(contentResponseText);

        contentBytes.setText(DBOperation.formatBytes(bytes, true));
    }

    public String getContentID() {
        return contentText.getName();
    }

    public String getUserID() {
        return userID;
    }

    private void addListeners() {
        MouseAdapter adapter = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                setBackground(ByteBoardTheme.MAIN_LIGHT);
                requestSwitchFrame(QnABoardFrame.class, getContentID(), getUserID());
            }

            public void mouseEntered(MouseEvent e) {
                setBackground(ByteBoardTheme.ACCENT);
            }

            public void mouseExited(MouseEvent e) {
                setBackground(ByteBoardTheme.MAIN_LIGHT);
            }
        };
        addMouseListener(adapter);
        contentText.addMouseListener(adapter);
    }
}
