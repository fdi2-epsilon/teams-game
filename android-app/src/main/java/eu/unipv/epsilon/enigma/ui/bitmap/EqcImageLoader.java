package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * An image loader which loads from an URL stream, it also sets input image density to eqc standard.
 * This should also be able to load from any URL with a registered protocol handler.
 */
public class EqcImageLoader extends InputStreamImageLoader {

    public EqcImageLoader(URL url) {
        super(openStream(url));
    }

    @Override
    protected void setAdditionalDecodingOptions(BitmapFactory.Options options) {
        super.setAdditionalDecodingOptions(options);

        // Images in EQC archives are in xxhdpi density
        options.inDensity = DisplayMetrics.DENSITY_XXHIGH;
    }

    @Override
    protected void postLoad() {
        super.postLoad();

        try {
            // Close the stream at the end
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This is only to handle exception in constructor
    private static InputStream openStream(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
