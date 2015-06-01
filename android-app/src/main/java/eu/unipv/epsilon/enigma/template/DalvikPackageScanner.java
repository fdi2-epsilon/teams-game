package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import dalvik.system.DexFile;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.JvmPackageScanner;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.PackageScanner;
import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * An utility {@link PackageScanner} to find classes given a package in the Dalvik Virtual Machine.
 *
 * @see JvmPackageScanner JVM version
 */
public class DalvikPackageScanner implements PackageScanner {

    private static final Logger LOG = LoggerFactory.getLogger(DalvikPackageScanner.class);

    private final Context context;

    public DalvikPackageScanner(Context context) {
        this.context = context;
    }

    @Override
    public List<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException {
        ClassLoader contextClassLoader = context.getClassLoader();
        if (contextClassLoader == null)
            throw new ClassNotFoundException("Cannot get the context class loader");

        return getClassesInPackage(packageName, contextClassLoader, false);
    }

    @Override
    public List<Class<?>> getClassesInPackage(
            String packageName, ClassLoader classLoader, boolean local) throws ClassNotFoundException {

        Set<String> dexPaths = new HashSet<>();
        // Search for classes in its own dex
        dexPaths.add(getClassLoaderLocalDex(classLoader));

        if (!local) {
            // Not local, so we also search in the application code path if not already in the set
            dexPaths.add(context.getPackageCodePath());
            // May we also add some system dexs?
        }

        return findClassesDexs(packageName, classLoader, dexPaths);
    }

    // Utility: gets the passed class loader's main dex path
    private String getClassLoaderLocalDex(ClassLoader classLoader) {
        return classLoader instanceof DexAssetsClassLoader
                ? ((DexAssetsClassLoader) classLoader).getDexPath()
                : context.getPackageCodePath();
    }

    // Calls 'findClassesDex' for all the given 'dexPaths'
    private List<Class<?>> findClassesDexs(String packageName, ClassLoader classLoader, Set<String> dexPaths) {
        List<Class<?>> classes = new LinkedList<>();
        for (String dexPath : dexPaths)
            classes.addAll(findClassesDex(packageName, classLoader, dexPath));
        return classes;
    }

    // Loads classes matching the given 'packageName' in the dex at 'dexPath' using 'classLoader'
    private List<Class<?>> findClassesDex(String packageName, ClassLoader classLoader, String dexPath) {
        try {
            DexFile dex = new DexFile(dexPath);
            List<Class<?>> classes = new LinkedList<>();

            for (String name : IterableEnumeration.make(dex.entries())) {
                if (!name.startsWith(packageName))
                    continue;
                tryLoadClass(name, classLoader, classes);
            }
            return classes;

        } catch (IOException e) {
            LOG.error("Cannot open DEX for reading; returning nothing", e);
            return Collections.emptyList();
        }
    }

    // Tries to load a class and to add it to the passed list, logs errors (extracted from findClassesDex)
    private void tryLoadClass(String name, ClassLoader classLoader, List<Class<?>> classes) {
        try {
            classes.add(classLoader.loadClass(name));
        } catch (ClassNotFoundException e) {
            // Class cannot be loaded by the class loader, skip it and log
            LOG.warn("Class " + name + "was found but cannot be loaded by the given class loader", e);
        }
    }

}
