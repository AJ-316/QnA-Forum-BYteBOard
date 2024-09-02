package QnAForumInterface.ProfileBoardPackage;

import CustomControls.RoundedJPanel;
import CustomControls.SimpleScrollPane;
import QnAForumInterface.InformationBarPackage.ActivityBar;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class ActivityContainer extends RoundedJPanel {

    private JLabel noActivityLabel;
    private JPanel activityContainer;

    public ActivityContainer() {
        setBackground(ResourceManager.getColor("main_dark"));
        setCornerRadius(90);
        setLayout(new GridBagLayout());

        initLabel();
        initContainer();
        setVisible(false);
        InterfaceEventManager.addListener("Display.ActivityContainer", ec -> setVisible((boolean) ec[0]));
    }

    private void initLabel() {
        noActivityLabel = new JLabel("No Activity Yet!");
        noActivityLabel.setForeground(ResourceManager.getColor("disabled"));
        noActivityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noActivityLabel.setFont(ResourceManager.getFont("inter_thin.48"));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(noActivityLabel, gridBagConstraints);
    }

    private void initContainer() {
        activityContainer = new JPanel();
        activityContainer.setLayout(new BoxLayout(activityContainer, BoxLayout.Y_AXIS));
        activityContainer.setOpaque(false);
        activityContainer.setBackground(ResourceManager.getColor("main_dark"));

        SimpleScrollPane scrollPane = new SimpleScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setMinimumSize(new Dimension(640, 360));
        scrollPane.setPreferredSize(new Dimension(640, 360));
        scrollPane.setBackground(ResourceManager.getColor("main"));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrollPane.setViewportView(activityContainer);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 5, 10, 5);
        add(scrollPane, gridBagConstraints);
    }

    public void addActivityBar(String questioner, String questionerProfileIndex, String questionHead, String questionID) {
        noActivityLabel.setVisible(false);
        ActivityBar activityBar = new ActivityBar();

        activityBar.setContent(questioner, questionerProfileIndex, questionHead, questionID);
        activityBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, activityBar.getPreferredSize().height));

        this.activityContainer.add(activityBar, 0);

        activityBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        updateBoardColors();
        revalidate();
        repaint();
    }

    private void updateBoardColors() {
        for (int i = 0; i < activityContainer.getComponentCount(); i++) {
            ActivityBar activityBar = (ActivityBar) activityContainer.getComponent(i);

            activityBar.setDefaultBackground(i % 2 == 0 ?
                    ResourceManager.getColor("main") :
                    ResourceManager.getColor("main_light"));
        }
    }

}
