package eu.unipv.epsilon.enigma.status;

import android.webkit.JavascriptInterface;
import eu.unipv.epsilon.enigma.quest.status.QuestViewInterface;

import java.io.Serializable;

/**
 * A {@link QuestViewInterface} designed to work with the Android preferences system
 * and with the Android WebView, requiring {@link JavascriptInterface} annotated API methods.
 */
public class AndroidQuestViewInterface implements QuestViewInterface, Serializable {

    // TODO: This can be simply an interface extending QuestViewInterface but with annotated methods,
    //       implementation can then be an anonymous class of QuestCollectionStatus, possible?!

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
