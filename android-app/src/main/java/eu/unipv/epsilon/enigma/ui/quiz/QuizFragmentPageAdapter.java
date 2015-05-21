package eu.unipv.epsilon.enigma.ui.quiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;

/**
 * Generates tab titles and pager fragments from {@link QuestCollection} data.
 */
public class QuizFragmentPageAdapter extends FragmentPagerAdapter {

    private final QuestCollection questCollection;
    private final QuestCollectionStatus collectionStatus;

    public QuizFragmentPageAdapter(FragmentManager fm, QuestCollection qc, QuestCollectionStatus collectionStatus) {
        super(fm);
        this.questCollection = qc;
        this.collectionStatus = collectionStatus;
    }

    @Override
    public int getCount() {
        return questCollection.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(
                position + 1,
                questCollection.get(position).getMainDocumentUrl(),
                collectionStatus.getQuestViewInterface(position + 1));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return questCollection.get(position).getName();
    }

}
