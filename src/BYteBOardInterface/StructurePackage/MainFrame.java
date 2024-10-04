package BYteBOardInterface.StructurePackage;

import BYteBOardDatabase.DatabaseManager;
import BYteBOardInterface.BoardsPackage.AuthenticationPackage.AuthenticationMainFrame;
import BYteBOardInterface.BoardsPackage.QnAForumPackage.QnAForumMainFrame;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class MainFrame extends JFrame {

    private final Map<String, Frame> boardFrames;
    private final Container contentPane;
    private static MainFrame currentMainFrame;

    public MainFrame(String title, int height, float ratio) {
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension((int) (height * ratio), height));
        setLocationRelativeTo(null);
        setVisible(false);
        getContentPane().setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        boardFrames = new HashMap<>();

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        setLayout(new BorderLayout());
        init();
    }

    public static void initMainFrames() {
        EventQueue.invokeLater(() -> {
            createMainFrames();

            switchMainFrame(AuthenticationMainFrame.class);
        });
    }

    protected static void createMainFrames() {
        if(currentMainFrame != null)
            currentMainFrame.dispose();
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

    public void restartMainFrame(Class<?> mainFrameClass, String recoverContext) {
        dispose();
        EventQueue.invokeLater(() -> {
            createMainFrames();
            switchMainFrame(mainFrameClass, recoverContext);
        });
    }

    protected static void switchMainFrame(Class<?> mainFrameClass, String... switchBoardFrameContext) {
        if(currentMainFrame != null) {
            currentMainFrame.setVisible(false);
            currentMainFrame.dispose();
            currentMainFrame = null;
        }

        try {
            currentMainFrame = (MainFrame) mainFrameClass.getDeclaredConstructor().newInstance();
            currentMainFrame.prepareMainFrame(switchBoardFrameContext);
            currentMainFrame.setVisible(true);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
