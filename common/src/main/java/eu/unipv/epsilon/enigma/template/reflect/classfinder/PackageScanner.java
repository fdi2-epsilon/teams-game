package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;

/**
 * @see <a href="http://stackoverflow.com/a/14002663">Original implementation</a>
 */
public class PackageScanner {

    public static LinkedList<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) throw new ClassNotFoundException("Cannot get the context class loader");

        return getClassesInPackage(contextClassLoader, packageName);
    }

    /**
     * Attempts to list all the classes in the specified package as determined by the context class loader.
     *
     * @param packageName The name of the package to search classes in
     * @param classLoader The class loader used to find resources and load classes
     * @return a list of classes found within the given package
     * @throws ClassNotFoundException if there was a problem while searching for classes
     */
    public static LinkedList<Class<?>> getClassesInPackage(ClassLoader classLoader, String packageName) throws ClassNotFoundException {
        assert classLoader != null;

        ScanAlgorithmFactory sf = new ScanAlgorithmFactory(classLoader);
        String packagePath = packageName.replace('.', '/');

        try {
            Iterable<URL> resources = IterableEnumeration.make(classLoader.getResources(packagePath));

            final LinkedList<Class<?>> classes = new LinkedList<>();
            for (URL url : resources)
                classes.addAll(sf.getUrlScanner(url).scan(packageName));
            return classes;

        } catch (NullPointerException | UnsupportedEncodingException e) {
            throw new ClassNotFoundException("Invalid package \"" + packageName + '"', e);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to get all resources for " + packageName, e);
        }
    }

}
