package eu.unipv.epsilon.enigma.ui.quiz;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

/**
 * Sample tabs adapter...
 */
public class SampleFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 8;
    private String tabTitles[] = new String[] {"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH"};
    private Context context;

    private QuestCollection questCollection;

    public SampleFragmentPageAdapter(FragmentManager fm, Context context, QuestCollection qc) {
        super(fm);
        this.context = context;
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
