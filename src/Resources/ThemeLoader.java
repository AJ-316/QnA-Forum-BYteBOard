package Resources;

import java.io.*;

public class ThemeLoader {

    public static ByteBoardTheme getTheme(String file) {
        if(file == null) return null;

        InputStream is = ThemeLoader.class.getResourceAsStream("Themes/" + file + ".bbt");

        if(is == null) return null;

        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line;
        ByteBoardTheme theme = new ByteBoardTheme(){};
        theme.setName(file);

        try {
            while((line = reader.readLine()) != null) {
                line = line.trim();

                // right now only reads color attributes
                String[] attribute = line.split("=");
                if(line.startsWith("#") || attribute.length != 2) continue;

                theme.loadColorAttribute(attribute[0].trim().toLowerCase(), attribute[1].trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return theme;
    }

}
