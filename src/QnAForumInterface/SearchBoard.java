/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;

import CustomControls.RoundedJPanel;
import DataObjects.DataObject;
import DataObjects.QuestionDataObject;
import DataObjects.TagDataObject;
import DataObjects.UserDataObject;
import QnAForumDatabase.Database;

import QnAForumInterface.InformationBarPackage.ActivityBar;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;

/**
 * @author AJ
 */
public class SearchBoard extends JPanel {

    private JLabel noResultLabel;
    private JButton profileBtn;
    private CustomControls.RoundedJPanel resultContainer;
    private JComboBox<String> search;
    private CustomControls.RoundedJPanel searchContainer;
    private JLabel searchIcon;
    private CustomControls.RoundedJPanel tagHolder;
    private JLabel tagText;
    private JPanel tagsContainer;
    private JPanel userContainer;
    private JLabel userName;
    private JLabel userProfile;

    /**
     * Creates new form SearchBoard
     */
    public SearchBoard() {
        initComponents();
    }

    public static SearchBoard init(String username, String userProfileIndex) {
        SearchBoard board = new SearchBoard();

        board.userName.setText(username);
        ResourceManager.setProfileIcon(userProfileIndex, board.userProfile, ResourceManager.REGULAR);
        board.tagText.setText("");
        board.tagsContainer.removeAll();
        board.resetQuestions(false);
        board.revalidate();
        board.repaint();
        return board;
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        userContainer = new JPanel();
        userProfile = new JLabel();
        userName = new JLabel();
        profileBtn = new JButton();
        searchContainer = new CustomControls.RoundedJPanel();
        searchIcon = new JLabel();
        search = new JComboBox<>();
        tagsContainer = new JPanel();
        tagHolder = new CustomControls.RoundedJPanel();
        tagText = new JLabel();
        noResultLabel = new JLabel();
        resultContainer = new CustomControls.RoundedJPanel();

        setBackground(ResourceManager.getColor("base"));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new GridBagLayout());

        userContainer.setBackground(ResourceManager.getColor("base"));
        userContainer.setMinimumSize(new Dimension(0, 0));
        userContainer.setOpaque(false);
        userContainer.setLayout(new GridBagLayout());

        userProfile.setIcon(ResourceManager.getIcon("user_profile", ResourceManager.REGULAR)
        );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        userContainer.add(userProfile, gridBagConstraints);

        userName.setText("Username");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        userContainer.add(userName, gridBagConstraints);
        userName.setFont(ResourceManager.getFont("inter_bold.36"));

        profileBtn.setIcon(ResourceManager.getIcon("home_default", 32));
        profileBtn.setText("Profile");
        profileBtn.setBorderPainted(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setFocusPainted(false);
        profileBtn.setPressedIcon(ResourceManager.getIcon("home_pressed", 32)
        );
        profileBtn.setRolloverIcon(ResourceManager.getIcon("home_rollover", 32)
        );
        profileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                profileBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        userContainer.add(profileBtn, gridBagConstraints);
        profileBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        add(userContainer, gridBagConstraints);

        searchContainer.setBackground(ResourceManager.getColor("main_dark"));
        searchContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchContainer.setCornerRadius(90);
        searchContainer.setMaximumSize(new Dimension(340, 2147483647));
        searchContainer.setMinimumSize(new Dimension(340, 74));
        searchContainer.setPreferredSize(new Dimension(340, 74));
        searchContainer.setLayout(new GridBagLayout());

        searchIcon.setIcon(ResourceManager.getIcon("search", 48)
        );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        searchContainer.add(searchIcon, gridBagConstraints);

        search.setBackground(ResourceManager.getColor("main_dark"));
        search.setEditable(true);
        search.setForeground(ResourceManager.getColor("text_fg_light"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        searchContainer.add(search, gridBagConstraints);
        search.setFont(ResourceManager.getFont("inter_regular.24"));

        search.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                return new JButton() {
                    @Override
                    public int getWidth() {
                        return 0;
                    }
                };
            }
        });
        search.setForeground(Color.red);
        search.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        search.setMaximumRowCount(8);

