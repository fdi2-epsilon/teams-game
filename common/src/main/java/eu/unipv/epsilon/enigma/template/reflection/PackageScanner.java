package eu.unipv.epsilon.enigma.template.reflection;

import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @see <a href="http://stackoverflow.com/a/14002663">Original implementation</a>
 */
public class PackageScanner {

    public static void main(String[] args) throws ClassNotFoundException {
        List<Class<?>> elements = PackageScanner.getClassesInPackage("eu.unipv.epsilon.enigma.loader.levels.protocol");

        for (Class clazz : elements) {
            System.out.println("> " + clazz);
        }
    }

    /**
     * Attempts to list all the classes in the specified package as determined by the context class loader.
     *
     * @param packageName The name of the package to search classes in
     * @return a list of classes found within the given package
     * @throws ClassNotFoundException if there was a problem while searching for classes
     */
    public static LinkedList<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException {
        final LinkedList<Class<?>> classes = new LinkedList<>();

        try {

            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) throw new ClassNotFoundException("Cannot get the context class loader.");

            final Enumeration<URL> resources = cl.getResources(packageName.replace('.', '/'));
            URLConnection connection;

            URL url;
            while (resources.hasMoreElements() && ((url = resources.nextElement()) != null)) {
                try {
                    connection = url.openConnection();

                    if (connection instanceof JarURLConnection)
                        scanArchive(((JarURLConnection) connection).getJarFile(), packageName, classes);
                    else if (connection instanceof FileURLConnection)
                        scanDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), packageName, classes);
                    else
                        throw new ClassNotFoundException(packageName + " (" + url.getPath() + ") does not appear to be a valid package");

                } catch (UnsupportedEncodingException e) {
                    throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Unsupported encoding)", e);
                } catch (IOException e) {
                    throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + packageName, e);
                }
            }

        } catch (NullPointerException e) {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Null pointer exception)", e);
        } catch (IOException e) {
            throw new ClassNotFoundException("IOException was throws when trying to get all resources for " + packageName, e);
        }

        return classes;
    }

    /**
     * Zip file crawler local utility.
     *
     * @param zip the Jar / Zip file to scan
     * @param packageName the package name used to find {@link JarEntry jar entries} and create {@link Class} objects
     * @param classes the list to add found classes to
     * @throws ClassNotFoundException if a class file in the Jar file could not be loaded
     */
    private static void scanArchive(ZipFile zip, String packageName, List<Class<?>> classes)
            throws ClassNotFoundException {

        final Enumeration<? extends ZipEntry> entries = zip.entries();

        for (ZipEntry jarEntry = null; entries.hasMoreElements() && ((jarEntry = entries.nextElement()) != null); ) {
            String name = jarEntry.getName();

            if (name.contains(".class")) {
                name = name.substring(0, name.length() - 6).replace('/', '.');
                if (name.contains(packageName)) classes.add(Class.forName(name));
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
    private static void scanDirectory(File directory, String packageName, List<Class<?>> classes)
            throws ClassNotFoundException {

        if (directory.isDirectory()) {
            final String[] files = directory.list();
            File innerDir;

            for (final String file : files) {
                if (file.endsWith(".class")) {
                    try {
                        Class clazz = Class.forName(packageName + '.' + file.substring(0, file.length() - 6));
                        classes.add(clazz);
                    } catch (NoClassDefFoundError e) {
                        // Do nothing, this class hasn't been found by the loader, and we don't care.
                    }
                } else if ((innerDir = new File(directory, file)).isDirectory())
                    scanDirectory(innerDir, packageName + '.' + file, classes);
            }
        }
    }

}
