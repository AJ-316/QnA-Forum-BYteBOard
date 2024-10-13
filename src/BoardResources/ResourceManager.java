package BoardResources;

import BoardControls.BoardDialog;
import BoardControls.BoardLoader;
import Tools.DEBUG;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceManager {

    public static final int NONE = -1;
    public static final int DEFAULT = 0;
    public static final int ROLLOVER = 1;
    public static final int PRESSED = 2;
    public static final int DEFAULT_LIGHT = 3;
    public static final int DEFAULT_DARK = 4;

    public static final int LARGE = 256;
    public static final int REGULAR = 128;
    public static final int SMALL = 48;
    public static final int MINI = 32;
    public static final int MICRO = 24;

    public static List<ByteBoardTheme> THEMES = new ArrayList<>();
    private static String CURRENT_THEME = "";

    public static void init() {
        THEMES.add(ByteBoardTheme.ByteBoardBaseTheme);
        setTheme(ByteBoardTheme.ByteBoardBaseTheme.getName());

        loadAvailableThemes();
    }

    private static void loadAvailableThemes() {
        String directory = "Themes/";
        URL dirURL = ResourceManager.class.getClassLoader().getResource(directory);

        if (dirURL == null) {
            throw new NullPointerException("Directory " + directory + " not found.");
        }

        try {
            if (dirURL.getProtocol().equals("file"))
                loadThemesFromFile(dirURL);

            else if (dirURL.getProtocol().equals("jar"))
                loadThemesFromJar(dirURL, directory);

            else {
                String errorMsg = "Unsupported protocol: " + dirURL.getProtocol();
                BoardLoader.forceStop("Failed to load themes", errorMsg);
                throw new UnsupportedOperationException(errorMsg);
            }

        } catch (URISyntaxException | IOException ex) {
            BoardLoader.forceStop("Failed to load themes", ex.toString());
            throw new RuntimeException("Failed to load themes", ex);
        }
    }

    private static void loadThemesFromFile(URL dirURL) throws URISyntaxException {
        File folder = new File(dirURL.toURI());

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                BoardLoader.setProgressTarget(files.length + 1);
                for (File file : files) {
                    String fileName = file.getName();
                    loadTheme(fileName.substring(0, fileName.length() - 4));  // Remove file extension
                    BoardLoader.progress("Loading Resources...");
                }
                BoardLoader.progress();
            }
        }
    }

    private static void loadThemesFromJar(URL dirURL, String directory) throws IOException {
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
        try (JarFile jar = new JarFile(new File(jarPath))) {
            Enumeration<JarEntry> entries = jar.entries();
            BoardLoader.setProgressTarget(jar.size() + 1);

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.startsWith(directory) && !entry.isDirectory()) {
                    entryName = entryName.substring(directory.length()); // remove dir prefix
                    loadTheme(entryName.substring(0, entryName.length() - 4)); // remove file ext
                }
                BoardLoader.progress("Loading Resources...");
            }
            BoardLoader.progress();
        }
    }

    public static void setTheme(String name) {
        if (CURRENT_THEME.equals(name)) return;

        CURRENT_THEME = name;
        deleteIconCache();
        for (ByteBoardTheme theme : THEMES) {
            if (theme.getName().equals(name)) {
                theme.load();
                return;
            }
        }
    }

    public static void loadTheme(String file) {
        ByteBoardTheme theme = ThemeLoader.getTheme(file);
        if (theme == null) return;
        THEMES.add(theme);
    }

    public static List<ByteBoardTheme> getByteBoardThemes() {
        return THEMES;
    }

    public static String getCurrentTheme() {
        return CURRENT_THEME;
    }

    public static String[] getThemes() {
        String[] themeNames = new String[THEMES.size()];
        for (int i = 0; i < themeNames.length; i++)
            themeNames[i] = THEMES.get(i).getName();

        return themeNames;
    }

    public static AttributeSet getAttributeSet(String label) {
        return (AttributeSet) UIManager.get("QnAForum.attribute." + label);
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
        if (icon != null)
            return icon;

        if ((icon = IconLoader.getImageIcon(label, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static Icon getColoredIcon(String label, Color blackRecolor, Color whiteRecolor, int size) {
        String key = "QnAForum.icon." + label + "_" +
                (blackRecolor == null ? "" : ("_" + blackRecolor.getRGB())) +
                (whiteRecolor == null ? "" : ("_" + whiteRecolor.getRGB())) + "." + size;

        Icon icon = UIManager.getIcon(key);
        if (icon != null)
            return icon;

        if ((icon = IconLoader.getRecoloredIcon(label, blackRecolor, whiteRecolor, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static Icon getStateIcon(String label, int state, int size) {
        String key = "QnAForum.icon." + label + "_" + state + "." + size;

        Icon icon = UIManager.getIcon(key);
        if (icon != null)
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
                break;
            case ResourceManager.DEFAULT_LIGHT:
                iconFg = ResourceManager.getColor(ByteBoardTheme.BASE);
                iconBg = ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT);
                break;
            case ResourceManager.DEFAULT_DARK:
                iconFg = ResourceManager.getColor(ByteBoardTheme.MAIN_DARK);
                iconBg = ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT);
                break;
        }

        if ((icon = IconLoader.getRecoloredIcon(label, iconFg, iconBg, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static Icon getProfileIcon(String userProfileIndex, int size) {
        return getProfileIcon(userProfileIndex, ResourceManager.getColor(ByteBoardTheme.MAIN), size);
    }

    public static Icon getProfileIcon(String userProfileIndex, Color recolor, int size) {
        String label = "profiles/profile_" + userProfileIndex;
        String key = "QnAForum.icon." + label + "_" + recolor.getRGB() + "." + size;

        Icon icon = UIManager.getIcon(key);
        if (icon != null)
            return icon;

        if ((icon = IconLoader.getRecoloredIconTargeted(label, recolor, Color.white, size)) == null)
            return null;

        UIManager.put(key, icon);
        return icon;
    }

    public static void setProfileIndexIcon(String userProfileIndex, JLabel userProfile, int size) {
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
}
