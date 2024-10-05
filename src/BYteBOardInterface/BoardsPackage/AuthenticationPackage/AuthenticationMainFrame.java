package BYteBOardInterface.BoardsPackage.AuthenticationPackage;

import BYteBOardInterface.BoardsPackage.QnAForumPackage.AskBoardPackage.AskBoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage.ProfileBoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage.QnABoardFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.SearchBoardPackage.SearchBoardFrame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLoader;

import java.awt.*;

public class AuthenticationMainFrame extends MainFrame {

    public AuthenticationMainFrame(String... switchContext) {
        super("BYteBOard Authentication", 720, 16 / 9f, switchContext);
    }

    protected void init(String... switchContext) {

        createBoardFrames(new Class<?>[] { AuthenticateBoardFrame.class }, switchContext);
    }

    protected void prepareMainFrame(String... switchBoardFrameContext) {
        setBoardFrame(AuthenticateBoardFrame.class, switchBoardFrameContext);
    }

    @Override
    public String getSwitchLoadingText() {
        return "Initializing QnA Forum...";
    }

    public void restartMainFrame() {
        super.restartMainFrame(getBoardFrame(AuthenticateBoardFrame.class).recoverContext());
    }
}
