package Resources;

import QnAForumInterface.AuthenticationForm;
import QnAForumInterface.InterfaceEventPackage.InterfaceEventManager;
import QnAForumInterface.QnAForum;

import javax.swing.*;
import javax.xml.ws.spi.Invoker;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceManager {

    public static final int DEFAULT = 0;
    public static final int ROLLOVER = 1;
    public static final int PRESSED = 2;

    public static final int LARGE = 256;
    public static final int REGULAR = 128;
    public static final int SMALL = 48;
    public static final int MINI = 32;
    public static final int MICRO = 24;

    public static List<ByteBoardTheme> themes = new ArrayList<>();
    private static String CURRENT_THEME = "";

    public static void init() {
        initLookNFeel();
        themes.add(ByteBoardTheme.ByteBoardBaseTheme);
        setTheme(ByteBoardTheme.ByteBoardBaseTheme.getName());

        loadAvailableThemes();
    }

    private static void loadAvailableThemes() {
        URL dirURL = ResourceManager.class.getResource("Themes/");
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            File directory = new File(dirURL.getFile());
            String[] files = directory.list();
            if (files != null) {
                for (String fileName : files) {
                    loadTheme(fileName.substring(0, fileName.length() - 4));
                }
            }
        }
    }

    public static void setTheme(String name) {
        CURRENT_THEME = name;
        deleteIconCache();
        for (ByteBoardTheme theme : themes) {
            if(theme.getName().equals(name)) {
                theme.load();
                return;
            }
        }
    }

    public static void loadTheme(String file) {
        ByteBoardTheme theme = ThemeLoader.getTheme(file);
        if(theme == null) return;
        themes.add(theme);
    }

    public static List<ByteBoardTheme> getThemes() {
        return themes;
    }

    public static String getCurrentTheme() {
        return CURRENT_THEME;
    }

    public static Color getColor(String label) {
        return UIManager.getColor("QnAForum.color." + label);
    }

    public static Font getFont(String label) {
        return UIManager.getFont("QnAForum.font." + label);
    }

    public static void deleteIconCache() {
        UIDefaults defaults = UIManager.getDefaults();
        List<Object> keysToRemove = new ArrayList<>();

        for (Object key : defaults.keySet()) {
            if (key instanceof String && ((String) key).startsWith("QnAForum.")) {
                keysToRemove.add(key);
            }
        }

        for (Object key : keysToRemove) {
            UIManager.put(key, null);
        }
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

    public static Icon getColoredIcon(String label, Color blackRecolor, Color whiteRecolor, int size) {
        String key = "QnAForum.icon." + label + "_" +
                (blackRecolor == null ? "" : ("_" + blackRecolor.getRGB())) +
                (whiteRecolor == null ? "" : ("_" + whiteRecolor.getRGB())) + "." + size;

        Icon icon = UIManager.getIcon(key);
        if(icon != null)
            return icon;

        if((icon = IconLoader.getRecoloredIcon(label, blackRecolor, whiteRecolor, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static Icon getStateIcon(String label, int state, int size) {
        String key = "QnAForum.icon." + label + "_" + state + "." + size;

        Icon icon = UIManager.getIcon(key);
        if(icon != null)
            return icon;

        Color iconFg = Color.black;
        Color iconBg = Color.white;
        switch (state) {
            case ResourceManager.DEFAULT:
                iconFg = ResourceManager.getColor(ByteBoardTheme.BASE);
                iconBg = ResourceManager.getColor(ByteBoardTheme.MAIN);
            break;
            case ResourceManager.ROLLOVER:
                iconFg = ResourceManager.getColor(ByteBoardTheme.BASE);
                iconBg = ResourceManager.getColor(ByteBoardTheme.ACCENT);
            break;
            case ResourceManager.PRESSED:
                iconFg = ResourceManager.getColor(ByteBoardTheme.ACCENT_DARK);
                iconBg = ResourceManager.getColor(ByteBoardTheme.BASE);
        }

        if((icon = IconLoader.getRecoloredIcon(label, iconFg, iconBg, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static Icon getProfileIcon(String userProfileIndex, int size) {
        String label = "profiles/profile_" + userProfileIndex;
        String key = "QnAForum.icon." + label + "." + size;

        Icon icon = UIManager.getIcon(key);
        if(icon != null)
           return icon;

        if((icon = IconLoader.getRecoloredIconTargeted(label, ResourceManager.getColor(ByteBoardTheme.MAIN), Color.white, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static void setProfileIcon(String userProfileIndex, JLabel userProfile, int size) {
        userProfile.setIcon(getProfileIcon(userProfileIndex, size));
        userProfile.setName(userProfileIndex);
    }

    public static void setButtonIcons(JButton button, String label, int size) {
        setButtonIcons(button, label, DEFAULT, PRESSED, ROLLOVER, size);
    }

    public static void setButtonIcons(JButton button, String label, int defaultState, int pressedState, int rolloverState, int size) {
        button.setIcon(ResourceManager.getStateIcon(label, defaultState, size));
        button.setPressedIcon(ResourceManager.getStateIcon(label, pressedState, size));
        button.setRolloverIcon(ResourceManager.getStateIcon(label, rolloverState, size));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
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

    /*private static void createColor(String label, int r, int g, int b) {
        UIManager.put("QnAForum.color." + label, new Color(r, g, b));
    }

    private static void createFont(int type, String fontFile, int... sizes) {
        if (sizes.length == 0) return;
        String label = fontFile.toLowerCase();

        switch (type) {
            case Font.BOLD: label += "_bold"; break;
            case Font.ITALIC: label += "_italic"; break;
            case Font.PLAIN: break;
            default: label += "_" + type;
        }

        Font font = FontLoader.getFont(fontFile, sizes[0], type);
        for (int size : sizes) {
            UIManager.put("QnAForum.font." + label + "." + size, font.deriveFont((float) size));
            System.out.println("Added Font: " + "QnAForum.font." + label + "." + size);
        }
    }

    private static void createFont(String fontFile, int... sizes) {
        createFont(Font.PLAIN, fontFile, sizes);
    }*/
}
