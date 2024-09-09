package BoardStructurePackage.BoardsPackage.AuthenticationPackage;

import BoardStructurePackage.MainFrame;

import java.awt.*;

public class AuthenticationMainFrame extends MainFrame {

    public static final int ID = generateMainFrameID();

    public AuthenticationMainFrame(boolean isVisible) {
        super("BYteBOard Authentication", 720, 16/9f, isVisible, ID);
    }

    protected void init() {
        new AuthenticateBoardFrame(this);
    }

    public void restartMainFrame() {
        dispose();
        new AuthenticationMainFrame(true);
    }

    public void prepareMainFrame(String switchBoardFrameContext) {
        setBoardFrame(AuthenticateBoardFrame.class.getSimpleName(), null);
    }
}
