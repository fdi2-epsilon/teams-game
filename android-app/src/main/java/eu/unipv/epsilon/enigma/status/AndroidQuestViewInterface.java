package eu.unipv.epsilon.enigma.status;

import android.webkit.JavascriptInterface;
import eu.unipv.epsilon.enigma.quest.status.QuestViewInterface;

/**
 * A {@link QuestViewInterface} designed to work with the Android preferences system
 * and with the Android WebView, requiring {@link JavascriptInterface} annotated API methods.
 */
public class AndroidQuestViewInterface implements QuestViewInterface {

    private final QuestCollectionStatus collectionStatus;
    private final int questIndex;
    private boolean hasDataChanged;

    public AndroidQuestViewInterface(QuestCollectionStatus collectionStatus, int questIndex) {
        this.collectionStatus = collectionStatus;
        this.questIndex = questIndex;
    }

    @Override
    @JavascriptInterface
    public void setComplete() {
        collectionStatus.setSolved(questIndex);
        hasDataChanged = true;
    }

    @Override
    @JavascriptInterface
    public boolean getComplete() {
        return collectionStatus.isSolved(questIndex);
    }

    public void flushData() {
        if (hasDataChanged)
            collectionStatus.flush();
    }

}
