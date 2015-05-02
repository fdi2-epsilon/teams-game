package eu.unipv.epsilon.enigma;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;

import java.io.IOException;
import java.net.URLStreamHandler;
import java.util.TreeSet;

public class GameAssetsSystem {

    CollectionsPool[] sources;

    public GameAssetsSystem(CollectionsPool... sources) {
        for (CollectionsPool source : sources)
            if (source == null) throw new IllegalArgumentException("Source may not be null");

        this.sources = sources;
    }

    public URLStreamHandler getURLStreamHandler() {
        return new LevelAssetsURLStreamHandler(this);
    }

    public TreeSet<String> getAvailableCollectionIDs() {
        TreeSet<String> ids = new TreeSet<>();
        for (CollectionsPool source : sources) ids.addAll(source.getStoredCollectionIDs());
        return ids;
    }

    public boolean containsCollection(String id) {
        for (CollectionsPool source : sources) if (source.containsCollection(id)) return true;
        return false;
    }

    public CollectionContainer getCollectionContainer(String id) throws IOException {
        for (CollectionsPool source : sources)
            if (source.containsCollection(id)) return source.getCollectionContainer(id);
        throw new IOException("Collection \"" + id + "\" not found.");
    }

}