        JTextField textField = (JTextField) search.getEditor().getEditorComponent();
        textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textField.setForeground(ResourceManager.getColor("text_fg_light"));
        textField.setCaretColor(Color.white);
        textField.setBackground(ResourceManager.getColor("main_dark"));
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_UP) {
                    search.revalidate();
                    search.repaint();
                    revalidate();
                    repaint();
                    return;
                }

                String text = textField.getText();
                search.removeAllItems();
                search.addItem(text);

                if (e.getKeyCode() == KeyEvent.VK_ENTER ||
                        e.getKeyCode() == KeyEvent.VK_TAB) {
                    addTag(textField.getText());
                    textField.setText("");
                    search.revalidate();
                    search.repaint();
                    revalidate();
                    repaint();
                    return;
                }

                for (String suggestion : getTags()) {
                    if (suggestion.equalsIgnoreCase(text)) {
                        search.removeAllItems();
                        search.addItem(text);
                        search.addItem(suggestion);
                        repaint();
                    } else if (suggestion.toLowerCase().startsWith(text.toLowerCase())) {
                        //if(search.getItemCount() > 3) break;
                        search.addItem(suggestion);
                        repaint();
                    }

                }
                search.setPopupVisible(search.getItemCount() > 1);
                search.revalidate();
                search.repaint();
                revalidate();
                repaint();
            }
        });

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        add(searchContainer, gridBagConstraints);

        tagsContainer.setBackground(ResourceManager.getColor("main"));
        tagsContainer.setMaximumSize(new Dimension(300, 300));
        tagsContainer.setMinimumSize(new Dimension(300, 300));
        tagsContainer.setOpaque(false);
        tagsContainer.setPreferredSize(new Dimension(300, 300));
        tagsContainer.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                tagsContainerMouseReleased(evt);
            }
        });

        tagHolder.setBackground(ResourceManager.getColor("main"));
        tagHolder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagHolder.setCornerRadius(90);
        tagHolder.setLayout(new BorderLayout());

        tagText.setBackground(ResourceManager.getColor("main_dark"));
        tagText.setForeground(ResourceManager.getColor("text_fg_light"));
        tagText.setText("tag1");
        tagText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagHolder.add(tagText, BorderLayout.CENTER);
        tagText.setFont(ResourceManager.getFont("inter_regular.22"));

        tagsContainer.add(tagHolder);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(tagsContainer, gridBagConstraints);

        noResultLabel.setForeground(ResourceManager.getColor("text_fg_light"));
        noResultLabel.setText("No Available Questions");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        add(noResultLabel, gridBagConstraints);
        noResultLabel.setFont(ResourceManager.getFont("inter_thin.48"));

        resultContainer.setBackground(ResourceManager.getColor("main_dark"));
        resultContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultContainer.setCornerRadius(90);
        resultContainer.setMaximumSize(new Dimension(350, 2147483647));
        resultContainer.setMinimumSize(new Dimension(350, 360));
        resultContainer.setPreferredSize(new Dimension(350, 0));
        resultContainer.setLayout(new BoxLayout(resultContainer, BoxLayout.Y_AXIS));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(resultContainer, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void profileBtnActionPerformed(ActionEvent evt) {
        QnAForum.setContent(ProfileBoard.init(userName.getText()));
    }

    private void tagsContainerMouseReleased(MouseEvent evt) {
        clearTag();
    }

    public void addResultBar(String questioner, String questionerProfileIndex, String questionHead, String questionID) {
        noResultLabel.setVisible(false);
        ActivityBar resultBar = new ActivityBar();

        resultBar.setContent(questioner, questionerProfileIndex, questionHead, questionID);
        resultBar.setMaximumSize(new Dimension(getPreferredSize().width, resultBar.getPreferredSize().height));

        this.resultContainer.add(resultBar, 0);

        resultBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        updateBoardColors();
        revalidate();
        repaint();
    }

    private void updateBoardColors() {
        for (int i = 0; i < resultContainer.getComponentCount(); i++) {
            ActivityBar activityBar = (ActivityBar) resultContainer.getComponent(i);

            activityBar.setDefaultBackground(i % 2 == 0 ?
                    ResourceManager.getColor("main") :
                    ResourceManager.getColor("main_light"));
        }
    }

    private void resetQuestions(boolean visible) {
        resultContainer.removeAll();
        noResultLabel.setVisible(visible);
        resultContainer.setVisible(visible);
    }

    private void createTag(String text) {

        RoundedJPanel tagHolder = new RoundedJPanel();
        tagHolder.setBackground(ResourceManager.getColor("main"));
        tagHolder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagHolder.setCornerRadius(90);
        tagHolder.setLayout(new BorderLayout());

        JLabel tagText = new JLabel(text);
        tagText.setBackground(ResourceManager.getColor("main_dark"));
        tagText.setForeground(ResourceManager.getColor("text_fg_light"));
        tagText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagHolder.add(tagText, BorderLayout.CENTER);
        tagText.setFont(ResourceManager.getFont("inter_regular.22"));

        tagsContainer.add(tagHolder);
    }

    private void searchQuestions() {
        resetQuestions(true);

        Component[] tags = tagsContainer.getComponents();
        if (tags.length == 0) {
            revalidate();
            repaint();
            return;
        }

        for (Component tagComponent : tags) {
            String tag = ((JLabel) ((RoundedJPanel) tagComponent).getComponent(0)).getText();
            DataObject[] tagObjects = Database.getData(TagDataObject.TABLE, TagDataObject.tagKey(), tag);
            for (DataObject tagObject : tagObjects) {
                String questionID = tagObject.get(TagDataObject.questionIDKey());
                if (resultContains(questionID)) continue;
                DataObject questionObject = Database.getData(QuestionDataObject.TABLE,
                        QuestionDataObject.questionIDKey(), questionID)[0];

                UserDataObject userObject = (UserDataObject) Database.getData(UserDataObject.TABLE,
                        UserDataObject.userIDKey(), questionObject.get(QuestionDataObject.userIDKey()))[0];

                addResultBar(
                        userObject.get(UserDataObject.usernameKey()),
                        userObject.get(UserDataObject.profileKey()),
                        questionObject.get(QuestionDataObject.questionHeadKey()), questionID);
            }
        }

        revalidate();
        repaint();
    }

    private boolean resultContains(String questionID) {
        for (Component component : resultContainer.getComponents()) {
            if (!(component instanceof ActivityBar)) continue;
            ActivityBar resultBar = (ActivityBar) component;
            if (resultBar.getQuestionID().equals(questionID)) return true;
        }
        return false;
    }

    private String[] getTags() {
        return Database.getData(TagDataObject.TABLE, TagDataObject.tagKey(), true);
    }

    private void clearTag() {
        if (tagsContainer.getComponents().length != 0)
            tagsContainer.remove(tagsContainer.getComponentCount() - 1);
        searchQuestions();
    }

    private void addTag(String tag) {
        tag = tag.replaceAll(" ", "");
        Component[] tagComponents = tagsContainer.getComponents();

        for (Component tagComponent : tagComponents) {
            RoundedJPanel tagHolder = (RoundedJPanel) tagComponent;
            if (((JLabel) tagHolder.getComponent(0)).getText().equals(tag)) {
                return;
            }
        }

        if (!tag.matches("^[a-zA-Z0-9]+$")) return;

        createTag(tag);
        searchQuestions();
    }
}
