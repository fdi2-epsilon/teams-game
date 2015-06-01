package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import eu.unipv.epsilon.enigma.R;

public final class MediumCollectionCard extends RichCollectionCardHolder {

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.main_card_medium;

    public MediumCollectionCard(ViewGroup parent) {
        super(parent, LAYOUT_RESOURCE);
    }

}
