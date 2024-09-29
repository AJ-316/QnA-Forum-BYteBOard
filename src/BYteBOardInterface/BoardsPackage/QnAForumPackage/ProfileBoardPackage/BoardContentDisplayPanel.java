package BYteBOardInterface.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardScrollPanel;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;

import javax.swing.*;
import java.awt.*;

public class BoardContentDisplayPanel extends BoardPanel {

    private BoardLabel statusLabel;
    private BoardScrollPanel contentsPanel;
    private GridBagBuilder layoutBuilder;

    public BoardContentDisplayPanel(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN_DARK);

        setVisible(false);
    }

    public void init(MainFrame main, Frame frame) {
        addInsets(20);
        setCornerRadius(90);

        GridBagBuilder builder = new GridBagBuilder(this);

        statusLabel = new BoardLabel();
        statusLabel.setName(statusLabel.getText());
        statusLabel.addInsets(30);
        statusLabel.setFGLight();
        statusLabel.setFontPrimary(ByteBoardTheme.FONT_T_THIN, 32);

        contentsPanel = getActivityPanel(main, frame);

        builder.weight(1, 1).fillBoth();
        builder.addToCurrentCell(contentsPanel.getComponent());

        clearQuestions();
    }

    private BoardScrollPanel getActivityPanel(MainFrame main, Frame frame) {
        BoardScrollPanel panel = new BoardScrollPanel(main, frame);
        panel.getComponent().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setBackground(getBackground());
        layoutBuilder = new GridBagBuilder(panel, 1);
        layoutBuilder.weightX(1).fillHorizontal()
                .insets(10, 10, 10, 20).anchor(GridBagConstraints.CENTER);
        return panel;
    }

    public void clearQuestions() {
        contentsPanel.removeAll();
        layoutBuilder.gridCell(0, 0);
        addActivityStatusLabel(statusLabel.getName());
    }

    public void setStatusLabel(String labelText) {
        String[] lines = labelText.split("\n");

        StringBuilder labelBuilder = new StringBuilder("<html><div style='text-align: center;'>");

        for (int i = 0; i < lines.length; i++) {
            if (i != 0) labelBuilder.append("<br>");
            labelBuilder.append(lines[i]);
        }

        labelBuilder.append("</div></html>");
        statusLabel.setText(labelBuilder.toString());
    }

    public BoardContentDisplayPane addContentDisplayPanel() {
        removeActivityStatusLabel();

        BoardContentDisplayPane pane = new BoardContentDisplayPane(getMain(), getFrame());
        layoutBuilder.addToNextCell(pane);

        addActivityStatusLabel("");

        return pane;
    }

    public void resetScrolls() {
        EventQueue.invokeLater(() -> contentsPanel.getComponent().getVerticalScrollBar().setValue(0));
    }

    private void addActivityStatusLabel(String text) {
        layoutBuilder.weightY(1)
                .addToNextCell(statusLabel);
        statusLabel.setText(text);
    }

    private void removeActivityStatusLabel() {
        layoutBuilder.skipCells(-1);
        contentsPanel.remove(statusLabel);
        statusLabel.setText("");

        layoutBuilder.weightY(0);
    }

}
