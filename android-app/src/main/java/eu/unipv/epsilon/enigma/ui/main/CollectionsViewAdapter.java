package eu.unipv.epsilon.enigma.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.GameStatus;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;
import eu.unipv.epsilon.enigma.ui.main.card.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;

public class CollectionsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionsViewAdapter.class);

    private GameStatus gameStatus;
    private List<QuestCollection> collections;
    private boolean firstStartCardVisible = false;

    /**
     * Creates a collections view adapter using the passed in metadata elements
     *
     * @param collections game quest collections to show in the view
     * @param gameStatus the game status interface to show progress in the view
     */
    public CollectionsViewAdapter(List<QuestCollection> collections, GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.collections = collections;

        setHasStableIds(true);  // Set for animated view
    }

    // Create new views (invoked by the Layout Manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // New card holder instances inflate their views and set layout manager params
        switch (CardType.values()[viewType]) {
            case FIRST_START:
                return new FirstStartCard(parent, this);
            case LARGE:
                return new LargeCollectionCard(parent);
            case MEDIUM:
                return new MediumCollectionCard(parent);
            case SMALL:
                return new SmallCollectionCard(parent);
            case TINY:
                return new TinyCollectionCard(parent);
            default:
                throw new NoSuchElementException("Unknown view type in main view");
        }
    }

    // Replace the contents of a view (invoked by the Layout Manager)
    //  - Get element from the data set at the given position
    //  - Replace the contents of the view with that element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LOG.debug("Recycling an existing ViewHolder of type {}", holder.getClass().getSimpleName());

        // If it is a card describing a Quest Collection, needs to be recycled with new data
        if (holder instanceof CollectionCardHolder) {
            QuestCollection collection = collections.get(position - (firstStartCardVisible ? 1 : 0));
            QuestCollectionStatus collectionStatus = gameStatus.getCollectionStatus(collection.getId());

            ((CollectionCardHolder) holder).updateViewFromData(collection, collectionStatus);
        }

        // Static views like CardType.FIRST_START do not need to be recycled, cause they don't have any data.
    }

    @Override
    public int getItemCount() {
        return collections.size() + (firstStartCardVisible ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (firstStartCardVisible) {
            if (position == 0)
                return CardType.FIRST_START.ordinal();
            position--;
        }

        // Temporary algorithm to get card size (i.e. L MST MST MST...)
        if (position == 0)
            return CardType.LARGE.ordinal();
        CardType[] types = { CardType.MEDIUM, CardType.SMALL, CardType.TINY};

        return types[(position - 1) % types.length].ordinal();
    }

    public void setFirstStartCardVisible(boolean visible) {
        if (this.firstStartCardVisible == visible)
            return;

        this.firstStartCardVisible = visible;
        notifyDataSetChanged();
    }

    public boolean getFirstStartCardVisible() {
        return firstStartCardVisible;
    }

}
