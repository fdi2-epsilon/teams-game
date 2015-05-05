package eu.unipv.epsilon.enigma;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Activity with all enigma's title
 */

public class ListActivity extends AppCompatActivity{

    private RecyclerView levelsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_list);

        //Inizialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   // Title assigned by manifest

        // Initialize view
        initializeListView();
    }

    private void initializeListView() {
        levelsView = (RecyclerView) findViewById(R.id.list_levels_view);

        // Use this setting to improve performance if you know that changes
        // in the content do not change the layout size of the RecyclerView.
        levelsView.setHasFixedSize(true);

        // Use linearLayout default vertical
        levelsView.setLayoutManager(new LinearLayoutManager(this));

        levelsView.setAdapter(new TmpAdapter());

    }
}

class TmpAdapter extends RecyclerView.Adapter<TmpViewHolder> {

    @Override
    public TmpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView text = new TextView(parent.getContext());
        return new TmpViewHolder(text);
    }

    @Override
    public void onBindViewHolder(TmpViewHolder holder, int position) {

        holder.getMyItemView().setText("PROVA " + position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}

class TmpViewHolder extends RecyclerView.ViewHolder {

    public TmpViewHolder(View itemView) {
        super(itemView);
    }

    public TextView getMyItemView() {
        return (TextView) itemView;
    }
}
