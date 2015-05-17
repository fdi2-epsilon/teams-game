package eu.unipv.epsilon.enigma.loader.levels.pool;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.WeakHashMap;

/**
 * A cached {@link CollectionsPool} backed by a {@link WeakHashMap}.
 *
 * Implement this if you expect to do multiple consecutive requests to a
 * resource-allocating {@link CollectionContainer} by accessing it from your pool by ID.
 */
public abstract class CachedCollectionsPool implements CollectionsPool {

    private static final Logger LOG = LoggerFactory.getLogger(CachedCollectionsPool.class);

    private WeakHashMap<String, CollectionContainer> cache = new WeakHashMap<>();

    @Override
    public final CollectionContainer getCollectionContainer(String id) {
        if (cache.containsKey(id) && !cache.get(id).isInvalidated())
            return cache.get(id);

        try {
            CollectionContainer val = createCollectionContainer(id);
            cache.put(id, val);
            return val;
        } catch (IOException e) {
            LOG.warn("The collection container \"" + id + "\" was not found, returning null", e);
            return null;
        }
    }

    /**
     * Creates a new {@link CollectionContainer} if not already in the cache.
     *
     * The implementation should be similar to {@link CollectionsPool#getCollectionContainer(String)} for
     * a non-caching pool, except that here you throw an exception if not found instead of returning {@code null}.
     *
     * @param id The identifier of the collection to build
     * @return The generated collection
     * @throws IOException If not found or if there was an error while loading / generating the collection
     */
    protected abstract CollectionContainer createCollectionContainer(String id) throws IOException;

}
