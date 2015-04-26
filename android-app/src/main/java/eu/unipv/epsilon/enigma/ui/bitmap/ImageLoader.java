package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static android.graphics.BitmapFactory.Options;

/**
 * Algoritms to load a sampled image, extending class must implement
 * {@link ImageLoader#decodeBitmapWithOptions(Options)} to effectively load the image.
 */
public abstract class ImageLoader {

    public Bitmap decodeSampledBitmap(int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final Options options = getImageOptions();

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        // Calculate inSampleSize and set additional options
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        setAdditionalDecodingOptions(options);

        int sampleSize = options.inSampleSize;

        // Decode bitmap with inSampleSize set
        Bitmap bitmap = decodeBitmapWithOptions(options);

        Log.i(getClass().getName(), String.format("Loaded %dx%d image as %dx%d (requested %dx%d) with sample size = %d",
                srcWidth, srcHeight, options.outWidth, options.outHeight, reqWidth, reqHeight, sampleSize));

        // Post loading operations
        postLoad();

        return bitmap;
    }

    /**
     * Called internally from {@link ImageLoader},
     * returns a bitmap loaded with the given options, but can also return {@code null};
     * if the passed in {@link Options#inJustDecodeBounds} is set to {@code true}.
     */
    protected abstract Bitmap decodeBitmapWithOptions(Options options);

    protected void setAdditionalDecodingOptions(Options options) {
        // Override me to add additional options like Options#inDensity.
    }

    protected void postLoad() {
        // Override me to add post-loading behavior like stream closing.
    }

    private Options getImageOptions() {
        Options options = new BitmapFactory.Options();

        //Load image info but not the image itself
        options.inJustDecodeBounds = true;
        decodeBitmapWithOptions(options);

        options.inJustDecodeBounds = false;
        return options;
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
