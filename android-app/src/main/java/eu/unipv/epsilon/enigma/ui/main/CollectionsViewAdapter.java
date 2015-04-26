package eu.unipv.epsilon.enigma.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import eu.unipv.epsilon.enigma.QuizActivity;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.ui.main.card.*;

import java.util.List;

public class CollectionsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // TODO: Restore first start card

    private List<QuestCollection> elements;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CollectionsViewAdapter(List<QuestCollection> elements) {
        this.elements = elements;
    }

    // Create new views (invoked by the Layout Manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(getClass().getName(), "Created new ViewHolder");

        CardHolder viewHolder;

        // New card holder instances inflate their views and set layout manager params
        switch (CardType.values()[viewType]) {
            case FIRST_START:   viewHolder = new FirstStartCard(parent); break;
            case LARGE:         viewHolder = new LargeCollectionCard(parent); break;
            case MEDIUM:        viewHolder = new MediumCollectionCard(parent); break;
            case SMALL:         viewHolder = new SmallCollectionCard(parent); break;
            case TINY:          viewHolder = new TinyCollectionCard(parent); break;
            default:            throw new RuntimeException("Unhandled view type in main view");
        }

        viewHolder.getItemView().setOnClickListener(new CardClickListener());
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the Layout Manager)
    //  - Get element from the data set at the given position
    //  - Replace the contents of the view with that element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(getClass().getName(), "Recycling an existing ViewHolder");

        // If it is a card describing dynamic content like a Quest Collection, needs to be recycled with new parameters.
        if (holder instanceof CollectionCardHolder)
            ((CollectionCardHolder) holder).updateViewFromData(elements.get(position));

        // Static views like CardType.FIRST_START do not need to be recycled, cause they don't have data.
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Temporary algorithm to get card size
        if (position == 0) return CardType.LARGE.ordinal();
        CardType[] types = { CardType.MEDIUM, CardType.SMALL, CardType.TINY};
        return types[(position - 1) % types.length].ordinal();
    }

    private class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int index = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Log.i(getClass().getName(), "Clicked #" + index);

            QuestCollection qc = elements.get(index);

            if (qc.size() > 0) {
                Context context = v.getContext();
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra(QuizActivity.PARAM_QUESTCOLLECTION, qc);
                context.startActivity(intent);
            } else {
                //TODO: use toast notification
                Log.i(getClass().getName(), "No Quests in this collection");
            }

        }

    }

}
