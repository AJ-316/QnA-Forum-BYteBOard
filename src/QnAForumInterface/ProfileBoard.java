package QnAForumInterface;

import CustomControls.*;
import DataObjects.AnswerDataObject;
import DataObjects.QuestionDataObject;
import DataObjects.UserDataObject;
import QnAForumDatabase.Database;
import QnAForumDatabase.EncryptionUtils;

import QnAForumInterface.InformationBarPackage.ActivityBar;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author AJ
 */
public class ProfileBoard extends JPanel {

    private static String CURRENT_USER;
    int i = 0;
    private JPanel activityContainer;
    private RoundedJPanel activityContainerHolder;
    private JScrollPane activityContainerScrollPane;
    private JButton askQuestionBtn;
    private JButton backBtn;
    private RoundedJPanel buttonHolder;
    private JPanel buttonHolder2;
    private RoundedJPanel chooseProfileHolder;
    private JPanel chooseProfilePane;
    private JScrollPane chooseProfileScrollPane;
    private JPanel editHolder;
    private JButton editProfileBtn;
    private JLabel editProfileIcon;
    private JTextField editUserEmail;
    private JTextField editUserName;
    private JPasswordField editUserPassword;
    private JLabel editUserProfile;
    private JPanel editUserProfileHolder;
    private JPasswordField editUserRePassword;
    private JButton hideActivityBtn;
    private JButton logoutBtn;
    private JLabel noActivityLabel;
    private JButton saveEditBtn;
    private JLabel saveErrorLabel;
    private JButton searchBtn;
    private JLabel userEmail;
    private JLabel userEmailLabel;
    private JLabel userName;
    private JLabel userNameLabel;
    private JLabel userPasswordLabel;
    private JLabel userProfile;
    private JPanel userProfileHolder;
    private JLabel userRePasswordLabel;
    private JButton viewActivityBtn;

    public ProfileBoard() {
        initComponents();
        setEditProfile(false);
        displayActivity(false);
        chooseProfileHolder.setVisible(false);
    }

    public static ProfileBoard init(String username) {
        ProfileBoard board = new ProfileBoard();

        String userID = Database.getData(UserDataObject.TABLE, UserDataObject.userIDKey(),
                UserDataObject.usernameKey(), username, true)[0];

        UserDataObject userData = (UserDataObject) Database.getData(UserDataObject.TABLE,
                UserDataObject.usernameKey(), username)[0];
        String userProfileIndex = userData.get(UserDataObject.profileKey());

        CURRENT_USER = username;
        board.userName.setText(username);
        board.userEmail.setText(userData.get(UserDataObject.emailKey()));
        Component selectedProfilePane = board.chooseProfilePane.getComponent(Integer.parseInt(userProfileIndex));
        ((ProfilePane) selectedProfilePane).setSelected();

        ResourceManager.setProfileIcon(userProfileIndex, board.userProfile, ResourceManager.LARGE);
        ResourceManager.setProfileIcon(userProfileIndex, board.editUserProfile, ResourceManager.LARGE);

        String[] questionIDList = Database.getData(QuestionDataObject.TABLE, QuestionDataObject.questionIDKey(),
                QuestionDataObject.userIDKey(), userID, false);

        for (String questionID : questionIDList) {
            QuestionDataObject questionData = (QuestionDataObject) Database.getData(QuestionDataObject.TABLE,
                    QuestionDataObject.questionIDKey(), questionID)[0];
            board.addActivityBar(
                    username,
                    userProfileIndex,
                    questionData.get(QuestionDataObject.questionHeadKey()),
                    questionData.get(QuestionDataObject.questionIDKey()));
        }

        String[] questionsAnswerdID = Database.getData(AnswerDataObject.TABLE, AnswerDataObject.questionIDKey(),
                AnswerDataObject.userIDKey(), userID, true);

        for (String questionAnswerdID : questionsAnswerdID) {
            QuestionDataObject questionAnswerd = (QuestionDataObject) Database.getData(QuestionDataObject.TABLE,
                    QuestionDataObject.questionIDKey(), questionAnswerdID)[0];

            UserDataObject userAsked = (UserDataObject) Database.getData(UserDataObject.TABLE, UserDataObject.userIDKey(),
                    questionAnswerd.get(QuestionDataObject.userIDKey()))[0];

            board.addActivityBar(
                    userAsked.get(UserDataObject.usernameKey()),
                    userAsked.get(UserDataObject.profileKey()),
                    questionAnswerd.get(QuestionDataObject.questionHeadKey()),
                    questionAnswerd.get(QuestionDataObject.questionIDKey()));
        }

        return board;
    }

