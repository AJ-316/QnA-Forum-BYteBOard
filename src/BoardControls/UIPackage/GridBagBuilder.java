package BoardControls.UIPackage;

import java.awt.*;

public class GridBagBuilder extends GridBagConstraints {

    private final Container container;
    private final GridBagLayout layout;
    private final int cols;
    public int currentCellPos;

    public GridBagBuilder(Container container) {
        this(container, -1);
    }

    public GridBagBuilder(Container container, int cols) {
        this.container = container;
        this.layout = new GridBagLayout();
        this.cols = cols;

        gridx = 0;
        gridy = 0;
        container.setLayout(layout);
    }

    public GridBagBuilder addToCurrentCell(Component component) {
        if (component != null)
            container.add(component, this);

        return this;
    }

    public GridBagBuilder addToNextCell(Component component) {
        skipCells(1);
        return addToCurrentCell(component);
    }

    /*public void add(Component component, int gridWidth, int gridHeight) {
        skipCells(1);
        gridWidth(gridWidth);
        gridHeight(gridHeight);

        container.add(component, this);

        gridWidth(1);
        gridHeight(1);
    }

    public void add(Component component, double weightX, double weightY, int fill) {
        skipCells(1);
        int lastFill = this.fill;

        weightX(weightX);
        weightY(weightY);
        fill(fill);

        container.add(component, this);

        weightX(0);
        weightY(0);
        fill(lastFill);
    }

    public void add(Component component, int width, int height, double weightX, double weightY, int fill) {
        skipCells(1);
        int lastFill = this.fill;

        gridWidth(width);
        gridHeight(height);
        weightX(weightX);
        weightY(weightY);
        fill(fill);

        container.add(component, this);

        gridWidth(1);
        gridHeight(1);
        weightX(0);
        weightY(0);
        fill(lastFill);
    }*/

    public GridBagBuilder skipCells(int cells) {
        currentCellPos += cells;
        gridx = (currentCellPos - 1) % cols;
        gridy = (currentCellPos - 1) / cols;

        return this;
    }

    public GridBagBuilder anchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GridBagBuilder weight(double weightX, double weightY) {
        weightX(weightX);
        weightY(weightY);
        return this;
    }

    public GridBagBuilder fillBoth() {
        this.fill = BOTH;
        return this;
    }

    public GridBagBuilder fillVertical() {
        this.fill = VERTICAL;
        return this;
    }

    public GridBagBuilder fillHorizontal() {
        this.fill = HORIZONTAL;
        return this;
    }

    public GridBagBuilder fillNone() {
        this.fill = NONE;
        return this;
    }

    public GridBagBuilder insets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public GridBagBuilder insets(int size) {
        return insets(size, size, size, size);
    }

    public GridBagBuilder weightX(double x) {
        this.weightx = x;
        return this;
    }

    public GridBagBuilder weightY(double y) {
        this.weighty = y;
        return this;
    }

    public GridBagBuilder gridSize(int w, int h) {
        gridWidth(w);
        gridHeight(h);
        return this;
    }

    public GridBagBuilder gridWidth(int w) {
        this.gridwidth = w;
        return this;
    }

    public GridBagBuilder gridHeight(int h) {
        this.gridheight = h;
        return this;
    }

    public GridBagBuilder gridCell(int x, int y) {
        this.gridx = x;
        this.gridy = y;
        return this;
    }

    public GridBagConstraints getConstraints(Component component) {
        return layout.getConstraints(component);
    }

    public Container getContainer() {
        return container;
    }
}
