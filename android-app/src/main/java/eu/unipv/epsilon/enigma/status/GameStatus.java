package eu.unipv.epsilon.enigma.status;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Caching provider for game progression status.
 */
public class GameStatus {

    private final SharedPreferences statusStore;
    private final Map<String, QuestCollectionStatus> cache = new WeakHashMap<>();

    public GameStatus(SharedPreferences statusStore) {
        this.statusStore = statusStore;
    }

    public QuestCollectionStatus getCollectionStatus(String collectionId) {
        if (cache.containsKey(collectionId))
            return cache.get(collectionId);

        QuestCollectionStatus status = new QuestCollectionStatus(statusStore, collectionId);
        cache.put(collectionId, status);
        return status;
    }

}
