package eu.unipv.epsilon.enigma;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import eu.unipv.epsilon.enigma.io.url.EqcURLStreamHandler;
import eu.unipv.epsilon.enigma.ui.main.CardType;
import eu.unipv.epsilon.enigma.ui.main.CollectionsViewAdapter;
import eu.unipv.epsilon.enigma.ui.main.TempElement;

import java.io.File;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class MainActivity extends ActionBarActivity {

    private RecyclerView collectionsView;

    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity-DEBUG", "Activity started, display density: " + getResources().getDisplayMetrics().density);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   // Title assigned by manifest

        // Create a new data source to load collections
        dataSource = new DataSource(getFilesDir(), getResources());

        // Initialize view
        initializeElementsView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Semitransparent UI configuration, only on compatible devices
            GuiHelper.extendMainActivityToSystemArea(this, toolbar, collectionsView);
        }
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
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.filetest_button) {
            // Implement here your test routines
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeElementsView() {
        collectionsView = (RecyclerView) findViewById(R.id.main_collections_view);

        // Use this setting to improve performance if you know that changes
        // in the content do not change the layout size of the RecyclerView.
        collectionsView.setHasFixedSize(true);

        // Use a staggered layout manager
        collectionsView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // Populate with data
        dataSource.populateMainView(collectionsView);
    }

}
