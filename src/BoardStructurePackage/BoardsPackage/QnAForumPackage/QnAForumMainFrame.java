package BoardStructurePackage.BoardsPackage.QnAForumPackage;

import BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BoardStructurePackage.MainFrame;

import java.awt.*;

public class QnAForumMainFrame extends MainFrame {

    public static final int ID = generateMainFrameID();

    public QnAForumMainFrame(boolean isVisible) {
        super("BYteBOard QnA-Forum", 720, 16/9f, isVisible, ID);
    }

    protected void init() {
        new ProfileBoardFrame(this);
    }

    public void restartMainFrame() {
        dispose();
        EventQueue.invokeLater(() -> new QnAForumMainFrame(true));
    }

    protected void prepareMainFrame(String switchBoardFrameContext) {
        setBoardFrame(ProfileBoardFrame.class.getSimpleName(), switchBoardFrameContext);
    }
}
