package eu.unipv.epsilon.enigma.loader.levels.pool;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;

import java.util.*;

/** Organizes access to a stack of {@link CollectionsPool} layers. */
public class MergedPool implements CollectionsPool {

    private List<CollectionsPool> sources;

    public MergedPool() {
        // Linked list is a better than ArrayList in this case, since we only do not-random traversals
        sources = new LinkedList<>();
    }

    public MergedPool(CollectionsPool... sources) {
        for (CollectionsPool source : sources)
            if (source == null)
                throw new IllegalArgumentException("Source may not be null");

        this.sources = new LinkedList<>(Arrays.asList(sources));
    }

    public void addSource(CollectionsPool pool) {
        sources.add(pool);
    }

    @Override
    public Set<String> getStoredCollectionIDs() {
        Set<String> ids = new TreeSet<>();
        for (CollectionsPool source : sources)
            ids.addAll(source.getStoredCollectionIDs());

        return ids;
    }

    @Override
    public boolean containsCollection(String id) {
        for (CollectionsPool source : sources) {
            if (source.containsCollection(id))
                return true;
        }

        return false;
    }

    @Override
    public CollectionContainer getCollectionContainer(String id) {
        for (CollectionsPool source : sources)
            if (source.containsCollection(id))
                return source.getCollectionContainer(id);

        return null;
    }

}
