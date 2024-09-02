package Resources;

import javax.swing.*;
import java.awt.*;

public class ResourceManager {

    public static int LARGE = 256;
    public static int REGULAR = 128;
    public static int SMALL = 50;
    public static int MINI = 32;

    public static void init() {
        initLookNFeel();

        // Theme Dark
        createColor("base", 30, 30, 30);  // A very dark grey, almost black for the base/background color
        createColor("main", 45, 95, 130);  // A muted blue for the main color
        createColor("main_light", 70, 120, 155);  // A lighter shade of the main blue for hover states or lighter accents
        createColor("main_dark", 25, 70, 105);  // A darker shade of the main blue for depth and contrast
        createColor("accent", 255, 150, 0);  // A bright orange accent color for highlights or important elements
        createColor("accent_dark", 200, 120, 0);  // A darker orange for a more subdued accent
        createColor("error", 255, 69, 58);  // A vivid red for error messages
        createColor("disabled", 85, 85, 85);  // A medium grey for disabled elements

        createColor("text_fg_light", 245, 245, 245);  // Light grey for text on dark backgrounds, easier on the eyes than pure white
        createColor("text_fg_dark", 200, 200, 200);  // Slightly darker grey for secondary text
        createColor("text_fg_main", 255, 150, 0);  // Bright orange text to match the accent color, suitable for important headings or primary text

        // Theme Purple
        /*createColor("base", 245, 245, 245);
        createColor("main", 102, 51, 153);  // A rich purple
        createColor("main_light", 153, 102, 204);  // A lighter shade of purple
        createColor("main_dark", 51, 25, 77);  // A darker shade of purple
        createColor("accent", 255, 204, 102);  // A warm yellow-orange
        createColor("accent_dark", 204, 153, 51);  // A deeper golden color
        createColor("error", 255, 87, 87);  // A bright coral red for errors
        createColor("disabled", 179, 179, 179);  // A light grey for disabled elements

        createColor("text_fg_light", 255, 255, 255);  // White for light text
        createColor("text_fg_dark", 28, 28, 28);  // A very dark grey for dark text
        createColor("text_fg_main", 102, 51, 153);  // The same rich purple as "main"*/

        // Theme default
        /*createColor("base", 255, 255, 255);
        createColor("main", 0, 120, 120);
        createColor("main_light", 10, 130, 130);
        createColor("main_dark", 0, 80, 80);
        createColor("accent", 81, 180, 127);
        createColor("accent_dark", 48, 150, 96);
        createColor("error", 255, 80, 80);
        createColor("disabled", 153, 153, 153);

        createColor("text_fg_light", 255, 255, 255);
        createColor("text_fg_dark", 0, 0, 0);
        createColor("text_fg_main", 0, 120, 120);*/

        createFont("inter_regular", "Inter-Regular", 14, 18, 20, 22, 24, 26);
        createFont("inter_semibold", "Inter-SemiBold", 16, 20, 22, 24, 26, 32, 36, 48);
        createFont("inter_thin", "Inter-Thin", 48);
        createFont("inter_bold", "Inter-Bold", 28, 32, 36);

        createFont(Font.ITALIC, "carltine_bold_italic", "Carltine-Bold", 48);

        UIManager.put("ComboBox.selectionBackground", getColor("accent"));
        UIManager.put("ComboBox.selectionForeground", getColor("text_fg_light"));
    }

    public static Color getColor(String label) {
        return UIManager.getColor("QnAForum.color." + label);
    }

    public static Font getFont(String label) {
        return UIManager.getFont("QnAForum.font." + label);
    }

    public static Icon getIcon(String label, int size) {
        String key = "QnAForum.icon." + label + "." + size;

        Icon icon = UIManager.getIcon(key);
        if(icon != null)
            return icon;

        if((icon = IconLoader.getImageIcon(label, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static void setProfileIcon(String userProfileIndex, JLabel userProfile, int size) {
        userProfile.setIcon(getIcon("profiles/profile_" + userProfileIndex, size));
        userProfile.setName(userProfileIndex);
    }

    private static void initLookNFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 javax.swing.UnsupportedLookAndFeelException ex) {
            System.err.print("Could not set L&F: " + ex.getMessage());
        }
    }

    private static void createColor(String label, int r, int g, int b) {
        UIManager.put("QnAForum.color." + label, new Color(r, g, b));
    }

    private static void createFont(int type, String label, String fontFile, int... sizes) {
        if (sizes.length == 0) return;

        Font font = FontLoader.getFont(fontFile, sizes[0], type);
        for (int size : sizes) {
            UIManager.put("QnAForum.font." + label + "." + size, font.deriveFont((float) size));
            System.out.println("Added Font: " + "QnAForum.font." + label + "." + size);
        }
    }

    private static void createFont(String label, String fontFile, int... sizes) {
        createFont(Font.PLAIN, label, fontFile, sizes);
    }
}
