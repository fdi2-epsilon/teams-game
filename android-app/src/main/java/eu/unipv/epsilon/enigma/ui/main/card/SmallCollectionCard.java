package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.ui.main.TempElement;

public class SmallCollectionCard extends CollectionCardHolder {

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.main_card_small;

    protected TextView subtitleRef;

    public SmallCollectionCard(ViewGroup parent) {
        super(parent, LAYOUT_RESOURCE);

        // Store additional layout specific references
        subtitleRef = (TextView) itemView.findViewById(R.id.card_subtitle);
    }

    @Override
    public void updateViewFromData(TempElement dataElement) {
        super.updateViewFromData(dataElement);
        subtitleRef.setText(dataElement.getSubtitle());
    }

}