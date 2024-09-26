package CustomControls;

import CustomControls.CustomRendererPackage.BoardSplitPaneUI;

import javax.swing.*;

public class BoardSplitPanel extends JSplitPane {

    public BoardSplitPanel(int split) {
        super(split);
        init();
    }

    public BoardSplitPanel(int split, JComponent component1, JComponent component2) {
        super(split, component1, component2);
        init();
    }

    private void init() {
        setOpaque(false);
        setDividerSize(30);
        setDividerLocation(0.8);
        setResizeWeight(0.8);
        setBorder(BorderFactory.createEmptyBorder());
        setUI(new BoardSplitPaneUI());
    }
}
