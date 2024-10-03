package BYteBOardInterface.BoardsPackage.QnAForumPackage.AskBoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBQuestion;
import BYteBOardDatabase.DBTag;
import BYteBOardDatabase.DBUser;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class AskBoardFrame extends BoardFrame {

    private AskInputPanel askInputPanel;
    private BoardPanel sidePanel;

    private BoardLabel usernameLabel;
    private BoardButton submitButton;
    private BoardButton backButton;
    private BoardButton previewButton;

    private String userID;

    public AskBoardFrame(MainFrame main) {
        super(main, (delegate, context) -> {
            String userID = delegate.getContextOrDefault(context, DBUser.TABLE, DBUser.K_USER_ID)[0];
            delegate.putContext(DBUser.TABLE, DBUser.getUser(userID));
            return null;
        });
    }

    public void init(MainFrame main) {
        askInputPanel = new AskInputPanel(this);
        sidePanel = getSidePanel(askInputPanel);

        add(askInputPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.WEST);
    }

    private BoardPanel getSidePanel(AskInputPanel askInputPanel) {
        BoardPanel panel = new BoardPanel(this, ByteBoardTheme.MAIN);
        panel.setCornerRadius(90);
        panel.addInsets(30);
        GridBagBuilder builder = new GridBagBuilder(panel, 1);

        usernameLabel = new BoardLabel("Username");
        usernameLabel.setProfileIcon("0", ResourceManager.REGULAR);
        usernameLabel.setHorizontalTextPosition(BoardLabel.CENTER);
        usernameLabel.setVerticalTextPosition(BoardLabel.BOTTOM);
        usernameLabel.setVerticalAlignment(SwingConstants.TOP);
        usernameLabel.setFGLight();
        usernameLabel.addInsets(35);

        submitButton = new BoardButton("   Submit", "submit");
        submitButton.setAlignmentLeading();
        submitButton.setFGLight();
        submitButton.addActionListener(e -> submitQuestion());

        previewButton = new BoardButton("   Preview", "show");
        previewButton.setAlignmentLeading();
        previewButton.setFGLight();
        previewButton.addActionListener(e -> {
            boolean isPreviewVisible = askInputPanel.togglePreview();
            previewButton.setText(isPreviewVisible ? "   Edit" : "   Preview");
            previewButton.setIcon(isPreviewVisible ? "edit" : "show");
        });

        backButton = new BoardButton("   Profile", "home");
        backButton.setAlignmentLeading();
        backButton.setFGLight();
        backButton.addActionListener(e -> exitAskFrame());

        builder.weight(1, 1).fillBoth().insets(10)
                .addToNextCell(usernameLabel)
                .weightY(0)
                .addToNextCell(previewButton)
                .addToNextCell(submitButton)
                .addToNextCell(backButton);

        return panel;
    }

    private void submitQuestion() {
        if(!askInputPanel.validateTags() || !askInputPanel.validateQuestion()) return;

        String questionID = DBQuestion.addQuestion(askInputPanel.getHead(), askInputPanel.getBody(), getUserID());

        askInputPanel.clearTagInputError();
        for (BoardTagButton boardTagButton : askInputPanel.getTagButtons()) {
            DBTag.addTag(boardTagButton.getText(), questionID);
        }

        askInputPanel.clearQuestion();
        exitAskFrame();
    }

    private void exitAskFrame() {
        requestSwitchFrame(ProfileBoardFrame.class, getUserID());
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate delegate) {
        DBDataObject userData = delegate.getContext(DBUser.TABLE);
        userID = userData.getValue(DBUser.K_USER_ID);

        usernameLabel.setText(userData.getValue(DBUser.K_USER_NAME));
        usernameLabel.setProfileIcon(userData.getValue(DBUser.K_USER_PROFILE), ResourceManager.REGULAR);
    }

    protected String getUserID() {
        return userID;
    }
}
