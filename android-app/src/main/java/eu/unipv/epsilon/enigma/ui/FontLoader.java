package eu.unipv.epsilon.enigma.ui;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/** Provides a standard way to load fonts from application assets. **/
public class FontLoader {

    public static final String FONTS_PATH = "fonts/";
    public static final String FONTS_EXT = ".ttf";

    // This is returned when called with no or null parameters.
    public static final Typeface DEFAULT_FONT = Typeface.SANS_SERIF;

    private static final Map<String, Typeface> fontCache = new HashMap<>();

    AssetManager assetManager;

    public FontLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Typeface loadFont() {
        Log.i(getClass().getName(), "No font loaded (used default) because called with invalid parameters.");
        return DEFAULT_FONT;
    }

    public Typeface loadFont(String fontFamily) {
        if (fontFamily == null) return loadFont();
        return genTypefaceFromName(fontFamily);
    }

    public Typeface loadFont(String fontFamily, String fontFamilyExtra) {
        if (fontFamilyExtra == null) return loadFont(fontFamily);
        return genTypefaceFromName(fontFamily + '-' + fontFamilyExtra);
    }

    private Typeface genTypefaceFromName(String name) {
        if (fontCache.containsKey(name)) {
            Log.i(getClass().getName(), "Loaded font \"" + name + "\" from cache.");
            return fontCache.get(name);
        }

        Typeface font = Typeface.createFromAsset(assetManager, FONTS_PATH + name + FONTS_EXT);
        Log.i(getClass().getName(), "Loaded new font \"" + name + "\" from file.");
        fontCache.put(name, font);
        return font;
    }

}
