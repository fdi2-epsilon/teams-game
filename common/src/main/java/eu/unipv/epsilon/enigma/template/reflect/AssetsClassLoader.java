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
import java.util.NoSuchElementException;
import java.util.WeakHashMap;

// We could have passed the CollectionContainer directly, but this approach
// uses the system cache and avoids changing client code on internal API changes.

public class AssetsClassLoader extends ClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(AssetsClassLoader.class);

    private WeakHashMap<String, Class> cache = new WeakHashMap<>();
    private GameAssetsSystem assetsSystem;
    private String collectionId;

    public AssetsClassLoader(GameAssetsSystem assetsSystem, String collectionId) {
        super(AssetsClassLoader.class.getClassLoader());
        this.assetsSystem = assetsSystem;
        this.collectionId = collectionId;
    }

    @Override
    public Class findClass(String className) throws ClassNotFoundException {
        logger.info("Trying to lookup class \"{}\" in collection \"{}\"", className, collectionId);

        if (cache.containsKey(className)) return cache.get(className);

        try {
            CollectionContainer eqc = assetsSystem.getCollectionContainer(collectionId);
            ContainerEntry entry = eqc.getEntry("classes/" + className.replace('.', '/') + ".class");
            InputStream is = entry.getStream();

            // If size not known use this
            /*ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nRead;
            while ((nRead = is.read()) != -1) byteStream.write(nRead);
            classBytes = byteStream.toByteArray();*/

            byte[] classBytes = new byte[(int) entry.getSize()];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(classBytes);

            Class result = defineClass(className, classBytes, 0, classBytes.length, null);
            cache.put(className, result);
            return result;

        } catch (IOException | NullPointerException e) {
            // NPEs caused by collections not found
            throw new ClassNotFoundException("Could not find class: " + className, e);
        }
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        final URL url = LevelAssetsURLStreamHandler.createURL(collectionId, "classes/" + name);

        return new Enumeration<URL>() {
            private boolean done = false;
            public boolean hasMoreElements() { return !done; }
            public URL nextElement() {
                if (!hasMoreElements()) throw new NoSuchElementException();
                done = true;
                return url;
            }
        };

    }

}
