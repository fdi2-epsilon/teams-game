package eu.unipv.epsilon.enigma;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.template.CandidateClassSource;
import eu.unipv.epsilon.enigma.template.TemplateRegistry;
import eu.unipv.epsilon.enigma.template.TemplateServer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;

public class GameAssetsSystem {

    TemplateServer templateServer = null;
    LinkedList<CollectionsPool> sources; // Linked list is a better than ArrayList in this case

    public GameAssetsSystem() {
        sources = new LinkedList<>();
    }

    public GameAssetsSystem(CollectionsPool... sources) {
        for (CollectionsPool source : sources)
            if (source == null) throw new IllegalArgumentException("Source may not be null");

        this.sources = new LinkedList<>(Arrays.asList(sources));
    }

    public void createTemplateServer(CandidateClassSource classSource) {
        this.templateServer = new TemplateServer(new TemplateRegistry(classSource));
    }

    public TemplateServer getTemplateServer() {
        return templateServer;
    }

    public void addCollectionsPool(CollectionsPool pool) {
        sources.add(pool);
    }

    public LevelAssetsURLStreamHandler getURLStreamHandler() {
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

    public CollectionContainer getCollectionContainer(String id) {
        for (CollectionsPool source : sources)
            if (source.containsCollection(id)) return source.getCollectionContainer(id);
        return null;
    }

}
