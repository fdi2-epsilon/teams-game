package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;
import eu.unipv.epsilon.enigma.ui.bitmap.EqcImageLoader;
import eu.unipv.epsilon.enigma.ui.bitmap.ImageLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public abstract class CollectionCardHolder extends CardHolder {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionCardHolder.class);

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

    public void updateViewFromData(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        titleRef.setText(collection.getTitle());
        loadImage(collection.getIconUrl());
    }

    // Temporary hook to get effective ImageView size for sampled image loading
    protected void loadImage(final URL path) {
        imageRef.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageRef.getViewTreeObserver().removeOnPreDrawListener(this);
                try {
                    ImageLoader loader = new EqcImageLoader(path);
                    imageRef.setImageBitmap(loader.decodeSampledBitmap(imageRef.getMeasuredWidth(), imageRef.getMeasuredHeight()));
                } catch (IOException e) {
                    LOG.warn("The image was not found", e);
                }
                return true;
            }
        });
    }

}
