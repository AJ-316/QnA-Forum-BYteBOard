package BYteBOardInterface.StructurePackage;

import BYteBOardDatabase.DatabaseManager;
import BYteBOardInterface.BoardsPackage.AuthenticationPackage.AuthenticationMainFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnAForumMainFrame;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class MainFrame extends JFrame {

    private static final MainFrame[] MAIN_FRAMES = new MainFrame[2];
    private static int FRAMES_ID_COUNTER = 0;
    private final Map<String, Frame> boardFrames;
    private final Container contentPane;
    public MainFrame(String title, int height, float ratio, int id) {
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension((int) (height * ratio), height));
        setLocationRelativeTo(null);
        setVisible(false);
        getContentPane().setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        boardFrames = new HashMap<>();

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        MAIN_FRAMES[id] = this;

        setLayout(new BorderLayout());
        init();
    }

    protected static int generateMainFrameID() {
        return FRAMES_ID_COUNTER++;
    }

    public static void initMainFrames() {
        EventQueue.invokeLater(() -> {
            createMainFrames();

            MAIN_FRAMES[QnAForumMainFrame.ID].prepareMainFrame("1");
            MAIN_FRAMES[QnAForumMainFrame.ID].setVisible(true);
        });
    }

    protected static void createMainFrames() {
        for (int i = 0; i < MAIN_FRAMES.length; i++) {
            if (MAIN_FRAMES[i] != null) {
                MAIN_FRAMES[i].dispose();
                MAIN_FRAMES[i] = null;
            }
        }

        new AuthenticationMainFrame();
        new QnAForumMainFrame();
    }

    public static void main(String[] args) {
        ResourceManager.init();
        DatabaseManager.init();
        MainFrame.initMainFrames();
    }

    public void addBoardFrame(BoardFrame frame) {
        boardFrames.put(frame.getClass().getSimpleName(), frame);
    }

    public void setBoardFrame(Class<?> frameClass, String... switchContext) {
        Frame frame = boardFrames.get(frameClass.getSimpleName());

        if (frame == null)
            return;

        frame.setContext(switchContext);

        if (!contentPane.isAncestorOf(frame.getBoardFrame())) {
            contentPane.removeAll();
            contentPane.add(frame.getBoardFrame(), BorderLayout.CENTER);

            contentPane.revalidate();
            contentPane.repaint();
        }

        ((BoardFrame) frame).requestFocus();
    }

    public Frame getBoardFrame(Class<?> frameClass) {
        return boardFrames.get(frameClass.getSimpleName());
    }

    protected abstract void init();

    protected abstract void prepareMainFrame(String... switchBoardFrameContext);

    public abstract void restartMainFrame();

    public void restartMainFrame(int mainFrameID, String recoverContext) {
        dispose();
        EventQueue.invokeLater(() -> {
            createMainFrames();
            switchMainFrame(mainFrameID, recoverContext);
        });
    }

    public void switchMainFrame(int mainFrameID, String... switchBoardFrameContext) {
        this.setVisible(false);
        this.dispose();

        MAIN_FRAMES[mainFrameID].prepareMainFrame(switchBoardFrameContext);
        MAIN_FRAMES[mainFrameID].setVisible(true);
    }
}
