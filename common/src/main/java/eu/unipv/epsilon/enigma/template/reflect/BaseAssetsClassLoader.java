package eu.unipv.epsilon.enigma.template.reflect;

import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Base for VM dependent (JVM / DVM) collection container (level assets) class loaders.
 */
public abstract class BaseAssetsClassLoader extends ClassLoader {

    public static final String EQC_CLASS_RESOURCES_PATH = "classes/";

    protected final String collectionId;

    public BaseAssetsClassLoader(ClassLoader parent, String collectionId) {
        super(parent);
        this.collectionId = collectionId;
    }

    @Override
    protected abstract Class<?> findClass(String name) throws ClassNotFoundException;

    @Override
    protected URL findResource(String name) {
        return LevelAssetsURLStreamHandler.createURL(collectionId, EQC_CLASS_RESOURCES_PATH + name);
    }

    // This is used by class-finding utilities to get a link to a folder/package to scan
    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        final URL url = findResource(name);

        // Return an enumeration of only one element
        return new Enumeration<URL>() {
            private boolean done = false;

            @Override
            public boolean hasMoreElements() {
                return !done;
            }

            @Override
            public URL nextElement() {
                if (!hasMoreElements())
                    throw new NoSuchElementException();
                done = true;
                return url;
            }
        };
    }

}
