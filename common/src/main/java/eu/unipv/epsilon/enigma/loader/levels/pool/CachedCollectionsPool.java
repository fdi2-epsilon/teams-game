package eu.unipv.epsilon.enigma.loader.levels.pool;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;

import java.io.IOException;
import java.util.WeakHashMap;

public abstract class CachedCollectionsPool implements CollectionsPool {

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
            e.printStackTrace();
            return null;
        }
    }

    protected abstract CollectionContainer createCollectionContainer(String id) throws IOException;

}
