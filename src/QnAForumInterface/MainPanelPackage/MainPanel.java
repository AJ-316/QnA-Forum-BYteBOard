package QnAForumInterface.MainPanelPackage;

import CustomControls.RoundedJPanel;
import DataObjects.UserDataObject;
import QnAForumDatabase.Database;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private final RoundedJPanel container;
    private final MainPanelHeader mainHeader;
    private final MainPanelBody mainBody;

    public MainPanel() {
        container = new RoundedJPanel();
        mainHeader = new MainPanelHeader();
        mainBody = new MainPanelBody();

        init();

        setContentType(0);
    }

    private void init() {
        setBackground(ResourceManager.getColor("main"));
        setMinimumSize(new Dimension(499, 300));
        setOpaque(false);
        setLayout(new BorderLayout());

        container.setBackground(ResourceManager.getColor("main"));
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

    public void setContent(String contentUserName, String contentUserProfileIndex, String[] tags, String heading, String body) {
        mainHeader.setContent(contentUserName, contentUserProfileIndex, tags);

        mainBody.setContentBody(body);
        if (tags == null) {
            setContentType(0);
            return;
        }

        setContentType(1);
        mainBody.setContentHead(heading);
    }

    /**
     * Sets visibility of tags and "view question" button
     * depending on content type
     *
     * @param type 0 = Responder, 1 = Questioner
     */
    public void setContentType(int type) {
        mainHeader.setContentType(type);
        mainBody.setContentType(type);
    }

    public String getContentUserID() {
        return Database.getData(UserDataObject.TABLE, UserDataObject.userIDKey(),
                UserDataObject.usernameKey(), mainHeader.getContentUserName(), true)[0];
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
