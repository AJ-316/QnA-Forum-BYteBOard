package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.DBCFeedback;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardTextArea;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardResponseCard extends BoardPanel {

    private static final Rectangle checkBounds = new Rectangle(0, 0, 0, 0);
    protected BoardTextArea contentText;
    protected BoardLabel contentUsername;
    protected BoardLabel contentAction;
    private boolean isSelected;
    private MouseAdapter contentActionAdapter;

    public BoardResponseCard(Frame frame) {
        super(frame, ByteBoardTheme.MAIN_DARK);
        Dimension preferredSize = new Dimension(200, 0);

        setBorderColor(ByteBoardTheme.MAIN_DARK);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setPreferredSize(null);
                revalidate();

                preferredSize.height = getPreferredSize().height;
                setPreferredSize(preferredSize);
            }
        });
    }

    public void init(Frame frame) {
        setCornerRadius(20);
        addInsets(5);

        contentText = new BoardTextArea("");
        contentText.setHintText("Content Response goes here...");
        contentText.setLineWrap(true);
        contentText.setFocusable(true);
        contentText.setEditable(false);
        contentText.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 20);

        contentAction = new BoardLabel();
        contentAction.setHorizontalTextPosition(BoardLabel.RIGHT);
        contentAction.setAlignmentLeading();
        contentAction.setVisible(false);
        contentAction.setFGLight();
        contentAction.setFontPrimary(ByteBoardTheme.FONT_T_REGULAR, 18);

        contentUsername = new BoardLabel("Username");
        contentUsername.setHorizontalTextPosition(BoardLabel.LEFT);
        contentUsername.setAlignmentTrailing();
        contentUsername.setFGLight();
        contentUsername.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);

        GridBagBuilder builder = new GridBagBuilder(this, 2);

        builder.insets(5).gridWidth(2).weightY(1).fillBoth()
                .addToNextCell(contentText).skipCells(1);

        builder.gridWidth(1).weight(1, 0)
                .addToNextCell(contentAction)
                .addToNextCell(contentUsername);
    }

    public void setContentAction(boolean isFeedbackGiven, ContentActionListener action, String contentActionText) {
        contentAction.removeMouseListener(contentActionAdapter);
        contentAction.addMouseListener(contentActionAdapter = getContentActionAdapter(action, "useful"));

        setActionSelected(isFeedbackGiven);
        setContentText(contentActionText);
        contentActionAdapter.mouseExited(null);
    }

    public void setContentText(String contentActionText) {
        contentAction.setToolTipText("Comment adds something Useful to the Post");
        contentAction.setVisible(true);
        contentAction.setText(contentActionText);

        contentAction.setColoredIcon("useful", ByteBoardTheme.ACCENT,
                ByteBoardTheme.MAIN_DARK, ResourceManager.MICRO);
    }

    public void setCardData(String contentUsername, String contentUserprofile, String contentText, String contentID) {
        this.contentUsername.setText(contentUsername);
        this.contentUsername.setProfileIcon(contentUserprofile, ResourceManager.MINI);
        this.contentText.setText(contentText);
        this.contentText.setName(contentID);
    }

    public void addMouseListeners(BoardResponseCardPanel cardPanel) {
        setBorderColor(ByteBoardTheme.ACCENT);

        MouseAdapter adapter = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (isSelected()) return;
                highlightCard(true, false);
            }

            public void mouseExited(MouseEvent e) {
                if (isSelected()) return;
                highlightCard(false, false);
            }

            public void mouseReleased(MouseEvent e) {
                checkBounds.width = BoardResponseCard.this.getBounds().width;
                checkBounds.height = BoardResponseCard.this.getBounds().height;

                if (!checkBounds.contains(e.getPoint()))
                    return;

                cardPanel.selectCard(BoardResponseCard.this);
            }
        };

        addMouseListener(adapter);
        contentText.addMouseListener(adapter);
    }

    protected boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        highlightCard(selected, selected);
    }

    /*cardSelectListener.invoke(isSelected ? getContentID() : null);*/
    private void highlightCard(boolean isHighlighted, boolean isSelected) {
        if (isHighlighted) {
            setBackground(isSelected ? ByteBoardTheme.ACCENT_DARK : ByteBoardTheme.ACCENT);
            setBorderColor(ByteBoardTheme.BASE);
            return;
        }

        setBackground(ByteBoardTheme.MAIN_DARK);
        setBorderColor(ByteBoardTheme.ACCENT);
    }

    private MouseAdapter getContentActionAdapter(ContentActionListener action, String icon) {
        return new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                contentAction.setColoredIcon(icon, ByteBoardTheme.BASE,
                        ByteBoardTheme.ACCENT, ResourceManager.MICRO);
            }

            public void mouseExited(MouseEvent e) {
                contentAction.setColoredIcon(icon,
                        isActionSelected() ? ByteBoardTheme.ACCENT : ByteBoardTheme.BASE,
                        isActionSelected() ? ByteBoardTheme.BASE : ByteBoardTheme.MAIN_DARK, ResourceManager.MICRO);
            }

            public void mouseReleased(MouseEvent e) {
                setActionSelected(!isActionSelected());

                contentAction.setColoredIcon(icon,
                        isActionSelected() ? ByteBoardTheme.ACCENT : ByteBoardTheme.BASE,
                        isActionSelected() ? ByteBoardTheme.BASE : ByteBoardTheme.MAIN_DARK, ResourceManager.MICRO);

                action.invoke(contentAction.getName());
            }
        };
    }

    private boolean isActionSelected() {
        return contentAction.getName().equals(DBCFeedback.V_FEEDBACK_USEFUL);
    }

    private void setActionSelected(boolean isSelected) {
        contentAction.setName(isSelected ? DBCFeedback.V_FEEDBACK_USEFUL : DBCFeedback.V_FEEDBACK_NONE);
    }

    public String getContentID() {
        return contentText.getName();
    }

    public interface ContentActionListener {
        void invoke(String feedback);
    }
}
