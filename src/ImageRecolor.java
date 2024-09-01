import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageRecolor {

    public static BufferedImage recolorImage(BufferedImage originalImage, Color color1, Color color2) {
        // Create a new image with the same dimensions and type as the original
        BufferedImage recoloredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());

        // Loop through each pixel of the image
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                // Get the original pixel color
                int pixel = originalImage.getRGB(x, y);

                // Extract the color components (red, green, blue, alpha)
                int newColor = getNewColor(color1, color2, pixel);

                // Set the new pixel color
                recoloredImage.setRGB(x, y, newColor);
            }
        }

        return recoloredImage;
    }

    private static int getNewColor(Color color1, Color color2, int pixel) {
        int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;

        // Convert the pixel color to grayscale (using luminance formula)
        float grayscale = (red * 0.299f + green * 0.587f + blue * 0.114f) / 255.0f;

        // Interpolate between the two colors based on grayscale value
        int newRed = (int) (color1.getRed() * (1 - grayscale) + color2.getRed() * grayscale);
        int newGreen = (int) (color1.getGreen() * (1 - grayscale) + color2.getGreen() * grayscale);
        int newBlue = (int) (color1.getBlue() * (1 - grayscale) + color2.getBlue() * grayscale);

        // Create the new color with the original alpha value
        return (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
    }

    public static BufferedImage convertToCustomGrayScale(BufferedImage originalImage, Color targetColor) {
        BufferedImage adjustedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // Calculate target luminance based on the target color
        double targetLuminance = getLuminance(targetColor);

        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color color = new Color(originalImage.getRGB(x, y), true);

                // Calculate the relative luminance of the current pixel
                double luminance = getLuminance(color);

                // Adjust luminance to map to the target color as "white"
                int adjustedGray = (int) (luminance / targetLuminance * 255);

                // Ensure within RGB range
                adjustedGray = Math.min(255, Math.max(0, adjustedGray));

                // Set the pixel with adjusted grayscale value
                Color adjustedColor = new Color(adjustedGray, adjustedGray, adjustedGray, color.getAlpha());
                adjustedImage.setRGB(x, y, adjustedColor.getRGB());
            }
        }

        return adjustedImage;
    }

    public static double getLuminance(Color color) {
        // Luminance calculation as per Rec. 709 (standard for HDTV)
        return 0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue();
    }

    private static void changeColor(Color c1, Color c2, String img, String dest) throws Exception {
        // Load the original image
        BufferedImage originalImage = ImageIO.read(new File("src/" + img + ".png"));

        // Define the new colors
        Color color1 = new Color(0, 120, 120); // Red for background
        Color color2 = new Color(255, 255, 255); // Green for magnifying glass

        // Recolor the image
        BufferedImage recoloredImage = ImageRecolor.recolorImage(originalImage, c1, c2);

        // Save the recolored image
        ImageIO.write(recoloredImage, "png", new File("src/" + img + "_" + dest + ".png"));
    }

    public static void main(String[] args) throws Exception {
        changeColor(new Color(0, 120, 120), new Color(120, 183, 183), "pressed", "rollover");
        changeColor(new Color(0, 120, 120), Color.white, "pressed", "pressed");
        changeColor(Color.white, new Color(0, 120, 120), "pressed", "default");

        /*// Load the original image
        BufferedImage originalImage = ImageIO.read(new File("src/pressed.png"));

        // Define the new colors
        Color color1 = new Color(0, 120, 120); // Red for background
        Color color2 = new Color(255, 255, 255); // Green for magnifying glass

        // Recolor the image
        BufferedImage recoloredImage = ImageRecolor.recolorImage(originalImage, color1, color2);

        // Save the recolored image
        ImageIO.write(recoloredImage, "png", new File("recolor_search_icon.png"));*/
    }
}
