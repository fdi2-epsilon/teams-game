package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;

/** A collection card holder containing a subtitle {@link TextView}. */
public class SubtitleCollectionCardHolder extends CollectionCardHolder {

    protected TextView subtitleRef;

    protected SubtitleCollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        super(parent, layoutResource);
    }

    protected SubtitleCollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource, boolean isFullSpan) {
        super(parent, layoutResource, isFullSpan);
    }

    @Override
    protected void bindLayoutElements() {
        super.bindLayoutElements();

        // Store additional layout specific references
        subtitleRef = (TextView) itemView.findViewById(R.id.card_subtitle);
    }

    @Override
    public void updateViewFromData(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        super.updateViewFromData(collection, collectionStatus);

        // Update local references view content from given data
        subtitleRef.setText(collection.getSubtitle());
    }

}
