package eu.unipv.epsilon.enigma;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import eu.unipv.epsilon.enigma.loader.levels.parser.MetadataNotFoundException;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.ui.main.CollectionsViewAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TranslucentControlsActivity {

    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);
    public static final String CONFIG_FIRST_START = "firstStart";

    private CollectionsViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locateViews(R.id.toolbar, R.id.main_collections_view);

        LOG.info("Activity created, display density: {}, locale: {}",
                getResources().getDisplayMetrics().density, getResources().getConfiguration().locale.getLanguage());

        // Initialize toolbar, its title gets assigned by activity name in manifest
        setSupportActionBar(toolbarView);

        // Initialize and populate view
        initializeElementsView();
        populateMainView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LOG.debug("Activity stopped, saving preferences...");
        SharedPreferences.Editor prefs = getPreferences(MODE_PRIVATE).edit();
        prefs.putBoolean(CONFIG_FIRST_START, viewAdapter.getFirstStartCardVisible());
        prefs.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button,
        // so long as you specify a parent activity in 'AndroidManifest.xml'.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.temp_button) {
            // Implement here your test routines
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeElementsView() {
        // This improves performance if content changes do not alter the RecyclerView's layout size
        recyclerView.setHasFixedSize(true);

        // Use a staggered layout manager and animate changes (i.e. removal of first start card)
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void populateMainView() {
        EnigmaApplication application = (EnigmaApplication) getApplication();
        GameAssetsSystem assetsSystem = application.getAssetsSystem();

        List<QuestCollection> collections = new ArrayList<>();

        // Fill a list with all available collections info
        for (String collectionId : assetsSystem.getAvailableCollectionIDs()) {
            try {
                collections.add(assetsSystem.getCollectionContainer(collectionId).getCollectionMeta());
            } catch (MetadataNotFoundException e) {
                LOG.error("Collection metadata not found", e);
            } catch (IOException e) {
                LOG.error("Error while opening collection metadata", e);
            }
        }

        viewAdapter = new CollectionsViewAdapter(collections, application.getGameStatus());
        viewAdapter.setFirstStartCardVisible(getPreferences(MODE_PRIVATE).getBoolean(CONFIG_FIRST_START, true));
        recyclerView.setAdapter(viewAdapter);
    }

}
