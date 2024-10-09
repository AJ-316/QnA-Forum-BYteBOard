package BYteBOardInterface.BoardsPackage.AuthenticationPackage;

import BYteBOardInterface.StructurePackage.MainFrame;

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
