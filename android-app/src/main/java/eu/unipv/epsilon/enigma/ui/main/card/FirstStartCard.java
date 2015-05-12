package eu.unipv.epsilon.enigma.ui.main.card;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import eu.unipv.epsilon.enigma.R;

public class FirstStartCard extends CardHolder {

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.main_card_first_start;

    protected Button okButtonRef;

    public FirstStartCard(ViewGroup parent) {
        super(parent, LAYOUT_RESOURCE, true);

        okButtonRef = (Button) itemView.findViewById(R.id.ok_button);
    }

    public void setOkButtonClickListener(View.OnClickListener clickListener) {
        okButtonRef.setOnClickListener(clickListener);
    }

}
