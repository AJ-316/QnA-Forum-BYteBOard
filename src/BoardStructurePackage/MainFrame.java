package BoardStructurePackage;

import BoardStructurePackage.BoardsPackage.AuthenticationMainFrame;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class MainFrame extends JFrame {

    private final Map<String, Frame> boardFrames;
    private final Container contentPane;

    public MainFrame(String title, int height, float ratio, boolean isVisible) {
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension((int) (height*ratio), height));
        setVisible(isVisible);

        boardFrames = new HashMap<>();

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
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
    }

    public static void main(String[] args) {
        ResourceManager.init();
        AuthenticationMainFrame.init(true);
    }

}
