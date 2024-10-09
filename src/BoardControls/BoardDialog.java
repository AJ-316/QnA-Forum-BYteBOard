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
import java.awt.event.ActionListener;

public class BoardDialog extends JDialog {

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

        BoardButton acceptBtn = new BoardButton("Yes", "accept");
        acceptBtn.addActionListener(onConfirmListener);
        acceptBtn.addActionListener(disposeListener);
        acceptBtn.addInsets(5);

        BoardButton cancelBtn = new BoardButton(" No", "cancel");
        cancelBtn.addActionListener(disposeListener);
        cancelBtn.addInsets(5);

        BoardTextPane msgText = new BoardTextPane(ByteBoardTheme.MAIN_LIGHT);
        msgText.setText(msg);
        msgText.setFocusable(false);

        StyledDocument documentStyle = msgText.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        BoardPanel msgPanel = new BoardPanel();
        msgPanel.setBackground(ByteBoardTheme.MAIN_LIGHT);
        msgPanel.setLayout(new BorderLayout());
        msgPanel.setCornerRadius(40);
        msgPanel.addInsets(20);

        msgPanel.add(msgText);

        GridBagBuilder builder = new GridBagBuilder(pane, 2);

        builder.gridWidth(2).weightY(1).fillHorizontal().insets(10)
                .addToNextCell(msgPanel)
                .skipCells(1).weight(1, 0).fillBoth();

        if(frame != null) {
            builder.gridWidth(1)
                    .addToNextCell(acceptBtn)
                    .addToNextCell(cancelBtn);

        } else {
            acceptBtn.setText("OK");
            builder.addToNextCell(acceptBtn);
        }

        add(pane);
        pack();

        setLocationRelativeTo(locationRelativeTo);
        setVisible(true);
    }

    public static void create(Frame frame, Component locationRelativeTo, String msg, ActionListener onConfirm) {
        new BoardDialog(frame == null ? null : frame.getBoardFrame().getMain(), locationRelativeTo, msg, onConfirm);
    }
}
