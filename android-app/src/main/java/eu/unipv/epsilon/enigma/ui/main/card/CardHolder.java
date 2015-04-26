package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// A ViewHolder provides a reference to the views for each data item.
// Complex data items may need more than one view per item, all can be accessed from the same view holder.

public abstract class CardHolder extends RecyclerView.ViewHolder {

    protected CardHolder(ViewGroup parent, @LayoutRes int layoutResource, boolean isFullSpan) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false));

        if (isFullSpan) {
            ((StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams()).setFullSpan(true);
        }
    }

    public View getItemView() {
        return this.itemView;
    }

}
