package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.DBVote;
import BoardControls.BoardButton;
import BoardResources.ByteBoardTheme;
import BoardResources.ResourceManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VoteButton extends BoardButton {

    private final String icon;
    private final String voteType;
    private Color defaultColor;
    private Color rolloverColor;
    private Color pressedColor;

    public VoteButton(String icon, int iconSize, String voteType) {
        super(icon, iconSize);
        this.icon = icon;
        this.voteType = voteType;

        setFocusable(false);
        setColors(ByteBoardTheme.MAIN, ByteBoardTheme.BASE, ByteBoardTheme.ACCENT);
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(rolloverColor);
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                setBackground(defaultColor);
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
                repaint();
            }
        });
    }

    private void setColors(String defaultColor, String pressedColor, String rolloverColor) {
        this.defaultColor = ResourceManager.getColor(defaultColor);
        this.pressedColor = ResourceManager.getColor(pressedColor);
        this.rolloverColor = ResourceManager.getColor(rolloverColor);
    }

    public void select(boolean isSelected) {
        if (isSelected) {
            setColors(ByteBoardTheme.BASE, ByteBoardTheme.ACCENT, ByteBoardTheme.BASE);
            setIcon(icon, ResourceManager.PRESSED, ResourceManager.ROLLOVER, ResourceManager.PRESSED);
            setName(DBVote.V_VOTE_NONE);
            return;
        }

        setColors(ByteBoardTheme.MAIN, ByteBoardTheme.BASE, ByteBoardTheme.ACCENT);
        setIcon(icon, ResourceManager.DEFAULT_LIGHT, ResourceManager.PRESSED, ResourceManager.ROLLOVER);
        setName(voteType);
    }
}
