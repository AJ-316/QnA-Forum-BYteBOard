package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.CustomRendererPackage.RoundedBorder;
import Resources.ByteBoardTheme;

import javax.swing.*;
import java.awt.*;

public class BoardLoader extends JDialog {

    private static final BoardLabel titleIcon = new BoardLabel("byteboard/byteboard-logo-transparent2", -512);

    private static final Color mainColor = new Color(0, 120, 120);
    private static final Color mainLightColor = new Color(10, 130, 130);
    private static final Color accentColor = new Color(81, 180, 127);
    private static final Font font = new Font("SansSerif", Font.BOLD, 18);

    public static BoardLoader CURRENT;

    private final Runnable onComplete;
    private final JProgressBar progressBar;
    private final BoardLabel loadingText;
    private final BoardLabel loadingPercent;
    private float progressIncrement;
    private float progressValue;

    private Timer progressTimer; // Timer for smooth progress
    private int targetProgress = 0; // Target value for progress bar

    public BoardLoader(Runnable onComplete) {
        this.onComplete = onComplete;

        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Loading");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        BoardPanel pane = new BoardPanel();
        pane.setBackground(mainColor);
        pane.setCornerRadius(40);
        pane.addInsets(10);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground(mainColor);
        progressBar.setForeground(accentColor);
        progressBar.setBorderPainted(false);

        loadingText = new BoardLabel("Initializing...");
        loadingText.setForeground(Color.white);
        loadingText.setAlignmentLeading();
        loadingText.setFont(font);
        loadingText.addInsets(40, 5, 5, 5);

        loadingPercent = new BoardLabel("0%");
        loadingPercent.setForeground(Color.white);
        loadingPercent.setAlignmentTrailing();
        loadingPercent.setFont(font);
        loadingPercent.addInsets(40, 5, 5, 5);

        BoardPanel contentPanel = new BoardPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(mainLightColor);
        contentPanel.setCornerRadius(40);
        contentPanel.addInsets(20);

        contentPanel.add(titleIcon, BorderLayout.NORTH);
        contentPanel.add(loadingText, BorderLayout.WEST);
        contentPanel.add(loadingPercent, BorderLayout.EAST);

        GridBagBuilder builder = new GridBagBuilder(pane, 1);

        builder.weight(1, 1).fillBoth().insets(10)
                .addToNextCell(contentPanel)
                .weightY(0)
                .addToNextCell(progressBar);

        add(pane);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void setProgressTarget(int size) {
        if (CURRENT == null) return;

        CURRENT.targetProgress = 100;
        CURRENT.progressIncrement = 100f / size;
        CURRENT.progressValue = 0;
        CURRENT.progressBar.setValue(0);
        // CURRENT.startProgressTimer();
    }

    /* private void startProgressTimer() {
        if (progressTimer != null && progressTimer.isRunning()) {
            progressTimer.stop();
        }

        progressTimer = new Timer(50, e -> {
            if (CURRENT != null) {
                if (CURRENT.progressValue < targetProgress) {
                    CURRENT.progressValue += 1;
                    CURRENT.progressBar.setValue((int) CURRENT.progressValue);
                    CURRENT.loadingPercent.setText((int) CURRENT.progressValue + "%");
                }

                if (CURRENT.progressValue >= targetProgress)
                    progressTimer.stop();
            } else {
                progressTimer.stop();
            }
        });

        progressTimer.start();
    }*/

    public static void stop(boolean isComplete) {
        if (CURRENT == null) return;

        CURRENT.dispose();

        if (isComplete)
            EventQueue.invokeLater(CURRENT.onComplete);

        CURRENT = null;
    }

    public static void progress() {
        if (CURRENT == null) return;
        progress(CURRENT.loadingText.getText());
    }

    public static void progress(String progressText) {
        if (CURRENT == null) return;

        CURRENT.progressValue += CURRENT.progressIncrement;

        if (CURRENT.progressValue > 100)
            return;

        CURRENT.loadingText.setText(progressText);
        CURRENT.loadingPercent.setText((int) CURRENT.progressValue + "%");
        CURRENT.progressBar.setValue((int) Math.ceil(CURRENT.progressValue));
    }

    public static void start(Runnable onConfirm) {
        if(CURRENT != null) {
            CURRENT.dispose();
        }
        CURRENT = new BoardLoader(onConfirm);
    }

    public static void setText(String text) {
        if(CURRENT == null) return;
        CURRENT.loadingText.setText(text);
    }
}
