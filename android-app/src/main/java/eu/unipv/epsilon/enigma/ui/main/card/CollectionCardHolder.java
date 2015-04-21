package eu.unipv.epsilon.enigma.ui.main.card;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.ui.DynamicView;
import eu.unipv.epsilon.enigma.ui.bitmap.ImageLoader;
import eu.unipv.epsilon.enigma.ui.bitmap.InputStreamImageLoader;
import eu.unipv.epsilon.enigma.ui.main.TempElement;

import java.io.IOException;
import java.io.InputStream;

public abstract class CollectionCardHolder extends CardHolder implements DynamicView<TempElement> {

    protected TextView titleRef;
    protected ImageView imageRef;

    protected CollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        this(parent, layoutResource, false);
    }

    protected CollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource, boolean isFullSpan) {
        super(parent, layoutResource, isFullSpan);

        // Store references to view elements to avoid potentially expensive lookups later.
        titleRef = (TextView) itemView.findViewById(R.id.card_title);
        imageRef = (ImageView) itemView.findViewById(R.id.card_image);
    }

    @Override
    public void updateViewFromData(TempElement dataElement) {
        titleRef.setText(dataElement.getTitle());
        //imageRef
    }

    // TEMPORARY METHOD
    protected void _tempLoadImageFromAssets(final Context context, final String path) {
        imageRef.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageRef.getViewTreeObserver().removeOnPreDrawListener(this);

                try {
                    InputStream is = context.getAssets().open(path);
                    ImageLoader loader = new InputStreamImageLoader(is);
                    imageRef.setImageBitmap(loader.decodeSampledBitmap(imageRef.getMeasuredWidth(), imageRef.getMeasuredHeight()));
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

}
