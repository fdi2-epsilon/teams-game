package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLConnection;
import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * Instrumentation used by {@link PackageScanner} to get appropriate scanning algorithms.
 */
public class PackageScanTools {

    private static final Logger LOG = LoggerFactory.getLogger(PackageScanTools.class);
    private final ClassLoader classLoader;
    private final boolean local;

    /** Constructs a scanning tools instance with the given class loader and settings. */
    public PackageScanTools(ClassLoader classLoader, boolean local) {
        this.classLoader = classLoader;
        this.local = local;
    }

    /**
     * Obtains an appropriate scanning algorithm given the type of the passed in URL.
     *
     * @param url The URL for which a scanner is needed
     * @return an appropriate {@link ScanAlgorithm} instance for this URL type
     * @throws ClassNotFoundException if there is no known implementation of {@link ScanAlgorithm} for this URL type
     * @throws IOException if it was not possible to open a connection to the URL or decode its path
     */
    public ScanAlgorithm getUrlScanner(URL url) throws ClassNotFoundException, IOException {
        URLConnection connection = url.openConnection();

        if (connection instanceof JarURLConnection)
            return new ArchiveScanAlgorithm(classLoader, ((JarURLConnection) connection).getJarFile());
        else if (connection instanceof FileURLConnection)
            return new DirectoryScanAlgorithm(classLoader, new File(URLDecoder.decode(url.getPath(), "UTF-8")));
        else if (connection instanceof LevelAssetsURLConnection)
            return new LevelAssetsScanAlgorithm(classLoader, ((LevelAssetsURLConnection) connection).getContainer());
        else
            throw new ClassNotFoundException("Cannot get archive scanner for URL \"" + url.getPath() + "\"");
    }

    /**
     * Calls the appropriate class loader resource retrieval method by checking the status of the {@link #local} flag.
     *
     * - If {@link #local} is set to false, {@link ClassLoader#getResources(String)} is used;
     * - Otherwise {@link ClassLoader#findResources(String)} is access-transformed and then called.
     *
     * @param packagePath The resource name
     * @return an {@link IterableEnumeration} of found resources URLs
     * @throws IOException if a class loader I/O error occurs
     */
    public Iterable<URL> getResources(String packagePath) throws IOException {

        // If not "local", use standard behavior with "getResources", walking up to parent class loaders
        if (!local)
            return IterableEnumeration.make(classLoader.getResources(packagePath));

        // Else, we need to call the internal method "findResources", which is protected
        try {
            Method m = ClassLoader.class.getDeclaredMethod("findResources", String.class);
            m.setAccessible(true);

            @SuppressWarnings("unchecked")
            Enumeration<URL> resources = (Enumeration<URL>) m.invoke(classLoader, packagePath);
            return IterableEnumeration.make(resources);

        } catch (ReflectiveOperationException e) {
            LOG.error("Cannot get resources in a not recursively way", e);
            // Throw InvalidStateException because in this context, "local = true" is not allowed
            throw new IllegalStateException("Local-only scan not allowed in this context");
        }
    }

}
