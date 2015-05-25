package eu.unipv.epsilon.enigma.ui.quiz;

import android.graphics.Color;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;

public class QuizStatusTabColorizer implements SlidingTabLayout.TabColorizer {

    public static final int COLOR_AVAILABLE = Color.LTGRAY;
    public static final int COLOR_SOLVED = Color.CYAN;

    private final QuestCollectionStatus collectionStatus;

    public QuizStatusTabColorizer(QuestCollectionStatus collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    @Override
    public int getIndicatorColor(int position) {
        // slidingTabs.invalidate(); should be called to update the strip color in real time
        return collectionStatus.isSolved(position + 1) ? COLOR_SOLVED : COLOR_AVAILABLE;
    }

}
