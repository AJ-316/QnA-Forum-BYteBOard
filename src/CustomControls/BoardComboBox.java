package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.CustomRendererPackage.RoundedBorder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class BoardComboBox extends JComboBox<String> {

    private int dropLocation;
    private JButton dropDownButton;
    private BoardPanel container;
    private BoardLabel label;

    public BoardComboBox(MainFrame main, Frame frame, String[] itemsList, String selectedItem) {
        super(itemsList);
        setForeground(ByteBoardTheme.MAIN_DARK);

        init(main, frame, 16);
        setSelectedItem(selectedItem);
    }

    public BoardComboBox(MainFrame main, Frame frame, String bg, int columns) {
        setBackground(bg);
        setForeground(ByteBoardTheme.TEXT_FG_LIGHT);

        init(main, frame, 22);
        getTextField().setColumns(columns);
        getTextField().setBackground(bg);
    }

    public void setPopupVisible(boolean v) {
        if(getItemCount() == 0) return;
        super.setPopupVisible(v);
    }

    private void init(MainFrame main, Frame frame, int fontSize) {
        container = createContainer(main, frame);
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_BOLD, fontSize)));
        addInsets(10);
        createPopupUI();
        getTextField().setBackground(ByteBoardTheme.MAIN);
    }

    public void setForeground(String fg) {
        super.setForeground(ResourceManager.getColor(fg));
    }

    public BoardTextField getTextField() {
        return (BoardTextField) getEditor().getEditorComponent();
    }

    public void setDropLocation(int dropLocation) {
        this.dropLocation = dropLocation;
    }

    private void createPopupUI() {
        setRenderer(new RoundedComboBoxRenderer(20, 20));
        setUI(new BasicComboBoxUI() {
            protected ComboBoxEditor createEditor() {
                return new BasicComboBoxEditor.UIResource() {
                    protected JTextField createEditorComponent() {
                        return new BoardTextField(BoardComboBox.this.container.getMain(),
                                BoardComboBox.this.container.getFrame(), BoardComboBox.this.container.getBackground());
                    }
                };
            }

            protected JButton createArrowButton() {
                return createDropDownButton();
            }

            protected ComboPopup createPopup() {
                return new BasicComboPopup(comboBox) {
                    protected JScrollPane createScroller() {
                        SimpleScrollPane scrollPane = new SimpleScrollPane(list);
                        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        scrollPane.setHorizontalScrollBar(null);
                        return scrollPane;
                    }

                    public void show() {
                        comboBox.firePopupMenuWillBecomeVisible();
                        setListSelection(comboBox.getSelectedIndex());

                        Point location = getPopupLocation();
                        show(comboBox, location.x, dropLocation == SwingConstants.TOP ? -getPreferredSize().height : location.y);
                    }

                    private void setListSelection(int selectedIndex) {
                        if (selectedIndex == -1) {
                            list.clearSelection();
                        } else {
                            list.setSelectedIndex(selectedIndex);
                            list.ensureIndexIsVisible(selectedIndex);
                        }
                    }

                    private Point getPopupLocation() {
                        Dimension popupSize = comboBox.getSize();
                        Insets insets = getInsets();

                        // reduce the width of the scrollpane by the insets so that the popup
                        // is the same width as the combo box.
                        popupSize.setSize(popupSize.width - (insets.right + insets.left),
                                getPopupHeightForRowCount(comboBox.getMaximumRowCount()));
                        Rectangle popupBounds = computePopupBounds(0, comboBox.getBounds().height,
                                popupSize.width, popupSize.height);
                        Dimension scrollSize = popupBounds.getSize();
                        Point popupLocation = popupBounds.getLocation();

                        scroller.setMaximumSize(scrollSize);
                        scroller.setPreferredSize(scrollSize);
                        scroller.setMinimumSize(scrollSize);

                        list.revalidate();

                        return popupLocation;
                    }

                    protected void configureList() {
                        super.configureList();
                        list.setFixedCellHeight(30);
                        list.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                    }
                };
            }
        });

        RoundedBorder roundedBorder = new RoundedBorder(40, 40, 12, getBackground(), ResourceManager.getColor(ByteBoardTheme.ACCENT));

        ((JComponent) getAccessibleContext().getAccessibleChild(0)).setBorder(roundedBorder);
    }

    public void setLabel(String text, String icon) {
        if (label == null) {
            label = new BoardLabel(text);
            label.addInsets(5);
            label.setFGLight();
            label.setAlignmentLeading();
            label.setIcon(icon, ResourceManager.DEFAULT, ResourceManager.MINI);
            label.setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_SEMIBOLD, 20)));

            container.addInsets(10);
            container.add(label, BorderLayout.NORTH);
        }

        label.setText(text);
    }

    public void setBackground(String bg) {
        super.setBackground(ResourceManager.getColor(bg));

        if (container != null)
            container.setBackground(getBackground());
    }

    private BoardPanel createContainer(MainFrame main, Frame frame) {
        BoardPanel panel = new BoardScrollPanel(main, frame);
        panel.setLayout(new BorderLayout());
        panel.setBackground(getBackground());
        panel.setCornerRadius(40);
        panel.addInsets(10);
        panel.add(this);
        return panel;
    }

    public BoardPanel getComponent() {
        return container;
    }

    public JButton getDropDownButton() {
        return dropDownButton;
    }

    public boolean contains(String item) {
        for (int i = 0; i < getItemCount(); i++) {
            if(getItemAt(i).equals(item))
                return true;
        }

        return false;
    }

    protected JButton createDropDownButton() {
        dropDownButton = new JButton() {
            public int getWidth() {
                return ResourceManager.MICRO * 2;
            }

            public Dimension getSize(Dimension rv) {
                return getPreferredSize();
            }
        };
        dropDownButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ResourceManager.setButtonIcons(dropDownButton, "arrowD", ResourceManager.MINI);

        dropDownButton.setBorderPainted(false);
        dropDownButton.setFocusPainted(false);
        dropDownButton.setContentAreaFilled(false);

        return dropDownButton;
    }

    public void removeDropButtonActions() {
        EventQueue.invokeLater(() -> {
            dropDownButton.removeMouseListener(dropDownButton.getMouseListeners()[1]);
            dropDownButton.removeMouseMotionListener(dropDownButton.getMouseMotionListeners()[1]);
        });
    }

    public void setIcon(String icon, int iconSize) {
        ResourceManager.setButtonIcons(dropDownButton, icon, iconSize);
    }

    public void addInsets(int insets) {
        addInsets(insets, insets, insets, insets);
    }

    public void addInsets(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public void setFontPrimary(String type, int size) {
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(type, size)));
    }

    private static class RoundedComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

        private final int arcWidth;
        private final int arcHeight;

        public RoundedComboBoxRenderer(int arcWidth, int arcHeight) {
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            setOpaque(false);
        }

        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value != null ? value.toString() : "");
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            if (value != null)
                setToolTipText(value.toString());

            setPreferredSize(new Dimension(list.getWidth(), list.getFontMetrics(list.getFont()).getHeight() + 20)); // Add some padding
            return this;
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            super.paintComponent(g2);
        }
    }
}
