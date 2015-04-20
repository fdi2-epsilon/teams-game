package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.ui.bitmap.ImageLoader;
import eu.unipv.epsilon.enigma.ui.bitmap.ResourceImageLoader;

public class TinyCollectionCard extends CollectionCardHolder {

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.main_card_tiny;

    public TinyCollectionCard(ViewGroup parent) {
        super(parent, LAYOUT_RESOURCE);

        // TEMP CODE
        final ViewGroup kParent = parent;
        imageRef.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageRef.getViewTreeObserver().removeOnPreDrawListener(this);
                ImageLoader loader = new ResourceImageLoader(kParent.getResources(), R.drawable.temp_img04);
                imageRef.setImageBitmap(loader.decodeSampledBitmapFromResource(imageRef.getMeasuredWidth(), imageRef.getMeasuredHeight()));
                return true;
            }
        });
    }

}
