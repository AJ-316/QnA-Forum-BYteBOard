package Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ThemeLoader {

    public static ByteBoardTheme getTheme(String file) {
        if (file == null) return null;

        InputStream is = ThemeLoader.class.getResourceAsStream("Themes/" + file + ".bbt");

        if (is == null) return null;

        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line;
        ByteBoardTheme theme = new ByteBoardTheme() {};
        theme.setName(file);

        String key, value;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                String[] attribute = line.split("=");
                if (line.startsWith("#") || attribute.length != 2) continue;

                key = attribute[0].trim().toLowerCase();
                value = attribute[1].trim().toLowerCase();

                if (line.startsWith("font_n_")) {
                    theme.loadFontAttributes(key, value);
                    continue;
                }

                if (line.startsWith("attrib_")) {
                    theme.loadAttributeSet(key, value);
                    continue;
                }
                theme.loadColorAttribute(key, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return theme;
    }

}
