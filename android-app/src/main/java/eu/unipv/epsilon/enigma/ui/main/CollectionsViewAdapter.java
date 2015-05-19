package eu.unipv.epsilon.enigma.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import eu.unipv.epsilon.enigma.QuizActivity;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.ui.main.card.*;

import java.util.List;
import java.util.NoSuchElementException;

public class CollectionsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String FIRST_START = "firstStart";
    private List<QuestCollection> elements;
    private SharedPreferences sharedPreferences;

    private boolean firstStart;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CollectionsViewAdapter(List<QuestCollection> elements, SharedPreferences sharedPreferences) {
        this.elements = elements;
        this.sharedPreferences = sharedPreferences;

        firstStart = sharedPreferences.getBoolean(FIRST_START, true);

        // Animated view
        setHasStableIds(true);
    }

    // Create new views (invoked by the Layout Manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(getClass().getName(), "Created new ViewHolder");

        CardHolder viewHolder = makeCardHolder(parent, CardType.values()[viewType]);

        if (viewHolder instanceof CollectionCardHolder)
            viewHolder.getItemView().setOnClickListener(new CardClickListener());
        else if (viewHolder instanceof FirstStartCard) {
            FirstStartCard fsc = (FirstStartCard) viewHolder;
            fsc.setOkButtonClickListener(new FirstCardClickListener());
        }
        return viewHolder;
    }

    private CardHolder makeCardHolder(ViewGroup parent, CardType cardType) {
        // New card holder instances inflate their views and set layout manager params
        switch (cardType) {
            case FIRST_START:
                return new FirstStartCard(parent);
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
        Log.i(getClass().getName(), "Recycling an existing ViewHolder");

        // If it is a card describing dynamic content like a Quest Collection, needs to be recycled with new parameters.
        if (holder instanceof CollectionCardHolder)
            ((CollectionCardHolder) holder).updateViewFromData(elements.get(position - (firstStart ? 1 : 0)));

        // Static views like CardType.FIRST_START do not need to be recycled, cause they don't have data.
    }

    @Override
    public int getItemCount() {
        return elements.size() + (firstStart ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (firstStart) {
            if (position == 0)
                return CardType.FIRST_START.ordinal();
            position--;
        }
        // Temporary algorithm to get card size
        if (position == 0)
            return CardType.LARGE.ordinal();
        CardType[] types = { CardType.MEDIUM, CardType.SMALL, CardType.TINY};

        return types[(position - 1) % types.length].ordinal();
    }

    private class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int index = ((RecyclerView) v.getParent()).getChildAdapterPosition(v) - (firstStart ? 1 : 0);
            Log.i(getClass().getName(), "Clicked #" + index);

            QuestCollection qc = elements.get(index);
            Context context = v.getContext();

            if (!qc.isEmpty()) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra(QuizActivity.PARAM_QUESTCOLLECTION, qc);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, R.string.main_toast_no_content, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class FirstCardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.i(getClass().getName(), "ClickedFirstStart");
            firstStart = false;
            sharedPreferences.edit().putBoolean(FIRST_START, false).apply();

            notifyDataSetChanged();
        }
    }

}
