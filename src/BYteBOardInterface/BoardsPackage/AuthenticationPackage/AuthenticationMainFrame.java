package BYteBOardInterface.BoardsPackage.AuthenticationPackage;

import BYteBOardInterface.StructurePackage.MainFrame;

public class AuthenticationMainFrame extends MainFrame {

    public static final int ID = generateMainFrameID();

    public AuthenticationMainFrame() {
        super("BYteBOard Authentication", 720, 16/9f, ID);
    }

    protected void init() {
        new AuthenticateBoardFrame(this);
    }

    public void restartMainFrame() {
        super.restartMainFrame(AuthenticationMainFrame.ID, getBoardFrame(AuthenticateBoardFrame.class).recoverContext());
    }

    public void prepareMainFrame(String... switchBoardFrameContext) {
        setBoardFrame(AuthenticateBoardFrame.class, switchBoardFrameContext);
    }
}
