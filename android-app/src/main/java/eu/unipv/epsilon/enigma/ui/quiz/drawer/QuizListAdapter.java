package eu.unipv.epsilon.enigma.ui.quiz.drawer;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListItem> {

    private final QuestCollection questCollection;
    private DrawerLayout drawer;
    private ViewPager pager;

    /**
     * Creates an adapter for the quiz list inside the drawer
     * @param questCollection the collection of quizzes to show in the list
     * @param drawer the (optional) instance of the drawer to close on item click
     * @param pager the (optional) pager to scroll to the item selected
     */
    public QuizListAdapter(QuestCollection questCollection, @Nullable DrawerLayout drawer, @Nullable ViewPager pager) {
        this.questCollection = questCollection;
        this.drawer = drawer;
        this.pager = pager;
    }

    @Override
    public QuizListItem onCreateViewHolder(ViewGroup parent, int i) {
        return new QuizListItem(parent);
    }

    @Override
    public void onBindViewHolder(QuizListItem quizListItem, int i) {
        quizListItem.updateViewFromData(questCollection.get(i));

        View item = ((ViewGroup) quizListItem.itemView).getChildAt(0);
        item.setOnClickListener(new ClickListener(quizListItem.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return questCollection.size();
    }

    public class ClickListener implements View.OnClickListener {

        private final int position;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (pager != null)
                pager.setCurrentItem(position, true);
            if (drawer != null)
                drawer.closeDrawers();
        }

    }

}
