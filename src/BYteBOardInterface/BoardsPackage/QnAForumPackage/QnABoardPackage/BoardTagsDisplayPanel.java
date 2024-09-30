package BYteBOardInterface.BoardsPackage.QnAForumPackage.QnABoardPackage;

import BYteBOardDatabase.DBDataObject;
import BYteBOardDatabase.DBTag;
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

    public void addTag(DBDataObject tagDataObject, String userID) {
        String tag = tagDataObject.getValue(DBTag.K_TAG);
        if (contains(tag)) return;
        BoardTagButton tagButton = new BoardTagButton(getFrame(), "search", ResourceManager.DEFAULT_DARK);
        tagButton.setTag(tagDataObject, userID);
        tagButton.addInsets(5);
        add(tagButton);

        if (visibleScrollBarAdapter != null) {
            tagButton.addMouseListener(visibleScrollBarAdapter);
            tagButton.addMouseWheelListener(scrollListener);
        }
    }

    public BoardTagButton addTag(DBDataObject tagDataObject, Container parent) {
        return addTag(tagDataObject, null, parent);
    }

    public BoardTagButton addTag(DBDataObject tagDataObject, ActionListener listener, Container parent) {
        String tag = tagDataObject.getValue(DBTag.K_TAG);

        if (contains(tag)) return null;
        scrollPane.setVisible(false);

        BoardTagButton tagButton = new BoardTagButton(getFrame(), "cancel", ResourceManager.DEFAULT_DARK);
        tagButton.setTag(tagDataObject, this);
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
                if (getComponents().length == 0)
                    scrollPane.setVisible(false);
                parent.revalidate();
                parent.repaint();
            }
        });

        if (visibleScrollBarAdapter != null) {
            tagButton.addMouseListener(visibleScrollBarAdapter);
            tagButton.addMouseWheelListener(scrollListener);
        }
        parent.revalidate();
        parent.repaint();

        return tagButton;
    }

    public boolean contains(String tag) {
        for (Component component : getComponents()) {
            if (!(component instanceof BoardTagButton))
                continue;

            if (((BoardTagButton) component).getTag().equals(tag))
                return true;
        }

        return false;
    }

    public BoardTagButton[] getTagButtons() {
        BoardTagButton[] boardTagButtons = new BoardTagButton[getComponents().length];

        for (int i = 0; i < boardTagButtons.length; i++) {
            if (!(getComponent(i) instanceof BoardTagButton))
                continue;
            boardTagButtons[i] = ((BoardTagButton) getComponent(i));
        }

        return boardTagButtons;
    }

    public void clearTags() {
        removeAll();
    }
}
