package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class InputStreamImageLoader extends ImageLoader {

    private InputStream inputStream;

    public InputStreamImageLoader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    protected Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

}
