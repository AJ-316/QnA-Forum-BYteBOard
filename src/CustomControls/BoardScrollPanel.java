package CustomControls;

import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.Frame;
import BYteBOardInterface.StructurePackage.MainFrame;
import Resources.ResourceManager;

import java.awt.*;

public class BoardScrollPanel extends BoardPanel {

    private final SimpleScrollPane scrollPane;

    public BoardScrollPanel(MainFrame main, Frame frame) {
        super(main, frame);
        scrollPane = new SimpleScrollPane(this);
    }

    public void setScrollMinSize(int width, int height) {
        scrollPane.getViewport().setMinimumSize(new Dimension(width, height));
    }

    public void setScrollSize(int width, int height) {
        scrollPane.getViewport().setMaximumSize(new Dimension(width, height));
    }

    public void setBackground(String bg) {
        super.setBackground(ResourceManager.getColor(bg));
        scrollPane.getViewport().setBackground(getBackground());
    }

    public void setBackground(Color bg) {
        super.setBackground(bg);
        if(scrollPane != null)
            scrollPane.getViewport().setBackground(bg);
    }

    public SimpleScrollPane getComponent() {
        return scrollPane;
    }
}
