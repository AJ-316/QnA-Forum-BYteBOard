/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package QnAForumInterface;

import CustomControls.CustomJPanel;
import CustomControls.SimpleScrollPane;
import BYteBOardDatabase.DBAnswer;
import BYteBOardDatabase.DBQuestion;
import BYteBOardDatabase.DBTag;
import BYteBOardDatabase.DBUser;

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
public class AskBoard extends JPanel {

    private JPanel askContainer;
    private JButton backBtn;
    private JTextArea body;
    private CustomJPanel bodyContainer;
    private JLabel bodyLabel;
    private SimpleScrollPane bodyScrollPane;
    private JPanel buttonHolder;
    private CustomJPanel container;
    private SimpleScrollPane containerScrollPane;
    private JTextArea head;
    private CustomJPanel headContainer;
    private JLabel headLabel;
    private SimpleScrollPane headScrollPane;
    private JButton searchBtn;
    private JButton submitBtn;
    private JComboBox<String> tag;
    private CustomJPanel tagContainer;
    private JLabel tagText;
    private JPanel tagsDisplay;
    private JLabel tagsErrorLabel;
    private JLabel tagsLabel;
    private CustomJPanel tagsRoundedPanel;
    private JLabel titleLabel;
    private JPanel userHolder;
    private JLabel userName;
    private JLabel userProfile;
    private String questionID;
    /**
     * Creates new form AskBoard
     */
    public AskBoard() {
        initComponents();
    }

    public static AskBoard init(String username, String userProfileIndex, String questioner, String questionHead, String questionBody, String questionID) {
        AskBoard board = new AskBoard();

        board.userName.setText(username);
        ResourceManager.setProfileIndexIcon(userProfileIndex, board.userProfile, ResourceManager.REGULAR);
        board.headLabel.setText(questioner + ": " + questionHead);
        board.head.setText(questionBody);
        board.head.setEditable(false);
        board.tagsDisplay.setVisible(false);
        board.tagContainer.setVisible(false);
        board.searchBtn.setVisible(false);
        board.bodyLabel.setText("Answer:");
        board.submitBtn.setText("Submit Answer");
        board.titleLabel.setText("Respond to Question");
        board.bodyLabel.setIcon(ResourceManager.getStateIcon("answer", ResourceManager.PRESSED, ResourceManager.MINI));
        board.questionID = questionID;
        return board;
    }

    public static AskBoard init(String username, String userProfileIndex) {
        AskBoard board = new AskBoard();

        board.userName.setText(username);
        ResourceManager.setProfileIndexIcon(userProfileIndex, board.userProfile, ResourceManager.REGULAR);
        board.headLabel.setText("Question Head - Keep it simple");
        board.bodyLabel.setText("Question Body - Elaborate your Question");
        board.submitBtn.setText("Ask a Question");
        board.tagText.setText("");
        board.tagsRoundedPanel.setVisible(false);
        board.questionID = null;
        return board;
    }

    public void clearTag() {
        String[] tags = tagText.getText().split(" \\| ");


        if (tags.length <= 1) {
            tagText.setText("");
            tagsRoundedPanel.setVisible(false);
            return;
        }

        StringBuilder clearedTagText = new StringBuilder();
        for (int i = 0; i < tags.length - 1; i++) {
            clearedTagText.append(tags[i]);
            if (i < tags.length - 2) {
                clearedTagText.append(" | ");
            }
        }
        tagText.setText(clearedTagText.toString());
    }

