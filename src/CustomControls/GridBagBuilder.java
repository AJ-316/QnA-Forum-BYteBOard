package CustomControls;

import java.awt.*;

public class GridBagBuilder {

    private final Container container;
    private final GridBagLayout layout;
    private final GridBagConstraints constraints;
    private final int cols;
    public int currentCellPos;

    public GridBagBuilder(Container container) {
        this(container, -1);
    }

    public GridBagBuilder(Container container, int cols) {
        this.container = container;
        this.layout = new GridBagLayout();
        this.constraints = new GridBagConstraints();
        this.cols = cols;

        constraints.gridx = 0;
        constraints.gridy = 0;
        container.setLayout(layout);
    }

    public void add(Component component) {
        skipCells(1);
        container.add(component, constraints);
    }

    public void add(Component component, int gridWidth, int gridHeight) {
        skipCells(1);
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        container.add(component, constraints);
    }

    public void skipCells(int cells) {
        currentCellPos += cells;
        constraints.gridx = (currentCellPos-1) % cols;
        constraints.gridy = (currentCellPos-1) / cols;
    }

    public void anchor(int anchor) {
        constraints.anchor = anchor;
    }

    public void fill(int fill) {
        constraints.fill = fill;
    }

    public void insets(Insets insets) {
        constraints.insets = insets;
    }

    public void gridWeightX(double x) {
        constraints.weightx = x;
    }

    public void gridWeightY(double y) {
        constraints.weighty = y;
    }

    public void gridHeight(int h) {
        constraints.gridheight = h;
    }

    public GridBagConstraints getConstraints(Component component) {
        return layout.getConstraints(component);
    }

    public GridBagConstraints getConstraints() {
        return constraints;
    }
}
