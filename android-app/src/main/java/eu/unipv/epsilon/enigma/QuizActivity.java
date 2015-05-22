package eu.unipv.epsilon.enigma;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;
import eu.unipv.epsilon.enigma.ui.quiz.QuizFragmentPageAdapter;

/** Shows the quiz that is currently played. */
public class QuizActivity extends AppCompatActivity {

    public static final String PARAM_QUESTCOLLECTION = "param_questcollection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        QuestCollection collection = (QuestCollection) getIntent().getSerializableExtra(PARAM_QUESTCOLLECTION);
        setTitle(collection.getTitle());

        // Get collection saved progression data
        QuestCollectionStatus collectionStatus = new QuestCollectionStatus(
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE), collection.getId());

        setupTabs(collection, collectionStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.temp_button2) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTabs(final QuestCollection collection, final QuestCollectionStatus collectionStatus) {

        //Get the ViewPager and set its PageAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new QuizFragmentPageAdapter(getSupportFragmentManager(), collection, collectionStatus));

        //Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        slidingTabs.setCustomTabView(R.layout.quiz_tab_element, R.id.text);

        //Center the tabs in the layout
        slidingTabs.setDistributeEvenly(false);
        slidingTabs.setViewPager(viewPager);

        //customize tab color
        slidingTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                // slidingTabs.invalidate(); should be called to update the strip color in real time
                return collectionStatus.isSolved(position + 1) ? Color.YELLOW : Color.WHITE;
            }
        });
    }

}
