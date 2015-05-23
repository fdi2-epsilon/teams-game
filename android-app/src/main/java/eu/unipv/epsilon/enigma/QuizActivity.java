package eu.unipv.epsilon.enigma;

import android.content.Intent;
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
import eu.unipv.epsilon.enigma.ui.quiz.QuizStatusTabColorizer;
import eu.unipv.epsilon.enigma.ui.util.CollectionDataBundle;

/** Shows the quiz that is currently played. */
public class QuizActivity extends AppCompatActivity {

    public static final String PARAM_COLLECTION_ID = QuizActivity.class.getName() + ":param_collection_id";

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

        // Get quest collection to display and its saved data from intent and update the view
        CollectionDataBundle collectionBundle = getIntentData(getIntent());
        setTitle(collectionBundle.getCollection().getTitle());

        setupTabs(collectionBundle.getCollection(), collectionBundle.getCollectionStatus());
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

    private CollectionDataBundle getIntentData(Intent intent) {
        String collectionId = intent.getStringExtra(PARAM_COLLECTION_ID);
        if (collectionId == null) throw new IllegalArgumentException("PARAM_COLLECTION_ID not set");

        return CollectionDataBundle.fromId((EnigmaApplication) getApplication(), collectionId);
    }

    private void setupTabs(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SlidingTabLayout slidingTabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // Set adapters
        viewPager.setAdapter(new QuizFragmentPageAdapter(getSupportFragmentManager(), collection));
        slidingTabs.setViewPager(viewPager);

        // Configure sliding tabs style
        slidingTabs.setCustomTabView(R.layout.quiz_tab_element, R.id.text);
        slidingTabs.setDistributeEvenly(false); // Keep same width for all tabs
        slidingTabs.setCustomTabColorizer(new QuizStatusTabColorizer(collectionStatus));
    }

}
