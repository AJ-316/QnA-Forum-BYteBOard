package BoardResources;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
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

    public final static String FONT_T_REGULAR = "_regular.";
    public final static String FONT_T_BOLD = "_bold.";
    public final static String FONT_T_SEMIBOLD = "_semibold.";
    public final static String FONT_T_THIN = "_thin.";

    public final static String AS_CODE_TEXT = "code.text";
    public final static String AS_CODE_TOKEN = "code.token";
    public final static String AS_CODE_NUMBER = "code.number";
    public final static String AS_CODE_STRING = "code.string";

    private static final String FONT_K_PRIMARY = "primary";
    private static final String FONT_K_SECONDARY = "secondary";
    private static String[] keyList;
    private static String[] attributeList;

    public final static ByteBoardTheme ByteBoardBaseTheme = new ByteBoardTheme() {

        public void init() {
            setName("BYteBOard Classic");
            loadAttributeSet(AS_CODE_TEXT, "255, 255, 255");
            loadAttributeSet(AS_CODE_TOKEN, "224, 126, 0");
            loadAttributeSet(AS_CODE_NUMBER, "0, 119, 204");
            loadAttributeSet(AS_CODE_STRING, "106, 171, 115");

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

            loadFontAttributes(FONT_K_PRIMARY, "inter");
            loadFontAttributes(FONT_K_SECONDARY, "jetbrains");
        }
    };

    private final Map<String, String> attributeSets;
    private final Map<String, String> colorAttributes;
    private final Map<String, String> fontAttributes;
    private String name;
    public ByteBoardTheme() {
        attributeSets = new HashMap<>();
        colorAttributes = new HashMap<>();
        fontAttributes = new HashMap<>();

        init();
    }

    public static String FONT_PRIMARY(String type, int size) {
        return UIManager.get("QnAForum.font." + FONT_K_PRIMARY) + type + size;
    }

    public static String FONT_SECONDARY(String type, int size) {
        return UIManager.getString("QnAForum.font." + FONT_K_SECONDARY) + type + size;
    }

    private static String[] getColorKeyList() {
        return keyList == null ? keyList = new String[]{
                BASE, MAIN, MAIN_LIGHT, MAIN_DARK, ACCENT, ACCENT_DARK,
                ERROR, DISABLED, TEXT_FG_LIGHT, TEXT_FG_DARK, TEXT_FG_MAIN, TEXT_FG_MAIN_LIGHT, TEXT_FG_MAIN_DARK} : keyList;
    }

    private static String[] getAttributeKeyList() {
        return attributeList == null ? attributeList = new String[]{ AS_CODE_TEXT, AS_CODE_NUMBER, AS_CODE_STRING, AS_CODE_TOKEN } : attributeList;
    }

    private static void addAttribute(String label, AttributeSet attributeSet) {
        UIManager.put("QnAForum.attribute." + label, attributeSet);
    }

    private static void addColor(String label, int r, int g, int b) {
        UIManager.put("QnAForum.color." + label, new Color(r, g, b));
    }

    private static void addFont(int type, String label, int... sizes) {
        if (sizes.length == 0) return;

        Font font = FontLoader.getFont(label.replace(".", ""), sizes[0], type);
        for (int size : sizes) {
            UIManager.put("QnAForum.font." + label + size, font.deriveFont((float) size));
        }
    }

    public void init() {
    }

    public void loadAttributeSet(String key, String value) {
        if (key.isEmpty() || value.isEmpty()) return;

        String[] splitKey = key.split("_");

        for (String validKey : getAttributeKeyList()) {
            if(validKey.equals(splitKey[splitKey.length-1])) {
                attributeSets.put(validKey, value);
                return;
            }
        }
    }

    public void loadColorAttribute(String key, String value) {
        if (key.isEmpty() || value.isEmpty()) return;

        for (String validKey : getColorKeyList()) {
            if (validKey.equals(key)) {
                colorAttributes.put(validKey, value);
                return;
            }
        }
    }

    public void loadFontAttributes(String key, String value) {
        // TODO: Loads all even font sizes from 12 to 40. Finalize required fonts later
        String sizes = "12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40";
        loadFontAttribute(value + FONT_T_REGULAR, sizes);
        loadFontAttribute(value + FONT_T_SEMIBOLD, sizes);
        loadFontAttribute(value + FONT_T_THIN, sizes);
        loadFontAttribute(value + FONT_T_BOLD, sizes);

        key = key.endsWith(FONT_K_PRIMARY) ? FONT_K_PRIMARY : key.endsWith(FONT_K_SECONDARY) ? FONT_K_SECONDARY : null;

        if (key != null)
            loadFontAttribute(key, value);
    }

    public void loadFontAttribute(String key, String value) {
        if (key.isEmpty() || value.isEmpty()) return;
        fontAttributes.put(key, value);
    }

    public void load() {
        if (!this.equals(ByteBoardBaseTheme))
            ByteBoardBaseTheme.load();

        for (String key : attributeSets.keySet()) {
            createAttribute(key, attributeSets.get(key));
        }

        for (String key : colorAttributes.keySet()) {
            createColor(key, colorAttributes.get(key));
        }

        for (String key : fontAttributes.keySet()) {
            createFont(key, fontAttributes.get(key));
        }

        UIManager.put("Label.foreground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        UIManager.put("Button.foreground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        UIManager.put("ComboBox.focusedBackground", ResourceManager.getColor(ByteBoardTheme.ACCENT_DARK));
        UIManager.put("ComboBox.selectionBackground", ResourceManager.getColor(ByteBoardTheme.ACCENT));
        UIManager.put("ComboBox.selectionForeground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        UIManager.put("ComboBox.background", ResourceManager.getColor(ByteBoardTheme.MAIN_LIGHT)); // _DARK
        UIManager.put("ComboBox.foreground", ResourceManager.getColor(ByteBoardTheme.MAIN_DARK));

        UIManager.put("TextField.foreground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));
        UIManager.put("TextField.caretForeground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_DARK));

        UIManager.put("TextArea.selectionBackground", ResourceManager.getColor(ByteBoardTheme.ACCENT_DARK));
        UIManager.put("TextArea.selectionForeground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
        UIManager.put("TextArea.caretForeground", ResourceManager.getColor(ByteBoardTheme.TEXT_FG_LIGHT));
    }

    // note: doesn't use the style consideration
    private void createFont(String key, String value) {
        if (key.equals(FONT_K_PRIMARY) || key.equals(FONT_K_SECONDARY)) {
            UIManager.put("QnAForum.font." + key, value);
            return;
        }

        String[] values = value.split("-");
        String[] sizesList = values[values.length == 1 ? 0 : 1].split(",");

        int[] sizes = new int[sizesList.length];

        try {
            for (int i = 0; i < sizesList.length; i++) {
                sizes[i] = Integer.parseInt(sizesList[i].trim());
            }

            addFont(values.length == 1 ? 0 : Integer.parseInt(values[0]), key.toLowerCase(), sizes);
        } catch (NumberFormatException ignored) {
        }

    }

    private void createColor(String key, String value) {
        int[] rgb = getRGB(value);
        if(rgb == null) return;

        addColor(key, rgb[0], rgb[1], rgb[2]);
    }

    public void createAttribute(String key, String value) {
        int[] rgb = getRGB(value);
        if(rgb == null) return;

        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, new Color(rgb[0], rgb[1], rgb[2]));

        addAttribute(key, attributeSet);
    }

    private int[] getRGB(String value) {
        String[] values = value.split(",");
        if (values.length != 3) return null;

        int[] rgb = new int[3];

        try {
            for (int i = 0; i < rgb.length; i++)
                rgb[i] = Integer.parseInt(values[i].trim());

        } catch (NumberFormatException ignored) {
            return null;
        }
        return rgb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
