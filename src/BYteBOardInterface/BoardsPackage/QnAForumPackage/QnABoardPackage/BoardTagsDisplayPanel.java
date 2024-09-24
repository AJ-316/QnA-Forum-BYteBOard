package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.BoardScrollPanel;
import CustomControls.BoardTagButton;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardTagsDisplayPanel extends BoardScrollPanel {

    private MouseAdapter visibleScrollBarAdapter;
    private MouseWheelListener scrollListener;

    public BoardTagsDisplayPanel(MainFrame main, Frame frame) {
        super(main, frame);
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 10));
        setBackground(ByteBoardTheme.MAIN);
        scrollPane.setVisible(false);
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
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

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

    public void addTag(String tag, String userID) {
        if(contains(tag)) return;

        scrollPane.setVisible(true);
        BoardTagButton tagButton = new BoardTagButton(getFrame(), "search", ResourceManager.DEFAULT_DARK);
        tagButton.setTag(tag, userID);
        tagButton.addInsets(5);
        add(tagButton);

        if (visibleScrollBarAdapter != null) {
            tagButton.addMouseListener(visibleScrollBarAdapter);
            tagButton.addMouseWheelListener(scrollListener);
        }
    }

    public BoardTagButton addTag(String tag, Container parent) {
        return addTag(tag, null, null, parent);
    }

    public BoardTagButton addTag(String tag, String tagID, ActionListener listener, Container parent) {
        if(contains(tag)) return null;

        BoardTagButton tagButton = new BoardTagButton(getFrame(), "cancel", ResourceManager.DEFAULT_DARK);
        tagButton.setTag(tag, this);
        tagButton.setTagID(tagID);
        tagButton.addActionListener(listener);
        tagButton.addInsets(5);
        add(tagButton);

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                scrollPane.setVisible(true);
            }
            @Override
            public void componentRemoved(ContainerEvent e) {
                super.componentRemoved(e);
                if(getComponents().length == 0)
                    scrollPane.setVisible(false);
                parent.revalidate();
            }
        });

        if (visibleScrollBarAdapter != null) {
            tagButton.addMouseListener(visibleScrollBarAdapter);
            tagButton.addMouseWheelListener(scrollListener);
        }
        parent.revalidate();

        return tagButton;
    }

    public boolean contains(String tag) {
        for (Component component : getComponents()) {
            if(!(component instanceof BoardTagButton))
                continue;

            if(((BoardTagButton) component).getTag().equals(tag))
                return true;
        }

        return false;
    }

    public void clearTags() {
        removeAll();
    }
}
