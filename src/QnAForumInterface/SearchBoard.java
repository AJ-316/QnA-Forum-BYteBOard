/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;

import BYteBOardDatabase.*;
import CustomControls.CustomJPanel;
import CustomControls.DEBUG;
import QnAForumInterface.InformationBarPackage.ActivityBar;
import QnAForumInterface.ProfileBoardPackage.ProfileBoard;
import Resources.ByteBoardTheme;
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
    private CustomJPanel resultContainer;
    private JComboBox<String> search;
    private CustomJPanel searchContainer;
    private JLabel searchIcon;
    private CustomJPanel tagHolder;
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
        ResourceManager.setProfileIndexIcon(userProfileIndex, board.userProfile, ResourceManager.REGULAR);
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
        searchContainer = new CustomJPanel();
        searchIcon = new JLabel();
        search = new JComboBox<>();
        tagsContainer = new JPanel();
        tagHolder = new CustomJPanel();
        tagText = new JLabel();
        noResultLabel = new JLabel();
        resultContainer = new CustomJPanel();

        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new GridBagLayout());

        userContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        userContainer.setMinimumSize(new Dimension(0, 0));
        userContainer.setOpaque(false);
        userContainer.setLayout(new GridBagLayout());

        userProfile.setIcon(ResourceManager.getProfileIcon("0", ResourceManager.REGULAR)
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
        userName.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        profileBtn.setText("Profile");
        profileBtn.setBorderPainted(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setFocusPainted(false);
        ResourceManager.setButtonIcons(profileBtn, "home", ResourceManager.MINI);
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
        profileBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        profileBtn.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        add(userContainer, gridBagConstraints);

        searchContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        searchContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchContainer.setCornerRadius(90);
        searchContainer.setMaximumSize(new Dimension(340, 2147483647));
        searchContainer.setMinimumSize(new Dimension(340, 74));
        searchContainer.setPreferredSize(new Dimension(340, 74));
        searchContainer.setLayout(new GridBagLayout());

        searchIcon.setIcon(ResourceManager.getIcon("search_icon", ResourceManager.SMALL)); // 48 -> 50[SMALL]
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        searchContainer.add(searchIcon, gridBagConstraints);

        search.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        search.setEditable(true);
        search.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        searchContainer.add(search, gridBagConstraints);
        search.setFont(ResourceManager.getFont("inter_regular.24"));
        search.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));

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
        textField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        textField.setCaretColor(Color.white);
        textField.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
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

                String text = textField.getText().toLowerCase();
                search.removeAllItems();
                search.addItem(text);

                if (e.getKeyCode() == KeyEvent.VK_ENTER ||
                        e.getKeyCode() == KeyEvent.VK_TAB) {
                    addTag(textField.getText().toLowerCase());
                    textField.setText("");
                    search.revalidate();
                    search.repaint();
                    revalidate();
                    repaint();
                    return;
                }

                for (DBDataObject suggestionTagData : getTags()) {
                    String suggestion = suggestionTagData.getValue(DBTag.K_TAG);

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

        tagsContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        tagsContainer.setMaximumSize(new Dimension(300, 300));
        tagsContainer.setMinimumSize(new Dimension(300, 300));
        tagsContainer.setOpaque(false);
        tagsContainer.setPreferredSize(new Dimension(300, 300));
        tagsContainer.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                tagsContainerMouseReleased(evt);
            }
        });

        tagHolder.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        tagHolder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagHolder.setCornerRadius(90);
        tagHolder.setLayout(new BorderLayout());

        tagText.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        tagText.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
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

        noResultLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        noResultLabel.setText("No Available Questions");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        add(noResultLabel, gridBagConstraints);
        noResultLabel.setFont(ResourceManager.getFont("inter_thin.48"));

        resultContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
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
                    ResourceManager.getColor(ByteBoardTheme.MAIN) :
                    ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT));
        }
    }

    private void resetQuestions(boolean visible) {
        resultContainer.removeAll();
        noResultLabel.setVisible(visible);
        resultContainer.setVisible(visible);
    }

    private void createTag(String text) {

        CustomJPanel tagHolder = new CustomJPanel();
        tagHolder.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        tagHolder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagHolder.setCornerRadius(90);
        tagHolder.setLayout(new BorderLayout());

        JLabel tagText = new JLabel(text);
        tagText.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        tagText.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
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
            String tag = ((JLabel) ((CustomJPanel) tagComponent).getComponent(0)).getText();

            // select DBTag.*, DBQuestion.question_head, DBUser.user_name, DBUser.user_profile
            // from DBTag
            // Join DBQuestion on DBTag.question_id = DBQuestion.question_id
            // Join DBUser on DBQuestion.user_id = DUser.user_id
            // where tag.tag = 'tag'

            // select DBTag.tag, (join:DBQueTag), DBQuestion.question_head, DBUser.user_name, DBUser.user_profile
            // from DBTag
            // Join DBQueTag on DBTag.tag_id = DBQueTag.qt_tag_id
            // Join DBQuestion on DBQueTag.qt_question_id = DBQuestion.question_id
            // Join DBUser on DBQuestion.user_id = DUser.user_id
            // where tag.tag = 'tag'

            // get questions and the questioner's data by tags
            DBDataObject[] tagData = DBTag.ops.joinValuesBy(DBTag.ops.matchByValue(DBTag.K_TAG, tag),
                    new String[]{
                            DBTag.ops.matchByKey(DBTag.K_TAG_ID, DBQueTag.ops.appendKeys(DBQueTag.K_TAG_ID)),
                            DBQueTag.ops.matchByKey(DBQueTag.K_QUESTION_ID, DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID)),
                            DBQuestion.ops.matchByKey(DBQuestion.K_USER_ID, DBUser.ops.appendKeys(DBUser.K_USER_ID))
                    },
                    DBTag.ops.appendKeys(DBTag.K_TAG),
                    DBQueTag.ops.appendEmptyKeys(),
                    DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_HEAD),
                    DBUser.ops.appendKeys(DBUser.K_USER_NAME, DBUser.K_USER_PROFILE));

            for (DBDataObject tagObject : tagData) {
                String questionID = tagObject.getValue(DBQueTag.K_QUESTION_ID);

                if (resultContains(questionID)) continue;

//                DataObject questionObject = Database.getData(QuestionDataObject.TABLE,
//                        QuestionDataObject.questionIDKey(), questionID)[0];

//                UserDataObject userObject = (UserDataObject) Database.getData(UserDataObject.TABLE,
//                        UserDataObject.userIDKey(), questionObject.get(QuestionDataObject.userIDKey()))[0];
                DEBUG.printlnYellow(questionID);
                addResultBar(
                        tagObject.getValue(DBUser.K_USER_NAME),
                        tagObject.getValue(DBUser.K_USER_PROFILE),
                        tagObject.getValue(DBQuestion.K_QUESTION_HEAD), questionID);
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

    private DBDataObject[] getTags() {
        return DBTag.ops.findValues(true, DBTag.K_TAG);
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
            CustomJPanel tagHolder = (CustomJPanel) tagComponent;
            if (((JLabel) tagHolder.getComponent(0)).getText().equals(tag)) {
                return;
            }
        }

        if (!tag.matches("^[a-zA-Z0-9]+$")) return;

        createTag(tag);
        searchQuestions();
    }
}
