package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import Resources.ByteBoardTheme;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;

public class BoardDialog extends JDialog {

    public BoardDialog(Frame frame, Component locationRelativeTo, String msg, ActionListener onConfirmListener) {
        super(frame.getBoardFrame().getMain(), true);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Confirm");

        BoardPanel pane = new BoardPanel(frame, ByteBoardTheme.MAIN);
        pane.setCornerRadius(40);
        pane.addInsets(10);

        ActionListener disposeListener = e -> dispose();

        BoardButton acceptBtn = new BoardButton("Yes", "cancel");
        acceptBtn.addActionListener(onConfirmListener);
        acceptBtn.addActionListener(disposeListener);

        BoardButton cancelBtn = new BoardButton("No", "cancel");
        cancelBtn.addActionListener(disposeListener);

        BoardTextPane msgText = new BoardTextPane(frame, ByteBoardTheme.MAIN_LIGHT);
        msgText.setText(msg);
        msgText.setFocusable(false);

        StyledDocument documentStyle = msgText.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        BoardPanel msgPanel = new BoardPanel(frame, ByteBoardTheme.MAIN_LIGHT);
        msgPanel.setLayout(new BorderLayout());
        msgPanel.setCornerRadius(40);
        msgPanel.addInsets(20);

        msgPanel.add(msgText);

        GridBagBuilder builder = new GridBagBuilder(pane, 2);

        builder.gridWidth(2).weightY(1).fillHorizontal().insets(10)
                .addToNextCell(msgPanel)
                .skipCells(1)
                .gridWidth(1).weight(1, 0).fillBoth()
                .addToNextCell(acceptBtn)
                .addToNextCell(cancelBtn);

        add(pane);
        pack();

        setLocationRelativeTo(locationRelativeTo);
        setVisible(true);
    }

    public static void create(Frame frame, Component locationRelativeTo, String msg, ActionListener onConfirm) {
        new BoardDialog(frame, locationRelativeTo, msg, onConfirm);
    }
}
