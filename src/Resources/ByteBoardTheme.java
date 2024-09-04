package Resources;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class ByteBoardTheme {

    public final static String BASE = "base";
    public final static String MAIN = "main";
    public final static String MAIN_LIGHT = "main_light";
    public final static String MAIN_DARK = "main_dark";
    public final static String ACCENT = "accent";
    public final static String ACCENT_DARK = "accent_dark";
    public final static String ERROR = "error";
    public final static String DISABLED = "disabled";
    public final static String TEXT_FG_LIGHT = "text_fg_light";
    public final static String TEXT_FG_DARK = "text_fg_dark";
    public final static String TEXT_FG_MAIN = "text_fg_main";
    public final static String TEXT_FG_MAIN_LIGHT = "text_fg_main_light";
    public final static String TEXT_FG_MAIN_DARK = "text_fg_main_dark";

    private static String[] keyList;

    private static String[] getKeyList() {
        if (keyList == null) {
            keyList = new String[]{
                    BASE, MAIN, MAIN_LIGHT, MAIN_DARK, ACCENT, ACCENT_DARK,
                    ERROR, DISABLED, TEXT_FG_LIGHT, TEXT_FG_DARK, TEXT_FG_MAIN, TEXT_FG_MAIN_LIGHT, TEXT_FG_MAIN_DARK
            };
        }
        return keyList;
    }

    private final Map<String, String> colorAttributes;
    private final Map<String, String> fontAttributes;
    private String name;

    public ByteBoardTheme() {
        colorAttributes = new HashMap<>();
        fontAttributes = new HashMap<>();

        init();
    }

    public void init(){}

    public void loadColorAttribute(String key, String value) {
        if(key.isEmpty() || value.isEmpty()) return;

        for (String validKey : getKeyList()) {
            if (validKey.equals(key)) {
                colorAttributes.put(validKey, value);
                return;
            }
        }
    }

    public void loadFontAttribute(String key, String value) {
        if(key.isEmpty() || value.isEmpty()) return;
        fontAttributes.put(key, value);
    }

    public void load() {
        if(!this.equals(ByteBoardBaseTheme))
            ByteBoardBaseTheme.load();

        for (String key : colorAttributes.keySet()) {
            createColor(key, colorAttributes.get(key));
        }

        for (String key : fontAttributes.keySet()) {
            createFont(key, fontAttributes.get(key));
        }

        UIManager.put("ComboBox.selectionBackground", ResourceManager.getColor(ByteBoardTheme.ACCENT));
        UIManager.put("ComboBox.selectionForeground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        UIManager.put("ComboBox.background", ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));
        UIManager.put("ComboBox.foreground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        UIManager.put("TextArea.selectionBackground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
    }

    private void createFont(String key, String value) {
        String[] values = value.split("-");
        String[] sizesList = values[values.length == 1 ? 0 : 1].split(",");

        int[] sizes = new int[sizesList.length];

        try {
            for (int i = 0; i < sizesList.length; i++) {
                sizes[i] = Integer.parseInt(sizesList[i].trim());
            }

            addFont(values.length == 1 ? 0 : Integer.parseInt(values[0]), key, sizes);
        } catch (NumberFormatException ignored){}

    }

    private void createColor(String key, String value) {
        String[] values = value.split(",");
        if(values.length != 3) return;

        int[] rgb = new int[3];

        try {
            for (int i = 0; i < rgb.length; i++)
                rgb[i] = Integer.parseInt(values[i].trim());

        } catch (NumberFormatException ignored) {
            return;
        }

        addColor(key, rgb[0], rgb[1], rgb[2]);
    }

    private static void addColor(String label, int r, int g, int b) {
        UIManager.put("QnAForum.color." + label, new Color(r, g, b));
    }

    private static void addFont(int type, String fontFile, int... sizes) {
        if (sizes.length == 0) return;
        String label = fontFile.toLowerCase();

        switch (type) {
            case Font.BOLD:
                label += "_bold";
                break;
            case Font.ITALIC:
                label += "_italic";
                break;
            case Font.PLAIN:
                break;
            default:
                label += "_" + type;
        }

        Font font = FontLoader.getFont(fontFile, sizes[0], type);
        for (int size : sizes) {
            UIManager.put("QnAForum.font." + label + "." + size, font.deriveFont((float) size));
        }

    }

    private static void addFont(String fontFile, int... sizes) {
        addFont(Font.PLAIN, fontFile, sizes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final static ByteBoardTheme ByteBoardBaseTheme = new ByteBoardTheme() {

        public void init() {
            setName("ByteBoardBaseTheme");
            loadColorAttribute(BASE, "255, 255, 255");
            loadColorAttribute(MAIN, "0, 120, 120");
            loadColorAttribute(MAIN_LIGHT, "10, 130, 130");
            loadColorAttribute(MAIN_DARK, "0, 80, 80");
            loadColorAttribute(ACCENT, "81, 180, 127");
            loadColorAttribute(ACCENT_DARK, "48, 150, 96");
            loadColorAttribute(ERROR, "255, 80, 80");
            loadColorAttribute(DISABLED, "153, 153, 153");

            loadColorAttribute(TEXT_FG_LIGHT, "255, 255, 255");
            loadColorAttribute(TEXT_FG_DARK, "0, 0, 0");
            loadColorAttribute(TEXT_FG_MAIN, "0, 120, 120");
            loadColorAttribute(TEXT_FG_MAIN_LIGHT, "10, 130, 130");
            loadColorAttribute(TEXT_FG_MAIN_DARK, "0, 80, 80");

            // TODO: change font loading
            loadFontAttribute("Inter_Regular",  "14, 18, 20, 22, 24, 26");
            loadFontAttribute("Inter_SemiBold", "16, 20, 22, 24, 26, 32, 36, 48");
            loadFontAttribute("Inter_Thin",     "48");
            loadFontAttribute("Inter_Bold",     "22, 24, 28, 32, 36");
            loadFontAttribute("Carltine_Bold",  "2 - 28, 32, 36");
        }
    };
}
