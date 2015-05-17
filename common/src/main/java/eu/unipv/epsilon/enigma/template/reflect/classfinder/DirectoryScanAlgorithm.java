package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * A scan algorithm to search for classes using the "file:" protocol on the local file system.
 */
public class DirectoryScanAlgorithm extends ScanAlgorithm {

    private final File directory;

    /**
     * Creates a directory scan algorithm.
     *
     * @param classLoader the class loader used to load found classes
     * @param directory the directory containing the package on disk
     */
    public DirectoryScanAlgorithm(ClassLoader classLoader, File directory) {
        super(classLoader);
        this.directory = directory;
    }

    @Override
    public List<Class<?>> scan(String packageName) throws ClassNotFoundException {
        LinkedList<Class<?>> classes = new LinkedList<>();
        scanRecursive(directory, packageName, classes);
        return classes;
    }

    // Internal recursive scan algorithm
    private void scanRecursive(File directory, String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        if (!directory.isDirectory()) return;
        final String[] files = directory.list();
        File innerDir;

        // If we scan the root we don't want inner package names to start with '.' like ".com.example"
        if (!packageName.equals("")) packageName += '.';

        for (String file : files) {
            if (file.endsWith(CLASS_FILE_EXT)) {
                // Try to load a class file
                try {
                    classes.add(classLoader.loadClass(packageName + stripClassExtension(file)));
                } catch (NoClassDefFoundError e) {
                    // Do nothing, this class hasn't been found by the loader, and we don't care.
                }
            } else if ((innerDir = new File(directory, file)).isDirectory()) {
                // We have found an inner directory, perform another scan recursively
                scanRecursive(innerDir, packageName + file, classes);
            }
        }
    }

}
