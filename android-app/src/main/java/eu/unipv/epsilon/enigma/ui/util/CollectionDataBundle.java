package eu.unipv.epsilon.enigma.ui.util;

import eu.unipv.epsilon.enigma.EnigmaApplication;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;

import java.io.IOException;

/**
 * Utility class to keep metadata and progression status for a quest collection.
 *
 * Useful in activities to get data from a collection id passed through intents in an easy way.
 */
public class CollectionDataBundle {

    private final QuestCollection collection;
    private final QuestCollectionStatus collectionStatus;

    public CollectionDataBundle(QuestCollection collection, QuestCollectionStatus collectionStatus) {
        this.collection = collection;
        this.collectionStatus = collectionStatus;
    }

    public QuestCollection getCollection() {
        return collection;
    }

    public QuestCollectionStatus getCollectionStatus() {
        return collectionStatus;
    }

    public static CollectionDataBundle fromId(EnigmaApplication application, String collectionId) {
        try {
            QuestCollection collection =
                    application.getCollectionsPool().getCollectionContainer(collectionId).getCollectionMeta();
            QuestCollectionStatus collectionStatus =
                    application.getGameStatus().getCollectionStatus(collectionId);

            return new CollectionDataBundle(collection, collectionStatus);
        } catch (IOException e) {
            // IllegalArgumentException because the passed intent argument is not valid.
            throw new IllegalArgumentException("Collection with id \"" + collectionId + "\" cannot be loaded", e);
        }
    }

}
