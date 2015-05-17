package eu.unipv.epsilon.enigma.template.reflect;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.ContainerEntry;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;

/**
 * Class loader for classes inside {@link CollectionContainer}s.
 */
public class AssetsClassLoader extends ClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(AssetsClassLoader.class);
    public static final String EQC_CLASSES_PATH = "classes/";

    private Map<String, Class> cache = new WeakHashMap<>();
    private GameAssetsSystem assetsSystem;
    private String collectionId;

    public AssetsClassLoader(GameAssetsSystem assetsSystem, String collectionId) {
        super(AssetsClassLoader.class.getClassLoader());

        // We could have passed the CollectionContainer directly, but this approach
        // uses the system cache and avoids changing client code on internal API changes.
        this.assetsSystem = assetsSystem;
        this.collectionId = collectionId;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        LOG.info("Trying to lookup class \"{}\" in collection \"{}\"", className, collectionId);

        // Return a cached class if exists
        if (cache.containsKey(className)) return cache.get(className);

        try {
            // Get an input stream for the class resource inside the collection
            CollectionContainer eqc = assetsSystem.getCollectionContainer(collectionId);
            ContainerEntry entry = eqc.getEntry(EQC_CLASSES_PATH + className.replace('.', '/') + ".class");
            InputStream is = entry.getStream();

            // If input size is not known use this instead:
            /*ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nRead;
            while ((nRead = is.read()) != -1) byteStream.write(nRead);
            classBytes = byteStream.toByteArray();*/

            // Read all bytes of the class input stream
            byte[] classBytes = new byte[(int) entry.getSize()];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(classBytes);

            // Define the new class given bytes, put it in cache and return
            Class result = defineClass(className, classBytes, 0, classBytes.length, null);
            cache.put(className, result);
            return result;

        } catch (IOException | NullPointerException e) {
            // NPEs caused by collections not found
            throw new ClassNotFoundException("Could not find class: " + className, e);
        }
    }

    @Override
    protected URL findResource(String name) {
        return LevelAssetsURLStreamHandler.createURL(collectionId, EQC_CLASSES_PATH + name);
    }

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
                if (!hasMoreElements()) throw new NoSuchElementException();
                done = true;
                return url;
            }
        };
    }

}
