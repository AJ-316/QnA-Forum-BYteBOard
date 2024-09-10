package BYteBOardInterface.BoardsPackage.QnAForumPackage;

import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.QnABoardFrame;
import BYteBOardInterface.StructurePackage.MainFrame;

import java.awt.*;

public class QnAForumMainFrame extends MainFrame {

    public static final int ID = generateMainFrameID();

    public QnAForumMainFrame() {
        super("BYteBOard QnA-Forum", 720, 16/9f, ID);
    }

    protected void init() {
        new ProfileBoardFrame(this);
        new QnABoardFrame(this);
    }

    public void restartMainFrame() {
        super.restartMainFrame(QnAForumMainFrame.ID, getBoardFrame(ProfileBoardFrame.class).recoverContext());
    }

    protected void prepareMainFrame(String switchBoardFrameContext) {
        setBoardFrame(ProfileBoardFrame.class, switchBoardFrameContext);
    }
}
