package eu.unipv.epsilon.enigma;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.template.CandidateClassSource;
import eu.unipv.epsilon.enigma.template.TemplateRegistry;
import eu.unipv.epsilon.enigma.template.TemplateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.*;

/**
 * Organizes access to a stack of {@link CollectionsPool} layers and provides an URL scheme to access the content
 * of containers held in them, registered on construction.
 */
public class GameAssetsSystem {

    // TODO: 'templateServer' can be... safely moved out of this class

    private static final Logger LOG = LoggerFactory.getLogger(GameAssetsSystem.class);

    private TemplateServer templateServer = null;
    private List<CollectionsPool> sources;

    public GameAssetsSystem() {
        // Linked list is a better than ArrayList in this case, since we only do not-random traversals
        sources = new LinkedList<>();
    }

    public GameAssetsSystem(CollectionsPool... sources) {
        for (CollectionsPool source : sources)
            if (source == null)
                throw new IllegalArgumentException("Source may not be null");

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

    public SortedSet<String> getAvailableCollectionIDs() {
        SortedSet<String> ids = new TreeSet<>();
        for (CollectionsPool source : sources)
            ids.addAll(source.getStoredCollectionIDs());

        return ids;
    }

    public boolean containsCollection(String id) {
        for (CollectionsPool source : sources) {
            if (source.containsCollection(id))
                return true;
        }

        return false;
    }

    public CollectionContainer getCollectionContainer(String id) {
        for (CollectionsPool source : sources)
            if (source.containsCollection(id))
                return source.getCollectionContainer(id);

        return null;
    }











    // TODO: WE WANT ALL OF THE FOLLOWING somewhere else

    private MyUrlHandlerFactory streamHandlerFactory;

    public MyUrlHandlerFactory getStreamHandlerFactory() {
        return streamHandlerFactory;
    }

    public static class MyUrlHandlerFactory implements URLStreamHandlerFactory {

        private LevelAssetsURLStreamHandler assetsUrlHandler;
        private ClasspathURLStreamHandler classpathUrlHandler;

        public MyUrlHandlerFactory(GameAssetsSystem assetsSystem) {
            assetsUrlHandler = new LevelAssetsURLStreamHandler(assetsSystem);
            classpathUrlHandler = new ClasspathURLStreamHandler();
        }

        public void setResourcesClassLoader(ClassLoader classLoader) {
            classpathUrlHandler.setClassLoader(classLoader);
        }

        @Override
        public URLStreamHandler createURLStreamHandler(String protocol) {
            if (protocol.equalsIgnoreCase(LevelAssetsURLStreamHandler.PROTOCOL_NAME))
                return assetsUrlHandler;
            if (protocol.equalsIgnoreCase(ClasspathURLStreamHandler.PROTOCOL_NAME))
                return classpathUrlHandler;
            return null;
        }
    }

    private void registerURLStreamHandlers() {
        Field factory;

        try {
            factory = URL.class.getDeclaredField("factory");
        } catch (NoSuchFieldException e) {
            LOG.info("Cannot find \"factory\" field while registering URLStreamHandler," +
                    "maybe we have a different implementation, trying with \"streamHandlerFactory\"", e);
            try {
                factory = URL.class.getDeclaredField("streamHandlerFactory");
            } catch (NoSuchFieldException e1) {
                LOG.error("\"streamHandlerFactory\" field not found", e1);

                // Critical
                throw new NoSuchFieldError("Cannot find stream handler factory field");
            }
        }

        factory.setAccessible(true);

        try {
            // Clear if already registered with another AssetsSystem / URLStreamHandler
            factory.set(null, null);

            // Register stream handler
            streamHandlerFactory = new MyUrlHandlerFactory(this);
            URL.setURLStreamHandlerFactory(streamHandlerFactory);

        } catch (IllegalAccessException e) {
            LOG.error("Unable to cleanup URLStreamHandlerFactory before registering", e);
        }
    }

}
