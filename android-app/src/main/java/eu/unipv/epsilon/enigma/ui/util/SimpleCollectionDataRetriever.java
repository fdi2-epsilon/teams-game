package eu.unipv.epsilon.enigma.ui.util;

import eu.unipv.epsilon.enigma.EnigmaApplication;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.status.QuestCollectionStatus;

import java.io.IOException;

/**
 * Utility class to get metadata and progression status for a quest collection.
 *
 * Useful in activities to get data from a collection id passed through intents in an easy.
 */
public class SimpleCollectionDataRetriever {

    private final EnigmaApplication application;
    private final String collectionId;

    public SimpleCollectionDataRetriever(EnigmaApplication application, String collectionId) {
        this.application = application;
        this.collectionId = collectionId;
    }

    public QuestCollection getCollection() {
        try {
            return application.getAssetsSystem().getCollectionContainer(collectionId).getCollectionMeta();
        } catch (IOException e) {
            // IllegalArgumentException because the passed intent argument is not valid.
            throw new IllegalArgumentException("Collection with id \"" + collectionId + "\" cannot be loaded", e);
        }
    }

    public QuestCollectionStatus getCollectionStatus() {
        return application.getGameStatus().getCollectionStatus(collectionId);
    }

}
