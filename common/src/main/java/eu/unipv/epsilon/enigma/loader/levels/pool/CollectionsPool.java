package eu.unipv.epsilon.enigma.loader.levels.pool;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;

import java.util.Set;

/**
 * A repository (pool) of Quest {@link CollectionContainer}s,
 * each container is identified by a string.
 */
public interface CollectionsPool {

    /**
     * Obtains a {@link CollectionContainer}, if available, from this pool.
     *
     * Stream or file based implementers can allocate resources in order to build returned container classes;
     * so if you only want to check for existence, you may want to use {@link #containsCollection(String)}
     * instead to avoid allocating unneeded resources.
     *
     * @param id The collection identifier to lookup
     * @return The {@link CollectionContainer} if found, {@code null} otherwise
     */
    CollectionContainer getCollectionContainer(String id);

    /**
     * Checks if a collection exists in this pool.
     *
     * @param id The collection identifier to lookup
     * @return {@code true} if a collection with the given identifier was found
     */
    boolean containsCollection(String id);

    /**
     * Returns a set of all collection IDs available in this pool.
     *
     * @return A set of available collection IDs
     */
    Set<String> getStoredCollectionIDs();

}
