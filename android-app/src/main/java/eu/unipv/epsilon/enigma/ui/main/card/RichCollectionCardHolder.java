package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;

/** A {@link SubtitleCollectionCardHolder} card holder containing also a description and a progress bar. */
public class RichCollectionCardHolder extends SubtitleCollectionCardHolder {

    protected TextView descriptionRef;
    protected ProgressBar progressRef;

    protected RichCollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        super(parent, layoutResource);
    }

    protected RichCollectionCardHolder(ViewGroup parent, @LayoutRes int layoutResource, boolean isFullSpan) {
        super(parent, layoutResource, isFullSpan);
    }

    @Override
    protected void bindLayoutElements() {
        super.bindLayoutElements();

        // Store additional layout specific references
        descriptionRef = (TextView) itemView.findViewById(R.id.card_description);
        progressRef = (ProgressBar) itemView.findViewById(R.id.card_progressbar);
    }

    @Override
    public void updateViewFromData(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        super.updateViewFromData(collection, collectionStatus);

        // Update local references view content from given data
        descriptionRef.setText(Html.fromHtml(collection.getDescription()));
        progressRef.setMax(collection.size());
        progressRef.setProgress(collectionStatus.getSolvedQuests().size());
        progressRef.setSecondaryProgress(0);
    }

}
