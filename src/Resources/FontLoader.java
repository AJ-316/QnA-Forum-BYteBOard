/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Resources;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author AJ
 */
public class FontLoader {

    public static Font getFont(String fontName, float size, int fontType) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        for (Font font : allFonts) {
            if (font.getName().equals(fontName) && font.getSize() == size)
                return font;
        }

        // Font not found, register a new font
        try {
            InputStream inputStream = FontLoader.class.getResourceAsStream("FontResource/" + fontName + ".ttf");
            System.out.println(fontName);
            Objects.requireNonNull(inputStream);

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(fontType, size);
            ge.registerFont(customFont);
            return customFont;
        } catch (IOException | FontFormatException | NullPointerException e) {
            // Return a default font if custom font loading fails
            return new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
        }
    }

    public static Font getFont(String fontName, float size) {
        return getFont(fontName, size, Font.PLAIN);
    }

}
