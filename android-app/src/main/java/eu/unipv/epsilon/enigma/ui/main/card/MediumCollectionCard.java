package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;

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
    public void updateViewFromData(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        super.updateViewFromData(collection, collectionStatus);
        subtitleRef.setText(collection.getSubtitle());
        descriptionRef.setText(Html.fromHtml(collection.getDescription()));

        double completionPercentage = (double) collectionStatus.getSolvedQuests().size() / collection.size() * 100;
        progressRef.setProgress((int) completionPercentage);
        progressRef.setSecondaryProgress(0);
    }

}
