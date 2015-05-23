package eu.unipv.epsilon.enigma.status;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class QuestCollectionStatus implements Serializable {

    public static final String STATUS_KEY_PREFIX = "gamestatus_";

    /* Quest index inside collection stored as strings to avoid
       useless conversions with the Android's SharedPreferences system. */

    // HashSet<> maintains this fully serializable
    private HashSet<String> solvedQuests; //NOSONAR

    private final SharedPreferences statusStore;
    private final String collectionId;

    public QuestCollectionStatus(SharedPreferences statusStore, String collectionId) {
        this.statusStore = statusStore;
        this.collectionId = collectionId;

        // Cast "safe" because Android uses HashSets
        solvedQuests = (HashSet<String>) statusStore.getStringSet(getStoreKey(), null);

        if (solvedQuests == null) {
            // Create a new set; since 'defValues' in 'getStringSet' is not a by-name-parameter,
            // I prefer to create a new HashSet ONLY if we had no stored data.
            solvedQuests = new HashSet<>();
        }
    }

    public Set<String> getSolvedQuests() {
        return solvedQuests;
    }

    public boolean isSolved(int questIndex) {
        return solvedQuests.contains(String.valueOf(questIndex));
    }

    public void setSolved(int questIndex) {
        solvedQuests.add(String.valueOf(questIndex));
    }

    /**
     * Called by the view code to get an interface to alter quest-specific status.
     *
     * This does not take advantage of polymorphism and return directly AndroidQuestViewInterface instead of
     * QuestViewInterface, this is because only the Android version has the required Android-dependant annotations.
     */
    public AndroidQuestViewInterface getQuestViewInterface(int questIndex) {
        return new AndroidQuestViewInterface(this, questIndex);
    }

    /** Stores the actual progress in this collection to disk. */
    public void flush() {
        storeData(solvedQuests);
    }

    /** Wipes any permanently stored data regarding this collection status. */
    public void clean() {
        storeData(null);
    }

    private void storeData(Set<String> data) {
        // data == 'null' is equivalent to remove the key from the store, see 'putStringSet' for reference
        statusStore.edit()
                .putStringSet(getStoreKey(), data)
                .apply();
    }

    public String getStoreKey() {
        return STATUS_KEY_PREFIX + collectionId;
    }

}
