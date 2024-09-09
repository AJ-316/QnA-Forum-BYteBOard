package BoardStructurePackage;

import BoardStructurePackage.BoardsPackage.AuthenticationPackage.AuthenticationMainFrame;
import BoardStructurePackage.BoardsPackage.QnAForumPackage.QnAForumMainFrame;
import DatabasePackage.DatabaseManager;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class MainFrame extends JFrame {

    private static final MainFrame[] MAIN_FRAMES = new MainFrame[2];
    private static int FRAMES_ID_COUNTER = 0;
    protected static int generateMainFrameID() { return FRAMES_ID_COUNTER++; }

    private final Map<String, Frame> boardFrames;
    private final Container contentPane;

    public MainFrame(String title, int height, float ratio, boolean isVisible, int id) {
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension((int) (height*ratio), height));
        setLocationRelativeTo(null);
        setVisible(isVisible);

        boardFrames = new HashMap<>();

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        MAIN_FRAMES[id] = this;

        setLayout(new BorderLayout());
        init();
    }

    public void addBoardFrame(BoardFrame frame) {
        boardFrames.put(frame.getClass().getSimpleName(), frame);
    }

    public void setBoardFrame(String frameName, String switchContext) {
        Frame frame = boardFrames.get(frameName);

        if (frame == null) return;

        frame.setContext(switchContext);

        contentPane.removeAll();
        contentPane.add(frame.getBoardFrame(), BorderLayout.CENTER);

        contentPane.revalidate();
        contentPane.repaint();

        ((BoardFrame)frame).requestFocus();
    }

    protected abstract void init();

    public static void initMainFrames() {
        EventQueue.invokeLater(() -> {
            new AuthenticationMainFrame(false);
            new QnAForumMainFrame(false);

            MAIN_FRAMES[QnAForumMainFrame.ID].prepareMainFrame("1");
            MAIN_FRAMES[QnAForumMainFrame.ID].setVisible(true);
        });
    }

    protected abstract void prepareMainFrame(String switchBoardFrameContext);
    public abstract void restartMainFrame();

    public void switchMainFrame(int mainFrameID, String switchBoardFrameContext) {
        this.setVisible(false);
        this.dispose();

        MAIN_FRAMES[mainFrameID].prepareMainFrame(switchBoardFrameContext);
        MAIN_FRAMES[mainFrameID].setVisible(true);
    }

    public static void main(String[] args) {
        ResourceManager.init();
        DatabaseManager.init();
        MainFrame.initMainFrames();
    }

}
