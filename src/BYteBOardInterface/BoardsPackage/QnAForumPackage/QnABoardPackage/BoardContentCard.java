package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.DBCFeedback;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardTextArea;
import CustomControls.DEBUG;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardContentCard extends BoardPanel {

    private static BoardContentCard lastSelectedCard;

    private BoardTextArea contentText;
    private BoardLabel contentUsername;
    private BoardLabel contentAction;
    private MouseAdapter contentActionAdapter;
    private final Dimension preferredSize;
    private static final Rectangle checkBounds = new Rectangle(0, 0, 0, 0);

    public BoardContentCard(MainFrame main, Frame frame) {
        super(main, frame, ByteBoardTheme.MAIN_LIGHT);
        preferredSize = new Dimension(200, 0);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setPreferredSize(null);
                revalidate();

                preferredSize.height = getPreferredSize().height;
                setPreferredSize(preferredSize);
            }
        });
    }

    public void init(MainFrame main, Frame frame) {
        setCornerRadius(20);
        addInsets(5);

        contentText = new BoardTextArea("Content Response goes here...Content Response goes here...Content Response goes here...Content Response goes here...Content Response goes here...");
        contentText.setLineWrap(true);
        contentText.setFocusable(false);
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

    public void setContentAction(boolean isFeedbackGiven, ContentActionListener action, String contentActionText, String icon) {
        contentAction.removeMouseListener(contentActionAdapter);
        contentAction.addMouseListener(contentActionAdapter = getContentActionAdapter(action, icon));
        contentAction.setToolTipText("Comment adds something Useful to the Post");

        setActionSelected(isFeedbackGiven);
        contentAction.setVisible(true);
        contentAction.setText(contentActionText);
        contentActionAdapter.mouseExited(null);
    }

    public void setCardData(String contentUsername, String contentUserprofile, String contentText, String contentID) {
        this.contentUsername.setText(contentUsername);
        this.contentUsername.setProfileIcon(contentUserprofile, ResourceManager.MINI);
        this.contentText.setText(contentText);
        this.contentText.setName(contentID);
    }

    public void addMouseListeners(CardSelectListener cardSelectListener) {

        MouseAdapter adapter = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(isSelected()) return;
                highlightCard(true);
            }

            public void mouseExited(MouseEvent e) {
                if(isSelected()) return;
                highlightCard(false);
            }

            public void mouseReleased(MouseEvent e) {
                checkBounds.width = BoardContentCard.this.getBounds().width;
                checkBounds.height = BoardContentCard.this.getBounds().height;

                if(!checkBounds.contains(e.getPoint()))
                    return;

                selectCard(!isSelected(), BoardContentCard.this);
                cardSelectListener.invoke(isSelected() ? getContentID() : null);
                refresh();
            }
        };
        addMouseListener(adapter);
        contentText.addMouseListener(adapter);
    }

    private boolean isSelected() {
        return lastSelectedCard != null && lastSelectedCard.equals(this);
    }

    private void highlightCard(boolean isHighlighted) {
        setBackground(isHighlighted ? ByteBoardTheme.ACCENT : ByteBoardTheme.MAIN_LIGHT);
        setBorderColor(isHighlighted ? ByteBoardTheme.ACCENT : null);
    }

    public void restoreCardSelection() {
        if(lastSelectedCard == null) return;
        System.out.println("restored: " + lastSelectedCard.getContentID() + ", " + getContentID());

        if(lastSelectedCard.getContentID().equals(getContentID()))
            selectCard(true, this);

        System.out.println(isSelected());
    }

    public static void selectCard(boolean isSelect, BoardContentCard contentCard) {
        if(!isSelect) {
            if(lastSelectedCard != null)
                lastSelectedCard.highlightCard(false);
            lastSelectedCard = null;
            return;
        }

        if (lastSelectedCard != null)
            lastSelectedCard.highlightCard(false);

        contentCard.highlightCard(true);
        contentCard.setBackground(ByteBoardTheme.ACCENT_DARK);
        lastSelectedCard = contentCard;
    }

    private void setActionSelected(boolean isSelected) {
        contentAction.setName(isSelected ? DBCFeedback.V_FEEDBACK_USEFUL : DBCFeedback.V_FEEDBACK_NONE);
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
                        isActionSelected() ? ByteBoardTheme.BASE : ByteBoardTheme.MAIN_LIGHT, ResourceManager.MICRO);
            }

            public void mouseReleased(MouseEvent e) {
                setActionSelected(!isActionSelected());

                contentAction.setColoredIcon(icon,
                        isActionSelected() ? ByteBoardTheme.ACCENT : ByteBoardTheme.BASE,
                        isActionSelected() ? ByteBoardTheme.BASE : ByteBoardTheme.MAIN_LIGHT, ResourceManager.MICRO);

                action.invoke(contentAction.getName());
            }
        };
    }

    private boolean isActionSelected() {
        return contentAction.getName().equals(DBCFeedback.V_FEEDBACK_USEFUL);
    }

    public String getContentID() {
        return contentText.getName();
    }

    public interface ContentActionListener {
        void invoke(String feedback);
    }

    public interface CardSelectListener {
        void invoke(String contentID);
    }

}
