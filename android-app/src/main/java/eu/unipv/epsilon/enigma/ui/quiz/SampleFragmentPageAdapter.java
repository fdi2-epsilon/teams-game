package eu.unipv.epsilon.enigma.ui.quiz;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Sample tabs adapter...
 */
public class SampleFragmentPageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 8;
    private String tabTitles[] = new String[] {"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH"};
    private Context context;

    public SampleFragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
