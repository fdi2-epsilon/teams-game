package eu.unipv.epsilon.enigma.loader.levels.protocol;

import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.template.TemplateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/** Handles and registers internal application protocols. */
public class ProtocolManager implements URLStreamHandlerFactory {

    /* This class uses Reflection to give more flexibility to the java.net API,
     * providing a getter and setter for the registered URLStreamHandlerFactory. */

    private static final Logger LOG = LoggerFactory.getLogger(ProtocolManager.class);

    /** The static field in URL class containing the currently registered URLStreamHandler */
    private static final Field urlFactoryField = findUrlFactoryField();

    static {
        urlFactoryField.setAccessible(true);
    }

    private final LevelAssetsURLStreamHandler assetsUrlHandler;
    private final ClasspathURLStreamHandler classpathUrlHandler;

    public ProtocolManager(CollectionsPool questCollections, TemplateServer templateServer) {
        assetsUrlHandler = new LevelAssetsURLStreamHandler(questCollections, templateServer);
        classpathUrlHandler = new ClasspathURLStreamHandler();
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        // Place constant first to avoid NullPointerExceptions
        if (LevelAssetsURLStreamHandler.PROTOCOL_NAME.equalsIgnoreCase(protocol))
            return assetsUrlHandler;
        if (ClasspathURLStreamHandler.PROTOCOL_NAME.equalsIgnoreCase(protocol))
            return classpathUrlHandler;
        return null;
    }

    /** Sets a class loader to be used to load "cp:/" resources. */
    public void setResourcesClassLoader(ClassLoader classLoader) {
        classpathUrlHandler.setClassLoader(classLoader);
    }

    /** Utility to retrieve the currently registered URLStreamHandlerFactory. */
    public static URLStreamHandlerFactory getRegisteredUrlFactory() {
        try {
            return (URLStreamHandlerFactory) urlFactoryField.get(null);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(
                "Cannot get registered URLStreamHandlerFactory, is the field accessible?", e);
        }
    }

    public void registerURLStreamHandlers() {
        try {
            // Cleanup any previously registered URL Stream Handler Factory, otherwise Java will throw a Runtime ERROR
            // and will crash our app. While it's good practice to register a stream handler only once,
            // we don't want to crash if we are re-registering a factory but we only want to log a warning.
            unregisterFactory();

            // Register stream handler
            URL.setURLStreamHandlerFactory(this);
        } catch (IllegalAccessException e) {
            LOG.error("Unable to cleanup URLStreamHandlerFactory before registering", e);
        }
    }

    private void unregisterFactory() throws IllegalAccessException {
        if (urlFactoryField.get(null) != null) {
            LOG.warn("An URL Stream handler was already registered in this context, " +
                "this operation would have throws a RuntimeError in normal conditions!!");
            urlFactoryField.set(null, null);
        }
    }

    private static Field findUrlFactoryField() {
        try {
            return URL.class.getDeclaredField("factory");
        } catch (NoSuchFieldException e) { //NOSONAR
            // Don't log the exception, factoryField is null
        }

        LOG.info("Cannot find \"factory\" field while registering URLStreamHandler," +
            "maybe we have a different implementation, trying with \"streamHandlerFactory\"");

        try {
            return URL.class.getDeclaredField("streamHandlerFactory");
        } catch (NoSuchFieldException e) { //NOSONAR
            // Don't log the exception, factoryField is null
        }

        throw new NoSuchFieldError("Cannot find stream handler factory field");
    }

}