    public static String getCurrentUser() {
        return CURRENT_USER;
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        buttonHolder = new RoundedJPanel();
        searchBtn = new JButton();
        askQuestionBtn = new JButton();
        viewActivityBtn = new JButton();
        hideActivityBtn = new JButton();
        editProfileBtn = new JButton();
        logoutBtn = new JButton();
        editUserProfileHolder = new JPanel();
        chooseProfileHolder = new RoundedJPanel();
        chooseProfileScrollPane = new SimpleScrollPane();
        chooseProfilePane = new JPanel();
        editProfileIcon = new JLabel();
        editUserProfile = new JLabel();
        editHolder = new JPanel();
        userNameLabel = new JLabel();
        editUserName = new JTextField();
        userEmailLabel = new JLabel();
        editUserEmail = new JTextField();
        userPasswordLabel = new JLabel();
        editUserPassword = new JPasswordField();
        userRePasswordLabel = new JLabel();
        editUserRePassword = new JPasswordField();
        saveErrorLabel = new JLabel();
        buttonHolder2 = new JPanel();
        saveEditBtn = new JButton();
        backBtn = new JButton();
        userProfileHolder = new JPanel();
        userProfile = new JLabel();
        userName = new JLabel();
        userEmail = new JLabel();
        activityContainerHolder = new RoundedJPanel();
        noActivityLabel = new JLabel();
        activityContainerScrollPane = new SimpleScrollPane();
        activityContainer = new JPanel();

        setBackground(ResourceManager.getColor("base"));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new GridBagLayout());

        buttonHolder.setBackground(ResourceManager.getColor("main"));
        buttonHolder.setCornerRadius(90);
        buttonHolder.setLayout(new GridBagLayout());

        searchBtn.setForeground(ResourceManager.getColor("text_fg_light"));
        searchBtn.setIcon(ResourceManager.getIcon("search_default", 32));
        searchBtn.setText("Search");
        searchBtn.setBorderPainted(false);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setPressedIcon(ResourceManager.getIcon("search_pressed", 32)
        );
        searchBtn.setRolloverIcon(ResourceManager.getIcon("search_rollover", 32)
        );
        searchBtn.addActionListener(evt -> searchBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(5, 30, 5, 35);
        buttonHolder.add(searchBtn, gridBagConstraints);
        searchBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        askQuestionBtn.setForeground(ResourceManager.getColor("text_fg_light"));
        askQuestionBtn.setIcon(ResourceManager.getIcon("question_default", 32)
        );
        askQuestionBtn.setText("Ask Question");
        askQuestionBtn.setBorderPainted(false);
        askQuestionBtn.setContentAreaFilled(false);
        askQuestionBtn.setFocusPainted(false);
        askQuestionBtn.setPressedIcon(ResourceManager.getIcon("question_pressed", 32)
        );
        askQuestionBtn.setRolloverIcon(ResourceManager.getIcon("question_rollover", 32)
        );
        askQuestionBtn.addActionListener(evt -> askQuestionBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 30, 5, 35);
        buttonHolder.add(askQuestionBtn, gridBagConstraints);
        askQuestionBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        viewActivityBtn.setForeground(ResourceManager.getColor("text_fg_light"));
        viewActivityBtn.setIcon(ResourceManager.getIcon("show_default", 32)
        );
        viewActivityBtn.setText("View Activity");
        viewActivityBtn.setBorderPainted(false);
        viewActivityBtn.setContentAreaFilled(false);
        viewActivityBtn.setFocusPainted(false);
        viewActivityBtn.setPressedIcon(ResourceManager.getIcon("show_pressed", 32)
        );
        viewActivityBtn.setRolloverIcon(ResourceManager.getIcon("show_rollover", 32)
        );
        viewActivityBtn.addActionListener(evt -> viewActivityBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 30, 5, 35);
        buttonHolder.add(viewActivityBtn, gridBagConstraints);
        viewActivityBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        hideActivityBtn.setForeground(ResourceManager.getColor("text_fg_light"));
        hideActivityBtn.setIcon(ResourceManager.getIcon("hide_default", 32)
        );
        hideActivityBtn.setText("Hide Activity");
        hideActivityBtn.setBorderPainted(false);
        hideActivityBtn.setContentAreaFilled(false);
        hideActivityBtn.setFocusPainted(false);
        hideActivityBtn.setPressedIcon(ResourceManager.getIcon("hide_pressed", 32)
        );
        hideActivityBtn.setRolloverIcon(ResourceManager.getIcon("hide_rollover", 32)
        );
        hideActivityBtn.addActionListener(evt -> hideActivityBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 30, 5, 35);
        buttonHolder.add(hideActivityBtn, gridBagConstraints);
        hideActivityBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        editProfileBtn.setForeground(ResourceManager.getColor("text_fg_light"));
        editProfileBtn.setIcon(ResourceManager.getIcon("edit_default", 32)
        );
        editProfileBtn.setText("Edit Profile");
        editProfileBtn.setBorderPainted(false);
        editProfileBtn.setContentAreaFilled(false);
        editProfileBtn.setFocusPainted(false);
        editProfileBtn.setPressedIcon(ResourceManager.getIcon("edit_pressed", 32)
        );
        editProfileBtn.setRolloverIcon(ResourceManager.getIcon("edit_rollover", 32)
        );
        editProfileBtn.addActionListener(evt -> editProfileBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 30, 5, 35);
        buttonHolder.add(editProfileBtn, gridBagConstraints);
        editProfileBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        logoutBtn.setForeground(ResourceManager.getColor("text_fg_light"));
        logoutBtn.setIcon(ResourceManager.getIcon("logout_default", 32)
        );
        logoutBtn.setText("Log Out");
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setPressedIcon(ResourceManager.getIcon("logout_pressed", 32)
        );
        logoutBtn.setRolloverIcon(ResourceManager.getIcon("logout_rollover", 32)
        );
        logoutBtn.addActionListener(evt -> logoutBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 30, 100, 35);
        buttonHolder.add(logoutBtn, gridBagConstraints);
        logoutBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 0);
        add(buttonHolder, gridBagConstraints);

