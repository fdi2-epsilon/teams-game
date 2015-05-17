package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import java.util.List;

/**
 * A specialized algorithm to search for classes given package names in a class source.
 */
public abstract class ScanAlgorithm {

    protected static final String CLASS_FILE_EXT = ".class";

    protected ClassLoader classLoader;

    public ScanAlgorithm(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /* Utility used by different implementations to drop the ".class"
       file extension in order to load found files with the class loader */
    protected static String stripClassExtension(String filePath) {
        return filePath.substring(0, filePath.length() - CLASS_FILE_EXT.length());
    }

    /**
     * Performs a package name scan using this algorithm, the package name should conform with construction arguments.
     *
     * First, a search for ".class" files is performed, then these are loaded using the given class loader.
     *
     * @param packageName the name of the package to scan for classes
     * @return found class names
     * @throws ClassNotFoundException if the algorithm thrown an exception
     */
    public abstract List<Class<?>> scan(String packageName) throws ClassNotFoundException;

}
