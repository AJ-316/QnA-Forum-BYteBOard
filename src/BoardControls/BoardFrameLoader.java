package BoardControls;

import BYteBOardInterface.StructurePackage.MainFrame;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class BoardFrameLoader extends SwingWorker<Void, Void> {
    private final Class<?>[] frames;
    private final MainFrame mainFrame;

    public BoardFrameLoader(MainFrame mainFrame, Class<?>[] frames) {
        this.mainFrame = mainFrame;
        this.frames = frames;
        BoardLoader.setProgressTarget(frames.length + 2);
        BoardLoader.progress();
    }

    public static void createLoader(MainFrame mainFrame, Class<?>[] frames, Runnable onComplete) {
        BoardFrameLoader loader = new BoardFrameLoader(mainFrame, frames);
        loader.execute();
        loader.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && loader.getState() == SwingWorker.StateValue.DONE) {
                onComplete.run();
            }
        });
    }

    protected Void doInBackground() {
        for (Class<?> frameClass : frames) {
            try {
                frameClass.getDeclaredConstructor(MainFrame.class).newInstance(mainFrame);
                BoardLoader.progress();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                BoardLoader.forceStop("Error Initializing", e.getMessage());
                break;
            }
        }

        BoardLoader.progress();
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
            BoardLoader.stop();
        } catch (Exception e) {
            BoardLoader.forceStop("Error Finalizing", e.getMessage());
        }
    }
}
