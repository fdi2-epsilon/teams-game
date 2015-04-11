package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.ui.DynamicView;
import eu.unipv.epsilon.enigma.ui.main.TempElement;

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

}
