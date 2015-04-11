package eu.unipv.epsilon.enigma.ui.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import eu.unipv.epsilon.enigma.ui.main.card.*;

public class CollectionsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TempElement[] elements;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CollectionsViewAdapter(TempElement[] elements) {
        this.elements = elements;
    }

    // Create new views (invoked by the Layout Manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(getClass().getName(), "Created new ViewHolder");

        // New card holder instances inflate their views and set layout manager params
        switch (CardType.values()[viewType]) {
            case FIRST_START:   return new FirstStartCard(parent);
            case LARGE:         return new LargeCollectionCard(parent);
            case MEDIUM:        return new MediumCollectionCard(parent);
            case SMALL:         return new SmallCollectionCard(parent);
            case TINY:          return new TinyCollectionCard(parent);
            default:            throw new RuntimeException("Unhandled view type in main view");
        }
    }

    // Replace the contents of a view (invoked by the Layout Manager)
    //  - Get element from the data set at the given position
    //  - Replace the contents of the view with that element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(getClass().getName(), "Recycling an existing ViewHolder");

        // If it is a card describing dynamic content like a Quest Collection, needs to be recycled with new parameters.
        if (holder instanceof CollectionCardHolder)
            ((CollectionCardHolder) holder).updateViewFromData(elements[position]);

        // Static views like CardType.FIRST_START do not need to be recycled, cause they don't have data.
    }

    @Override
    public int getItemCount() {
        return elements.length;
    }

    @Override
    public int getItemViewType(int position) {
        return elements[position].getType().ordinal();
    }

}
