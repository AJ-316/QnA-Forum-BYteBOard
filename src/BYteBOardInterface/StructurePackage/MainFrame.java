package BYteBOardInterface.StructurePackage;

import BYteBOardDatabase.DatabaseManager;
import BYteBOardInterface.BoardsPackage.AuthenticationPackage.AuthenticationMainFrame;
import BoardControls.BoardFrameLoader;
import BoardControls.BoardLoader;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class MainFrame extends JFrame {

    private static MainFrame currentMainFrame;
    private final Map<String, Frame> boardFrames;
    private final Container contentPane;

    public MainFrame(String title, int height, float ratio, String... switchBoardFrameContext) {
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
        init(switchBoardFrameContext);
    }

    public static void main(String[] args) {
        BoardLoader.start(() -> switchMainFrame("Setting up Authentication Form...", AuthenticationMainFrame.class));

        ResourceManager.init();
        DatabaseManager.init();
    }

    protected static void switchMainFrame(String switchLoadText, Class<?> mainFrameClass, String... switchBoardFrameContext) {

        if (currentMainFrame != null) {
            currentMainFrame.setVisible(false);
            currentMainFrame.dispose();
            currentMainFrame = null;
        }

        BoardLoader.start(MainFrame::showCurrentMainFrame);
        BoardLoader.setText(switchLoadText);

        EventQueue.invokeLater(() -> {
            try {
                currentMainFrame = (MainFrame) mainFrameClass.getDeclaredConstructor(String[].class).newInstance((Object) switchBoardFrameContext);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                BoardLoader.forceStop("Error Initializing", e.getMessage());
            }
        });
    }

    public static void showCurrentMainFrame() {
        if (currentMainFrame == null) return;
        currentMainFrame.setVisible(true);
    }

    public void createBoardFrames(Class<?>[] boardFrames, String... switchContext) {
        BoardFrameLoader.createLoader(this, boardFrames, () -> {
            prepareMainFrame(switchContext);
        });
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

    protected abstract void init(String... switchBoardFrameContext);

    protected abstract void prepareMainFrame(String... switchBoardFrameContext);

    public abstract void restartMainFrame();

    public void restartMainFrame(String recoverContext) {
        EventQueue.invokeLater(() -> switchMainFrame("Restarting...", getClass(), recoverContext));
    }

    public String getSwitchLoadingText() {
        return "Switching Frame";
    }
}
