package eu.unipv.epsilon.enigma.template.reflect;

import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @see <a href="http://stackoverflow.com/a/14002663">Original implementation</a>
 */
public class PackageScanner {

    private static final String CLASS_FILE_EXT = ".class";

    public static LinkedList<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException {
        return getClassesInPackage(Thread.currentThread().getContextClassLoader(), packageName);
    }

    /**
     * Attempts to list all the classes in the specified package as determined by the context class loader.
     *
     * @param packageName The name of the package to search classes in
     * @param classLoader The class loader used to find resources
     * @return a list of classes found within the given package
     * @throws ClassNotFoundException if there was a problem while searching for classes
     */
    public static LinkedList<Class<?>> getClassesInPackage(final ClassLoader classLoader, String packageName) throws ClassNotFoundException {
        final LinkedList<Class<?>> classes = new LinkedList<>();
        if (classLoader == null) throw new ClassNotFoundException("Cannot get the context class loader");
        try {
            String packagePath = packageName.replace('.', '/');
            Iterable<URL> resources = IterableEnumeration.make(classLoader.getResources(packagePath));
            for (URL url : resources) scanResource(url, packageName, classes);
        } catch (NullPointerException | UnsupportedEncodingException e) {
            throw new ClassNotFoundException("Invalid package \"" + packageName + '"', e);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to get all resources for " + packageName, e);
        }

        return classes;
    }

    private static void scanResource(URL url, String packageName, List<Class<?>> classes) throws ClassNotFoundException, IOException {
        URLConnection connection = url.openConnection();

        if (connection instanceof JarURLConnection)
            scanArchive(((JarURLConnection) connection).getJarFile(), packageName, classes);
        else if (connection instanceof FileURLConnection)
            scanDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), packageName, classes);
        else
            throw new ClassNotFoundException(packageName + " (" + url.getPath() + ") location unknown");
    }

    /**
     * Zip file crawler local utility.
     *
     * @param zip the Jar / Zip file to scan
     * @param packageName the package name used to find {@link JarEntry jar entries} and create {@link Class} objects
     * @param classes the list to add found classes to
     * @throws ClassNotFoundException if a class file in the Jar file could not be loaded
     */
    private static void scanArchive(ZipFile zip, String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        for (ZipEntry entry : IterableEnumeration.make(zip.entries())) {
            String name = entry.getName();
            if (name.endsWith(CLASS_FILE_EXT)) {
                name = stripClassExtension(name).replace('/', '.');
                if (name.startsWith(packageName)) classes.add(Class.forName(name));
            }
        }
    }

    /**
     * Directory crawler local utility.
     *
     * @param directory the directory containing the package on disk
     * @param packageName the package name used to create {@link Class} objects
     * @param classes the list to add found classes to
     * @throws ClassNotFoundException if a class file in the directory could not be loaded
     */
    private static void scanDirectory(File directory, String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        if (!directory.isDirectory()) return;
        final String[] files = directory.list();
        File innerDir;

        for (String file : files) {
            if (file.endsWith(CLASS_FILE_EXT)) {
                try {
                    classes.add(Class.forName(packageName + '.' + stripClassExtension(file)));
                } catch (NoClassDefFoundError e) {
                    // Do nothing, this class hasn't been found by the loader, and we don't care.
                }
            } else if ((innerDir = new File(directory, file)).isDirectory())
                scanDirectory(innerDir, packageName + '.' + file, classes);
        }
    }

    private static String stripClassExtension(String filePath) {
        return filePath.substring(0, filePath.length() - CLASS_FILE_EXT.length());
    }

}
