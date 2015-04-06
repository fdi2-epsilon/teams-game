package eu.unipv.epsilon.enigma;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
        // Create a new view
        CardView v = (CardView) LayoutInflater.from(
                parent.getContext()).inflate(R.layout.activity_main_card_element, parent, false);

        // Set the view's size, margins, paddings and layout parameters
        /* ... */

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - Get element from your dataset at this position
        // - Replace the contents of the view with that element
        ((TextView) holder.card.getChildAt(0)).setText(dataset[position]);

        if (position == 0) {
            ((StaggeredGridLayoutManager.LayoutParams) holder.card.getLayoutParams()).setFullSpan(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.length;
    }

}
