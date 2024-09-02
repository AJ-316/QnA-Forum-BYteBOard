package Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author AJ
 */
public class IconLoader {

    public static BufferedImage recolorImage(BufferedImage originalImage, Color color1, Color color2, boolean isTargeted) {
        BufferedImage recoloredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int pixel = originalImage.getRGB(x, y);
                int newColor = isTargeted ?
                        getRecolorTargeted(color1, color2, pixel) :
                        getRecolor(color1, color2, pixel);
                recoloredImage.setRGB(x, y, newColor);
            }
        }
        return recoloredImage;
    }

    private static int getRecolor(Color blackRecolor, Color whiteRecolor, int pixel) {
        int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;

        float grayscale = (red * 0.299f + green * 0.587f + blue * 0.114f) / 255.0f;

        int newRed = (int) (blackRecolor.getRed() * (1 - grayscale) + whiteRecolor.getRed() * grayscale);
        int newGreen = (int) (blackRecolor.getGreen() * (1 - grayscale) + whiteRecolor.getGreen() * grayscale);
        int newBlue = (int) (blackRecolor.getBlue() * (1 - grayscale) + whiteRecolor.getBlue() * grayscale);

        return (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
    }

    private static int getRecolorTargeted(Color recolor, Color targetColor, int pixel) {
        int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
        int c = 0;

        int newRed = red;
        int newGreen = green;
        int newBlue = blue;

        if (red == targetColor.getRed() && green == targetColor.getGreen() && blue == targetColor.getBlue()) {
            newRed = recolor.getRed();
            newGreen = recolor.getGreen();
            newBlue = recolor.getBlue();
        }

        return (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
    }

    public static Icon getRecoloredIcon(String file, Color blackRecolor, Color whiteRecolor, int size) {
        BufferedImage image = getImage(file);
        if (image == null) return null;

        image = recolorImage(image, blackRecolor, whiteRecolor, false);
        return new ImageIcon(getScaledImage(image, size));
    }

    public static Icon getRecoloredIconTargeted(String file, Color recolor, Color targetColor, int size) {
        BufferedImage image = getImage(file);
        if (image == null) return null;

        image = recolorImage(image, recolor, targetColor, true);
        return new ImageIcon(getScaledImage(image, size));
    }

    public static Icon getImageIcon(String file, int size) {
        BufferedImage image = getImage(file);
        if(image == null) return null;

        return new ImageIcon(getScaledImage(image, size));
    }

    private static BufferedImage getScaledImage(BufferedImage image, int size) {
        int absSize = Math.abs(size);

        return size < 0 ?
                getScaledByRatioImage(absSize, image) :
                getScaledImage(image, absSize, absSize);
    }

    public static BufferedImage getScaledImage(BufferedImage img, int width, int height) {
        Image image = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, null);
        g.dispose();

        return scaledImage;
    }

    private static BufferedImage getScaledByRatioImage(int width, BufferedImage img) {
        double ratio = (double) img.getWidth(null) / img.getHeight(null);
        return getScaledImage(img, width, (int) (width / ratio));
    }

    private static BufferedImage getImage(String image) {
        String path = "IconResource/" + image + ".png";
        URL imagePath = IconLoader.class.getResource(path);

        BufferedImage img;

        try {
            if (imagePath == null)
                return null;

            img = ImageIO.read(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Cannot Load Image Icon [" + path + "]: " + e.getMessage());
        }

        return img;
    }

}
