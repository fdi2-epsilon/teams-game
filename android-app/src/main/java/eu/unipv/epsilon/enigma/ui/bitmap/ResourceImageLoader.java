package eu.unipv.epsilon.enigma.ui.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

public class ResourceImageLoader extends ImageLoader {

    private Resources resources;
    private @DrawableRes int resID;

    public ResourceImageLoader(Resources resources, @DrawableRes int resID) {
        this.resources = resources;
        this.resID = resID;
    }

    @Override
    protected Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
        return BitmapFactory.decodeResource(resources, resID, options);
    }
}
