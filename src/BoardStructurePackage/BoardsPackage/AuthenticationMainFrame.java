package BoardStructurePackage.BoardsPackage;

import BoardStructurePackage.MainFrame;

import java.awt.*;

public class AuthenticationMainFrame extends MainFrame {

    private static AuthenticationMainFrame INSTANCE;

    public AuthenticationMainFrame(String title, boolean isVisible) {
        super(title, 720, 16/9f, isVisible);
    }

    public static void init(boolean isVisible) {
        EventQueue.invokeLater(() -> {
            INSTANCE = new AuthenticationMainFrame("BYteBOard Authentication", isVisible);
            INSTANCE.setLayout(new BorderLayout());

            new LoginBoardFrame(INSTANCE);
            INSTANCE.setBoardFrame(LoginBoardFrame.class.getSimpleName(), null);
        });
    }

    public static void authenticateUser(String userID) {
        System.out.println("User authenticated: " + userID);
    }

}
