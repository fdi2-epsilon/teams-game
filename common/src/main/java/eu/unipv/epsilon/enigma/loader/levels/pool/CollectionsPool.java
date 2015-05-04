package eu.unipv.epsilon.enigma.loader.levels.pool;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;

import java.util.TreeSet;

public interface CollectionsPool {

    CollectionContainer getCollectionContainer(String id);

    boolean containsCollection(String id);

    TreeSet<String> getStoredCollectionIDs();

}
