package Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author AJ
 */
public class IconLoader {

    private static Image getScaledRatio(int width, Image img) {
        double ratio = (double) img.getWidth(null) / img.getHeight(null);
        return img.getScaledInstance(width, (int) (width / ratio), Image.SCALE_SMOOTH);
    }

    public static Icon getImageIcon(String image, int size) {
        String path = "IconResource/" + image + ".png";
        URL imagePath = IconLoader.class.getResource(path);

        Image img;

        try {
            if (imagePath == null)
                return null;

            img = ImageIO.read(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Cannot Load Image Icon [" + path + "]: " + e.getMessage());
        }

        int absSize = Math.abs(size);
        Image scaledImg = size < 0 ?
                getScaledRatio(absSize, img) :
                img.getScaledInstance(absSize, absSize, Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImg);
    }

}
