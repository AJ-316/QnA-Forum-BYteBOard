package BoardControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BoardControls.UIPackage.GridBagBuilder;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;

public class BoardDialog extends JDialog {

    private static final int MAX_MSG_WIDTH = 50;
    private final BoardButton acceptButton;
    private Timer selfDestroyTimer;

    public BoardDialog(JFrame frame, Component locationRelativeTo, String msg, ActionListener onConfirmListener) {
        super(frame, true);

        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Confirm");

        BoardPanel pane = new BoardPanel();
        pane.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        pane.setShadowState(BoardPanel.OFFSET_SHADOW);
        pane.setCornerRadius(40);
        pane.addInsets(10);

        ActionListener disposeListener = e -> dispose();

        acceptButton = new BoardButton("Yes", "accept");
        acceptButton.setName("Yes");
        acceptButton.addActionListener(disposeListener);
        acceptButton.addActionListener(onConfirmListener);
        acceptButton.addActionListener(e -> selfDestroyTimer.stop());
        acceptButton.addInsets(5);

        BoardButton cancelButton = new BoardButton(" No", "cancel");
        cancelButton.addActionListener(disposeListener);
        cancelButton.addInsets(5);

        BoardTextPane msgText = new BoardTextPane(ByteBoardTheme.MAIN_LIGHT);
        msgText.setText(msg);
        msgText.setFocusable(false);

        BoardPanel msgPanel = new BoardPanel();
        msgPanel.setBackground(ByteBoardTheme.MAIN_LIGHT);
        msgPanel.setLayout(new BorderLayout());
        msgPanel.setCornerRadius(40);
        msgPanel.addInsets(20);

        msgPanel.add(msgText);

        GridBagBuilder builder = new GridBagBuilder(pane, 2);

        builder.gridWidth(2).weightY(1).fillHorizontal().insets(10)
                .addToNextCell(msgPanel)
                .skipCells(1).weight(1, 0).fillBoth().gridWidth(1);

        add(pane);

        if (frame == null) {
            acceptButton.setText("OK");
            acceptButton.setName("OK");

            cancelButton.setText("");
            cancelButton.setIcon("copy");
            cancelButton.removeActionListener(disposeListener);
            cancelButton.addActionListener(e -> Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(msgText.getText()), null));

            builder.weightX(0).addToNextCell(cancelButton)
                    .weightX(1).addToNextCell(acceptButton);

            pack();
            acceptButton.requestFocus();

        } else {
            StyledDocument documentStyle = msgText.getStyledDocument();
            SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
            documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

            builder.addToNextCell(acceptButton)
                    .addToNextCell(cancelButton);

            pack();
            cancelButton.requestFocus();
        }

        setLocationRelativeTo(locationRelativeTo);
    }

    private static String wrapText(String text, int maxLineLength) {
        StringBuilder wrappedText = new StringBuilder();
        String[] lines = text.replace("\n", "\n@#~").split("@#~");

        for (String line : lines) {
            String[] words = line.split(" "); // Split the text into words
            int currentLineLength = 0;

            for (String word : words) {
                // Check if adding the new word exceeds the max line length
                if (currentLineLength + word.length() > maxLineLength) {
                    wrappedText.append("\n"); // Add a new line
                    currentLineLength = 0; // Reset current line length
                } else if (currentLineLength > 0) {
                    wrappedText.append(" "); // Add a space for non-first words
                }

                wrappedText.append(word); // Add the word to the wrapped text
                currentLineLength += word.length(); // Update the current line length
            }
        }

        return wrappedText.toString();
    }

    public static void create(Frame frame, Component locationRelativeTo, String msg, ActionListener onConfirm) {
        msg = wrapText(msg, MAX_MSG_WIDTH);
        new BoardDialog(frame == null ? null : frame.getBoardFrame().getMain(),
                locationRelativeTo, msg, onConfirm).setVisible(true);
    }

    public static void create(Frame frame, Component locationRelativeTo, String title, String msg, ActionListener onConfirm, int selfDestroyDelaySeconds) {
        msg = wrapText(msg, MAX_MSG_WIDTH);
        BoardDialog dialog = new BoardDialog(frame == null ? null : frame.getBoardFrame().getMain(), locationRelativeTo, msg, onConfirm);
        dialog.selfDestroy(selfDestroyDelaySeconds);
        dialog.setTitle(title);
        dialog.setVisible(true);
    }

    public void selfDestroy(int delaySeconds) {
        final int[] counter = new int[]{delaySeconds};

        selfDestroyTimer = new Timer(1000, null);
        selfDestroyTimer.addActionListener(e -> {
            if (counter[0]-- <= 0) {
                acceptButton.getActionListeners()[0].actionPerformed(null);
                acceptButton.getActionListeners()[1].actionPerformed(null);
                acceptButton.getActionListeners()[2].actionPerformed(null);
                return;
            }
            acceptButton.setText(acceptButton.getName() + " (" + counter[0] + ")");
        });
        selfDestroyTimer.start();
    }
}
