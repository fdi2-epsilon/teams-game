package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/** An {@link ImageLoader image loader} from {@link InputStream}. */
public class InputStreamImageLoader extends ImageLoader {

    protected InputStream inputStream;

    public InputStreamImageLoader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    protected Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    // Do not override 'postLoad' here since the caller creates the inputStream

}
