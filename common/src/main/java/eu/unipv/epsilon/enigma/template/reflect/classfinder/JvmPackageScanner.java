package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility {@link PackageScanner} to find classes filtering them by their containing package.
 * Intended to run on the Java Virtual Machine (does not work on Android)
 */
public class JvmPackageScanner implements PackageScanner {

    @Override
    public List<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null)
            throw new ClassNotFoundException("Cannot get the context class loader");

        return getClassesInPackage(packageName, contextClassLoader, false);
    }

    @Override
    public List<Class<?>> getClassesInPackage(
            String packageName, ClassLoader classLoader, boolean local) throws ClassNotFoundException {

        if (classLoader == null)
            throw new IllegalArgumentException("classLoader should not be null");

        PackageScanTools scanTools = new PackageScanTools(classLoader, local);
        String packagePath = packageName.replace('.', '/');

        try {
            final List<Class<?>> classes = new LinkedList<>();
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
