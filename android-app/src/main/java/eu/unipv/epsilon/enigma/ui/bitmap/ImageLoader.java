package eu.unipv.epsilon.enigma.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public abstract class ImageLoader {

    protected abstract Bitmap decodeBitmapWithOptions(BitmapFactory.Options options);

    public Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = getImageOptions();

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        int sampleSize = options.inSampleSize;

        // Decode bitmap with inSampleSize set
        Bitmap bitmap = decodeBitmapWithOptions(options);

        Log.i(getClass().getName(), String.format("Loaded %dx%d image as %dx%d (requested %dx%d) with sample size = %d",
                srcWidth, srcHeight, options.outWidth, options.outHeight, reqWidth, reqHeight, sampleSize));

        return bitmap;
    }

    private BitmapFactory.Options getImageOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();

        //Load image info but not the image itself
        options.inJustDecodeBounds = true;
        decodeBitmapWithOptions(options);

        options.inJustDecodeBounds = false;
        return options;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
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