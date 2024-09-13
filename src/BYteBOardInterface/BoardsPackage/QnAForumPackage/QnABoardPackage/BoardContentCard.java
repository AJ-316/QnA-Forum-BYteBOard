package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardLabel;
import CustomControls.BoardTextArea;
import CustomControls.DEBUG;
import CustomControls.GridBagBuilder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardContentCard extends BoardPanel {

    private BoardTextArea contentText;
    private BoardLabel contentUsername;
    private BoardLabel contentAction;
    private MouseAdapter contentActionAdapter;
    private final Dimension preferredSize;

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
        contentAction.setAlignmentLeading();
        contentAction.setVisible(false);

        contentUsername = new BoardLabel("Username");
        contentUsername.setAlignmentTrailing();
        contentUsername.setFGLight();
        contentUsername.setFontPrimary(ByteBoardTheme.FONT_T_SEMIBOLD, 18);

        GridBagBuilder builder = new GridBagBuilder(this, 2);
        builder.insets(5, 5, 5, 5);

        builder.add(contentText, 2, 1, 0, 1, GridBagConstraints.BOTH);
        builder.skipCells(1);
        builder.add(contentAction, 1, 0, GridBagConstraints.BOTH);
        builder.add(contentUsername, 1, 0, GridBagConstraints.BOTH);
    }

    public void setContentAction(boolean isFeedbackUseful, ContentActionListener action, String icon) {
        contentAction.removeMouseListener(contentActionAdapter);
        contentAction.addMouseListener(contentActionAdapter = getContentActionAdapter(action, icon));

        setActionSelected(isFeedbackUseful);
        contentAction.setVisible(true);
        contentActionAdapter.mouseExited(null);
    }

    private MouseAdapter getContentActionAdapter(ContentActionListener action, String icon) {
        return new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                contentAction.setColoredIcon(icon, ByteBoardTheme.BASE,
                        ByteBoardTheme.ACCENT, ResourceManager.MICRO);
            }

            public void mouseExited(MouseEvent e) {
                contentAction.setColoredIcon(icon,
                        isActionSelected() ? ByteBoardTheme.MAIN_LIGHT : ByteBoardTheme.BASE,
                        isActionSelected() ? ByteBoardTheme.BASE : ByteBoardTheme.MAIN_LIGHT, ResourceManager.MICRO);
            }

            public void mouseReleased(MouseEvent e) {
                setActionSelected(!isActionSelected());

                contentAction.setColoredIcon(icon,
                        isActionSelected() ? ByteBoardTheme.MAIN_LIGHT : ByteBoardTheme.BASE,
                        isActionSelected() ? ByteBoardTheme.BASE : ByteBoardTheme.MAIN_LIGHT, ResourceManager.MICRO);

                action.invoke(isActionSelected());
            }
        };
    }

    private boolean isActionSelected() {
        return contentAction.getName().equals("1");
    }

    private void setActionSelected(boolean isSelected) {
        contentAction.setName(isSelected ? "1" : "0");
    }

    public void setCardData(String contentUsername, String contentText, String contentID) {
        this.contentUsername.setText(contentUsername);
        this.contentText.setText(contentText);
        this.contentText.setName(contentID);
    }

    public String getContentID() {
        return contentText.getName();
    }

    public interface ContentActionListener {
        void invoke(boolean isActionSelect);
    }

}
