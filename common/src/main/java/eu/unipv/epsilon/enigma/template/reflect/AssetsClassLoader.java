package eu.unipv.epsilon.enigma.template.reflect;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.ContainerEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * JVM class loader for classes inside {@link CollectionContainer}s.
 */
public class AssetsClassLoader extends BaseAssetsClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(AssetsClassLoader.class);

    private Map<String, Class> cache = new WeakHashMap<>();
    private GameAssetsSystem assetsSystem;

    public AssetsClassLoader(GameAssetsSystem assetsSystem, String collectionId) {
        super(AssetsClassLoader.class.getClassLoader(), collectionId);

        // We could have passed the CollectionContainer directly, but this approach
        // uses the system cache and avoids changing client code on internal API changes.
        this.assetsSystem = assetsSystem;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        LOG.info("Trying to lookup class \"{}\" in collection \"{}\"", className, collectionId);

        // Return a cached class if exists
        if (cache.containsKey(className))
            return cache.get(className);

        try {
            // Get an input stream for the class resource inside the collection
            CollectionContainer eqc = assetsSystem.getCollectionContainer(collectionId);
            ContainerEntry entry = eqc.getEntry(EQC_CLASS_RESOURCES_PATH + className.replace('.', '/') + ".class");
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

}
