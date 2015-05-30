package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import dalvik.system.DexClassLoader;
import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.ContainerEntry;
import eu.unipv.epsilon.enigma.template.reflect.BaseAssetsClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Dalvik class loader for classes inside {@link CollectionContainer}s.
 */
public class DexAssetsClassLoader extends BaseAssetsClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(DexAssetsClassLoader.class);

    public static final String CLASSES_DEX_NAME = "classes.dex";
    public static final String CLASSES_DEX_OPTIMIZED_NAME = "classes.odex";

    private Map<String, Class> cache = new WeakHashMap<>();

    /** Extracted dex cache directory */
    private File cacheDir;

    /** Actual class loader from optimized dex */
    private ClassLoader dexClassLoader;

    // Set to true if this collection container contains class files
    private boolean hasClasses = true;

    public DexAssetsClassLoader(Context context, GameAssetsSystem assetsSystem, String collectionId) {
        super(context.getClassLoader(), collectionId);

        try {
            setupClassLoader(context, assetsSystem);
        } catch (IOException e) {
            LOG.info("Cannot initialize class loading, treating collection as classless", e);
            hasClasses = false;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        LOG.info("Trying to lookup class \"{}\" in collection \"{}\"", name, collectionId);

        // Return a cached class if exists
        if (cache.containsKey(name))
            return cache.get(name);

        if (!hasClasses)
            throw new ClassNotFoundException("This collection does not contain classes at all!");

        Class<?> clazz = dexClassLoader.loadClass(name);
        cache.put(name, clazz);
        return clazz;
    }

    public String getDexPath() {
        return new File(cacheDir, CLASSES_DEX_NAME).getPath();
    }

    public boolean hasClasses() {
        return hasClasses;
    }

    // Does class loader initialization
    private void setupClassLoader(Context context, GameAssetsSystem assetsSystem) throws IOException {
        // Find the "classes.dex" stream from assets
        CollectionContainer cc = assetsSystem.getCollectionContainer(collectionId);
        ContainerEntry entry = cc.getEntry(CLASSES_DEX_NAME);
        if (entry == null)
            throw new IOException("This collection does not contain classes");

        // Collection has classes, create a cache dir...
        cacheDir = new File(String.format("%s/code-cache/%s", context.getCacheDir().getPath(), collectionId));
        validateDirectory(cacheDir);

        // ...then extract the dex file and optimize it
        File dexFile = extractDex(entry);
        dexClassLoader = makeOptimizedDex(context, dexFile);
    }

    // Generates an optimized dex file and returns the class loader used to generate it
    private ClassLoader makeOptimizedDex(Context context, File inputDex) throws IOException {
        /* PathClassLoader tries to write caches in system area, which is protected.
         * So we have to use DexClassLoader with a custom caching path but it complains if input
         * file does not have a DEX, JAR, APK or ZIP extension so we have to extract "classes.dex"
         * More info at: http://stackoverflow.com/a/22889113 */

        // Create the optimized output directory
        File dexOptDir = new File(cacheDir, "opt");
        validateDirectory(dexOptDir);

        final ClassLoader resHandler = this;

        // Use a DexClassLoader to generate an optimized dex
        ClassLoader cl = new DexClassLoader(inputDex.getPath(), dexOptDir.getPath(), null, context.getClassLoader()) {

            @Override
            protected URL findResource(String name) {
                return resHandler.getResource(name);
            }

            @Override
            protected Enumeration<URL> findResources(String name) {
                try {
                    return resHandler.getResources(name);
                } catch (IOException e) {
                    LOG.error("Cannot lookup resource " + name);
                    return null;
                }
            }

        };

        // Move the optimized generated file (./opt/classes.dex -> ./classes.odex) to make new DexFile not complain.
        File dexOptFile = new File(dexOptDir, CLASSES_DEX_NAME);
        if (!dexOptFile.renameTo(new File(cacheDir, CLASSES_DEX_OPTIMIZED_NAME)))
            throw new IOException("Failed to move optimized dex to odex file");

        // Return the used class loader
        return cl;
    }

    // Extracts a dex file from a container entry, this method does not ensure that the entry is correct
    private File extractDex(ContainerEntry entry) throws IOException {
        // Extract "classes.dex" to cache directory
        File dexFile = new File(cacheDir, CLASSES_DEX_NAME);
        writeStream(entry.getStream(), dexFile);
        return dexFile;
    }

    // Utility: writes an InputStream to file
    private static void writeStream(InputStream stream, File destination) throws IOException {
        OutputStream out = new FileOutputStream(destination);
        byte[] buffer = new byte[8192];
        int len;
        while ((len = stream.read(buffer)) != -1)
            out.write(buffer, 0, len);
        out.close();
    }

    // Utility: creates directories if they don't exist
    private void validateDirectory(File directory) throws IOException {
        // Try to create the directory if it does not exist (mkdirs short-circuited if isDirectory);
        // isDirectory() implies exists() so this fails as intended if a file with the same name exists.
        if (!directory.isDirectory() && !directory.mkdirs())
            throw new IOException("Corrupted or inaccessible directory: " + directory.getPath());
    }

}
