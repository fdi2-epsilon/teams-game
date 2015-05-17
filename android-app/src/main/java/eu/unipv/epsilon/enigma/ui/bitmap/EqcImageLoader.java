package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * An image loader which loads from an URL stream, it also sets input image density to eqc standard.
 * This should also be able to load from any URL with a registered protocol handler.
 */
public class EqcImageLoader extends InputStreamImageLoader {

    private static final Logger LOG = LoggerFactory.getLogger(EqcImageLoader.class);

    // TODO: Transfer BufferedInputStream code to InputStream class and allow changing mark

    public EqcImageLoader(URL url) throws IOException {
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
            LOG.error("Cannot reset stream for decoding", e);
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
            LOG.error("Cannot close the input stream", e);
        }
    }

    // This is only to handle exception in constructor
    private static InputStream openStream(URL url) throws IOException {
        return new BufferedInputStream(url.openStream());
    }

}
