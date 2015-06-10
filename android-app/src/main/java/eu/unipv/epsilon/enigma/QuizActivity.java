package eu.unipv.epsilon.enigma;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;
import eu.unipv.epsilon.enigma.ui.quiz.QuizFragmentPageAdapter;
import eu.unipv.epsilon.enigma.ui.quiz.QuizStatusTabColorizer;
import eu.unipv.epsilon.enigma.ui.quiz.drawer.QuizListAdapter;
import eu.unipv.epsilon.enigma.ui.util.CollectionDataBundle;
import eu.unipv.epsilon.enigma.ui.widget.recycler.DividerItemDecoration;

/** Shows the quiz that is currently played in a fragment and allows navigation between quizzes using tabs. */
public class QuizActivity extends AppCompatActivity {

    public static final String PARAM_COLLECTION_ID = QuizActivity.class.getName() + ":param_collection_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize toolbar and enable "up" navigation
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get quest collection from intent to display its saved data and update the view
        CollectionDataBundle bundle = getCollectionArgument();

        setTitle(bundle.getCollection().getTitle());

        setupTabs(bundle.getCollection(), bundle.getCollectionStatus());
        setupDrawer(bundle.getCollection());
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

    private CollectionDataBundle getCollectionArgument() {
        String collectionId = getIntent().getStringExtra(PARAM_COLLECTION_ID);
        if (collectionId == null)
            throw new IllegalArgumentException("PARAM_COLLECTION_ID not set");

        return CollectionDataBundle.fromId((EnigmaApplication) getApplication(), collectionId);
    }

    private void setupTabs(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SlidingTabLayout slidingTabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // Configure sliding tabs style
        slidingTabs.setCustomTabView(R.layout.quiz_tab_element, R.id.text);
        slidingTabs.setDistributeEvenly(false); // Keep same width for all tabs

        // Set adapters
        viewPager.setAdapter(new QuizFragmentPageAdapter(getSupportFragmentManager(), collection));
        slidingTabs.setViewPager(viewPager);
        slidingTabs.setCustomTabColorizer(new QuizStatusTabColorizer(collectionStatus));
    }

    private void setupDrawer(QuestCollection collection) {
        RecyclerView v = (RecyclerView) findViewById(R.id.drawer_recycler);
        v.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        v.setHasFixedSize(true);
        v.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        v.setItemAnimator(new DefaultItemAnimator());
        v.setAdapter(new QuizListAdapter(collection,
                (DrawerLayout) findViewById(R.id.drawer_layout), (ViewPager) findViewById(R.id.viewpager)));
    }

}
