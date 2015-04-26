package eu.unipv.epsilon.enigma;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.ui.quiz.QuizFragmentPageAdapter;


public class QuizActivity extends ActionBarActivity {

    public static final String PARAM_QUESTCOLLECTION = "param_questcollection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        QuestCollection collection = (QuestCollection) getIntent().getSerializableExtra(PARAM_QUESTCOLLECTION);
        setTitle(collection.getName());

        setupTabs(collection);
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

    private void setupTabs(QuestCollection collection) {

        //Get the ViewPager and set its PageAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new QuizFragmentPageAdapter(getSupportFragmentManager(), collection));

        //Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        slidingTabLayout.setCustomTabView(R.layout.temp_tab_view, R.id.text);

        //Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        //customize tab color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }

        });
    }

}
