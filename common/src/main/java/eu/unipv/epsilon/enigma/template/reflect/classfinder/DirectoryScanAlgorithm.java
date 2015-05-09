package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DirectoryScanAlgorithm extends ScanAlgorithm {

    private final File directory;

    /*
     * Directory crawler local utility.
     *
     * @param directory the directory containing the package on disk
     * @param packageName the package name used to create {@link Class} objects
     * @param classes the list to add found classes to
     * @throws ClassNotFoundException if a class file in the directory could not be loaded
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

    private void scanRecursive(File directory, String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        if (!directory.isDirectory()) return;
        final String[] files = directory.list();
        File innerDir;

        for (String file : files) {
            if (file.endsWith(CLASS_FILE_EXT)) {
                try {
                    classes.add(classLoader.loadClass(packageName + '.' + stripClassExtension(file)));
                } catch (NoClassDefFoundError e) {
                    // Do nothing, this class hasn't been found by the loader, and we don't care.
                }
            } else if ((innerDir = new File(directory, file)).isDirectory())
                scanRecursive(innerDir, packageName + '.' + file, classes);
        }
    }

}
