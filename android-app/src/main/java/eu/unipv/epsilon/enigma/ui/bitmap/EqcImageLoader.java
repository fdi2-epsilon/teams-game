package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * An image loader which loads from an URL stream, it also sets input image density to eqc standard.
 * This should also be able to load from any URL with a registered protocol handler.
 */
public class EqcImageLoader extends InputStreamImageLoader {

    // TODO: Transfer BufferedInputStream code to InputStream class and allow changing mark

    public EqcImageLoader(URL url) {
        super(openStream(url));
        // With this, up to 50KB can be read while decoding bounds, avoiding to read them again when decoding image.
        // So we don't need to reopen the URL stream.
        inputStream.mark(50 * 1024);
    }

    @Override
    protected void setAdditionalDecodingOptions(BitmapFactory.Options options) {
        super.setAdditionalDecodingOptions(options);

        // Images in EQC archives are in xxhdpi density
        options.inDensity = DisplayMetrics.DENSITY_XXHIGH;
    }

    @Override
    protected Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
        try {
            //inputStream = url.openStream();
            //Log.i("", inputStream.markSupported() ? "Mark supported" : "Fail");
            inputStream.reset();
            return super.decodeBitmapWithOptions(options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
            return new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