    public void addTag(String tag) {

        tag = tag.replaceAll(" ", "");
        String[] tags = tagText.getText().split(" \\| ");
        if(tag.length() > 10) {
            tagsErrorLabel.setText("Tag too long!");
            tagsErrorLabel.setVisible(true);
            return;
        }

        if(tags.length >= 5) {
            tagsErrorLabel.setText("Maximum 5 Tags allowed!");
            tagsErrorLabel.setVisible(true);
            return;
        }

        boolean containsTag = false;

        for (String checkTag : tags) {
            if (checkTag.equals(tag)) {
                containsTag = true;
                break;
            }
        }

        if (!tag.matches("^[a-zA-Z0-9]+$") || containsTag) return;

        tagsRoundedPanel.setVisible(true);
        tag = tagText.getText().isEmpty() ? tag : tagText.getText() + " | " + tag;
        tagText.setText(tag);
        tagsRoundedPanel.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        container = new CustomJPanel();
        userHolder = new JPanel();
        userProfile = new JLabel();
        userName = new JLabel();
        buttonHolder = new JPanel();
        searchBtn = new JButton();
        submitBtn = new JButton();
        backBtn = new JButton();
        titleLabel = new JLabel();
        containerScrollPane = new SimpleScrollPane();
        containerScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        askContainer = new JPanel();
        headLabel = new JLabel();
        headContainer = new CustomJPanel();
        headScrollPane = new SimpleScrollPane();
        headScrollPane.getVerticalScrollBar().setUnitIncrement(2);
        head = new JTextArea();
        bodyLabel = new JLabel();
        bodyContainer = new CustomJPanel();
        bodyScrollPane = new SimpleScrollPane(null);
        bodyScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        body = new JTextArea();
        tagsErrorLabel = new JLabel();
        tagsDisplay = new JPanel();
        tagsLabel = new JLabel();
        tagsRoundedPanel = new CustomJPanel();
        tagText = new JLabel();
        tagContainer = new CustomJPanel();
        tag = new JComboBox<>();

        setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        setLayout(new GridBagLayout());

        container.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        container.setCornerRadius(90);
        container.setPreferredSize(new Dimension(400, 120));
        container.setLayout(new GridBagLayout());

        userHolder.setOpaque(false);
        userHolder.setLayout(new GridBagLayout());

        userProfile.setIcon(ResourceManager.getProfileIcon("0", ResourceManager.REGULAR)
        );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 20, 0, 20);
        userHolder.add(userProfile, gridBagConstraints);

        userName.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        userName.setText("Username");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new Insets(0, 20, 0, 20);
        userHolder.add(userName, gridBagConstraints);
        userName.setFont(ResourceManager.getFont("inter_semibold.36"));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weighty = 1.0;
        container.add(userHolder, gridBagConstraints);

        buttonHolder.setOpaque(false);
        buttonHolder.setLayout(new GridBagLayout());

        searchBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        searchBtn.setText("Search");
        searchBtn.setBorderPainted(false);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setFocusPainted(false);
        ResourceManager.setButtonIcons(searchBtn, "search", ResourceManager.MINI);
        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new Insets(20, 40, 5, 40);
        buttonHolder.add(searchBtn, gridBagConstraints);
        searchBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        submitBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        submitBtn.setText("Submit");
        submitBtn.setBorderPainted(false);
        submitBtn.setContentAreaFilled(false);
        submitBtn.setFocusPainted(false);
        ResourceManager.setButtonIcons(submitBtn, "submit", ResourceManager.MINI);
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                submitBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new Insets(5, 40, 5, 40);
        buttonHolder.add(submitBtn, gridBagConstraints);
        submitBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        backBtn.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        backBtn.setText("Back");
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        ResourceManager.setButtonIcons(backBtn, "home", ResourceManager.MINI);
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new Insets(5, 40, 20, 40);
        buttonHolder.add(backBtn, gridBagConstraints);
        backBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        container.add(buttonHolder, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(container, gridBagConstraints);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setText("Ask Question");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(20, 20, 20, 20);
        add(titleLabel, gridBagConstraints);
        titleLabel.setFont(ResourceManager.getFont("inter_semibold.32"));
        titleLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        containerScrollPane.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        containerScrollPane.setBorder(null);
        containerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        containerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        containerScrollPane.setAutoscrolls(true);
        containerScrollPane.setOpaque(false);

        askContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.BASE));
        askContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        askContainer.setLayout(new GridBagLayout());

        headLabel.setIcon(ResourceManager.getStateIcon("question", ResourceManager.PRESSED, ResourceManager.MINI)
        );
        headLabel.setText("Question Head - Keep it simple");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        askContainer.add(headLabel, gridBagConstraints);
        headLabel.setFont(ResourceManager.getFont("inter_regular.24"));
        headLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        headContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        headContainer.setCornerRadius(90);
        headContainer.setLimitRadius(false);
        headContainer.setLayout(new BorderLayout());

        headScrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 20, 15));
        headScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        headScrollPane.setMinimumSize(new Dimension(23, 90));
        headScrollPane.setOpaque(false);
        headScrollPane.setPreferredSize(new Dimension(486, 100));

        head.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        head.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        head.setLineWrap(true);
        head.setText("Head...");
        head.setWrapStyleWord(true);
        head.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        head.setCaretColor(ResourceManager.getColor(ByteBoardTheme.BASE));
        head.setMinimumSize(new Dimension(0, 0));
        head.setOpaque(false);
        head.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                headFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                headFocusLost(evt);
            }
        });
        headScrollPane.setViewportView(head);
        head.setFont(ResourceManager.getFont("inter_regular.24"));

        headContainer.add(headScrollPane, BorderLayout.CENTER);
        headScrollPane.getViewport().setOpaque(false);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        askContainer.add(headContainer, gridBagConstraints);

        bodyLabel.setIcon(ResourceManager.getStateIcon("question", ResourceManager.PRESSED, ResourceManager.MINI)
        );
        bodyLabel.setText("Question Body - Elaborate your Question");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(40, 0, 0, 0);
        askContainer.add(bodyLabel, gridBagConstraints);
        bodyLabel.setFont(ResourceManager.getFont("inter_regular.24"));
        bodyLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        bodyContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        bodyContainer.setCornerRadius(90);
        bodyContainer.setLayout(new BorderLayout());

        bodyScrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 20, 15));
        bodyScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        bodyScrollPane.setMinimumSize(new Dimension(23, 100));
        bodyScrollPane.setOpaque(false);
        bodyScrollPane.setPreferredSize(new Dimension(486, 200));

        body.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        body.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        body.setLineWrap(true);
        body.setText("Body...");
        body.setWrapStyleWord(true);
        body.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        body.setCaretColor(ResourceManager.getColor(ByteBoardTheme.BASE));
        body.setMinimumSize(new Dimension(0, 0));
        body.setOpaque(false);
        body.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                bodyFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                bodyFocusLost(evt);
            }
        });
        bodyScrollPane.setViewportView(body);
        body.setFont(ResourceManager.getFont("inter_regular.24"));

        bodyContainer.add(bodyScrollPane, BorderLayout.CENTER);
        bodyScrollPane.getViewport().setOpaque(false);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 0, 40, 0);
        askContainer.add(bodyContainer, gridBagConstraints);

        tagsErrorLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.ERROR));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 20, 0, 0);
        askContainer.add(tagsErrorLabel, gridBagConstraints);
        tagsErrorLabel.setFont(ResourceManager.getFont("inter_semibold.16"));
        tagsErrorLabel.setVisible(false);

        tagsDisplay.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tagsDisplay.setOpaque(false);

        tagsLabel.setIcon(ResourceManager.getStateIcon("tag", ResourceManager.PRESSED, ResourceManager.MINI));
        tagsLabel.setText("Tags");
        tagsLabel.setToolTipText("");
        tagsDisplay.add(tagsLabel);
        tagsLabel.setFont(ResourceManager.getFont("inter_regular.24"));
        tagsLabel.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        tagsRoundedPanel.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN));
        tagsRoundedPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        tagsRoundedPanel.setCornerRadius(50);
        tagsRoundedPanel.setLimitRadius(false);
        tagsRoundedPanel.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                tagsRoundedPanelMouseReleased(evt);
            }
        });
        tagsRoundedPanel.setLayout(new BoxLayout(tagsRoundedPanel, BoxLayout.X_AXIS));

        tagText.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        tagText.setText("tag1 | tag2 | tag3");
        tagText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tagsRoundedPanel.add(tagText);
        tagText.setFont(ResourceManager.getFont("inter_regular.22"));

        tagsDisplay.add(tagsRoundedPanel);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        askContainer.add(tagsDisplay, gridBagConstraints);

        tagContainer.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        tagContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tagContainer.setCornerRadius(90);
        tagContainer.setLayout(new BorderLayout());

        tag.setBackground(tagContainer.getBackground());
        tag.setEditable(true);
        tag.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        tagContainer.add(tag, BorderLayout.PAGE_END);
        tag.setFont(ResourceManager.getFont("inter_regular.24"));

        tag.setUI(new BasicComboBoxUI() {
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
        tag.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tag.setMaximumRowCount(3);

        JTextField textField = (JTextField) tag.getEditor().getEditorComponent();
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                tagsLabel.setIcon(ResourceManager.getStateIcon("tag", ResourceManager.ROLLOVER, ResourceManager.MINI));
            }

            public void focusLost(FocusEvent evt) {
                tagsLabel.setIcon(ResourceManager.getStateIcon("tag", ResourceManager.PRESSED, ResourceManager.MINI));
            }
        });
        textField.setForeground(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        textField.setCaretColor(ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        textField.setBackground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_UP) {
                    revalidate();
                    repaint();
                    return;
                }

                String text = textField.getText();
                tag.removeAllItems();
                tag.addItem(text);

                if (e.getKeyCode() == KeyEvent.VK_ENTER ||
                        e.getKeyCode() == KeyEvent.VK_TAB) {
                    addTag(textField.getText().toLowerCase());
                    textField.setText("");
                    revalidate();
                    repaint();
                    return;
                }

                for (String suggestion : getTags()) {
                    if (suggestion.equalsIgnoreCase(text)) {
                        tag.removeAllItems();
                        tag.addItem(text);
                        tag.addItem(suggestion);
                    } else if (suggestion.toLowerCase().startsWith(text.toLowerCase())) {
                        tag.addItem(suggestion);
                    }

                }
                tag.setPopupVisible(tag.getItemCount() > 1);
                tag.revalidate();
                tag.repaint();
                revalidate();
                repaint();
                tagsErrorLabel.setVisible(false);
            }
        });

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        askContainer.add(tagContainer, gridBagConstraints);

        containerScrollPane.setViewportView(askContainer);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        add(containerScrollPane, gridBagConstraints);
        containerScrollPane.getViewport().setOpaque(false);
        containerScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

    }// </editor-fold>//GEN-END:initComponents

    private void switchToProfile() {
        QnAForum.setContent(ProfileBoard.init(userName.getText()));
    }

    private boolean isAskBoard() {
        return questionID == null;
    }

    private void submitBtnActionPerformed(ActionEvent evt) {
        String[] tags = tagText.getText().split(" \\| ");
        if (isAskBoard() && tags.length < 3) {
            tagsErrorLabel.setText("Add at least 3 Tags!");
            tagsErrorLabel.setVisible(true);
            return;
        }

        // get userID where username matches
        String userID = DBUser.accessUser(userName.getText(), false, true).getValue(DBUser.K_USER_ID);

        if (isAskBoard()) {
            String headText = head.getText().trim();
            String bodyText = body.getText().trim();

            if (headText.isEmpty() || bodyText.isEmpty()) return;

            String questionID = DBQuestion.ops.addQuestion(headText, bodyText, userID);
            for (String tag : tags)
                DBTag.ops.addTag(tag, questionID);

        } else {
            String bodyText = body.getText().trim();

            if (bodyText.isEmpty()) return;
            DBAnswer.ops.addAnswer(body.getText().trim(), userID, questionID);
        }

        switchToProfile();
    }

    private void backBtnActionPerformed(ActionEvent evt) {
        switchToProfile();
    }

    private void bodyFocusGained(FocusEvent evt) {
        if (isAskBoard())
            bodyLabel.setIcon(ResourceManager.getStateIcon("question", ResourceManager.ROLLOVER, ResourceManager.MINI));
        else
            bodyLabel.setIcon(ResourceManager.getStateIcon("answer", ResourceManager.ROLLOVER, ResourceManager.MINI));
    }

    private void bodyFocusLost(FocusEvent evt) {
        if (isAskBoard())
            bodyLabel.setIcon(ResourceManager.getStateIcon("question", ResourceManager.PRESSED, ResourceManager.MINI));
        else
            bodyLabel.setIcon(ResourceManager.getStateIcon("answer", ResourceManager.PRESSED, ResourceManager.MINI));
    }

    private void headFocusGained(FocusEvent evt) {
        headLabel.setIcon(ResourceManager.getStateIcon("question", ResourceManager.ROLLOVER, ResourceManager.MINI));
    }

    private void headFocusLost(FocusEvent evt) {
        headLabel.setIcon(ResourceManager.getStateIcon("question", ResourceManager.PRESSED, ResourceManager.MINI));
    }

    private void tagsRoundedPanelMouseReleased(MouseEvent evt) {
        clearTag();
    }

    private void searchBtnActionPerformed(ActionEvent evt) {
        QnAForum.setContent(SearchBoard.init(userName.getText(), userProfile.getName()));
    }

    private String[] getTags() {
        return TagExtractor.extractPotentialTags(head.getText() + " " + body.getText());
    }
}
