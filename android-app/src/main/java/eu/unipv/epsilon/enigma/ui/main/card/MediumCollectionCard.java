package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.ui.main.TempElement;

public class MediumCollectionCard extends CollectionCardHolder {

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.main_card_medium;

    protected TextView subtitleRef;
    protected TextView descriptionRef;
    protected ProgressBar progressRef;

    public MediumCollectionCard(ViewGroup parent) {
        super(parent, LAYOUT_RESOURCE);

        // Store additional layout specific references
        subtitleRef = (TextView) itemView.findViewById(R.id.card_subtitle);
        descriptionRef = (TextView) itemView.findViewById(R.id.card_description);
        progressRef = (ProgressBar) itemView.findViewById(R.id.card_progressbar);
    }

    @Override
    public void updateViewFromData(TempElement dataElement) {
        super.updateViewFromData(dataElement);
        subtitleRef.setText(dataElement.getSubtitle());
        descriptionRef.setText(Html.fromHtml(dataElement.getDescription()));
        //progressRef
    }

}