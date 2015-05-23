package eu.unipv.epsilon.enigma;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import eu.unipv.epsilon.enigma.loader.levels.exception.MetadataNotFoundException;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.template.DalvikCandidateClassSource;
import eu.unipv.epsilon.enigma.ui.main.CollectionsViewAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TranslucentControlsActivity {

    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);
    private GameAssetsSystem assetsSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locateViews(R.id.toolbar, R.id.main_collections_view);

        LOG.info("Activity started, display density: {}, locale: {}",
                getResources().getDisplayMetrics().density, getResources().getConfiguration().locale.getLanguage());

        // Initialize toolbar
        setSupportActionBar(toolbarView);   // Title assigned by manifest

        // Create a new local data source to load any built-in collections
        File collectionsDir = new File(getFilesDir(), "collections");
        LOG.info("Internal collections path: " + collectionsDir.getPath());

        // Do the same for external storage so users can add new collections on-the-go
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // External storage is readable
            File extDocsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File extCollectionsDir = new File(extDocsDir, getString(R.string.app_name) + '/' + "Collections");

            LOG.info("External collections path: " + extCollectionsDir.getPath());
            if (!extCollectionsDir.mkdirs())
                LOG.error("External collections directory not created");

            assetsSystem = new GameAssetsSystem(new DirectoryPool(collectionsDir), new DirectoryPool(extCollectionsDir));
        } else {
            LOG.info("External storage is not accessible");
            assetsSystem = new GameAssetsSystem(new DirectoryPool(collectionsDir));
        }

        // Initialize Templating system
        assetsSystem.createTemplateServer(new DalvikCandidateClassSource(this, assetsSystem));

        // Initialize view
        initializeElementsView();
    }

    /** Generate a popup menu to navigate toward the quiz activity. */
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
        // Use this setting to improve performance if you know that changes
        // in the content do not change the layout size of the RecyclerView.
        recyclerView.setHasFixedSize(true);

        // Use a staggered layout manager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Populate with data
        populateMainView();
    }

    private void populateMainView() {
        List<QuestCollection> collections = new ArrayList<>();

        for (String collectionId : assetsSystem.getAvailableCollectionIDs()) {
            try {
                collections.add(assetsSystem.getCollectionContainer(collectionId).loadCollectionMeta());
            } catch (MetadataNotFoundException e) {
                LOG.error("Collection metadata not found", e);
            } catch (IOException e) {
                LOG.error("Error while opening collection metadata", e);
            }
        }

        recyclerView.setAdapter(new CollectionsViewAdapter(collections,
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)));
    }

}
