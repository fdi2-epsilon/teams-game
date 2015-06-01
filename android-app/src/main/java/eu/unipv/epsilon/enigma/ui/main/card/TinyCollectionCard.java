package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import eu.unipv.epsilon.enigma.R;

public final class TinyCollectionCard extends CollectionCardHolder {

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.main_card_tiny;

    public TinyCollectionCard(ViewGroup parent) {
        super(parent, LAYOUT_RESOURCE);
    }

}