        editUserProfileHolder.setBackground(ResourceManager.getColor("base"));
        editUserProfileHolder.setMinimumSize(new Dimension(640, 360));
        editUserProfileHolder.setPreferredSize(new Dimension(640, 360));
        editUserProfileHolder.setLayout(new GridBagLayout());

        chooseProfileHolder.setBackground(ResourceManager.getColor("main_dark"));
        chooseProfileHolder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chooseProfileHolder.setMinimumSize(new Dimension(310, 41));
        chooseProfileHolder.setLayout(new BorderLayout());

        chooseProfileScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chooseProfileScrollPane.setMaximumSize(new Dimension(300, 32767));
        chooseProfileScrollPane.setOpaque(false);
        chooseProfileScrollPane.setPreferredSize(new Dimension(300, 100));
        chooseProfileScrollPane.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                chooseProfileScrollPaneMouseReleased(evt);
            }
        });

        chooseProfilePane.setName("");
        chooseProfilePane.setOpaque(false);
        chooseProfileScrollPane.setViewportView(chooseProfilePane);
        chooseProfilePane.setLayout(new WrapLayout(WrapLayout.LEADING, 10, 10));
        Icon icon;
        int iconPtr = 0;
        while ((icon = ResourceManager.getIcon("profiles/profile_" + iconPtr, ResourceManager.REGULAR)) != null) {
            ProfilePane profilePane = ProfilePane.create(icon);
            profilePane.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    Component[] components = chooseProfilePane.getComponents();
                    for (int i = 0; i < components.length; i++) {
                        if (components[i].equals(e.getComponent())) {
                            selectNewProfile(i);
                            break;
                        }
                    }
                }
            });
            chooseProfilePane.add(profilePane);

            iconPtr++;
        }

        chooseProfileScrollPane.revalidate();
        chooseProfileScrollPane.repaint();

        chooseProfileHolder.add(chooseProfileScrollPane, BorderLayout.CENTER);
        chooseProfileScrollPane.getViewport().setOpaque(false);
        chooseProfileScrollPane.getVerticalScrollBar().setUnitIncrement(15);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        editUserProfileHolder.add(chooseProfileHolder, gridBagConstraints);

        editProfileIcon.setIcon(ResourceManager.getIcon("edit_profile", ResourceManager.REGULAR)
        );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        editUserProfileHolder.add(editProfileIcon, gridBagConstraints);

        editUserProfile.setIcon(ResourceManager.getIcon("user_profile", ResourceManager.LARGE)
        );
        editUserProfile.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                editUserProfileMouseEntered(evt);
            }

            public void mouseExited(MouseEvent evt) {
                editUserProfileMouseExited(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                editUserProfileMouseReleased(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.1;
        editUserProfileHolder.add(editUserProfile, gridBagConstraints);

        editHolder.setBackground(ResourceManager.getColor("base"));
        editHolder.setLayout(new GridBagLayout());

        userNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        userNameLabel.setText("Username:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        editHolder.add(userNameLabel, gridBagConstraints);
        userNameLabel.setFont(ResourceManager.getFont("inter_regular.24"));

        editUserName.setText("Username");
        editUserName.setMinimumSize(new Dimension(220, 40));
        editUserName.setPreferredSize(new Dimension(220, 40));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 10, 20);
        editHolder.add(editUserName, gridBagConstraints);
        editUserName.setFont(ResourceManager.getFont("inter_regular.24"));

        userEmailLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        userEmailLabel.setText("Email-ID:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        editHolder.add(userEmailLabel, gridBagConstraints);
        userEmailLabel.setFont(ResourceManager.getFont("inter_regular.24"));

        editUserEmail.setText("EmailId");
        editUserEmail.setMinimumSize(new Dimension(220, 40));
        editUserEmail.setPreferredSize(new Dimension(220, 40));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 10, 20);
        editHolder.add(editUserEmail, gridBagConstraints);
        editUserEmail.setFont(ResourceManager.getFont("inter_regular.24"));

        userPasswordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        userPasswordLabel.setText("New Password:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        editHolder.add(userPasswordLabel, gridBagConstraints);
        userPasswordLabel.setFont(ResourceManager.getFont("inter_regular.24"));

        editUserPassword.setAutoscrolls(false);
        editUserPassword.setMinimumSize(new Dimension(220, 40));
        editUserPassword.setPreferredSize(new Dimension(220, 40));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 10, 20);
        editHolder.add(editUserPassword, gridBagConstraints);
        editUserPassword.setFont(ResourceManager.getFont("inter_regular.14"));

        userRePasswordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        userRePasswordLabel.setText("Re-Password:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        editHolder.add(userRePasswordLabel, gridBagConstraints);
        userRePasswordLabel.setFont(ResourceManager.getFont("inter_regular.24"));

        editUserRePassword.setAutoscrolls(false);
        editUserRePassword.setMinimumSize(new Dimension(220, 40));
        editUserRePassword.setPreferredSize(new Dimension(220, 40));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 10, 20);
        editHolder.add(editUserRePassword, gridBagConstraints);
        editUserRePassword.setFont(ResourceManager.getFont("inter_regular.14"));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        editUserProfileHolder.add(editHolder, gridBagConstraints);

        saveErrorLabel.setForeground(new Color(210, 0, 0));
        saveErrorLabel.setText("errorLabel");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(50, 0, 0, 0);
        editUserProfileHolder.add(saveErrorLabel, gridBagConstraints);
        saveErrorLabel.setFont(ResourceManager.getFont("inter_semibold.20"));

        buttonHolder2.setOpaque(false);
        buttonHolder2.setLayout(new GridBagLayout());

        saveEditBtn.setIcon(ResourceManager.getIcon("save_default", 32)
        );
        saveEditBtn.setText("Save Changes");
        saveEditBtn.setBorderPainted(false);
        saveEditBtn.setContentAreaFilled(false);
        saveEditBtn.setFocusPainted(false);
        saveEditBtn.setPressedIcon(ResourceManager.getIcon("save_pressed", 32)
        );
        saveEditBtn.setRolloverIcon(ResourceManager.getIcon("save_rollover", 32)
        );
        saveEditBtn.addActionListener(evt -> saveEditBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.1;
        buttonHolder2.add(saveEditBtn, gridBagConstraints);
        saveEditBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        backBtn.setIcon(ResourceManager.getIcon("home_default", 32)
        );
        backBtn.setText("Back");
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setPressedIcon(ResourceManager.getIcon("home_pressed", 32)
        );
        backBtn.setRolloverIcon(ResourceManager.getIcon("home_rollover", 32)
        );
        backBtn.addActionListener(evt -> backBtnActionPerformed(evt));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.1;
        buttonHolder2.add(backBtn, gridBagConstraints);
        backBtn.setFont(ResourceManager.getFont("inter_regular.22"));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(25, 0, 0, 0);
        editUserProfileHolder.add(buttonHolder2, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        add(editUserProfileHolder, gridBagConstraints);

        userProfileHolder.setBackground(ResourceManager.getColor("base"));
        userProfileHolder.setMinimumSize(new Dimension(640, 360));
        userProfileHolder.setPreferredSize(new Dimension(640, 360));
        userProfileHolder.setLayout(new GridBagLayout());

        userProfile.setIcon(ResourceManager.getIcon("user_profile", ResourceManager.LARGE)
        );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        userProfileHolder.add(userProfile, gridBagConstraints);

        userName.setText("Username");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        userProfileHolder.add(userName, gridBagConstraints);
        userName.setFont(ResourceManager.getFont("inter_bold.32"));

        userEmail.setText("EmailID");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        userProfileHolder.add(userEmail, gridBagConstraints);
        userEmail.setFont(ResourceManager.getFont("inter_semibold.22"));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        add(userProfileHolder, gridBagConstraints);

        activityContainerHolder.setBackground(ResourceManager.getColor("main_dark"));
        activityContainerHolder.setCornerRadius(90);
        activityContainerHolder.setLayout(new GridBagLayout());

        noActivityLabel.setForeground(new Color(153, 153, 153));
        noActivityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noActivityLabel.setText("No Activity Yet!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        activityContainerHolder.add(noActivityLabel, gridBagConstraints);
        noActivityLabel.setFont(ResourceManager.getFont("inter_thin.48"));

        activityContainerScrollPane.setBackground(ResourceManager.getColor("main"));
        activityContainerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        activityContainerScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        activityContainerScrollPane.setMinimumSize(new Dimension(640, 360));
        activityContainerScrollPane.setOpaque(false);
        activityContainerScrollPane.setPreferredSize(new Dimension(640, 360));

        activityContainer.setBackground(ResourceManager.getColor("main_dark"));
        activityContainer.setOpaque(false);
        activityContainer.setLayout(new BoxLayout(activityContainer, BoxLayout.Y_AXIS));
        activityContainerScrollPane.setViewportView(activityContainer);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 5, 10, 5);
        activityContainerHolder.add(activityContainerScrollPane, gridBagConstraints);
        activityContainerScrollPane.setOpaque(false);
        activityContainerScrollPane.getViewport().setOpaque(false);
        activityContainerScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 0, 5, 5);
        add(activityContainerHolder, gridBagConstraints);
    }

    private void selectNewProfile(int index) {
        ResourceManager.setProfileIcon(Integer.toString(index), editUserProfile, ResourceManager.LARGE);
    }

    private void setEditProfile(boolean editProfile) {
        if (activityContainerHolder.isVisible()) {
            displayActivity(false);
        }

        userProfileHolder.setVisible(!editProfile);
        editUserProfileHolder.setVisible(editProfile);
        buttonHolder.setVisible(!editProfile);
        saveErrorLabel.setText("");

        if (editProfile) {
            editUserName.setText(userName.getText());
            editUserEmail.setText(userEmail.getText());
            saveEditBtn.requestFocus();
        }
    }

    private boolean saveEditedProfile() {
        String username = editUserName.getText();
        String email = editUserEmail.getText();

        if (username.length() <= 3) {
            saveErrorLabel.setText("Username too small");
            return false;
        }

        if (!userName.getText().equals(username) &&
                !Database.isUsernameAvailable(username)) {
            saveErrorLabel.setText("Username already taken");
            return false;
        }

        if (!EncryptionUtils.isValidEmail(email)) {
            saveErrorLabel.setText("Invaild Email");
            return false;
        }

        String password = null;

        if (editUserPassword.getPassword().length != 0) {
            if (!isPasswordConfirmed()) {
                saveErrorLabel.setText("Passwords do not match");
                return false;
            }

            if (!EncryptionUtils.isValidPassword(editUserPassword.getPassword())) {
                if (editUserPassword.getPassword().length < 8 || editUserPassword.getPassword().length > 20) {
                    saveErrorLabel.setText("Password length must be 8-20");
                    return false;
                }
                saveErrorLabel.setText("Include Uppercase, Lowercase, Digit, and Special Character");
                return false;
            }
            password = EncryptionUtils.encryptPwd(editUserPassword.getPassword());
        }

        CURRENT_USER = username;

        String profileIndex = "0";
        Component[] components = chooseProfilePane.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (((ProfilePane) components[i]).isSelected()) {
                profileIndex = Integer.toString(i);
                break;
            }
        }

        UserDataObject userData = new UserDataObject(username, password, email.toLowerCase(), profileIndex);
        Database.updateData(userData, UserDataObject.usernameKey(), userName.getText());
        ResourceManager.setProfileIcon(profileIndex, userProfile, ResourceManager.LARGE);
        return true;
    }

    private boolean isPasswordConfirmed() {
        if (editUserPassword.getPassword().length != editUserRePassword.getPassword().length)
            return false;

        for (int i = 0; i < editUserPassword.getPassword().length; i++) {
            if (editUserPassword.getPassword()[i] != editUserRePassword.getPassword()[i])
                return false;
        }

        return true;
    }

    private void editProfileBtnActionPerformed(ActionEvent evt) {
        setEditProfile(true);
    }

    private void displayActivity(boolean display) {
        activityContainerHolder.setVisible(display);
        hideActivityBtn.setVisible(display);
        viewActivityBtn.setVisible(!display);
    }

    private void viewActivityBtnActionPerformed(ActionEvent evt) {
        displayActivity(true);
    }

    private void hideActivityBtnActionPerformed(ActionEvent evt) {
        displayActivity(false);
    }

    private void saveEditBtnActionPerformed(ActionEvent evt) {
        if (saveEditedProfile()) {
            setEditProfile(false);
            userName.setText(editUserName.getText());
            userEmail.setText(editUserEmail.getText());
        }
    }

    private void askQuestionBtnActionPerformed(ActionEvent evt) {
        QnAForum.setContent(AskBoard.init(userName.getText(), userProfile.getName()));
    }

    private void logoutBtnActionPerformed(ActionEvent evt) {
        QnAForum.logout();
    }

    private void searchBtnActionPerformed(ActionEvent evt) {
        QnAForum.setContent(SearchBoard.init(userName.getText(), userProfile.getName()));
    }

    private void editUserProfileMouseEntered(MouseEvent evt) {
        editProfileIcon.setIcon(ResourceManager.getIcon("edit_profile_rollover", ResourceManager.REGULAR));
    }

    private void editUserProfileMouseExited(MouseEvent evt) {
        editProfileIcon.setIcon(ResourceManager.getIcon("edit_profile", ResourceManager.REGULAR));
    }

    private void editUserProfileMouseReleased(MouseEvent evt) {
        chooseProfileHolder.setVisible(!chooseProfileHolder.isVisible());
        revalidate();
        repaint();
    }

    private void chooseProfileScrollPaneMouseReleased(MouseEvent evt) {
        saveEditBtn.setText(chooseProfilePane.getSize().getHeight() + ", " + i++);
    }

    private void backBtnActionPerformed(ActionEvent evt) {
        setEditProfile(false);
    }

    public void clearAnswerBoards() {
        this.activityContainer.removeAll();
        this.noActivityLabel.setVisible(true);
        revalidate();
        repaint();
    }

    public void removeAnswerBoard(String questionerName) {
        int indexToRemove = -1;
        for (int i = 0; i < activityContainer.getComponents().length; i++) {
            Component component = activityContainer.getComponents()[i];
            if (component instanceof ActivityBar) {
                if (((ActivityBar) component).isRespondent(questionerName)) {
                    indexToRemove = i;
                    break;
                }
            }
        }
        if (indexToRemove == -1) return;

        activityContainer.remove(indexToRemove);
        this.noActivityLabel.setVisible(activityContainer.getComponentCount() == 0);

        updateAnswerBoardColors();

        revalidate();
        repaint();
    }

    public void addActivityBar(String questioner, String questionerProfileIndex, String questionHead, String questionID) {
        noActivityLabel.setVisible(false);
        ActivityBar activityBar = new ActivityBar();

        activityBar.setContent(questioner, questionerProfileIndex, questionHead, questionID);
        activityBar.setMaximumSize(new Dimension(getPreferredSize().width, activityBar.getPreferredSize().height));

        this.activityContainer.add(activityBar, 0);

        activityBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        updateAnswerBoardColors();
        revalidate();
        repaint();
    }

    private void updateAnswerBoardColors() {
        for (int i = 0; i < activityContainer.getComponentCount(); i++) {
            ActivityBar activityBar = (ActivityBar) activityContainer.getComponent(i);

            activityBar.setDefaultBackground(i % 2 == 0 ?
                    ResourceManager.getColor("main") :
                    ResourceManager.getColor("main_light"));
        }
    }
}
