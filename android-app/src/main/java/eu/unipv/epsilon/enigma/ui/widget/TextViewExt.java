package eu.unipv.epsilon.enigma.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.ui.FontLoader;

/** An extended {@link TextView} capable of loading fonts from application assets. */
public class TextViewExt extends TextView {

    public TextViewExt(Context context) {
        super(context);
        loadTypefaceAttributes(null, 0);
    }

    public TextViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadTypefaceAttributes(attrs, 0);
    }

    public TextViewExt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadTypefaceAttributes(attrs, defStyleAttr);
    }

    private void loadTypefaceAttributes(AttributeSet attrs, int defStyleAttr) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TextViewExt, defStyleAttr, 0);

        try {
            String fontFamily = a.getString(R.styleable.TextViewExt_font_family);
            int fontStyle = a.getInt(R.styleable.TextViewExt_font_style, Typeface.NORMAL);

            Typeface font;
            if (fontFamily != null) {
                final FontLoader fontLoader = new FontLoader(getContext().getAssets());
                String fontFamilyExt = a.getString(R.styleable.TextViewExt_font_family_ext);
                font = fontLoader.loadFont(fontFamily, fontFamilyExt);
            } else
                font = FontLoader.DEFAULT_FONT;

            setTypeface(font, fontStyle);

        } finally {
            a.recycle();
        }
    }

}
