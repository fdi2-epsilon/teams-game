package eu.unipv.epsilon.enigma;

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
import eu.unipv.epsilon.enigma.ui.util.SimpleCollectionDataRetriever;

/** Shows the quiz that is currently played. */
public class QuizActivity extends AppCompatActivity {

    public static final String PARAM_COLLECTION_ID = QuizActivity.class.getName() + ":param_collection_id";

    private QuestCollectionStatus collectionStatus;

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

        String collectionId = getIntent().getStringExtra(PARAM_COLLECTION_ID);
        if (collectionId == null)
            throw new IllegalArgumentException("PARAM_COLLECTION_ID not set");

        SimpleCollectionDataRetriever cData =
                new SimpleCollectionDataRetriever((EnigmaApplication) getApplication(), collectionId);

        QuestCollection collection = cData.getCollection();
        collectionStatus = cData.getCollectionStatus();

        setTitle(collection.getTitle());
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
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SlidingTabLayout slidingTabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // Set adapters
        viewPager.setAdapter(new QuizFragmentPageAdapter(getSupportFragmentManager(), collection));
        slidingTabs.setViewPager(viewPager);

        // Configure sliding tabs style
        slidingTabs.setCustomTabView(R.layout.quiz_tab_element, R.id.text);
        slidingTabs.setDistributeEvenly(false); // Keep same width for all tabs

        // Customize tab color
        slidingTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                // slidingTabs.invalidate(); should be called to update the strip color in real time
                return collectionStatus.isSolved(position + 1) ? Color.CYAN : Color.LTGRAY;
            }
        });
    }

}
