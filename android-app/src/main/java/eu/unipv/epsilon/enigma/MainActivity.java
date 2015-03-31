package eu.unipv.epsilon.enigma;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity-DEBUG", "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
