package eu.unipv.epsilon.enigma.ui.quiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

/**
 * Generates tab titles and pager fragments from {@link QuestCollection} data.
 */
public class QuizFragmentPageAdapter extends FragmentPagerAdapter {

    private QuestCollection questCollection;

    public QuizFragmentPageAdapter(FragmentManager fm, QuestCollection qc) {
        super(fm);
        this.questCollection = qc;
    }

    @Override
    public int getCount() {
        return questCollection.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1, questCollection.get(position).getMainDocumentUrl());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return questCollection.get(position).getName();
    }

}
