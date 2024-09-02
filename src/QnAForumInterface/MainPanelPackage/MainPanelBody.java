package QnAForumInterface.MainPanelPackage;

import CustomControls.SimpleScrollPane;
import CustomControls.RoundedJPanel;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class MainPanelBody extends JPanel {

    private RoundedJPanel containerHead;
    private JTextArea contentHead;
    private JTextArea contentBody;

    public MainPanelBody() {
        setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        setOpaque(false);
        setLayout(new GridBagLayout());

        initContentHead();
        initContentBody();
    }

    private void initContentHead() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(10, 40, 0, 20);

        containerHead = getContainer();

        contentHead = getContent("Head...", "inter_semibold.26");
        SimpleScrollPane contentScrollPane = getContentScrollPane(2, 484, 70, contentHead);

        containerHead.add(contentScrollPane, BorderLayout.CENTER);
        add(containerHead, gridBagConstraints);
    }

    private void initContentBody() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 40, 40, 20);

        /*contentBody.setText("Body...");
        contentBody.setColumns(20);
        contentBody.setRows(5);
        contentBody.setWrapStyleWord(true);
        contentBody.setLineWrap(true);
        contentBody.setEditable(false);
        contentBody.setOpaque(false);
        contentBody.setBorder(null);
        contentBody.setCaretColor(ResourceManager.getColor("base"));
        contentBody.setFont(ResourceManager.getFont("inter_semibold.26"));
        contentBody.setBackground(ResourceManager.getColor("main_light"));
        contentBody.setForeground(ResourceManager.getColor("text_fg_light"));
        //////
        contentHeadScrollPane.setBorder(null);
        contentBodyScrollPane.setAutoscrolls(true);
        contentHeadScrollPane.setOpaque(false);
        contentHeadScrollPane.setPreferredSize(new Dimension(484, 70));

        contentHeadScrollPane.setViewportView(contentHead);
        contentHeadScrollPane.getViewport().setOpaque(false);
        contentHeadScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));*/
        RoundedJPanel containerBody = getContainer();

        contentBody = getContent("Body...", "inter_semibold.24");
        SimpleScrollPane contentScrollPane = getContentScrollPane(4, 480, 0, contentBody);

        containerBody.add(contentScrollPane, BorderLayout.CENTER);
        add(containerBody, gridBagConstraints);
    }

    private JTextArea getContent(String text, String fontLabel) {
        JTextArea content = new JTextArea();
        content.setText(text);
        content.setWrapStyleWord(true);
        content.setLineWrap(true);
        content.setEditable(false);
        content.setOpaque(false);
        content.setBorder(null);
        content.setCaretColor(ResourceManager.getColor(ByteBoardTheme.BASE));
        content.setFont(ResourceManager.getFont(fontLabel));
        content.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
        content.setForeground(content.getCaretColor());

        return content;
    }

    private RoundedJPanel getContainer() {
        RoundedJPanel container = new RoundedJPanel();
        container.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
        container.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        container.setBorderColor(ResourceManager.getColor(ByteBoardTheme.MAIN));
        container.setCornerRadius(90);
        container.setLimitRadius(false);
        container.setLayout(new BorderLayout());
        return container;
    }

    private SimpleScrollPane getContentScrollPane(int unitIncrement, int sizeX, int sizeY, JTextArea viewportComponent) {
        SimpleScrollPane contentScrollPane = new SimpleScrollPane(viewportComponent);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(unitIncrement);

        contentScrollPane.setBorder(null);
        contentScrollPane.setAutoscrolls(true);
        contentScrollPane.setOpaque(false);
        contentScrollPane.setPreferredSize(new Dimension(sizeX, sizeY));

        contentScrollPane.setViewportView(viewportComponent);
        contentScrollPane.getViewport().setOpaque(false);
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        return contentScrollPane;
    }

    public void setContentType(int type) {
        containerHead.setVisible(type != 0);
    }

    public String getContentBody() {
        return contentBody.getText();
    }

    public String getContentHead() {
        return contentHead.getText();
    }

    public void setContentHead(String head) {
        contentHead.setText(head);
    }

    public void setContentBody(String body) {
        contentBody.setText(body);
    }
}
