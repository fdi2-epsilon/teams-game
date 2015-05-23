package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * An image loader which loads from an URL stream, it also sets input image density to game assets spec.
 * This should also be able to load from any URL with a registered protocol handler.
 */
public class AssetsURLImageLoader extends InputStreamImageLoader {

    private static final Logger LOG = LoggerFactory.getLogger(AssetsURLImageLoader.class);

    // TODO: Transfer BufferedInputStream code to InputStream class and allow changing mark

    public AssetsURLImageLoader(URL url) throws IOException {
        super(new BufferedInputStream(url.openStream()));
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
            // Without a BufferedInputStream: inputStream = url.openStream();
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

}
