package eu.unipv.epsilon.enigma.ui.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

/** An {@link ImageLoader image loader} from resource file. */
public class ResourceImageLoader extends ImageLoader {

    private Resources resources;

    @DrawableRes
    private int resourceId;

    public ResourceImageLoader(Resources resources, @DrawableRes int resourceId) {
        this.resources = resources;
        this.resourceId = resourceId;
    }

    @Override
    protected Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
        return BitmapFactory.decodeResource(resources, resourceId, options);
    }

}
