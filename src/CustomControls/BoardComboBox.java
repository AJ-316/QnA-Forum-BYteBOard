package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.CustomRendererPackage.RoundedBorder;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BoardComboBox extends JComboBox<String> {

    private final BoardPanel container;
    private BoardLabel label;

    public BoardComboBox(MainFrame main, Frame frame, String[] itemsList, String selectedItem) {
        super(itemsList);
        container = createContainer(main, frame);

        setSelectedItem(selectedItem);
        setForeground(ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_BOLD, 16)));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createPopupUI();
    }

    private void createPopupUI() {
        setRenderer(new RoundedComboBoxRenderer(20, 20));
        setUI(new BasicComboBoxUI() {
            protected JButton createArrowButton() {
                JButton button = new JButton(){
                    public int getWidth() {
                        return ResourceManager.MICRO*2;
                    }
                    public Dimension getSize(Dimension rv) {
                        return getPreferredSize();
                    }
                };
                button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                ResourceManager.setButtonIcons(button, "arrowD", ResourceManager.MINI);

                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                return button;
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
                };
            }
        });

        RoundedBorder roundedBorder = new RoundedBorder(40, 40, 12, getBackground(), ResourceManager.getColor(ByteBoardTheme.ACCENT));

        ((JComponent) getAccessibleContext().getAccessibleChild(0)).setBorder(roundedBorder);
    }

    public void setLabel(String text) {
        if (label == null) {
            label = new BoardLabel(text);
            label.addInsets(5);
            label.setFGLight();
            label.setFont(ResourceManager.getFont(ByteBoardTheme.FONT_PRIMARY(ByteBoardTheme.FONT_T_SEMIBOLD, 20)));
            label.setAlignmentLeading();
            container.addInsets(10);
            container.add(label, BorderLayout.NORTH);
        }

        label.setText(text);
    }

    public void setBackground(String bg) {
        super.setBackground(ResourceManager.getColor(bg));
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
