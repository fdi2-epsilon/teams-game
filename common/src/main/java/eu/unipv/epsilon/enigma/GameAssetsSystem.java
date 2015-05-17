package eu.unipv.epsilon.enigma;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.template.CandidateClassSource;
import eu.unipv.epsilon.enigma.template.TemplateRegistry;
import eu.unipv.epsilon.enigma.template.TemplateServer;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
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
            if (source == null){
                throw new IllegalArgumentException("Source may not be null");
            }

        this.sources = new LinkedList<>(Arrays.asList(sources));
        registerURLStreamHandlers();
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

    private void registerURLStreamHandlers() {
        final GameAssetsSystem sys = this;
        Field factory;

        try {
            factory = URL.class.getDeclaredField("factory");
        } catch (NoSuchFieldException e) {
            try {
                factory = URL.class.getDeclaredField("streamHandlerFactory");
            } catch (NoSuchFieldException e1) {
                e.printStackTrace();
                e1.printStackTrace();
                throw new RuntimeException("Cannot find stream handler factory field.", e1);
            }
        }

        factory.setAccessible(true);

        // Clear if already registered with another AssetsSystem / URLStreamHandler
        try {
            factory.set(null, null);

            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    if (protocol.equalsIgnoreCase(LevelAssetsURLStreamHandler.PROTOCOL_NAME))
                        return new LevelAssetsURLStreamHandler(sys);
                    if (protocol.equalsIgnoreCase(ClasspathURLStreamHandler.PROTOCOL_NAME))
                        return new ClasspathURLStreamHandler();
                    return null;
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
