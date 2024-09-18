package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardScrollPanel;
import CustomControls.BoardTagButton;
import Resources.ByteBoardTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class BoardTagsDisplayPanel extends BoardScrollPanel {

    private MouseAdapter visibleScrollBarAdapter;
    private MouseWheelListener scrollListener;

    public BoardTagsDisplayPanel(MainFrame main, Frame frame) {
        super(main, frame);
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
        setBackground(ByteBoardTheme.MAIN);

        addInsets(5);
    }

    public void setHorizontalDisplay() {
        visibleScrollBarAdapter = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                repaint();
            }
        };
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        addMouseListener(visibleScrollBarAdapter);
        scrollPane.getHorizontalScrollBar().addMouseListener(visibleScrollBarAdapter);

        scrollListener = e -> {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                int amount = e.getUnitsToScroll() * 10;
                scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getValue() + amount);
            }
        };
        scrollPane.getHorizontalScrollBar().addMouseWheelListener(scrollListener);
        addMouseWheelListener(scrollListener);
    }

    public void addTag(String tag) {
        BoardTagButton tagButton = new BoardTagButton();
        tagButton.setTag(tag);
        add(tagButton);

        if (visibleScrollBarAdapter != null) {
            tagButton.addMouseListener(visibleScrollBarAdapter);
            tagButton.addMouseWheelListener(scrollListener);
        }
    }

    public void clearTags() {
        removeAll();
    }
}
