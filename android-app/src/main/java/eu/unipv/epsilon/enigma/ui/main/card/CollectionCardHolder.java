package eu.unipv.epsilon.enigma.ui.main.card;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import eu.unipv.epsilon.enigma.QuizActivity;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;
import eu.unipv.epsilon.enigma.ui.bitmap.AssetsURLImageLoader;
import eu.unipv.epsilon.enigma.ui.bitmap.ImageLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/** The base collection card holder with a title and background image. */
public class CollectionCardHolder extends CardHolder {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionCardHolder.class);

    private QuestCollection boundCollection;

    protected TextView titleRef;
    protected ImageView imageRef;

    protected CollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        this(parent, layoutResource, false);
    }

    protected CollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource, boolean isFullSpan) {
        super(parent, layoutResource, isFullSpan);
        bindLayoutElements();
    }

    protected void bindLayoutElements() {
        // Store references to view elements to avoid potentially expensive lookups later.
        titleRef = (TextView) itemView.findViewById(R.id.card_title);
        imageRef = (ImageView) itemView.findViewById(R.id.card_image);

        getItemView().setOnClickListener(new ClickListener());
    }

    public void updateViewFromData(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        boundCollection = collection;

        // Update local references view content from given data
        titleRef.setText(collection.getTitle());
        loadImage(collection.getIconUrl());
    }

    // Hook to get effective ImageView size for sampled image loading
    protected void loadImage(final URL path) {
        imageRef.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageRef.getViewTreeObserver().removeOnPreDrawListener(this);
                try {
                    ImageLoader loader = new AssetsURLImageLoader(path);
                    imageRef.setImageBitmap(loader.decodeSampledBitmap(imageRef.getMeasuredWidth(), imageRef.getMeasuredHeight()));
                } catch (IOException e) {
                    LOG.warn("The image was not found", e);
                }
                return true;
            }
        });
    }

    /**
     * Creates a new QuizActivity displaying the content of the QuestCollection currently bound to this card.
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();

            if (!boundCollection.isEmpty()) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra(QuizActivity.PARAM_COLLECTION_ID, boundCollection.getId());
                context.startActivity(intent);
            } else
                Toast.makeText(context, R.string.main_toast_no_content, Toast.LENGTH_SHORT).show();
        }
    }

}
