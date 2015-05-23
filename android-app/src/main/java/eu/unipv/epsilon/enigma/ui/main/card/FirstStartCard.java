package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.ui.main.CollectionsViewAdapter;

public class FirstStartCard extends CardHolder {

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.main_card_first_start;

    private final CollectionsViewAdapter viewAdapter;
    protected Button okButtonRef;

    public FirstStartCard(ViewGroup parent, CollectionsViewAdapter viewAdapter) {
        super(parent, LAYOUT_RESOURCE, true);

        this.viewAdapter = viewAdapter;
        okButtonRef = (Button) itemView.findViewById(R.id.ok_button);

        okButtonRef.setOnClickListener(new ClickListener());
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            viewAdapter.setFirstStartCardVisible(false);
        }
    }

}
