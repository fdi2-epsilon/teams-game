package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;

/**
 * Utility facade to class retrieval algorithm given its containing package.
 */
public class PackageScanner {

    /**
     * Attempts to list all the classes in the specified package as determined by the context class loader.
     *
     * @param packageName The name of the package to search classes in
     * @return a list of classes found within the given package
     * @throws ClassNotFoundException
     */
    public static LinkedList<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) throw new ClassNotFoundException("Cannot get the context class loader");

        return getClassesInPackage(packageName, contextClassLoader, false);
    }

    /**
     * Attempts to list all the classes in the specified package as determined by the given class loader.
     *
     * Disabling recursive scans (i.e. walk through the class loader hierarchy) requires security permissions
     * to access-transform {@link ClassLoader#findResources(String)} to {@code public}.
     *
     * @param packageName The name of the package to search classes in
     * @param classLoader The class loader used to find resources and load classes
     * @param local If the scan should not be extended also to the parent class loaders, false by default
     * @return a list of classes found within the given package
     * @throws ClassNotFoundException if there was a problem while searching for classes
     */
    public static LinkedList<Class<?>> getClassesInPackage(
            String packageName, ClassLoader classLoader, boolean local) throws ClassNotFoundException {

        if (classLoader == null)
            throw new IllegalArgumentException("classLoader should not be null");

        PackageScanTools scanTools = new PackageScanTools(classLoader, local);
        String packagePath = packageName.replace('.', '/');

        try {
            final LinkedList<Class<?>> classes = new LinkedList<>();
            for (URL url : scanTools.getResources(packagePath))
                classes.addAll(scanTools.getUrlScanner(url).scan(packageName));
            return classes;

        } catch (NullPointerException | UnsupportedEncodingException e) {
            throw new ClassNotFoundException("Invalid package \"" + packageName + '"', e);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to get all resources for " + packageName, e);
        }
    }

}
