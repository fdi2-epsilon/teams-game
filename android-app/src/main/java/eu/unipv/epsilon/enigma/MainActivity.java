package eu.unipv.epsilon.enigma;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity-DEBUG", "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        // Use this setting to improve performance if you know that changes
        // in the content do not change the layout size of the RecyclerView.
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // ?? 'getApplicationContext()' or 'this'?

        // Specify an adapter
        recyclerView.setAdapter(new TempAdapter(new String[]{
                "Hello", "Everybody", "How are you?", "I am testing", "Yes, testing", "It is a fun thing",
                "No, I am only joking", "OMG WATCH OUT!", "Nothing", "Goodbye", "...", "Are you still here?",
                "No?", "Ok", "Bye"
        }));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
