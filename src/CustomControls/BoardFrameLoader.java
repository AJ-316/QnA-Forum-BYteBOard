package CustomControls;

import BYteBOardInterface.StructurePackage.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class BoardFrameLoader extends SwingWorker<Void, Void> {
    private final Class<?>[] frames;
    private final MainFrame mainFrame;

    public static void createLoader(MainFrame mainFrame, Class<?>[] frames, Runnable onComplete) {
        BoardFrameLoader loader = new BoardFrameLoader(mainFrame, frames);
        loader.execute();
        loader.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && loader.getState() == SwingWorker.StateValue.DONE) {
                onComplete.run();
            }
        });
    }

    public BoardFrameLoader(MainFrame mainFrame, Class<?>[] frames) {
        this.mainFrame = mainFrame;
        this.frames = frames;
        BoardLoader.setProgressTarget(frames.length + 2);
        BoardLoader.progress();
    }

    protected Void doInBackground() {
        for (Class<?> frameClass : frames) {
            try {
                frameClass.getDeclaredConstructor(MainFrame.class).newInstance(mainFrame);
                BoardLoader.progress();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                BoardDialog.create(null, null,
                        "Error Initializing: " + e.getMessage(),
                        evt -> BoardLoader.stop(false));
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
            BoardLoader.stop(true);
        } catch (Exception e) {
            BoardDialog.create(null, null,
                    "Error Finalizing: " + e.getMessage(),
                    evt -> BoardLoader.stop(false));
        }
    }
}
