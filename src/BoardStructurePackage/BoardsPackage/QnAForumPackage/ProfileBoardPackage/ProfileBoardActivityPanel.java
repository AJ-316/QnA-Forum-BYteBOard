package BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.Frame;
import BoardStructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardScrollPanel;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;

import java.awt.*;

public class ProfileBoardActivityPanel extends BoardPanel {

    private BoardLabel activityStatusLabel;
    private BoardScrollPanel activitiesPanel;
    private GridBagBuilder activitiesLayoutBuilder;

    public ProfileBoardActivityPanel(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN);

        setVisible(false);
    }

    public void init(MainFrame main, Frame frame) {
        addInsets(20);
        setCornerRadius(90);

        GridBagBuilder builder = new GridBagBuilder(this);

        activityStatusLabel = new BoardLabel();
        activityStatusLabel.setText("<html><div style='text-align: center;'>Lets get Started!<br>No Activity</div></html>");
        activityStatusLabel.setName(activityStatusLabel.getText());
        activityStatusLabel.addInsets(30);
        activityStatusLabel.setFGLight();
        activityStatusLabel.setFontPrimary(ByteBoardTheme.FONT_T_THIN, 32);

        activitiesPanel = getActivityPanel(main, frame);
        builder.gridWeightX(1);
        builder.gridWeightY(1);
        builder.fill(GridBagConstraints.BOTH);
        add(activitiesPanel.getComponent(), builder.getConstraints());

        clearActivities();
    }

    private BoardScrollPanel getActivityPanel(MainFrame main, Frame frame) {
        BoardScrollPanel panel = new BoardScrollPanel(main, frame);
        panel.setBackground(getBackground());

        activitiesLayoutBuilder = new GridBagBuilder(panel, 1);
        activitiesLayoutBuilder.fill(GridBagConstraints.HORIZONTAL);
        activitiesLayoutBuilder.gridWeightX(1);
        activitiesLayoutBuilder.insets(10, 10, 10, 20);
        return panel;
    }

    public void clearActivities() {
        activitiesPanel.removeAll();
        activitiesLayoutBuilder.gridCell(0, 0);
        addActivityStatusLabel(activityStatusLabel.getName());
    }

    public ContentPane addActivity() {
        removeActivityStatusLabel();

        ContentPane pane = new ContentPane(getMain(), getFrame());
        activitiesLayoutBuilder.add(pane);

        addActivityStatusLabel("");

        return pane;
    }

    private void addActivityStatusLabel(String text) {
        activitiesLayoutBuilder.anchor(GridBagConstraints.CENTER);
        activitiesLayoutBuilder.gridWeightY(1);
        activitiesLayoutBuilder.add(activityStatusLabel);
        activityStatusLabel.setText(text);
    }

    private void removeActivityStatusLabel() {
        activitiesLayoutBuilder.skipCells(-1);
        activitiesPanel.remove(activityStatusLabel);
        activityStatusLabel.setText("");

        activitiesLayoutBuilder.anchor(GridBagConstraints.CENTER);
        activitiesLayoutBuilder.gridWeightY(0);
    }

}
