package eu.unipv.epsilon.enigma;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ViewHolder> {

    private String[] dataset;

    // Provide a reference to the views for each data item.
    // Complex data items may need more than one view per item, and you
    // provide access to all the views for a data item in a view holder.
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Each data item is just a string in this case.
        public CardView card;

        public ViewHolder(CardView v) {
            super(v);
            card = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TempAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    // Create new views (invoked by the Layout Manager)
    @Override
    public TempAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("RECYCLER", "Created element");
        // Create a new view
        CardView v = (CardView) LayoutInflater.from(
                parent.getContext()).inflate(R.layout.main_card_small, parent, false);

        // Set the view's size, margins, paddings and layout parameters
        /* ... */

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("RECYCLER", "Recycled element");

        // - Get element from your dataset at this position
        // - Replace the contents of the view with that element

        ((TextView) holder.card.findViewById(R.id.card_description)).setText(dataset[position]);

        StaggeredGridLayoutManager.LayoutParams cardLayoutParams =
                (StaggeredGridLayoutManager.LayoutParams) holder.card.getLayoutParams();

        if (position == 0) cardLayoutParams.setFullSpan(true);
        else cardLayoutParams.setFullSpan(false);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.length;
    }

}
