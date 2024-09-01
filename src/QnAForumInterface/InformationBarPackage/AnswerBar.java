package QnAForumInterface.InformationBarPackage;


import CustomControls.RoundedJPanel;
import QnAForumInterface.QnABoard;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author AJ
 */
public class AnswerBar extends InformationBar {

    private static AnswerBar SELECTED_ANSWER_BAR;
    private JLabel responderAnswer;
    private JLabel responderName;
    private JLabel responderProfile;
    private Color defaultBackground;

    protected void init() {
        responderProfile = new JLabel();

        initResponderProfile();
        initResponderName();
        initResponderAnswer();
    }

    private void initResponderProfile() {
        responderProfile.setIcon(ResourceManager.getIcon("user_profile", ResourceManager.SMALL));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(15, 15, 15, 5);
        container.add(responderProfile, gridBagConstraints);
    }

    private void initResponderName() {
        responderName = getLabel("Responder Name", SwingConstants.LEFT);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(15, 5, 15, 5);
        container.add(responderName, gridBagConstraints);
    }

    private void initResponderAnswer() {
        responderAnswer = getLabel("Answer...", SwingConstants.LEFT);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(15, 5, 15, 15);
        container.add(responderAnswer, gridBagConstraints);
    }

    public static void setSelected(AnswerBar answerBar) {
        if (SELECTED_ANSWER_BAR != null) {
            SELECTED_ANSWER_BAR.setColor(SELECTED_ANSWER_BAR.defaultBackground);
        }
        SELECTED_ANSWER_BAR = answerBar;

        if (answerBar != null)
            answerBar.setColor(ResourceManager.getColor("accent_dark"));
    }

    protected void addMouseListeners() {
        AnswerBar bar = this;
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (isSelected()) return;
                setColor(ResourceManager.getColor("accent"));
            }

            public void mouseExited(MouseEvent evt) {
                if (isSelected()) return;
                setColor(defaultBackground);
            }

            public void mouseReleased(MouseEvent evt) {
                if (isSelected()) return;
                AnswerBar.setSelected(bar);
                QnABoard.CurrentInstance.setAnswerPanelContent(responderName.getName(), responderProfile.getName(), responderAnswer.getText());
            }
        });
    }

    private boolean isSelected() {
        return SELECTED_ANSWER_BAR != null && SELECTED_ANSWER_BAR.equals(this);
    }

    public void setContent(String responderName, String responderProfileIndex, String answerBody) {
        this.responderName.setText(responderName + ":");
        this.responderName.setName(responderName);
        this.responderAnswer.setText(answerBody);
        ResourceManager.setProfileIcon(responderProfileIndex, responderProfile, ResourceManager.SMALL);
    }

    public boolean isRespondent(String responderName) {
        return this.responderName.getName().equals(responderName);
    }

    public void setDefaultBackground(Color color) {
        setColor(color);
        defaultBackground = color;
    }

    private void setColor(Color c) {
        container.setBackground(c);
    }
}
