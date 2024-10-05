package BYteBOardInterface.BoardsPackage.QnAForumPackage;

import BYteBOardInterface.BoardsPackage.QnAForumPackage.AskBoardPackage.AskBoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.QnABoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage.SearchBoardFrame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardFrameLoader;
import CustomControls.BoardLoader;

import javax.swing.*;

public class QnAForumMainFrame extends MainFrame {

    public QnAForumMainFrame(String... switchContext) {
        super("BYteBOard QnA-Forum", 720, 16 / 9f, switchContext);
    }

    protected void init(String... switchContext) {
        createBoardFrames(new Class<?>[] {
                ProfileBoardFrame.class,
                QnABoardFrame.class,
                SearchBoardFrame.class,
                AskBoardFrame.class
        }, switchContext);
    }

    protected void prepareMainFrame(String... switchBoardFrameContext) {
        setBoardFrame(ProfileBoardFrame.class, switchBoardFrameContext);
    }

    public String getSwitchLoadingText() {
        return "Exiting...";
    }

    public void restartMainFrame() {
        super.restartMainFrame(getBoardFrame(ProfileBoardFrame.class).recoverContext());
    }
}
