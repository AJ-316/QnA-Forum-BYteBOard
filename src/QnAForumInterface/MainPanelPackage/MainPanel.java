package QnAForumInterface.MainPanelPackage;

import CustomControls.CustomJPanel;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    public static final String CONTENT_QUESTION = "Question";
    public static final String CONTENT_ANSWER = "Answer";

    private final CustomJPanel container;
    private final MainPanelHeader mainHeader;
    private final MainPanelBody mainBody;

    public MainPanel(String contentType) {
        container = new CustomJPanel();
        mainHeader = new MainPanelHeader(contentType);
        mainBody = new MainPanelBody();

        init();

        setContentType(MainPanel.CONTENT_ANSWER);
    }

    private void init() {
        setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        setMinimumSize(new Dimension(499, 300));
        setOpaque(false);
        setLayout(new BorderLayout());

        container.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        container.setCornerRadius(90);
        container.setLayout(new GridBagLayout());

        initHeader();
        initBody();

        add(container, BorderLayout.CENTER);
    }

    private void initHeader() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(20, 20, 0, 20);

        container.add(mainHeader, gridBagConstraints);
    }

    private void initBody() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;

        container.add(mainBody, gridBagConstraints);
    }

    public void setContent(String contentUserName, String contentUserProfileIndex, String[] tags,
                           String heading, String body, String contentBytescore, String lastVoteType) {

        mainHeader.setContent(contentUserName, contentUserProfileIndex, tags, contentBytescore, lastVoteType);

        mainBody.setContentBody(body);
        if (tags == null) {
            setContentType(MainPanel.CONTENT_ANSWER);
            return;
        }

        setContentType(MainPanel.CONTENT_QUESTION);
        mainBody.setContentHead(heading);

        System.out.println("Setting content: name:" + contentUserName + ", dp:" + contentUserProfileIndex + ", heading:" + heading + ", body:"+body+ ", contentScore: " + contentBytescore + ", lastVoteType: " + lastVoteType + "\u001b[0m");
    }

    public void setContentType(String type) {
        mainHeader.setContentType(type);
        mainBody.setContentType(type);
    }

    public void updateByteScore(String bytescore) {
        mainHeader.setContentBytescore(bytescore);
    }

    public void disableVotes() {
        mainHeader.disableVotes();
    }

    public String getContentUser() {
        return mainHeader.getContentUserName();
    }

    public String getContentBody() {
        return mainBody.getContentBody();
    }

    public String getContentHead() {
        return mainBody.getContentHead();
    }

}
